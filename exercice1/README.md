# Exercice 1: Introduction à Spark

## Objectifs

Le but de cet exercice est la prise en main de Spark à travers le [REPL](http://en.wikipedia.org/wiki/Read%E2%80%93eval%E2%80%93print_loop).

## Terminologie

* RDD: Resilient Distributed Dataset.
C'est une collection sur laquelle peut etre fait des opérations en parallèle, distribuées et à tolérance de panne (fault-tolerant).

## Spark Shell - REPL

Le spark shell permet d'avoir une console pour manipuler directement les objects Spark/Scala.

Lancer la commande `/path/to/usb/spark/bin/spark-shell` dans un terminal depuis le répertoire "exercice1".

```sh
 % /path/to/usb/spark/bin/spark-shell
Spark assembly has been built with Hive, including Datanucleus jars on classpath
...
15/03/18 22:46:29 INFO HttpServer: Starting HTTP Server
15/03/18 22:46:29 INFO Utils: Successfully started service 'HTTP class server' on port 38156.
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 1.3.0
      /_/

Using Scala version 2.10.4 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_31)
15/03/18 22:46:35 INFO SparkContext: Running Spark version 1.3.0
15/03/18 22:46:36 INFO Remoting: Remoting started; listening on addresses :[akka.tcp://sparkDriver@192.168.0.20:40508]
15/03/18 22:46:36 INFO Utils: Successfully started service 'sparkDriver' on port 40508.
15/03/18 22:46:36 INFO MemoryStore: MemoryStore started with capacity 265.1 MB
15/03/18 22:46:36 INFO HttpServer: Starting HTTP Server
15/03/18 22:46:36 INFO Utils: Successfully started service 'HTTP file server' on port 54195.
15/03/18 22:46:41 INFO Utils: Successfully started service 'SparkUI' on port 4040.
15/03/18 22:46:41 INFO SparkUI: Started SparkUI at http://192.168.0.20:4040
15/03/18 22:46:41 INFO Executor: Using REPL class URI: http://192.168.0.20:38156
15/03/18 22:46:41 INFO AkkaUtils: Connecting to HeartbeatReceiver: akka.tcp://sparkDriver@192.168.0.20:40508/user/HeartbeatReceiver
15/03/18 22:46:41 INFO NettyBlockTransferService: Server created on 57660
15/03/18 22:46:42 INFO SparkILoop: Created spark context..
Spark context available as sc.
15/03/18 22:46:42 INFO SparkILoop: Created sql context (with Hive support)..
SQL context available as sqlContext.

scala> 
```

Le Spark shell est maintenant disponible. Pour sortir de ce shell, vous pouvez taper `exit`.

Ouvrer la [console Web Spark](http://127.0.0.1:4040/).

![alt tag](https://raw.githubusercontent.com/obazoud/devoxx-quand-devops-rencontre-bigdata/screenshots/spark-web-console.png)

## Charger le fichier de test

La variable `sc` est disponible dans le Spark Shell, elle représente le [SparkContext](https://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.SparkContext).

```sh
scala> sc.version
res1: String = 1.3.0
```

Maintenant, nous allons charger le fichier `test.txt` qui nous sera utile pour les étapes suivantes.
Ce fichier représente un texte simple sur plusieurs lignes, et nous allons continuer la prise en main de Spark en faisant quelques analyses simples dessus.

* La fonction `textFile` permet de lire un fichier et de retourner un object de type RRD.

```sh
scala> val textFile = sc.textFile("test.txt")
textFile: org.apache.spark.rdd.RDD[String] = test.txt MapPartitionsRDD[9]
```

Ouvrer la [console Web Spark](http://127.0.0.1:4040/) pour voir le job Spark lancé.

![alt tag](https://raw.githubusercontent.com/obazoud/devoxx-quand-devops-rencontre-bigdata/screenshots/spark-web-console1.png)

## Comptez les lignes

```sh
scala> textFile.count()
res2: Long = 7
```

## Afficher la première ligne

```sh
scala> textFile.first()
res3: String = Apache Spark is a fast and general-purpose cluster computing system. It provides high-level APIs in Java, Scala and Python, and an optimized engine that supports general execution graphs. It also supports a rich set of higher-level tools including Spark SQL for SQL and structured data processing, MLlib for machine learning, GraphX for graph processing, and Spark Streaming.
```

## Compter les lignes qui contiennent le mot "Scala"

* La fonction `filter` filtre un element suivant une condition.

```sh
scala> textFile.filter(line => line.contains("Scala")).count()
res4: Long = 2
```

## Calculer le maximum de mots dans une ligne

* La fonction `map` transforme un element en un autre.
* La fonction `reduce` agrége les éléments entre eux.

```sh
scala> textFile.map(
  line => line.split(" ").size
).reduce(
  (a, b) => Math.max(a,b)
)
res5: Long = 57
```

## Compter la répartition des mots

* La fonction `flatMap` transforme un élément en une collection d'élément.
* La fonction `reduceByKey` agrége les éléments entre eux groupés par clé.
* La fonction `collect` transforme le résultat dans une collection.

```sh
scala> textFile.flatMap(
  line => line.split(" ")
).map(
  word => (word, 1)
).reduceByKey(
  (a, b) => a + b
).collect()
res6: Array[(String, Int)] = Array((GraphX,1), (Python,1), (is,3), (runs,2), (general,1), ...
```

## Console Web Spark

Ouvrer la [console Web Spark](http://127.0.0.1:4040/) pour voir l'ensemble des jobs qui ont été lancés pat cet exercice.

![alt tag](https://raw.githubusercontent.com/obazoud/devoxx-quand-devops-rencontre-bigdata/screenshots/spark-web-console2.png)
