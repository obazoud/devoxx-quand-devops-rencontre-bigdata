package fr.devoxx.devops.logs.sql;

import fr.devoxx.devops.logs.ApacheAccessLog;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import scala.Tuple4;

import java.io.Serializable;

import static java.lang.Double.valueOf;

/* Statistiques sur la taille des requêtes */
public class SparkSQL5 extends SparkSQL {

    public Tuple4<Long, Long, Double, Long> process(JavaRDD<String> rdd, SQLContext sqlContext) {
        JavaRDD<ApacheAccessLog> accessLogs = rdd.map(ApacheAccessLog::parse);
        configure(sqlContext, accessLogs);

        return new Tuple4<>(0L, 0L, 0.0, 0L);
    }

}
