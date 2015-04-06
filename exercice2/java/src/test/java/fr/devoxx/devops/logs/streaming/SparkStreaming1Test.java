package fr.devoxx.devops.logs.streaming;

import org.junit.Test;

public class SparkStreaming1Test extends SparkStreamingTest {

    @Test
    public void test_spark() throws Exception {
        SparkStreaming1 test = new SparkStreaming1();
        test.process("127.0.0.1", 9999, sc);
        sc.start();
        sc.awaitTerminationOrTimeout(30000L);
    }
}
