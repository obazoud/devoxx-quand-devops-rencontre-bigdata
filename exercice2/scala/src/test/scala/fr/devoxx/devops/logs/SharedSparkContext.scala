package fr.devoxx.devops.logs

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfterAll, Suite}

trait SharedSparkContext extends BeforeAndAfterAll { self: Suite =>
  @transient var _sc: SparkContext = _
  @transient var _rdd: RDD[String] = _

  def sc: SparkContext = _sc
  def rdd: RDD[String] = _rdd

  override def beforeAll() {
    val conf = new SparkConf().setAppName(getClass().getName()).setMaster("local[2]")
    _sc = new SparkContext(conf)
    _rdd = sc.textFile(file(), 1)
    super.beforeAll()
  }

  override def afterAll() {
    _sc.stop()
    _sc = null
    super.afterAll()
  }

  def file(): String = Thread.currentThread
    .getContextClassLoader
    .getResource(getClass.getPackage.getName.replace('.', '/') + "/../apache-access-log")
    .getFile
}