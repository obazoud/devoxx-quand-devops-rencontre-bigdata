package fr.devoxx.devops.logs.spark

import fr.devoxx.devops.logs.SharedSparkContext
import org.scalatest.{FunSuite, Matchers}

class Spark5Test extends FunSuite with SharedSparkContext with Matchers {

  test("statistiques sur la taille des requêtes") {
    val result = Spark5(rdd).process
    result.count shouldBe 6102
    result.min shouldBe 40.0
    result.mean shouldBe 90.044 +- 1e-3
    result.max shouldBe 139.0
  }
}
