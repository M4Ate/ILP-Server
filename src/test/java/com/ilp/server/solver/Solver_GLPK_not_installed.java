package com.ilp.server.solver;

import java.io.IOException;

public class Solver_GLPK_not_installed extends Solver_GLPK{
    @Override
    protected String callGLPK(String input) throws IOException {
        throw new IOException("Cannot run program: GLPK is not installed");
    }

}
