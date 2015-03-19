
# Setup de l'environnement Spark/Scala

### Préalable : Installation Scala

Pour fonctionner spark 1.3 requiert scala 2.11

```sh
$ sudo apt-get install openjdk-7-jre

$ sudo apt-get remove scala-library scala
$ sudo wget www.scala-lang.org/files/archive/scala-2.11.1.deb
$ sudo dpkg -i scala-2.11.1.deb
$ sudo apt-get install scala
$ sudo apt-get update
$ sudo apt-get -f install
```

### Vérification du bon focntionnement de SBT
Le package "usb" comprend la distribution Spark 1.3, sbt et un squelette d'application copilable avec sbt. A noter que le cache sbt est déjà chargé avec tout ce qui est nécessaire pour compiler une application Spark.

Pour vérifier le bon fonctionnement de votre installation, nous allons compiler et executer cette application. 

```sh
$ cd /path/to/usb/simple-app
$ ../sbt/sbt package
$ ../spark/bin/spark-submit --class "SimpleApp" --master local[*] target/scala-2.11/simple-project_2.11-1.0.jar
```

