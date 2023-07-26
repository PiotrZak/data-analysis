import org.apache.spark.sql.SparkSession

object Main {
  def main(args: Array[String]): Unit = {
    println("Hello world!")
  }
}

object SparkSessionCreator {
  def sparkSessionCreate(): SparkSession = {
    SparkSession
      .builder()
      .master("local[*]")
      .appName("SparkScalaML")
      .getOrCreate()
  }
}

  object ModelTrain {

    def main(args: Array[String]): Unit = {

      val spark = SparkSessionCreator.sparkSessionCreate()
      val rawTrainData = DataSourcer.rawTrainData(sparkSession = spark)
      val cleanTrainData = DataCleaner.cleanData(dataFrame = rawTrainData)
      val featureTrainData = FeatureEngineering.featureData(dataFrame = cleanTrainData)

      featureTrainData.show()

      val fittedPipeline = MachineLearning.pipelineFit(dataFrame = featureTrainData)

      // save fitted pipeline
      fittedPipeline
        .write
        .overwrite()
        .save("./pipelines/fitted-pipeline")
    }
  }


//def predictionSave(dataFrame:DataFrame): Unit = {
//
//   if user survived at titanic?
//   if user have loan?
//   if this book is good recommendation?
//   if this user will buy this product?
//   if this user will click this ad?
//   if this user will like this movie?
//   if this user will like this song?
//   if this user will like this video?
//   if this user will like this article?
//   if this user will like this post?
//   if this user will like this comment?
//
//}

//object ModelPredict {
//
//  def main(args: Array[String]): Unit = {
//    // create spark session
//    val spark = SparkSessionCreator.sparkSessionCreate()
//    val rawTestData = DataSourcer.rawTestData(sparkSession = spark)
//    val cleanTestData = DataCleaner.cleanData(dataFrame = rawTestData)
//    val featureTestData = FeatureEngineering.featureData(dataFrame = cleanTestData)
//  }
//
//}
