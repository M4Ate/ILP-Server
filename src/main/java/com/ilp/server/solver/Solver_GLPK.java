package com.ilp.server.solver;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Implementation for the GLPK ILP Solver.
 */

public class Solver_GLPK implements Solver {

    /**
     * Solves the given problem using the GLPK ILP solver.
     *
     * @param input The JSON representation of the problem.
     * @return The result of the solver.
     */
    public JSONObject solve(JSONObject input) {
        try {
            String glpkInput = translateToGLPK(input);
            String glpkOutput = callGLPK(glpkInput);
            return translateFromGLPK(glpkOutput);
        } catch (IOException e) {
            return errorMsg("GLPK is not installed or not reachable.");
        } catch (InterruptedException | JSONException e) {
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

        if (constraints.isEmpty()) throw new JSONException("Cannot solve empty graph");

        StringBuilder sb = new StringBuilder();
        for (Object var : vars) {
            sb.append("var ");
            sb.append(var.toString());
            //sb.append(" binary;\n");            // using integer seems slightly faster
            sb.append(" >= 0, integer;\n");
        }
        for (int i = 0; i < constraints.length(); i++) {
            sb.append("s.t. ");
            sb.append("c_");
            sb.append(i);
            sb.append(": ");
            sb.append(constraints.getString(i));
            sb.append(";\n");
        }
        sb.append("minimize obj: ");
        sb.append(optimizationFunc);
        sb.append(";\n");
        sb.append("end;");
        return sb.toString();
    }

    /**
     * Translates the given GLPK output to a JSON object.
     *
     * @param output The GLPK output to translate.
     * @return The JSON object.
     */
    private JSONObject translateFromGLPK(String output) {
        if (!solvable(output)) {
            return errorMsg("The problem is not solvable.");
        }

        ArrayList<String> columns = parseColumnSection(output);

        // Could be prettier
        ArrayList<String> namesTemp = new ArrayList<>();
        ArrayList<String> valuesTemp = new ArrayList<>();
        for (String column : columns) {
            String[] parts = column.trim().split("\\s+");
            namesTemp.add(parts[1]);
            valuesTemp.add(parts[3]);
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
            if (line.startsWith("Karush-Kuhn-Tucker") || line.startsWith("End of output") || line.isEmpty()) {
                inColumnSection = false;
            }

            if (inColumnSection && !line.startsWith("-")) {
                columns.add(line);
            }

            if (line.startsWith("   No. Column name")) {
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
        ProcessBuilder pb = new ProcessBuilder("glpsol", "--math", inputFileName, "-o", outputFileName);
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

       //noinspection ResultOfMethodCallIgnored
        file.delete();
        //noinspection ResultOfMethodCallIgnored
        outputFile.delete();

        return sb.toString();
    }



    private boolean solvable(String output) {
        return !output.contains("INFEASIBLE");
    }

    private JSONObject errorMsg(String msg){
        return new JSONObject("{\"error\": true, \"errorMessage\": \"" + msg + "\", \"result\": []}");
    }

    public boolean isAvailable() {
        try {
            ProcessBuilder pb = new ProcessBuilder("glpsol", "--version");
            pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
            pb.redirectError(ProcessBuilder.Redirect.DISCARD);
            Process p = pb.start();
            return p.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}
