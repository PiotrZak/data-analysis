import org.apache.spark.ml.feature.{Bucketizer, OneHotEncoderEstimator, StringIndexer}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{split, when}

// 05.06.2023 - defined features based on ideas, insights.
object FeatureEngineering {

  def featureData(dataFrame: DataFrame): DataFrame = {

    def ageBucketizer(dateFrame: DataFrame): DataFrame = {

      val ageSplits = Array(0.0, 5.0, 18.0, 25.0, 35.0, 45.0, 55.0, 65.0, 100.0)

      val ageBucketizer = new Bucketizer()
        .setInputCol("age")
        .setOutputCol("ageGroup")
        .setSplits(ageSplits)

      ageBucketizer.transform(dataFrame).drop("age")
    }

    def balanceBucketizer(dataFrame: DataFrame): DataFrame = {

      val balanceSplits = Array(-50000.0, -10000.0, -1000.0, -200.0, 0.0, 1000, 5000.0, 10000.0, 50000.0, 100000.0, 500000.0)

      val balanceBucketizer = new Bucketizer()
        .setInputCol("balance")
        .setOutputCol("balanceGroup")
        .setSplits(balanceSplits)

      balanceBucketizer.transform(dataFrame).drop("balance")
    }

    val oneHotEncoder = new OneHotEncoderEstimator()
      .setInputCols(Array[String]("ageGroup", "balanceGroup"))
      .setOutputCols(Array[String]("ageGroupVec", "balanceGroupVec"))

    val data = ageBucketizer(dataFrame)
    val balanceData = balanceBucketizer(dataFrame=data)

    val oneHotModel = oneHotEncoder.fit(balanceData)
    val outputData = oneHotModel.transform(balanceData)

    outputData

  }
}