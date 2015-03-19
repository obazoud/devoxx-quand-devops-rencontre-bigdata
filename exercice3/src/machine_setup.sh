#machine setup

sudo apt-get install openjdk-7-jre


sudo apt-get remove scala-library scala
sudo wget www.scala-lang.org/files/archive/scala-2.10.4.deb
sudo dpkg -i scala-2.10.4.deb
sudo apt-get install scala
sudo apt-get update
sudo apt-get -f install


wget http://scalasbt.artifactoryonline.com/scalasbt/sbt-native-packages/org/scala-sbt/sbt/0.12.4/sbt.deb
sudo dpkg -i sbt.deb
sudo apt-get update
sudo apt-get install sbt
sudo apt-get -f install


ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys


cd /vagrant/usb/spark

bin/spark-shell --master local[2]


sbin/start-all.sh
MASTER=spark://precise64:7077 bin/spark-shell

