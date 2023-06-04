import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.lit


object DataSourcer {

  // proportion is usually 70% to 30% for train and test
  def rawTrainData(sparkSession: SparkSession): DataFrame = {

    val pathToCSV = "./src/data/Books/books.csv";
    print("Reading CSV file from: " + pathToCSV + "Is file exist on path: " + scala.reflect.io.File(pathToCSV).exists);

    sparkSession.read.option("header", "true").csv(pathToCSV)

  }
}