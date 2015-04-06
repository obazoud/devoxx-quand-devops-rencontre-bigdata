# Exercice 4: Déploiement sur le cluster

## Objectifs

Le but de cet exercice déployer sur le cluster Spark un programme écrit via le REPL.

## Connexion au cluster

* Aller sur https://console.developers.google.com/project/blast-machine-201504/compute/instances
* Cliquer sur SSH de la colonne "Connect" sur l'instance " hadoop-m"
* puis "Open in a browser window"

![alt tag](https://raw.githubusercontent.com/obazoud/devoxx-quand-devops-rencontre-bigdata/screenshots/google-cloud.png)

La console ssh en mode web s'ouvre:

![alt tag](https://raw.githubusercontent.com/obazoud/devoxx-quand-devops-rencontre-bigdata/screenshots/spark-cluster-console.png)

Le spark-shell se lance avec l'utilisateur `hadoop`.

```sh
olivier_bazoud_gmail_com@hadoop-m:~$ sudo su -
root@hadoop-m:~# su hadoop
hadoop@hadoop-m:/root$ cd
hadoop@hadoop-m:~$ pwd
/home/hadoop
```

Lancer le spark-shell.

```sh
hadoop@hadoop-m:~$ ./spark-install/bin/spark-shell --master spark://xxxx:7077 --total-executor-cores 4 --executor-memory 2G
...
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 1.3.0
      /_/
...
```

Pour lire les fichiers sur Google Cloud, il faut utiliser `gs://`.

apache-access.log fait ~ 800Mo pour 3 500 000 lignes.

```sh
scala> val apacheaccess = sc.textFile("gs://handsondevoxxfr/apache-access.log")
apacheaccess: org.apache.spark.rdd.RDD[String] = gs://handsondevoxxfr/apache-access.log MapPartitionsRDD[1] at textFile at <console>:21
```

Mettre le RDD en cache.

```sh
scala> apacheaccess.cache
res0: apacheaccess.type = gs://handsondevoxxfr/apache-access.log MapPartitionsRDD[1] at textFile at <console>:21
```

Compter les lignes.

```sh
scala> apacheaccess.count
...
15/04/04 15:34:13 INFO scheduler.DAGScheduler: Job 0 finished: count at <console>:24, took 7.874900 s
res1: Long = 3500001
```

Pour créer la `case class`, il est préférable d'utiliser le `:paste -raw` qui permet de copier/coller directement du code.

```sh
scala> :paste -raw
// Entering paste mode (ctrl-D to finish)
case class ApacheAccessLog(host: String, client: String, user: String, dateTime: String, method: String,
path: String, protocol: String, code: Integer, size: Long, referer: String,
agent: String)
object ApacheAccessParser {
  val ACCESS_LOG_PATTERN =
  "^(\\S+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+) (\\S+) (\\S+)\" (\\d{3}) (\\d+) \"(\\S+)\" \"(.+)\"".r
  def parse(line: String): ApacheAccessLog = {
    val res = ACCESS_LOG_PATTERN.findFirstMatchIn(line)
    if (res.isEmpty) {
      throw new RuntimeException("Error parsing: " + line);
    }
    val m = res.get
    ApacheAccessLog(m.group(1), m.group(2), m.group(3), m.group(4), m.group(5), m.group(6), m.group(7), m.group(8).toInt, m.group(9).toLong, m.group(10), m.group(11))
  }
}
// Exiting paste mode, now interpreting.
defined class ApacheAccessLog
defined module ApacheAccessParser
```

Faites une analyse.

```sh
scala> apacheaccess.map(ApacheAccessParser.parse).filter(log => log.code == 404).map(log => log.referer).distinct.count
15/04/04 15:55:08 INFO scheduler.DAGScheduler: Job 2 finished: count at <console>:28, took 6.030779 s
res4: Long = 504
```

Puis une autre.

```sh
scala> apacheaccess.map(ApacheAccessParser.parse).map(log => (log.code, 1L)).reduceByKey((a, b) => a + b).collect
15/04/04 15:56:47 INFO scheduler.DAGScheduler: Job 3 finished: collect at <console>:28, took 5.404070 s
res5: Array[(Integer, Long)] = Array((404,8342), (200,3491315), (500,344))
```

Amusez vous bien!