## Devoxx 2015: Quand DevOps rencontre BigData!

### Hand's on Labs

Ce [Hand's on Labs](http://cfp.devoxx.fr/2015/talk/QTQ-9573/Quand_DevOps_rencontre_BigData!) a été sélectionné

Cette session est l’occasion de rapprocher le monde DevOps et le BigData!
Venez découvrir [Apache Spark](https://spark.apache.org/) à travers l'analyse de logs et de la détection d'anomalies de trames réseaux.

Chaque étape de ce lab (sauf le REPL) peut se faire en Java soit en Scala, à vous de choisir!

* Après une introduction à Apache Spark, vous utiliserez le REPL d'Apache Spark en Scala pour manipuler les notions de bases.
* Plus de serveurs, c'est aussi plus de logs à analyser. Vous analyserez les access logs d'Apache en calculant la répartition des codes http et le top N des navigateurs, des IP, ... à l'aide successivement de Spark, [Spark SQL](https://spark.apache.org/sql/) et [Spark Streaming](https://spark.apache.org/streaming/) afin de voir les avantages de chacun.
* Plus de serveurs, c'est aussi plus de problème réseaux. Venez découvrir comment détecter des anomalies de type ralentissement réseaux à travers l'analyse de trames réseaux avec [Spark ML](https://spark.apache.org/mllib/).
* Les présentateurs feront une démo dans le cloud sur de plus grands volumes.
* Enfin, vous pourrez déployer votre code sur le cluster Spark pour faire des tests à plus grande échelle.

Prérequis: JVM 8, Scala 2.11, Spark 1.3, Git 2, un IDE

Github: https://github.com/obazoud/devoxx-quand-devops-rencontre-bigdata

### Exercice 1

[Introduction à Spark](exercice1/README.md)

### Exercice 2

[Analyse des access logs Apache](exercice2/README.md)

### Exercice 3

[Analyse de trame réseaux](exercice3/00-environnement.md)

### Exercice 4

[Déploiement sur le cluster](exercice4/README.md)
