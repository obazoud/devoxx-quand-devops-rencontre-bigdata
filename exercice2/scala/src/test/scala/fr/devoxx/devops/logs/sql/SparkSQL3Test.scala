package fr.devoxx.devops.logs.sql

import fr.devoxx.devops.logs.{SharedSparkSQLContext, SharedSparkContext}
import org.scalatest.{FunSuite, Matchers}

class SparkSQL3Test extends FunSuite with SharedSparkSQLContext with Matchers {

  test("Top 3 des familles user agents") {
    val result = SparkSQL3(rdd, sqlContext).process
    result shouldBe (Array(("IE", 2269), ("Firefox", 1601), ("Chrome", 1509)))
  }
}
