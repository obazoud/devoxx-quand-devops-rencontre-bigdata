package fr.devoxx.devops.logs.sql

import fr.devoxx.devops.logs.{SharedSparkSQLContext, SharedSparkContext}
import org.scalatest.{FunSuite, Matchers}

class SparkSQL4Test extends FunSuite with SharedSparkSQLContext with Matchers {

  test("Top 3 des plages d'IP") {
    val result = SparkSQL4(rdd, sqlContext).process
    result shouldBe (Array(("140.x.x.x", 165), ("84.x.x.x", 163), ("48.x.x.x", 157)))
  }
}
