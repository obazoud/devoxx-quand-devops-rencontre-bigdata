package fr.devoxx.devops.logs.sql

import fr.devoxx.devops.logs.ApacheAccessLog
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext

/* Statistiques sur la taille des requêtes */
case class SparkSQL5(rdd: RDD[String] , sqlContext: SQLContext) {

  def process: (Long, Long, Double, Long) = {
    val dataFrame = sqlContext.createDataFrame(rdd.map(ApacheAccessLog.parse));
    dataFrame.registerTempTable("ApacheAccessLog");

    (0L, 0L, 0.0, 0L)
  }
}
