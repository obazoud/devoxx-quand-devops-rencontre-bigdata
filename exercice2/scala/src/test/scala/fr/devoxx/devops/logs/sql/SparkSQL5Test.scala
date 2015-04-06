package fr.devoxx.devops.logs.sql

import fr.devoxx.devops.logs.{SharedSparkSQLContext, SharedSparkContext}
import org.scalatest.{FunSuite, Matchers}

class SparkSQL5Test extends FunSuite with SharedSparkSQLContext with Matchers {

  test("Statistiques sur la taille des requêtes") {
    val result = SparkSQL5(rdd, sqlContext).process
    result._1 shouldBe 6102
    result._2 shouldBe 40
    result._3 shouldBe 90.044 +- 1e-3
    result._4 shouldBe 139
  }
}
