package fr.devoxx.devops.logs.spark;

import fr.devoxx.devops.logs.ApacheAccessLog;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.StatCounter;
import scala.Tuple4;

import java.io.Serializable;

/* Statistiques sur la taille des requêtes */
public class Spark5 implements Serializable {

    public StatCounter process(JavaRDD<String> rdd) {
        return new StatCounter();
    }

}
