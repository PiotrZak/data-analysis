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

      // 05.06.2023 - Process with Machine Learning for predict one of feature based on rest.
      // eg. if some have this balance and this job, and this maritial status - if this person will have loan?

    }
  }
