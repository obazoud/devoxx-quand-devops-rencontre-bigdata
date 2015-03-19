import sqlContext.implicits._

var data = sc.textFile("/vagrant/data/kddcup10.data")


case class Line(proto:String,service:String,hit:Integer)
val hits = data.map(_.split(",")).map(l => Line(l(1),l(2),1)).toDF()
hits.registerTempTable("hits")


val counts = sqlContext.sql("SELECT service, COUNT(*) c FROM hits GROUP BY service ORDER BY c DESC LIMIT 10")
counts.collect().foreach(println)


val counts2 = sqlContext.sql("SELECT proto,service, COUNT(*) c FROM hits GROUP BY proto,service ORDER BY c DESC LIMIT 10")
counts2.collect().foreach(println)