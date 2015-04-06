package fr.devoxx.devops.logs.spark;

import fr.devoxx.devops.logs.SparkTest;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class Spark1Test extends SparkTest {
    @Test
    public void test_spark() {
        Spark1 test = new Spark1();
        long result = test.process(rdd());
        assertThat(result, is(25L));
    }
}
