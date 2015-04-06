package fr.devoxx.devops.logs.sql;

import fr.devoxx.devops.logs.SparkTest;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SparkSQL1Test extends SparkTest {
    @Test
    public void test_spark() {
        SparkSQL1 test = new SparkSQL1();
        long result = test.process(rdd(), sqlContext());
        assertThat(result, is(25L));
    }
}
