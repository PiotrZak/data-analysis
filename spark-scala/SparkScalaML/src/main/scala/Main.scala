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
      // create spark session
      val spark = SparkSessionCreator.sparkSessionCreate()

      val df = spark.createDataFrame(
        List(("Scala", 25000), ("Spark", 35000), ("PHP", 21000)))

      df.show()

      // train data

//      val rawTrainData = DataSourcer.rawTrainData(sparkSession = spark)
//      // clean train data
//      val cleanTrainData = DataCleaner.cleanData(dataFrame = rawTrainData)
//      // feature data
//      val featureTrainData = FeatureEngineering.featureData(dataFrame = cleanTrainData)
//      // fitted pipeline
//      val fittedPipeline = MachineLearning.pipelineFit(dataFrame = featureTrainData)
//      // save fitted pipeline
//      OutputSaver.pipelineSaver(pipelineModel = fittedPipeline)

    }
  }