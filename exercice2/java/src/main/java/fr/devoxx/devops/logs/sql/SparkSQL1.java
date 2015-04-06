package fr.devoxx.devops.logs.sql;

import fr.devoxx.devops.logs.ApacheAccessLog;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;

/* Les liens cassés */
public class SparkSQL1 extends SparkSQL {

    public long process(JavaRDD<String> rdd, SQLContext sqlContext) {
        JavaRDD<ApacheAccessLog> accessLogs = rdd.map(ApacheAccessLog::parse);
        configure(sqlContext, accessLogs);

        /*
        return sqlContext.sql("select ...")
                .toJavaRDD()
                .map(...)
                .xxx();
        */
        return 0;
    }

}
