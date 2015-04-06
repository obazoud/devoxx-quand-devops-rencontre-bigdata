# Exercice 2: Analyse de logs Apache

C'est le même tutorial pour Java ou Scala.

## Objectifs

Le but de cet exercice est d'utiliser différents techniques de Spark pour analyser les logs Aapache.

## Apache logs

Nous allons utiliser des logs Apache basé sur le ["Combined Log Format"](http://httpd.apache.org/docs/2.4/logs.html)

>
> Another commonly used format string is called the Combined Log Format. It can be used as follows.
>
```
LogFormat "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\"" combined
CustomLog log/access_log combined
````
>
> This format is exactly the same as the Common Log Format, with the addition of two more fields. Each of the additional fields uses the percent-directive %{header}i, where header can be any HTTP request header.
>
> The access log under this format will look like:
```
127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.0" 200 2326 "http://www.example.com/start.html" "Mozilla/4.08 [en] (Win98; I ;Nav)"
```
>

Le format est le suivant:

`host` `client` `user` [`dateTime`] "`method` `path` `protocol`" `code` `size` "`referer`" "`agent`"

Voici un [exemple de log Apache](./src/test/resources/fr/devoxx/devops/logs/apache-access-log) que nous allons étudier:

```
...
127.0.0.1 - frank [10/Oct/2000:13:55:36 -0700] "GET /apache_pb.gif HTTP/1.0" 200 2326 "http://www.example.com/start.html" "Mozilla/4.08 [en] (Win98; I ;Nav)"
144.111.47.190 - - [19/Mar/2015:22:06:24 +0100] "GET /category/networking HTTP/1.1" 200 62 "-" "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.46 Safari/535.11"
152.42.128.129 - - [19/Mar/2015:22:06:24 +0100] "GET /item/books/1999 HTTP/1.1" 200 59 "/search/?c=Books+Finance" "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; YTB730; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C)"
...
```

## Maven

Pour les utilisateurs de Java, il faut utiliser la configuration suivante pour Maven en ligne de commande pour éviter le téléchargement par Internet. Le repository est contenu dans la clé.

```sh
 % export M2_HOME=/path/to/usb/maven 
 % export PATH=$M2_HOME/bin:$PATH
 % mvn test
```

Vérifier que ces valeurs sont prise en compte avec `mvn --version`. Si ce n'est pas le cas, vérifiez votre `/etc/mavenrc`

Et ne pas oublier de faire pointer votre IDE vers ce repository `/path/to/usb/maven/repository`!

Voici la commande pour lancer un seul test avec Maven:

```sh
 % /path/to/usb/maven/bin/mvn -Dtest=Spark1Test test
```

Pour packager le projet avec un seul jar:

```sh
/path/to/usb/maven/bin/mvn clean package assembly:single
```

## SBT

Pour les utilisateurs de Scala, il faut utiliser le sbt situé dans la clé usb.

```sh
 % /path/to/usb/sbt/sbt
```

Et ne pas oublier de faire pointer votre IDE vers le cache sbt!

Voici la commande pour lancer un seul test avec sbt:

```sh
 % /path/to/usb/sbt/sbt
> test-only fr.devoxx.devops.logs.spark.Spark1Test
```

## Terminologie

* RDD: Resilient Distributed Dataset.
C'est une collection sur laquelle peut etre fait des opérations en parallèle, distribuées et à tolérance de panne (fault-tolerant).

* La `class` (Java) ou `case class` (Scala) `ApacheAccessLog` est la representation d'une ligne de log Apache.

## Spark

Nous allons continuer commencer par découvrir Spark avec l'analyse de logs.

### Méthodologie

Pour chaque étape:
* Ouvrir `SparkXTest`
* Vérifier que le test est incorrect
* Apporter les modifications nécessaire à `SparkX`
* Vérifier pour que le test passe correctement, sinon recommencer.

#### Java

Chaque classe a une méthode `process` à compléter:

```java
public xxx process(JavaRDD<String> rdd) {
    return rdd.map(ApacheAccessLog::parse)
        .xxx()
        .xxx()
        .xxx;
}
```

`rdd.map(ApacheAccessLog::parse)` permet de transformer une ligne texte de log `String` en objet `ApacheAccessLog` grace à la méthode statique `parse`.

_Note_:

Une autre écriture en Java 8 serait:

```java
public xxx process(JavaRDD<String> rdd) {
    return rdd.map(line -> ApacheAccessLog.parse(line))
        .xxx()
        .xxx()
        .xxx;
}
```

Et en Java 7:
```java
public xxx process(JavaRDD<String> rdd) {
    return rdd.map(new Function<String, ApacheAccessLog>() {
                    @Override
                    public ApacheAccessLog call(String line) throws Exception {
                        return ApacheAccessLog.parse(line);
                    }
                })
                .xxx()
                .xxx()
                .xxx;
}
```

#### Scala

Chaque `case class` a une méthode `process` à compléter:

```scala
def process: Long = {
  rdd.map(ApacheAccessLog.parse)
    .xxx
    .xxx
}
```

`rdd.map(ApacheAccessLog.parse)` permet de transformer une ligne texte de log `String` en `ApacheAccessLog` grace à la méthode `parse`.

### Les liens cassés

Nous vous proposons de calculer le nombre de "referer" unique qui ont créé des `404` dans les logs.

Il vous faudra implémenter `process` de `Spark1` et vérifier le test `Spark1Test`. Pour chaque ligne:
* garder les lignes qui ont un code 404
* transformer `ApacheAccessLog` en une `String` referer
* garder les referers distincts
* compter le nombre de 'élements dans le RDD

**_Conseils_**

_RRD_
* `filter` filtre un élément suivant une condition
* `map` transforme un element en un autre
* `distinct` garde les élements distincts
* `count` retourne le nombre d'élement dans le RDD

_ApacheAccessLog_
* `referer` est le referer trouvé dans la ligne de log

### Répartition des codes http

Nous vous proposons de calculer la répartition des status codes http dans les logs.

Il vous faudra implémenter `process` de `Spark2` et vérifier le test `Spark2Test`. Pour chaque ligne:
* transformer `ApacheAccessLog` en un tuple de deux élements: le `code` en clé associé à la valeur 1
* faire une réduction par la fonction somme

**_Conseils_**

_RRD_
* Java: `mapToPair` transforme un element en une paire, un `Tuple2`, et retourne un `JavaPairRDD<K,V>`
* Scala: `map` transforme un element en un autre
* `reduceByKey` merge les données par clé en appliquant une fonction de reduction passé en paramètre, une somme par exemple
* `collect` retourne une collection qui contient tous les élements de cet RRD

_ApacheAccessLog_
* `code` est le code http dans la ligne de log

### Top 3 des familles user agents

Nous vous proposons de calculer le top 3 des user agents.
Il vous faudra implémenter `process` de `Spark3` et vérifier le test `Spark3Test`. Pour chaque ligne:
* transformer `ApacheAccessLog` en un tuple de deux élements: l'`agentFamily` en clé associé à la valeur 1
* faire une réduction par la fonction somme
* trier
* prendre les 3 premiers élements

**_Conseils_**

_RRD_
* Java: La fonction `mapToPair` transforme un element en une paire, un `Tuple2`, et retourne un `JavaPairRDD<K,V>`
* Scala: La fonction `map` transforme un element en un autre
* `reduceByKey` merge les données par clé en appliquant une fonction de reduction passé en paramètre, une somme par exemple
* `sortByKey` trie les tuples par les clés
* `take`(X) renvoie les X premiers élement de cet RRD et les renvoie sous la forme d'une collection

_Tuple_
* La fonction `swap` échange la clé et la valeur

_ApacheAccessLog_
* `agentFamily` est la famille du user agent trouvé dans la ligne de log. Au contraire de `agent` qui est la donnée brute

### Top 3 des plages d'IP

Nous vous proposons de calculer le top 3 des plages d'IP.
Il vous faudra implémenter `process` de `Spark4` et vérifier le test `Spark4Test`. Pour chaque ligne:
* transformer `ApacheAccessLog` en un tuple de deux élements: l'`ipRange` en clé associé à la valeur 1
* faire une réduction par la fonction somme
* prendre les 3 premiers élements avec un comparateur

**_Conseils_**

_RRD_
* Java: `mapToPair` transforme un element en une paire, un `Tuple2`, et retourne un `JavaPairRDD<K,V>`
* Scala: `map` transforme un element en un autre
* `reduceByKey` merge les données par clé en appliquant une fonction de reduction passé en paramètre, une somme par exemple
* `take`(X, C) renvoie les X premiers élement de cet RRD en appliquant le tri C et les renvoie sous la forme d'une collection

_ApacheAccessLog_
* La méthode `ipRange` transforme l'IP trouvé dans les logs `123.4.5.6` en `123.x.x.x`

### Statistiques sur la taille des requêtes

Nous vous proposons de calculer les statistiques sur la taille des requêtes
Il vous faudra implémenter `process` de `Spark5` et vérifier le test `Spark5Test`. Pour chaque ligne:
* transformer `ApacheAccessLog` en un double `size`
* faire une réduction par la fonction somme
* calculer les stats
* retourner uniquement le nombre, le min, la moyenne et la max de la taille des requetes dans un Tuple

**_Conseils_**

_RRD_
* Java: `mapToDouble` transforme un element en un `JavaDoubleRDD`
* Scala: `map` transforme un element en un autre
* `stats` renvoie un `StatCounter` qui est un objet qui va calculer le min, la moyenne, le max, ...

_ApacheAccessLog_
* `size` est la taille de la requete trouvé dans les logs

## Spark SQL

Continuons avec SparkSQL.

### Méthodologie

Pour chaque étape:
* Ouvrir la classe SparkXTest
* Vérifier que le test est incorrect
* Apporter les modifications nécessaire à la classe SparkX
* Vérifier pour que le test passe correctement, sinon recommencer.

#### Java

Chaque classe a une méthode `process` à compléter:

```java
public xxx process(JavaRDD<String> rdd, SQLContext sqlContext) {
    JavaRDD<ApacheAccessLog> accessLogs = rdd.map(ApacheAccessLog::parse);
    configure(sqlContext, accessLogs);

    return sqlContext.sql("select .....")
        .toJavaRDD()
        .xxx()
        .xxx();
}
```

Le `SQLContext` est le point d'entrée pour faire du Spark SQL et ainsi éxécuter des requetes SQL.

#### Scala

```scala
  def process: Xxxx = {
    val dataFrame = sqlContext.createDataFrame(rdd.map(ApacheAccessLog.parse));
    dataFrame.registerTempTable("ApacheAccessLog");

    sqlContext.sql("select ......")
      .map(row => row.getLong(0))
      .first()
  }
```

Le `SQLContext` est le point d'entrée pour faire du Spark SQL et ainsi éxécuter des requetes SQL.

### Les liens cassés

Nous vous proposons de calculer le nombre de "referer" unique qui ont créé des `404` dans les logs.

Il vous faudra implémenter `process` de `SparkSQL1` et vérifier le test `SparkSQL1Test` et pour cela:
* écrire la requete SQL
* transformer le résultat `long`

**_Conseils_**

_RDD_
* `map` transforme un ligne de résultat en un autre objet, par exemple un long
* `first` renvoie unqiement le premier résultat

_SQL__
* Les fonctions `count` et `distinct` sont disponibles dans Spark SQL

_ApacheAccessLog_
* `referer` est le referer trouvé dans la ligne de log
* `code` est le code http trouvé dans la ligne de log

### Répartition des codes http

Nous vous proposons de calculer la répartition des status codes http dans les logs.

Il vous faudra implémenter la méthode `process` de `SparkSQL2` et vérifier le test `SparkSQL2Test` et pour cela:
* écrire la requete SQL
* transformer chaque ligne de résultat en tuple (`Tuple2` pour Java)

**_Conseils_**

_RRD_
* Java: `mapToPair` transforme un element en une paire, un `Tuple2`, et retourne un `JavaPairRDD<K,V>`
* Scala: `map` transforme un element en un autre
* `collect` retourne une collection qui contient tous les élements de cet RRD

_SQL__
* La fonction `group by` est disponible dans Spark SQL

_ApacheAccessLog_
* Le champs `code` revoit le code http trouvé dans la ligne de log

### Top 3 des familles user agents

Nous vous proposons de calculer le top 3 des user agents.
Il vous faudra implémenter la méthode `process` de `SparkSQL3` et vérifier le test `SparkSQL3Test` et pour cela:
* écrire la requete SQL
* transformer chaque ligne de résultat en `Tuple2`

**_Conseils_**

_RRD_
* Java: `mapToPair` transforme un element en une paire, un `Tuple2`, et retourne un `JavaPairRDD<K,V>`
* Scala: `map` transforme un element en un autre
* `collect` retourne une collection qui contient tous les élements de cet RRD

_SQL__
* `group by` est disponible dans Spark SQL
* `order by xxx asc|desc` est disponible dans Spark SQL
* `limit X` est disponible dans Spark SQL

_ApacheAccessLog_
* `agentFamily` est la famille du user agent trouvé dans la ligne de log

### Top 3 des plages d'IP

Nous vous proposons de calculer le top 3 des plages d'IP.
Il vous faudra implémenter `process` de `SparkSQL4` et vérifier le test `SparkSQL4Test` et pour cela:
* écrire la requete SQL
* transformer chaque ligne de résultat en tuple (`Tuple2` en Java)

**_Conseils_**

_RRD_
* Java: `mapToPair` transforme un element en une paire, un `Tuple2`, et retourne un `JavaPairRDD<K,V>`
* Scala: `map` transforme un element en un autre
* `collect` retourne une collection qui contient tous les élements de cet RRD

_SQL__
* La fonction `group by` est disponible dans Spark SQL
* La fonction `order by xxx asc|desc` est disponible dans Spark SQL
* La fonction `limit X` est disponible dans Spark SQL

_ApacheAccessLog_
* `ipRange` transforme l'IP trouvé dans les logs de `123.4.5.6` en `123.x.x.x`

### Statistiques sur la taille des requêtes

Nous vous proposons de calculer les statistiques sur la taille des requêtes
Il vous faudra implémenter `process` de `SparkSQL5` et vérifier le test `SparkSQL5Test` et pour cela:
* écrire la requete SQL
* transformer chaque ligne de résultat en tuple (`Tuple4` en Java)

**_Conseils_**

_RRD_
* Java: `mapToPair` transforme un element en une paire, un `Tuple4`, et retourne un `JavaPairRDD<K,V>`
* Scala: `map` transforme un element en un autre
* `first` renvoie unqiement le premier résultat

_SQL__
* La fonction `count` est disponible dans Spark SQL
* La fonction `min` est disponible dans Spark SQL
* La fonction `avg` est disponible dans Spark SQL
* La fonction `max` est disponible dans Spark SQL

_ApacheAccessLog_
* `size` est la taille de la requete trouvé dans les logs

## Spark Streaming

Allez encore un effort, c'est presque fini...:)

### Méthodologie

* Ouvrir la classe `SparkStreaming1Test`
* Vérifier que le test est incorrect
* Apporter les modifications nécessaire à la classe `SparkStreaming1`
* Vérifier pour que le test passe correctement, sinon recommencer.


#### Java

Chaque classe a une méthode `process` à compléter:

```java
public void process(String hostname, int port, JavaStreamingContext sc) {
    sc.socketTextStream(hostname, port)
        .map(ApacheAccessLog::parse)
        .xxx()
        .xxx();
}
```

Le `JavaStreamingContext` est le point d'entrée pour les fonctionnalités Spark Streaming.

#### Scala

`process` est à compléter:

```scala
def process: Unit = {
  sc.socketTextStream(hostname, port)
    .xxx
    .xxx
}
```

Le `StreamingContext` est le point d'entrée pour les fonctionnalités Spark Streaming.

### Détecter les '404 Not Found'

Nous vous proposons de détecter en streaming les '404 Not Found' à partir des logs Apache.
Il vous faudra implémenter `process` de `SparkStreaming1` et vérifier le test `SparkStreaming1Test`. Pour chaque ligne recu:
* garder les lignes qui ont un code 404
* les compter
* les imprimer

**_Conseils_**

_RRD_
* `map` transforme un element en un autre
* `filter` filtre un élément suivant une condition
* `count` retourne le nombre d'élement dans le RDD
* `print` affiche le résultat

_ApacheAccessLog_
* `referer` est le referer trouvé dans la ligne de log
