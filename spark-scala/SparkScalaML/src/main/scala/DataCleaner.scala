import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.lit



object DataCleaner {

  def cleanData(dataFrame: DataFrame): DataFrame = {

    def formatData(dataFrame: DataFrame): DataFrame = {

      dataFrame
        .withColumn("age", dataFrame("age").cast("Int"))
        .withColumn("job", dataFrame("job").cast("String"))
        .withColumn("balance", dataFrame("balance").cast("Int"))
        .withColumn("marital", dataFrame("marital").cast("String"))
        .withColumn("education", dataFrame("education").cast("Int"))
        .withColumn("housing", dataFrame("housing").cast("String"))
        .withColumn("loan", dataFrame("loan").cast("String"))
    }

    def meanAge(dataFrame: DataFrame): Double = {
      dataFrame.select("age").agg("age" -> "avg").first().getDouble(0)
    }

    val formattedData = formatData(dataFrame)
    val outputData = formattedData
      .na.fill(Map(
        "age" -> meanAge(formattedData).toInt))
      .drop("default", "contact", "day", "month", "duration", "campaign", "pdays", "previous", "poutcome")

    outputData
  }
}