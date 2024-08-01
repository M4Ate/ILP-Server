package com.ilp.server.solver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

public class Solver_HiGHS_CPLEX implements Solver {


        /**
     * Solves the given problem using the GLPK ILP solver.
     *
     * @param input The JSON representation of the problem.
     * @return The result of the solver.
     */
    public JSONObject solve(JSONObject input) {
        try {
            String glpkInput = translateToGLPK(input);
            String glpkOutput = callSolver(glpkInput);
            return translateFromGLPK(glpkOutput);
        } catch (IOException | InterruptedException | JSONException e) {
            return errorMsg(e.getMessage().replace("\"", "\\\""));
        }
    }

    /**
     * Translates the given JSON object to a GLPK input file.
     *
     * @param input The JSON object to translate.
     * @return The GLPK input file.
     */
    protected String translateToGLPK(JSONObject input) throws JSONException {
        JSONArray vars = input.getJSONArray("variables");
        JSONArray constraints = input.getJSONArray("constraints");
        String optimizationFunc = input.get("optimizationFunction").toString();

        optimizationFunc = optimizationFunc.replace("*", "");   // .lp format doesnt need the * operator
        for (int i = 0; i < constraints.length(); i++) {
            String constraint = constraints.getString(i);
            constraint = constraint.replace("*", "");   // .lp format doesnt need the * operator
            constraints.put(i, constraint);
        }

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

    /**
     * Translates the given GLPK output to a JSON object.
     *
     * @param output The GLPK output to translate.
     * @return The JSON object.
     */
    private JSONObject translateFromGLPK(String output) {
        ArrayList<String> columns = parseColumnSection(output);

        // Could be prettier
        ArrayList<String> namesTemp = new ArrayList<>();
        ArrayList<String> valuesTemp = new ArrayList<>();
        for (String column : columns) {
            String[] parts = column.trim().split(" ");
            namesTemp.add(parts[0]);
            valuesTemp.add(parts[1]);
        }

        String[] names = new String[namesTemp.size()];
        String[] values = new String[valuesTemp.size()];
        names = namesTemp.toArray(names);
        values = valuesTemp.toArray(values);

        return convertToJSON(names, values);
    }

    private ArrayList<String> parseColumnSection(String output) {
        String[] lines = output.split("\n");
        boolean inColumnSection = false;
        ArrayList<String> columns = new ArrayList<>();
        for (String line : lines){
            if (line.startsWith("# Rows")) {
                inColumnSection = false;
            }

            if (inColumnSection) {
                columns.add(line);
            }

            if (line.startsWith("# Columns")) {
                inColumnSection = true;
            }
        }

        return columns;
    }

    private JSONObject convertToJSON(String[] names, String[] values) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", false);
        jsonObject.put("errorMessage", "");

        JSONArray resultArray = new JSONArray();

        for (int i = 0; i < names.length; i++) {
            JSONObject variableObject = new JSONObject();
            variableObject.put("variable", names[i]);
            variableObject.put("value", values[i]);
            resultArray.put(variableObject);
        }

        jsonObject.put("result", resultArray);
        return jsonObject;
    }


    protected String callSolver(String input) throws IOException, InterruptedException {

        // Randomize file names to avoid conflicts
        int random = (int) (Math.random() * 1000000);
        String inputFileName = "input" + random + ".lp";
        String outputFileName = "output" + random + ".tmp";
        // Write input to file
        File file = new File(inputFileName);

        FileWriter writer = new FileWriter(file);
        writer.write(input);
        writer.close();
        // Make sure options file is present

        File optionsFile = new File("options");
        if (optionsFile.createNewFile()) {
            FileWriter optionsWriter = new FileWriter(optionsFile);
            optionsWriter.write("""
                    parallel = on
                    threads =  16""");
            optionsWriter.close();
        }


        // Call GLPK
        ProcessBuilder pb = new ProcessBuilder("highs", inputFileName, "--solution_file", outputFileName, "--options_file", "options");//, "--options_file", "options"
        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
        pb.redirectError(ProcessBuilder.Redirect.DISCARD);

        pb.environment().putAll(System.getenv());
        Process p = pb.start();
        int exitCode = p.waitFor();

        if (exitCode != 0) {
            switch (exitCode) {
                case -1 -> throw new IOException("HiGHS could not be found in the PATH");
            }
            throw new IOException("HiGHS exited with error code " + exitCode);
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

    private JSONObject errorMsg(String msg){
        return new JSONObject("{\"error\": true, \"errorMessage\": \"" + msg + "\", \"result\": []}");
    }


}
