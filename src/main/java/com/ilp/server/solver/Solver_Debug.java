package com.ilp.server.solver;

import org.json.JSONObject;

public class Solver_Debug implements Solver {

    @Override
    public JSONObject solve(JSONObject input) {
        System.out.println(input);
        return new JSONObject("{\"error\": true, \"errorMessage\": \"Your using the debug solver, this solver does not solve anything.\", \"result\": []}");
    }
}
