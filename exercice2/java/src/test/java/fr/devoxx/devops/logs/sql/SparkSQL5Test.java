package fr.devoxx.devops.logs.sql;

import fr.devoxx.devops.logs.SparkTest;
import org.junit.Test;
import scala.Tuple4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

public class SparkSQL5Test extends SparkTest {
    @Test
    public void test_spark() {
        SparkSQL5 test = new SparkSQL5();
        Tuple4<Long, Long, Double, Long> result = test.process(rdd(), sqlContext());
        assertThat(result._1(), is(6102L));
        assertThat(result._2(), is(40L));
        assertThat(result._3(), closeTo(90.044, 0.001));
        assertThat(result._4(), is(139L));
    }

}
