package com.ilp.server;

import com.ilp.server.solver.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.JSONObject;


/**
 * Server class that handles incoming HTTP requests.
 *
 */

public class Server {
    private static final int WORKER_THREADS = 4;
    private static final int DEFAULT_PORT = 1337;
    private static final String DEFAULT_SOLVER = "HiGHS";
    private static final BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
    private static Solver solver;

    /**
     * Main method that starts the server.
     *
     * @param args command line arguments, the first argument is the port number
     * @throws IOException if an I/O error occurs
     */
    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {
        int port = getPort(args);
        String solverType = getSolverType(args);
        switch (solverType) {
            case "GLPK":
            case "glpk":
                solver = new Solver_GLPK();
                break;

            case "Debug":
                solver = new Solver_Debug();
                break;

            default:
                System.out.println("Invalid solver type: " + solverType + ". Using HiGHS instead.");
            case "HiGHS":
            case "highs":
                solver = new Solver_HiGHS_CPLEX();
                break;

            case "any":
                if (solverInstalled(new Solver_HiGHS_CPLEX())) {
                    solver = new Solver_HiGHS_CPLEX();
                } else if (solverInstalled(new Solver_GLPK())) {
                    solver = new Solver_GLPK();
                } else {
                    System.out.println("No solver found. Exiting.");
                    System.exit(1);
                }
        }

        if (!solver.isAvailable()) {
            System.out.println("Solver not available. Exiting.");
            System.exit(1);
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/post", new WebHandler());
        server.setExecutor(null);
        server.start();

        Runnable queueHandler = () -> {
            while (true) {
                try {
                    Task task = taskQueue.take();
                    long startTime = System.currentTimeMillis();
                    task.start();
                    long endTime = System.currentTimeMillis();
                    System.out.println("Task completed in " + (endTime - startTime) + "ms");

                    JSONObject output = task.getOutput();
                    String response = output.toString();
                    task.getExchange().sendResponseHeaders(200, response.length());
                    OutputStream outputStream = task.getExchange().getResponseBody();
                    outputStream.write(response.getBytes());
                    outputStream.close();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        };

        ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(WORKER_THREADS);
        for (int i = 0; i < WORKER_THREADS; i++) {
            executor.execute(queueHandler);
        }

        System.out.println("Server started on port " + port);
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
                    taskQueue.add(task);
                    System.out.println("Received new task");
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

    private static int getPort(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p") && i + 1 < args.length) {
                return Integer.parseInt(args[i + 1]);
            }
        }
        return DEFAULT_PORT;
    }

    private static String getSolverType(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-s") && i + 1 < args.length) {
                return args[i + 1];
            }
        }
        return DEFAULT_SOLVER;
    }

    private static boolean solverInstalled(Solver solver) {
        return solver.isAvailable();
    }
}
