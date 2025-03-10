import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.classification.{GBTClassifier, RandomForestClassifier}
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{IndexToString, StringIndexer, VectorAssembler, VectorIndexer}
import org.apache.spark.sql.DataFrame


object MachineLearning {

  def pipelineFit(dataFrame: DataFrame): PipelineModel = {

      val featureAssembler = new VectorAssembler()
        .setInputCols(Array[String](
          "ageGroupVec",
          "balanceGroupVec"
        )
        ).setOutputCol("features")

      // Random Forest Algorithm (there are also different to choose from)
      // Carefull for overfitting
      val rf = new RandomForestClassifier()
        .setLabelCol("label")
        .setFeaturesCol("features")
        .setNumTrees(10)
        .setMaxDepth(10)
        .setSeed(1L)

      val gbt = new GBTClassifier()
        .setLabelCol("label")
        .setFeaturesCol("features")
        .setMaxIter(100)
        .setSeed(1L)

      // chain indexers and forest in a Pipeline
      val pipeline = new Pipeline()
        .setStages(
          Array(
            featureAssembler,
            rf
          )
        )

      val pipelineModel = pipeline.fit(dataFrame)

      val predictions = pipelineModel.transform(dataFrame)

      // select (prediction, true label) and compute test error
      val evaluator = new MulticlassClassificationEvaluator()
        .setLabelCol("label")
        .setPredictionCol("prediction")
        .setMetricName("accuracy")

      val accuracy = evaluator.evaluate(predictions)
      println("Test Error = " + (1.0 - accuracy))

      // return fitted pipeline
      pipelineModel
  }
}

