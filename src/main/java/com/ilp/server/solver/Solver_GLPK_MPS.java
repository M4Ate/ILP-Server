package com.ilp.server.solver;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class Solver_GLPK_MPS extends Solver_GLPK {
    @Override
    protected String translateToGLPK(JSONObject input) throws JSONException {
        JSONArray vars = input.getJSONArray("variables");
        JSONArray constraints = input.getJSONArray("constraints");
        String optimizationFunc = input.get("optimizationFunction").toString();
        StringBuilder sb = new StringBuilder();

        // NAME section
        sb.append("NAME          PROBLEM\n");

        // ROWS section
        sb.append("ROWS\n");
        sb.append(" N  OBJ\n");
        for (int i = 0; i < constraints.length(); i++) {
            sb.append(" L  C_");
            sb.append(i);
            sb.append("\n");
        }

        // COLUMNS section
        sb.append("COLUMNS\n");
        for (Object var : vars) {
            String varName = var.toString();

            // Add variable in the correct column
            sb.append(padding(4));
            sb.append(varName);
            sb.append(padding(10 - varName.length()));


            for (int i = 0; i < constraints.length(); i++) {
                Object constraint = constraints.get(i);
                if (constraint.toString().contains(varName)) {
                    sb.append("C_");
                    sb.append(i);
                    sb.append(padding(19 - String.valueOf(i).length()));
                    sb.append("1");
                    sb.append(padding(3));
                }
            }

            if (optimizationFunc.contains(varName)) {
                sb.append("    OBJ     1");
            }
            sb.append("\n");
        }

        // RHS section
        sb.append("RHS\n");
        for (int i = 0; i < constraints.length(); i++) {
            Object constraint = constraints.get(i);
            String rhsValue = constraint.toString().split("=")[1].trim();
            sb.append("    RHS    C_");
            sb.append(i);
            sb.append("    ");
            sb.append(rhsValue);
            sb.append("\n");
        }

        // BOUNDS section
        sb.append("BOUNDS\n");
        for (Object var : vars) {
            sb.append(" UI BND    ");
            sb.append(var.toString());
            sb.append("    1\n");
        }

        sb.append("ENDATA\n");


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
        ProcessBuilder pb = new ProcessBuilder("glpsol", "--freemps", inputFileName, "-o", outputFileName);
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

    private static String padding(int padding) {
        return " ".repeat(Math.max(0, padding));
    }
}
