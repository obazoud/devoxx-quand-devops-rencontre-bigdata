package fr.devoxx.devops.logs.sql;

import fr.devoxx.devops.logs.ApacheAccessLog;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

import java.io.Serializable;

public class SparkSQL implements Serializable {

    protected void configure(SQLContext sqlContext, JavaRDD<ApacheAccessLog> accessLogs) {
        DataFrame dataFrame = sqlContext.createDataFrame(accessLogs, ApacheAccessLog.class);
        dataFrame.registerTempTable("ApacheAccessLog");
        sqlContext.cacheTable("ApacheAccessLog");
    }

}
