package fr.devoxx.devops.logs.streaming

import java.net.InetSocketAddress
import fr.devoxx.devops.logs.SharedSparkStreamingContext
import org.scalatest.{Matchers, FunSuite}

import scala.concurrent.duration._
import akka.pattern.ask
import akka.io.{Tcp, IO}
import akka.util.Timeout
import akka.actor._

class SparkStreaming1Test extends FunSuite with SharedSparkStreamingContext with Matchers {

  test("Compter le nombre de code http à 404") {
    implicit val system = ActorSystem("tcp-server")
    val server = system.actorOf(Props(new EchoServer), name = "tcp-server")
    val endpoint = new InetSocketAddress("127.0.0.1", 9999)
    implicit val bindingTimeout = Timeout(2.second)
    import system.dispatcher
    val boundFuture = IO(Tcp) ? Tcp.Bind(server, endpoint)
    SparkStreaming1("127.0.0.1", 9999, sc).process
    boundFuture.onSuccess { case Tcp.Bound(address) =>
      println("\nBound tcp-server to " + address)
    }
    sc.start
    sc.awaitTerminationOrTimeout(30000L)

  }
}
