package fr.devoxx.devops.logs.spark;

import fr.devoxx.devops.logs.ApacheAccessLog;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* Top 3 des familles user agents */
public class Spark3 implements Serializable {

    public List<Tuple2<String, Long>> process(JavaRDD<String> rdd) {
        return new ArrayList<>();
    }

}
