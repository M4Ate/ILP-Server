package com.ilp_server;

import com.ilp_server.solver.Solver;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

public class Task {
    private final JSONObject input;
    private JSONObject output;
    private Status status;
    private final Solver solver;
    private final HttpExchange exchange;

    public Task(JSONObject input, Solver solver, HttpExchange exchange) {
        this.input = input;
        this.status = Status.PENDING;
        this.solver = solver;
        this.output = null;
        this.exchange = exchange;
    }

    public void start() {
        this.status = Status.RUNNING;
        this.output = solver.solve(input);
        this.status = Status.FINISHED;
    }

    public Status getStatus() {
        return status;
    }

    public JSONObject getOutput() {
        return output;
    }

    public HttpExchange getExchange() {
        return exchange;
    }

}
