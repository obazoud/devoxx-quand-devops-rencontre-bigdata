package fr.devoxx.devops.logs.spark;

import fr.devoxx.devops.logs.SparkTest;
import org.apache.spark.util.StatCounter;
import org.junit.Test;
import scala.Tuple4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

public class Spark5Test extends SparkTest {
    @Test
    public void test_spark() {
        Spark5 test = new Spark5();
        StatCounter stats = test.process(rdd());
        assertThat(stats.count(), is(6102L));
        assertThat(stats.min(), is(40.0));
        assertThat(stats.mean(), closeTo(90.044, 0.001));
        assertThat(stats.max(), is(139.0));
    }

}
