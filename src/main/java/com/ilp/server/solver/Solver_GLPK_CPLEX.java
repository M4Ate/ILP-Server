package com.ilp.server.solver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class Solver_GLPK_CPLEX extends Solver_GLPK {
    @Override
    protected String translateToGLPK(JSONObject input) throws JSONException {
        JSONArray vars = input.getJSONArray("variables");
        JSONArray constraints = input.getJSONArray("constraints");
        String optimizationFunc = input.get("optimizationFunction").toString();
        StringBuilder sb = new StringBuilder();

        sb.append("Minimize\n");
        sb.append("    obj: ");
        sb.append(optimizationFunc);
        sb.append("\n");
        sb.append("Subject To\n");
        for (int i = 0; i < constraints.length(); i++) {
            sb.append("    c_");
            sb.append(i);
            sb.append(": ");
            sb.append(constraints.get(i));
            sb.append("\n");
        }
        sb.append("Bounds\n");
        for (Object var : vars) {
            sb.append("    ");
            sb.append(var.toString());
            sb.append(" <= 1\n");
        }
        sb.append("General\n");
        for (Object var : vars) {
            sb.append("    ");
            sb.append(var.toString());
            sb.append("\n");
        }
        sb.append("End\n");

        return sb.toString();
    }

    @Override
    protected String callGLPK(String input) throws IOException, InterruptedException {

        // Randomize file names to avoid conflicts
        int random = (int) (Math.random() * 1000000);
        String inputFileName = "input" + random + ".temp";
        String outputFileName = "output" + random + ".temp";
        // Write input to file
        File file = new File(inputFileName);

        FileWriter writer = new FileWriter(file);
        writer.write(input);
        writer.close();

        // Call GLPK
        ProcessBuilder pb = new ProcessBuilder("glpsol", "--lp", inputFileName, "-o", outputFileName);
        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        pb.redirectError(ProcessBuilder.Redirect.DISCARD);
        Process p = pb.start();
        int exitCode = p.waitFor();

        if (exitCode != 0) {
            throw new IOException("GLPK exited with error code " + exitCode);
        }

        // Read output from file
        File outputFile = new File(outputFileName);
        BufferedReader reader = new BufferedReader(new FileReader(outputFile));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null){
            sb.append(line );
            sb.append("\n");
        }
        reader.close();

        boolean fd = file.delete();
        boolean ofd = outputFile.delete();

        if (!fd || !ofd) {
            System.out.println("A temporary file failed to be deleted, should not be an issue.");
        }

        return sb.toString();
    }
}