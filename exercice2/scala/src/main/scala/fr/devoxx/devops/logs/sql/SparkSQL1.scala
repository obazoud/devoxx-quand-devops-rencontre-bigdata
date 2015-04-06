package fr.devoxx.devops.logs.sql

import fr.devoxx.devops.logs.ApacheAccessLog
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext

/* Les liens cassés */
case class SparkSQL1(rdd: RDD[String] , sqlContext: SQLContext) {

  def process: Long = {
    val dataFrame = sqlContext.createDataFrame(rdd.map(ApacheAccessLog.parse));
    dataFrame.registerTempTable("ApacheAccessLog");

    /*
    sqlContext.sql("select ...")
      .xxx
    */
    0L
  }
}
