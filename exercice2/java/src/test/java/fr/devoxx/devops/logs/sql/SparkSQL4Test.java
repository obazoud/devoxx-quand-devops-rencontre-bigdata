package fr.devoxx.devops.logs.sql;

import fr.devoxx.devops.logs.SparkTest;
import org.junit.Test;
import scala.Tuple2;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

public class SparkSQL4Test extends SparkTest {
    @Test
    public void test_spark() {
        SparkSQL4 test = new SparkSQL4();
        List<Tuple2<String, Long>> result = test.process(rdd(), sqlContext());
        assertThat(result, hasSize(3));
        assertThat(result, contains(
                new Tuple2<>("140.x.x.x", 165L),
                new Tuple2<>("84.x.x.x", 163L),
                new Tuple2<>("48.x.x.x", 157L)
        ));
    }

}
