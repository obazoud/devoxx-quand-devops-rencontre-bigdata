package fr.devoxx.devops.logs.spark

import fr.devoxx.devops.logs.SharedSparkContext
import org.scalatest.{FunSuite, Matchers}

class Spark1Test extends FunSuite with SharedSparkContext with Matchers {

  test("Les liens cassés") {
    val result = Spark1(rdd).process
    result shouldBe 25
  }
}
