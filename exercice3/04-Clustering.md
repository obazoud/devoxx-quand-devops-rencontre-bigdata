# Utiliser le MLLib pour Clusteriser

Pour regrouper les différentes sessions en fonction de leur comportements nous allons utiliser le K-Means. Spark dispose de MLLib qui permet d'executer de manière distribuée la plupart des algorithmes courants de Machine Learning.

Le K-Means est basé sur le calcul de distances entre des points dans un espace multidimensionnel, et permet de déterminer facilement des classes de similarités.

### Import MLLib & Co

```
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import scala.collection.mutable.ArrayBuffer
``` 

### Préparer les données
Le K-Means ne peut se faire que sur des données numériques, il faut donc extraire celles-ci, et enlever les colonnes textuelles.

```
var data = sc.textFile("/vagrant/data/kddcup10.data")

val vect = data.map { l =>
 val buffer = ArrayBuffer[String]()
 buffer.appendAll(l.split(","))
 buffer.remove(1,3)
 buffer.remove(buffer.length-1)
 val vector = Vectors.dense(buffer.toArray.map(_.toDouble))
 (vector)
}

vect.cache()

vect.take(1)
``` 

### Apprentissage
On considère un nombre arbitraire de clusters (10), qu'on va caratériser au travers de 20 itérations. Merci les données en cache !

```
val numClusters = 10
val numIterations = 20
val clusters = KMeans.train(vect, numClusters, numIterations)

//a quoi ressemble de KMeansModel ?
clusters.clusterCenters

//combien de centres ?
clusters.clusterCenters.length

// combien de dimensions ?
clusters.clusterCenters(0).size

// identique au vecteur d'origine ?
vect.take(1)(0).size


// Evaluation de la performance au travers du WSSSE
val WSSSE = clusters.computeCost(vect)
println("Within Set Sum of Squared Errors = " + WSSSE)
``` 

### Prédiction
Pour chaque enregistrement on le confronte au model pour déterminer sa classe d'appartenance.

```
val affectations = vect.map {l => clusters.predict(l)}

affectations.map(s => (s,1))
            .reduceByKey((a,b) => a+b)
            .map(_.swap)
            .top(10)
```

### Déterminer le K
Le nombre de classe est souvent l'inconnue, et dans ce cas il faut le déterminer par une démarche itérative. Le K qui donne le meilleur score est le bon !

```
var res = new Array[Double](100);
var x = 0

for(i <- 2 to 100) {
	val clusters = KMeans.train(vect, i, 20)
 	res(x) = clusters.computeCost(vect); 
 	x = x+1;
};
```

### Avec des colonnes catégorielles
On souhaite utiliser la colonne service comme critère de segmentation. Il faut la rendre numérique pour qu'elle puisse être utilisée pour calculer une distance.

```
val vect = data.map { l =>
 val buffer = ArrayBuffer[String]()
 val l2 = l.split(",")
 buffer.appendAll(l2)
 buffer.remove(buffer.length-1)
 buffer.remove(1,3)
 buffer.append(if(l2(2)=="http") "1" else "0")
 buffer.append(if(l2(2)=="smtp") "1" else "0")
 buffer.append(if(l2(2)=="ftp") "1" else "0")
 val vector = Vectors.dense(buffer.toArray.map(_.toDouble))
 (vector)
}
```

### Pour améliorer ce modèle...
Ce que nous venons de faire est assez naif et reprend les grands principes d'une segmentation de donnnées. Ce modèle devra être améliorer : 

* Evaluer les correlations entre colonnes
* Mesurer l'entropie des colonnes sélectionnées
* Scaller les colonnes, pour eviter qu'une écrase les autres
* Pondérer les colonnes
* Entrainer le modèle sur un jeu de données et le tester sur un autre.
* Tester différents paramètres et options.

D'autres techniques permettent de classifier des enregistrements : Random Forests, SVM, etc...

Ce type de modèles peuvent parfaitement être appliquées à des données qui seraient streamées pour détecter des anomalies en temps réel.
