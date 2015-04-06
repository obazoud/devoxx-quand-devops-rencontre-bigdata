package fr.devoxx.devops.logs.spark

import fr.devoxx.devops.logs.ApacheAccessLog
import org.apache.spark.rdd.RDD

/* Top 3 des plages d'IP */
case class Spark4(rdd: RDD[String]) {

  def process: Array[(String, Long)] = {
    Array(("", 0L))
  }
}
