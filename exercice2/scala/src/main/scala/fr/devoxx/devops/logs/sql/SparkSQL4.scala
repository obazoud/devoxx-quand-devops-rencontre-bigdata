package fr.devoxx.devops.logs.sql

import fr.devoxx.devops.logs.ApacheAccessLog
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext

/* Top 3 des plages d'IP */
case class SparkSQL4(rdd: RDD[String] , sqlContext: SQLContext) {

  def process: Array[(String, Long)] = {
    val dataFrame = sqlContext.createDataFrame(rdd.map(ApacheAccessLog.parse));
    dataFrame.registerTempTable("ApacheAccessLog");

    Array(("", 0L))
  }
}
