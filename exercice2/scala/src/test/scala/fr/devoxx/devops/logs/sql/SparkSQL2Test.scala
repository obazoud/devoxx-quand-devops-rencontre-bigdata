package fr.devoxx.devops.logs.sql

import fr.devoxx.devops.logs.{SharedSparkSQLContext, SharedSparkContext}
import org.scalatest.{FunSuite, Matchers}

class SparkSQL2Test extends FunSuite with SharedSparkSQLContext with Matchers {

  test("Répartition des codes http") {
    val result = SparkSQL2(rdd, sqlContext).process
    result shouldBe (Array((200, 6067), (404, 35)))
  }
}
