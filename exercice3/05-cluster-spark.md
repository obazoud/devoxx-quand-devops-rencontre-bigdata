# Utilisation d'un cluster Spark

### connection à un cluster Spark déployé sur Google Cloud
Ouvrir [https://cloudssh.developers.google.com/projects/blast-machine-201504/zones/europe-west1-b/instances/hadoop-m-goxf?authuser=0&hl=fr](https://cloudssh.developers.google.com/projects/blast-machine-201504/zones/europe-west1-b/instances/hadoop-m-goxf?authuser=0&hl=fr) pour avoir un shell.

On execute un spark-shell en limitant l'allocation de ressources pour partager le cluster
```sh
$ MASTER=spark://hadoop-m-goxf:7077 bin/spark-shell \
         --total-executor-cores 4 \
         --executor-memory 2G
```



### Accès aux fichiers dans le google cloud storage
```scala
val data = sc.textFile("gs://handsondevoxxfr/kddcup.data")

data.cache()

data.count()
// combien de temps ?

data.count()
// combien de temps ?
```