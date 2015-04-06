package fr.devoxx.devops.logs.streaming;

import fr.devoxx.devops.logs.ApacheAccessLog;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.io.Serializable;

/**
 * Compter le nombre de code http à 404.
 */
public class SparkStreaming1 implements Serializable {

    public void process(String hostname, int port, JavaStreamingContext sc) {
        /*
        sc.socketTextStream(hostname, port)
                .xxx();
        */
    }

}
