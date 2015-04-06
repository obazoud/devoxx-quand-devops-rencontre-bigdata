package fr.devoxx.devops.logs.spark

import fr.devoxx.devops.logs.SharedSparkContext
import org.scalatest.{FunSuite, Matchers}

class Spark2Test extends FunSuite with SharedSparkContext with Matchers {

  test("Répartition des codes http") {
    val result = Spark2(rdd).process
    result shouldBe (Array((404, 35), (200, 6067)))
  }
}
