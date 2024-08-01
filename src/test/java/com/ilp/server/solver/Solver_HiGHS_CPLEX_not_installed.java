package com.ilp.server.solver;

import java.io.IOException;

public class Solver_HiGHS_CPLEX_not_installed extends Solver_HiGHS_CPLEX{
    @Override
    protected String callSolver(String input) throws IOException, InterruptedException {
        throw new IOException("Cannot run program: HiGHS is not installed");
    }

}
