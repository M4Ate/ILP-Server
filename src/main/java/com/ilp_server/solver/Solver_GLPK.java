package com.ilp_server.solver;

import org.json.JSONObject;

/**
 * Implementation for the GLPK ILP Solver.
 * @author maxroll
 */

public class Solver_GLPK implements Solver{
    public JSONObject solve(JSONObject input){

        return new JSONObject("{\"solution\": \"not implemented\"}");   //TODO: Implement this method
    }

    /**
     * Translates the given JSON object to a GLPK input file.
     * @param input The JSON object to translate.
     * @return The GLPK input file.
     */
    private String translateToGLPK(JSONObject input){
        return null;
    }

    /**
     * Translates the given GLPK output to a JSON object.
     * @param output The GLPK output to translate.
     * @return The JSON object.
     */
    private JSONObject translateFromGLPK(String output){
        return null;
    }
}
