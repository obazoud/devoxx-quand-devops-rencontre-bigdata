# Exploration avec le Spark-SQL

Plutôt que d'écrire du code scala pour requeter les données on peut créer une structure de données et écrire des requetes SQL dessus.

### Créer la structure de données : 

```
import sqlContext.implicits._

var data = sc.textFile("/vagrant/data/kddcup10.data")

case class Line(proto:String,service:String,hit:Integer)

val hits = data.map(_.split(","))
               .map(l => Line(l(1),l(2),1))
               .toDF()

hits.registerTempTable("hits")
```

### Requêter avec SQL 

```
val counts = sqlContext.sql("SELECT service, COUNT(*) c FROM hits GROUP BY service ORDER BY c DESC LIMIT 10")

counts.collect().foreach(println)
```
