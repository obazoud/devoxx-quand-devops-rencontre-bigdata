package fr.devoxx.devops.logs;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.junit.After;
import org.junit.Before;

public class SparkTest {
    private JavaSparkContext sc;
    private JavaRDD<String> rdd;
    private SQLContext sqlContext;

    @Before
    public void before() {
        SparkConf conf = new SparkConf().setAppName(this.getClass().getName()).setMaster("local[2]");
        sc = new JavaSparkContext(conf);
        rdd = sc.textFile(file());
        sqlContext = new SQLContext(sc);
    }

    @After
    public void after() {
        sc.stop();
    }

    protected JavaRDD<String> rdd() {
        return rdd;
    }

    public SQLContext sqlContext() {
        return sqlContext;
    }

    protected String file() {
        return Thread.currentThread()
                .getContextClassLoader()
                .getResource(this.getClass().getPackage().getName().replace('.', '/') + "/../apache-access-log")
                .getFile();
    }
}
