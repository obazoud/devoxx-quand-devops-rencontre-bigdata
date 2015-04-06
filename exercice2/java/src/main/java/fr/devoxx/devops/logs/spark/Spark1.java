package fr.devoxx.devops.logs.spark;

import fr.devoxx.devops.logs.ApacheAccessLog;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.Serializable;

/* Les liens cassés */
public class Spark1 implements Serializable {

    public long process(JavaRDD<String> rdd) {
        /*
        return rdd.map(ApacheAccessLog::parse)
                .xxx()
                .xxx();
        */
        return 0;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: " + Spark1.class.getName() + " <file>");
            System.exit(1);
        }
        System.err.println("Apache Access Log: " + args[0]);
        SparkConf conf = new SparkConf().setAppName(Spark1.class.getName());
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            Spark1 spark1 = new Spark1();
            long result = spark1.process(sc.textFile(args[0]));
            System.out.println(" -> " + result);
        }
    }
}
