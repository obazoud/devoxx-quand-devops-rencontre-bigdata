package fr.devoxx.devops.logs.spark

import fr.devoxx.devops.logs.ApacheAccessLog
import org.apache.spark.rdd.RDD

/* Top 3 des familles user agents */
case class Spark3(rdd: RDD[String]) {

  def process: Array[(String, Long)] = {
    Array(("", 0L))
  }
}
