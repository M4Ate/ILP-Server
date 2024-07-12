package com.ilp.server.solver;
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
    JSONObject solve(JSONObject input);



}
