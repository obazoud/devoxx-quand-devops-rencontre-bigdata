package fr.devoxx.devops.logs.streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.junit.After;
import org.junit.Before;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;

public class SparkStreamingTest {
    protected JavaStreamingContext sc;
    protected SimpleSocketServer socketServer;

    @Before
    public void before() {
        SparkConf conf = new SparkConf().setAppName(this.getClass().getName()).setMaster("local[3]");
        sc = new JavaStreamingContext(conf, Durations.seconds(5));

        socketServer = new SimpleSocketServer(9999);
        socketServer.startServer();
    }

    @After
    public void after() {
        sc.stop();
        socketServer.stopServer();
    }

    protected String file() {
        return Thread.currentThread()
                .getContextClassLoader()
                .getResource(this.getClass().getPackage().getName().replace('.', '/') + "/../apache-access-log")
                .getFile();
    }

    public class SimpleSocketServer extends Thread {
        private ServerSocket serverSocket;
        private int port;
        private boolean running = false;

        public SimpleSocketServer(int port) {
            this.port = port;
        }

        public void startServer() {
            try {
                serverSocket = new ServerSocket(port);
                this.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void stopServer() {
            running = false;
            this.interrupt();
        }

        @Override
        public void run() {
            running = true;
            while (running) {
                try {
                    System.out.println("Listening for a connection");
                    Socket socket = serverSocket.accept();
                    RequestHandler requestHandler = new RequestHandler(socket);
                    requestHandler.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class RequestHandler extends Thread {
        private Socket socket;

        RequestHandler(Socket socket) throws Exception {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                System.out.println("Received a connection");
                PrintWriter out = new PrintWriter(socket.getOutputStream());

                while (true) {
                    Files.lines(new File(file()).toPath()).forEach(line -> {
                        out.println(line);
                        out.flush();
                        try {
                            Thread.sleep(0L, 250);
                        } catch (InterruptedException e) {
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
