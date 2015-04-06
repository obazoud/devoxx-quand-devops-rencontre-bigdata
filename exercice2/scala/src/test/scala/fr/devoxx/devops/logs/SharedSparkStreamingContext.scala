package fr.devoxx.devops.logs

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.streaming.{StreamingContext, Seconds}
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, Suite}

trait SharedSparkStreamingContext extends BeforeAndAfterAll { self: Suite =>
  @transient var _sc: StreamingContext = _
  @transient var _rdd: RDD[String] = _

  def sc: StreamingContext = _sc
  def rdd: RDD[String] = _rdd

  override def beforeAll() {
    val conf = new SparkConf().setAppName(getClass().getName()).setMaster("local[3]")
    _sc = new StreamingContext(conf, Seconds(5))
    super.beforeAll()
  }

  override def afterAll() {
    _sc.stop()
    _sc = null
    super.afterAll()
  }
}