
Master: ./sbin/start-master.sh
Slave: ./bin/spark-class org.apache.spark.deploy.worker.Worker spark://olivier:7077 --cores 2 --memory 3G
Spark shell: ./bin/spark-shell --master spark://olivier:7077
Deploiement: ./bin/spark-submit --master spark://olivier:7077 --class fr.devoxx.devops.logs.spark.Spark1 --deploy-mode cluster spark-logs-1.0-SNAPSHOT-jar-with-dependencies.jar /tmp/apache.access10M.log
