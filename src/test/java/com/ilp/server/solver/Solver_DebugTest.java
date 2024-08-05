package com.ilp.server.solver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Solver_DebugTest {

    @Test
    void solve() {
        Solver_Debug solver = new Solver_Debug();
        assertEquals("{\"result\":[],\"errorMessage\":\"Your using the debug solver, this solver does not solve anything.\",\"error\":true}", solver.solve(null).toString());
    }
}