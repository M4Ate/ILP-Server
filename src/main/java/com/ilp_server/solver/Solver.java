package com.ilp_server.solver;
import org.json.JSONObject;

/**
 * Interface for an ILP Solver.
 * @author maxroll
 */

public interface Solver {

    /**
     * Solves the given ILP problem.
     * @param input The ILP problem to solve.
     * @return The solution to the ILP problem.
     */
    public JSONObject solve(JSONObject input);



}
