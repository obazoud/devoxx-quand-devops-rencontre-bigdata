package fr.devoxx.devops.logs.spark

import fr.devoxx.devops.logs.SharedSparkContext
import org.scalatest.{FunSuite, Matchers}

class Spark3Test extends FunSuite with SharedSparkContext with Matchers {

  test("Top 3 des familles user agents") {
    val result = Spark3(rdd).process
    result shouldBe (Array(("IE", 2269), ("Firefox", 1601), ("Chrome", 1509)))
  }
}
