package fr.devoxx.devops.logs.sql;

import fr.devoxx.devops.logs.SparkTest;
import org.junit.Test;
import scala.Tuple2;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

public class SparkSQL3Test extends SparkTest {
    @Test
    public void test_spark() {
        SparkSQL3 test = new SparkSQL3();
        List<Tuple2<String, Long>> result = test.process(rdd(), sqlContext());
        assertThat(result, hasSize(3));
        assertThat(result, contains(
                new Tuple2<>("IE", 2269L),
                new Tuple2<>("Firefox", 1601L),
                new Tuple2<>("Chrome", 1509L)
        ));
    }

}
