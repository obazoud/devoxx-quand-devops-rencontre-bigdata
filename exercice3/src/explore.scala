var data = sc.textFile("/vagrant/data/kddcup10.data")
//data.cache()
data.count()

data.take(1)
//res11: Array[String] = Array(0,tcp,http,SF,215,45076,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,1,0.00,0.00,0.00,0.00,1.00,0.00,0.00,0,0,0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00,normal.)

val l10 = data.take(10).map(_.split(','))

// quels sont les protocols
val protocols = data.map(l => l.split(",")(2))
protocols.distinct().collect()

//compter un protocol
val httpCount = data.filter(l => l.split(",")(2).contains("http")).count()

//comprend des lignes complètes, pas très efficace qand on aura un très gros dataset
val httpCount2 = protocols.filter(_.contains("http")).count()
val distinctHttp = httpCount2.distinct()


//compter tous les protocols
val protocolPairs = protocols.map(s => (s,1))
val counts = protocolPairs.reduceByKey((a,b) => a+b)
counts.collect()

// tri
val protocolsCountsOrdered = counts.sortByKey()
protocolsCountsOrdered.collect()

// tri sur la valeur serait mieux
val protocolsCountsOrdered = counts.map(_.swap).sortByKey()
protocolsCountsOrdered.collect()

// finalement ce qu'on veut c'est le top10
val top = counts.map(_.swap).top(10)
top.foreach(println)


//Repartition des durees de sessions 
data.map(l => l.split(",")(0)).map(s => (s,1)).reduceByKey((a,b) => a+b).map(_.swap).top(10)
res15: Array[(Int, String)] = Array((481671,0), (2476,1), (870,2), (625,3), (554,5), (496,2630), (413,4), (322,14), (194,10), (169,7))

// protocols
data.map(l => l.split(",")(1)).map(s => (s,1)).reduceByKey((a,b) => a+b).map(_.swap).top(10)
res16: Array[(Int, String)] = Array((283602,icmp), (190065,tcp), (20354,udp))

// services
data.map(l => l.split(",")(1)).map(s => (s,1)).reduceByKey((a,b) => a+b).map(_.swap).top(10)
res16: Array[(Int, String)] = Array((283602,icmp), (190065,tcp), (20354,udp))

// flag
data.map(l => l.split(",")(1)).map(s => (s,1)).reduceByKey((a,b) => a+b).map(_.swap).top(10) 
 res17: Array[(Int, String)] = Array((378440,SF), (87007,S0), (26875,REJ), (903,RSTR), (579,RSTO), (107,SH), (57,S1), (24,S2), (11,RSTOS0), (10,S3)

// label
data.map(l => l.split(",")(41)).map(s => (s,1)).reduceByKey((a,b) => a+b).map(_.swap).top(10)
res11: Array[(Int, String)] = Array((280790,smurf.), (107201,neptune.), (97278,normal.), (2203,back.), (1589,satan.), (1247,ipsweep.), (1040,portsweep.), (1020,warezclient.), (979,teardrop.), (264,pod.))



