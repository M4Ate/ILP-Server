package com.ilp_server.solver;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.*;


/**
 * Implementation for the GLPK ILP Solver.
 * @author maxroll
 */

public class Solver_GLPK implements Solver{
    public JSONObject solve(JSONObject input){
        String glpkInput = translateToGLPK(input);
        try {
            String glpkOutput = callGLPK(glpkInput);
            return translateFromGLPK(glpkOutput);
        } catch (IOException | InterruptedException e){
            return new JSONObject("{\"solution\": \"error\"}");
        }
    }

    /**
     * Translates the given JSON object to a GLPK input file.
     * @param input The JSON object to translate.
     * @return The GLPK input file.
     */
    private String translateToGLPK(JSONObject input){
        JSONArray vars = input.getJSONArray("variables");
        JSONArray constraints = input.getJSONArray("constraints");
        String optimizationFunc = input.get("optimizationFunction").toString();
        StringBuilder sb = new StringBuilder();
        for (Object var : vars){
            sb.append("var ");
            sb.append(var.toString());
            sb.append(" >= 0;\n");
        }
        for (int i = 0; i < constraints.length(); i++){
            sb.append("s.t. ");
            sb.append("c_");
            sb.append(i);
            sb.append(": ");
            sb.append(constraints.getString(i));
            sb.append(";\n");
        }
        sb.append("maximize obj: ");
        sb.append(optimizationFunc);
        sb.append(";\n");
        sb.append("end;");
        return sb.toString();
    }

    /**
     * Translates the given GLPK output to a JSON object.
     * @param output The GLPK output to translate.
     * @return The JSON object.
     */
    private JSONObject translateFromGLPK(String output){


        return new JSONObject("{\"solution\": \"not implemented\"}");
    }

    private String callGLPK(String input) throws IOException, InterruptedException{
        // Write input to file
        File file = new File("input.temp");

        FileWriter writer = new FileWriter(file);
        writer.write(input);
        writer.close();

        // Call GLPK
        ProcessBuilder pb = new ProcessBuilder("glpsol", "--math", "input.temp", "-o", "output.temp");
        pb.start();
        pb.wait();

        // Read output from file
        File outputFile = new File("output.temp");
        BufferedReader reader = new BufferedReader(new FileReader(outputFile));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null){
            sb.append(line);
        }
        reader.close();

        file.delete();
        outputFile.delete();

        return sb.toString();
    }
}
