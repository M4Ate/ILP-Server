package com.ilp_server.solver;

import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

class Solver_GLPKTest {

    @org.junit.jupiter.api.Test
    void solve() {
        Solver_GLPK solver = new Solver_GLPK();
        JSONObject input = new JSONObject("{\n" +
                "    \"variables\":[\n" +
                "       \"x_v0_c0\",\n" +
                "       \"x_v0_c1\",\n" +
                "       \"x_v0_c2\",\n" +
                "       \"x_v0_c3\",\n" +
                "       \"x_v1_c0\",\n" +
                "       \"x_v1_c1\",\n" +
                "       \"x_v1_c2\",\n" +
                "       \"x_v1_c3\",\n" +
                "       \"x_v2_c0\",\n" +
                "       \"x_v2_c1\",\n" +
                "       \"x_v2_c2\",\n" +
                "       \"x_v2_c3\",\n" +
                "       \"y_e0_c0\",\n" +
                "       \"y_e0_c1\",\n" +
                "       \"y_e0_c2\",\n" +
                "       \"y_e0_c3\",\n" +
                "       \"y_e1_c0\",\n" +
                "       \"y_e1_c1\",\n" +
                "       \"y_e1_c2\",\n" +
                "       \"y_e1_c3\",\n" +
                "       \"y_e2_c0\",\n" +
                "       \"y_e2_c1\",\n" +
                "       \"y_e2_c2\",\n" +
                "       \"y_e2_c3\",\n" +
                "       \"z_c0\",\n" +
                "       \"z_c1\",\n" +
                "       \"z_c2\",\n" +
                "       \"z_c3\"\n" +
                "    ],\n" +
                "    \"constraints\":[\n" +
                "       \"x_v0_c0 + x_v0_c1 + x_v0_c2 + x_v0_c3 = 1\",\n" +
                "       \"x_v1_c0 + x_v1_c1 + x_v1_c2 + x_v1_c3 = 1\",\n" +
                "       \"x_v2_c0 + x_v2_c1 + x_v2_c2 + x_v2_c3 = 1\",\n" +
                "       \"y_e0_c0 + y_e0_c1 + y_e0_c2 + y_e0_c3 = 1\",\n" +
                "       \"y_e1_c0 + y_e1_c1 + y_e1_c2 + y_e1_c3 = 1\",\n" +
                "       \"y_e2_c0 + y_e2_c1 + y_e2_c2 + y_e2_c3 = 1\",\n" +
                "       \"x_v0_c0 + x_v1_c0 <= 1\",\n" +
                "       \"x_v0_c1 + x_v1_c1 <= 1\",\n" +
                "       \"x_v0_c2 + x_v1_c2 <= 1\",\n" +
                "       \"x_v0_c3 + x_v1_c3 <= 1\",\n" +
                "       \"x_v1_c0 + x_v2_c0 <= 1\",\n" +
                "       \"x_v1_c1 + x_v2_c1 <= 1\",\n" +
                "       \"x_v1_c2 + x_v2_c2 <= 1\",\n" +
                "       \"x_v1_c3 + x_v2_c3 <= 1\",\n" +
                "       \"x_v2_c0 + x_v0_c0 <= 1\",\n" +
                "       \"x_v2_c1 + x_v0_c1 <= 1\",\n" +
                "       \"x_v2_c2 + x_v0_c2 <= 1\",\n" +
                "       \"x_v2_c3 + x_v0_c3 <= 1\",\n" +
                "       \"x_v0_c0 + y_e0_c0 + y_e2_c0 <= 1\",\n" +
                "       \"x_v0_c1 + y_e0_c1 + y_e2_c1 <= 1\",\n" +
                "       \"x_v0_c2 + y_e0_c2 + y_e2_c2 <= 1\",\n" +
                "       \"x_v0_c3 + y_e0_c3 + y_e2_c3 <= 1\",\n" +
                "       \"x_v1_c0 + y_e0_c0 + y_e1_c0 <= 1\",\n" +
                "       \"x_v1_c1 + y_e0_c1 + y_e1_c1 <= 1\",\n" +
                "       \"x_v1_c2 + y_e0_c2 + y_e1_c2 <= 1\",\n" +
                "       \"x_v1_c3 + y_e0_c3 + y_e1_c3 <= 1\",\n" +
                "       \"x_v2_c0 + y_e1_c0 + y_e2_c0 <= 1\",\n" +
                "       \"x_v2_c1 + y_e1_c1 + y_e2_c1 <= 1\",\n" +
                "       \"x_v2_c2 + y_e1_c2 + y_e2_c2 <= 1\",\n" +
                "       \"x_v2_c3 + y_e1_c3 + y_e2_c3 <= 1\",\n" +
                "       \"x_v0_c0 - z_c0 <= 0\",\n" +
                "       \"x_v0_c1 - z_c1 <= 0\",\n" +
                "       \"x_v0_c2 - z_c2 <= 0\",\n" +
                "       \"x_v0_c3 - z_c3 <= 0\",\n" +
                "       \"x_v1_c0 - z_c0 <= 0\",\n" +
                "       \"x_v1_c1 - z_c1 <= 0\",\n" +
                "       \"x_v1_c2 - z_c2 <= 0\",\n" +
                "       \"x_v1_c3 - z_c3 <= 0\",\n" +
                "       \"x_v2_c0 - z_c0 <= 0\",\n" +
                "       \"x_v2_c1 - z_c1 <= 0\",\n" +
                "       \"x_v2_c2 - z_c2 <= 0\",\n" +
                "       \"x_v2_c3 - z_c3 <= 0\",\n" +
                "       \"y_e0_c0 - z_c0 <= 0\",\n" +
                "       \"y_e0_c1 - z_c1 <= 0\",\n" +
                "       \"y_e0_c2 - z_c2 <= 0\",\n" +
                "       \"y_e0_c3 - z_c3 <= 0\",\n" +
                "       \"y_e1_c0 - z_c0 <= 0\",\n" +
                "       \"y_e1_c1 - z_c1 <= 0\",\n" +
                "       \"y_e1_c2 - z_c2 <= 0\",\n" +
                "       \"y_e1_c3 - z_c3 <= 0\",\n" +
                "       \"y_e2_c0 - z_c0 <= 0\",\n" +
                "       \"y_e2_c1 - z_c1 <= 0\",\n" +
                "       \"y_e2_c2 - z_c2 <= 0\",\n" +
                "       \"y_e2_c3 - z_c3 <= 0\"\n" +
                "    ],\n" +
                "    \"optimizationFunction\":\"z_c0 + z_c1 + z_c2 + z_c3\"\n" +
                " }");
        JSONObject output = solver.solve(input);
        assertEquals("{\"solution\": \"not implemented\"}", output.toString());
    }
}