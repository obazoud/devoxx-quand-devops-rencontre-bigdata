package fr.devoxx.devops.logs.sql;

import fr.devoxx.devops.logs.SparkTest;
import org.junit.Test;
import scala.Tuple2;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

public class SparkSQL2Test extends SparkTest {
    @Test
    public void test_spark() {
        SparkSQL2 test = new SparkSQL2();
        List<Tuple2<Integer, Long>> result = test.process(rdd(), sqlContext());
        assertThat(result, hasSize(2));
        assertThat(result, contains(new Tuple2<>(200, 6067L), new Tuple2<>(404, 35L)));
    }

}
