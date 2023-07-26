import org.apache.spark.sql.functions._
import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.types.{StructType, DoubleType, StructField, StringType, IntegerType};
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.evaluation.RegressionEvaluator;
import scala.util.Random

object CollaborativeFiltering {

  def main(args: Array[String]): Unit = {

    val books = addRating()
    val booksISBN = books.select("ISBN")

    // Let's say that profiles (bots or people) are rating books from 1 to 10
    val booksRating = mockRandomData(booksISBN)
    var booksRatingsDf: DataFrame = books.join(booksRating, Seq("ISBN"), "inner")

    print(booksRatingsDf.show(10))

    // Books rating by UserId = 1
    booksRatingsDf.filter(col("UserId") === "1")
      .limit(5)
      .show()

    booksRatingsDf = booksRatingsDf.withColumn("ISBN", col("ISBN").cast(IntegerType))
    booksRatingsDf = booksRatingsDf.withColumn("UserId", col("UserId").cast(IntegerType))

    val cleanBooksRatingsDf = booksRatingsDf.na.drop(Seq("UserId", "ISBN"))

    buildALSModel(cleanBooksRatingsDf)


  }

  val bookSchema = StructType(
      StructField("ISBN", StringType, nullable = true) ::
      StructField("Title", StringType, nullable = true) ::
      StructField("Author", StringType, nullable = true) ::
      StructField("Year", IntegerType, nullable = true) ::
      StructField("Publisher", StringType, nullable = true) ::
      Nil
  )

  // rating schema
  val ratingSchema = StructType(
    StructField("USER-ID", IntegerType, nullable = true) ::
      StructField("ISBN", IntegerType, nullable = true) ::
      StructField("Rating", IntegerType, nullable = true) ::
      Nil
  )

// Alternating Least Squares(ALS)
// ALS is a very popular algorithm for making recommendations.

  def buildALSModel(ratings: DataFrame) = {

    val als = new ALS()
      .setMaxIter(5)
      .setRegParam(0.01)
      .setUserCol("UserId")
      .setItemCol("ISBN")
      .setRatingCol("Rating")

    val alsModel = als.fit(ratings)

    alsModel.setColdStartStrategy("drop")
    val predictions = alsModel.transform(ratings)

    val evaluator = new RegressionEvaluator()
      .setMetricName("rmse")
      .setLabelCol("Rating")
      .setPredictionCol("prediction")

//
//    RMSE = √(Σ(ŷ - y) ²/ n )
//
//    It measures the average magnitude of the error (or residuals) between the predicted values and the actual values.
//
//    Where:
//      ŷ: Predicted value from the model.
//      y: Actual value from the dataset.
//      n : Number of data points in the dataset.
    val rmse = evaluator.evaluate(predictions)
    println(s"root mean square error $rmse")

    //root mean square error 0.6420646267060414

    // Generate top 10 book recommendations for each user

    val userRecs = alsModel.recommendForAllUsers(10)
    userRecs.show(10, false)

    // User with Id 1 - will like those:
    // I Love My Mother -> prediction: 9.997123
    // The Poisonwood Bible
    // Without Reservation: The Making of America's Most Powerful Indian Tribe and Foxwoods, the World's Largest Casino
    // (...) - and another 10 recommendations

    // Generate top 10 user recommendations for each book

    val bookRecs = alsModel.recommendForAllItems(10)
    bookRecs.show(10, false)

    //ISBN => 2250810 -> Santa Speaks: The Wit and Wisdom of Santas Across the Nation
    // UserId 1 - will like based on his previous ratings
    // Users with Id 3, 5 - won't like it based on their previous ratings
    // 1: 5.998274
    // 2: 3.290016
    // 4: 3.196826
    // 3: 1.55993136
    // 5: 1.4406497

    // Generate top 10 book recommendations for a specified set of users


  }

  def mockRandomData(booksISBN: DataFrame): DataFrame = {

    val ISBNWithRating = booksISBN.withColumn("Rating", (rand() * 10 + 1).cast("int"))
    // There are 5 profiles, which rating books
    val ISBNWithRatingUsers = ISBNWithRating.withColumn("UserId", (rand() * 5 + 1).cast("int"))
    return ISBNWithRatingUsers
  }


  def addRating(): DataFrame = {

    val sparkSession = SparkSessionCreator.sparkSessionCreate()
    val pathToBooksCSV = "./src/data/Books/books.csv";

    //26.07.2023 - may be done also in Azure Synapse Analytics
    print("Start processing to adding column to .csv file:")
    val books = sparkSession.read
      .option("header", "true")
      .option("sep", ";")
      .csv(pathToBooksCSV)
      .cache()

    val booksDf = books.toDF("ISBN", "Book-Title", "Book-Author", "Year-Of-Publication", "Publisher", "Image-URL-S", "Image-URL-M", "Image-URL-L")
    val sortedByPublish = booksDf.orderBy(col("Year-Of-Publication").desc)

    // Some books - have year of publication > 2023 - eg. 2030, 2050
    // Images are very expensive in cloud - not to mention movies.
    val booksWithoutImages = sortedByPublish.drop("Image-URL-S", "Image-URL-M", "Image-URL-L")

    return booksWithoutImages;
  }

}