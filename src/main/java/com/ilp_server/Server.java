package com.ilp_server;

import com.ilp_server.solver.Solver;
import com.ilp_server.solver.Solver_GLPK;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;


public class Server {
    private static final int WORKER_THREADS = 1;
    private static BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private static final Solver solver = new Solver_GLPK();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/post", new WebHandler());
        server.setExecutor(null);
        server.start();

        Runnable queueHandler = () -> {
            while (true) {
                try {
                    Task task = taskQueue.take();
                    task.start();
                    if(task.getStatus() == Status.FINISHED){
                        JSONObject output = task.getOutput();
                        String response = output.toString();
                        task.getExchange().sendResponseHeaders(200, response.length());
                        OutputStream outputStream = task.getExchange().getResponseBody();
                        outputStream.write(response.getBytes());
                        outputStream.close();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch (IOException e){

                }
            }
        };

        ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(WORKER_THREADS);
        executor.execute(queueHandler);



        System.out.println("Server started on port 8000");
    }

    static class WebHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Read the request body
                String response;

                try {
                    JSONObject json = getJson(exchange);
                    Task task = new Task(json, solver, exchange);
                    taskQueue.add(task);    // currently not used
                } catch (IOException e) {
                    response = "Error reading the request body";
                    exchange.sendResponseHeaders(400, response.length());
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(response.getBytes());
                    outputStream.close();
                }

            } else {
                // Return a 405 Method Not Allowed error for other request methods
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    private static JSONObject getJson(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        // Parse the request body as JSON
        return new JSONObject(sb.toString());
    }
}
