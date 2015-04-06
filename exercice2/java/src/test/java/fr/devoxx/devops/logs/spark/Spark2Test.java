package fr.devoxx.devops.logs.spark;

import fr.devoxx.devops.logs.SparkTest;
import org.junit.Test;
import scala.Tuple2;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Spark2Test extends SparkTest {
    @Test
    public void test_spark() {
        Spark2 test = new Spark2();
        List<Tuple2<Integer, Long>> result = test.process(rdd());
        assertThat(result, hasSize(2));
        assertThat(result, containsInAnyOrder(new Tuple2<>(404, 35L), new Tuple2<>(200, 6067L)));
    }

}
