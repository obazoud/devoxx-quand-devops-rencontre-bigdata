///////////////
////Clustering

import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import scala.collection.mutable.ArrayBuffer

var data = sc.textFile("/vagrant/data/kddcup10.data")

val vect = data.map { l =>
 val buffer = ArrayBuffer[String]()
 buffer.appendAll(l.split(","))
 buffer.remove(1,3)
 buffer.remove(buffer.length-1)
 val vector = Vectors.dense(buffer.toArray.map(_.toDouble))
 (vector)
}

vect.take(1)

// Cluster the data into two classes using KMeans
val numClusters = 10
val numIterations = 20
val clusters = KMeans.train(vect, numClusters, numIterations)

// Evaluate clustering by computing Within Set Sum of Squared Errors
val WSSSE = clusters.computeCost(vect)
println("Within Set Sum of Squared Errors = " + WSSSE)


var res = new Array[Double](100);
var x = 0

for(i <- 2 to 10) {
	val clusters = KMeans.train(vect, i, 20)
 	res(x) = clusters.computeCost(vect); 
 	x = x+1;
};

val affectations = vect.map {l => clusters.predict(l)}


