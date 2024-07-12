package com.ilp.server.solver;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.*;
import java.util.ArrayList;


/**
 * Implementation for the GLPK ILP Solver.
 * @author maxroll
 */

public class Solver_GLPK implements Solver{
    public JSONObject solve(JSONObject input){
        try {
            String glpkInput = translateToGLPK(input);
            String glpkOutput = callGLPK(glpkInput);
            return translateFromGLPK(glpkOutput);
        } catch (IOException | InterruptedException | JSONException e){
            return errorMsg(e.getMessage().replace("\"", "\\\""));
        }
    }

    /**
     * Translates the given JSON object to a GLPK input file.
     * @param input The JSON object to translate.
     * @return The GLPK input file.
     */
    private String translateToGLPK(JSONObject input) throws JSONException {
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
        sb.append("minimize obj: ");
        sb.append(optimizationFunc);
        sb.append(";\n");
        sb.append("end;");
        System.out.println(sb);
        return sb.toString();
    }

    /**
     * Translates the given GLPK output to a JSON object.
     * @param output The GLPK output to translate.
     * @return The JSON object.
     */
    private JSONObject translateFromGLPK(String output){
        ArrayList<String> columns = parseColumnSection(output);

        // Could be prettier
        ArrayList<String> namesTemp = new ArrayList<>();
        ArrayList<String> valuesTemp = new ArrayList<>();
        for (String column : columns){
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

    private ArrayList<String> parseColumnSection(String output){
        String[] lines = output.split("\n");
        boolean inColumnSection = false;
        ArrayList<String> columns = new ArrayList<>();
        for (String line : lines){
            if (line.startsWith("Karush-Kuhn-Tucker") || line.startsWith("End of output")) {
                inColumnSection = false;
            }

            if (inColumnSection && !line.startsWith("-") && !line.isEmpty()) {
                columns.add(line);
            }

            if (line.startsWith("   No. Column name")) {
                inColumnSection = true;
            }
        }

        return columns;
    }

    private JSONObject convertToJSON(String[] names, String[] values){
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


    private String callGLPK(String input) throws IOException, InterruptedException{
        // Write input to file
        File file = new File("input.temp");

        FileWriter writer = new FileWriter(file);
        writer.write(input);
        writer.close();

        // Call GLPK
        ProcessBuilder pb = new ProcessBuilder("glpsol", "--math", "input.temp", "-o", "output.temp");
        Process p = pb.start();
        int exitCode = p.waitFor();

        if (exitCode != 0){
            throw new IOException("GLPK exited with error code " + exitCode);
        }

        // Read output from file
        File outputFile = new File("output.temp");
        BufferedReader reader = new BufferedReader(new FileReader(outputFile));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null){
            sb.append(line );
            sb.append("\n");
        }
        reader.close();

        file.delete();
        outputFile.delete();

        return sb.toString();
    }

    private JSONObject errorMsg(String msg){
        return new JSONObject("{\"error\": true, \"errorMessage\": \"" + msg + "\", \"result\": []}");
    }
}
