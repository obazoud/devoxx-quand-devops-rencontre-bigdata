package fr.devoxx.devops.logs.sql;

import fr.devoxx.devops.logs.ApacheAccessLog;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* Top 3 des plages d'IP */
public class SparkSQL4 extends SparkSQL {

    public List<Tuple2<String, Long>> process(JavaRDD<String> rdd, SQLContext sqlContext) {
        JavaRDD<ApacheAccessLog> accessLogs = rdd.map(ApacheAccessLog::parse);
        configure(sqlContext, accessLogs);

        return new ArrayList<>();
    }

}
