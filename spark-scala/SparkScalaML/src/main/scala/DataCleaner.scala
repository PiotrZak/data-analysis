import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.lit
import org.apache.spark.sql.functions._


object DataCleaner {

  def cleanData(dataFrame: DataFrame): DataFrame = {

    def formatData(dataFrame: DataFrame): DataFrame = {

      dataFrame
        .withColumn("age", dataFrame("age").cast("Int"))
        .withColumn("job", dataFrame("job").cast("String"))
        .withColumn("balance", dataFrame("balance").cast("Double"))
        .withColumn("marital", dataFrame("marital").cast("String"))
        .withColumn("education", dataFrame("education").cast("String"))
        .withColumn("housing", dataFrame("housing").cast("String"))
        .withColumn("loan", when(dataFrame("loan") === "Yes", 1).otherwise(0))
        .withColumnRenamed("loan", "label")
    }

    def meanAge(dataFrame: DataFrame): Double = {
      dataFrame.select("age").agg("age" -> "avg").first().getDouble(0)
    }

    val formattedData = formatData(dataFrame)
    val outputData = formattedData
      .na.fill(Map(
        "age" -> meanAge(formattedData).toInt))
      .drop("default", "contact", "day", "month", "duration", "campaign", "pdays", "previous", "poutcome", "y")

    outputData
  }
}