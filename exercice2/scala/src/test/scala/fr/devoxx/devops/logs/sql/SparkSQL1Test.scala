package fr.devoxx.devops.logs.sql

import fr.devoxx.devops.logs.{SharedSparkSQLContext, SharedSparkContext}
import fr.devoxx.devops.logs.spark.Spark3
import org.scalatest.{FunSuite, Matchers}

class SparkSQL1Test extends FunSuite with SharedSparkSQLContext with Matchers {

  test("Les liens cassés") {
    val result = SparkSQL1(rdd, sqlContext).process
    result shouldBe 25

  }
}
