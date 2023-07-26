import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.lit


object DataSourcer {

  // proportion is usually 70% to 30% for train and test
  def rawTrainData(sparkSession: SparkSession): DataFrame = {

    val pathToBooksCSV = "./src/data/Books/books.csv";
    val pathToBankingCSV = "./src/data/Banking/train.csv";

    print("Reading CSV file from: " + pathToBooksCSV + "Is file exist on path: " + scala.reflect.io.File(pathToBankingCSV).exists);

    sparkSession.read
      .option("header", "true")
      .option("sep", ";")
      .csv(pathToBooksCSV)

  }
}