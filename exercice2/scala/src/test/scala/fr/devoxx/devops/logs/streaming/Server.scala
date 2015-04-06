package fr.devoxx.devops.logs.streaming

import java.io.File

import java.net.InetSocketAddress
import scala.collection.immutable.Queue
import scala.concurrent.duration._
import akka.pattern.ask
import akka.io.{Tcp, IO}
import akka.util.{ByteString, Timeout}
import akka.actor._

import scala.io.Source

object Main extends App {
  implicit val system = ActorSystem("echo-server")
  val server = system.actorOf(Props(new EchoServer), name = "echo-server")
  val endpoint = new InetSocketAddress("localhost", 9999)
  import system.dispatcher // execution context for the future
  implicit val bindingTimeout = Timeout(5.second)
  val boundFuture = IO(Tcp) ? Tcp.Bind(server, endpoint)
  boundFuture.onSuccess { case Tcp.Bound(address) =>
    println("\nBound echo-server to " + address)
    println("Run `telnet localhost 9999`, type something and press RETURN. Type `STOP` to exit...\n")
  }
}

class EchoServer extends Actor with ActorLogging {
  var childrenCount = 0

  def receive = {
    case Tcp.Connected(_, _) =>
      val tcpConnection = sender
      val newChild = context.watch(context.actorOf(Props(new EchoServerConnection(tcpConnection))))
      childrenCount += 1
      sender ! Tcp.Register(newChild)
      val file = getClass.getClassLoader.getResource(getClass.getPackage.getName.replace('.', '/') + "/../apache-access-log").getFile
      while(true) {
        for (line <- Source.fromFile(file).getLines()) {
          sender ! Tcp.Write(ByteString(line + "\n"))
          Thread.sleep(0L, 250)
        }
      }

    case Terminated(_) if childrenCount > 0 =>
      childrenCount -= 1
      log.debug("Connection handler stopped, another {} connections open", childrenCount)

    case Terminated(_) =>
      log.debug("Last connection handler stopped, shutting down")
      context.system.shutdown()
  }
}

class EchoServerConnection(tcpConnection: ActorRef) extends Actor with ActorLogging {
  context.watch(tcpConnection)

  def receive = idle

  def idle: Receive = stopOnConnectionTermination orElse {
    case Tcp.Connected =>
      log.info("Connected")
      tcpConnection ! Tcp.Write(ByteString("Welcome message...\n"))
      tcpConnection ! Tcp.Close

    case Tcp.Received(data) if data.utf8String.trim == "STOP" =>
      log.info("Shutting down")
      tcpConnection ! Tcp.Write(ByteString("Shutting down...\n"))
      tcpConnection ! Tcp.Close

    case x: Tcp.ConnectionClosed =>
      log.debug("Connection closed: {}", x)
      context.stop(self)
  }

  def waitingForAck: Receive = stopOnConnectionTermination orElse {
    case Tcp.Received(data) =>
      tcpConnection ! Tcp.SuspendReading
      context.become(waitingForAckWithQueuedData(Queue(data)))

    case SentOk =>
      context.become(idle)

    case x: Tcp.ConnectionClosed =>
      log.debug("Connection closed: {}, waiting for pending ACK", x)
      context.become(waitingForAckWithQueuedData(Queue.empty, closed = true))
  }

  def waitingForAckWithQueuedData(queuedData: Queue[ByteString], closed: Boolean = false): Receive =
    stopOnConnectionTermination orElse {
      case Tcp.Received(data) =>
        context.become(waitingForAckWithQueuedData(queuedData.enqueue(data)))

      case SentOk if queuedData.isEmpty && closed =>
        log.debug("No more pending ACKs, stopping")
        tcpConnection ! Tcp.Close
        context.stop(self)

      case SentOk if queuedData.isEmpty =>
        tcpConnection ! Tcp.ResumeReading
        context.become(idle)

      case SentOk =>
        // for brevity we don't interpret STOP commands here
        tcpConnection ! Tcp.Write(queuedData.head, ack = SentOk)
        context.become(waitingForAckWithQueuedData(queuedData.tail, closed))

      case x: Tcp.ConnectionClosed =>
        log.debug("Connection closed: {}, waiting for completion of {} pending writes", x, queuedData.size)
        context.become(waitingForAckWithQueuedData(queuedData, closed = true))
    }

  def stopOnConnectionTermination: Receive = {
    case Terminated(`tcpConnection`) =>
      log.debug("TCP connection actor terminated, stopping...")
      context.stop(self)
  }

  object SentOk extends Tcp.Event
}
