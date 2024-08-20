# ILP-Server
This is the server component of the Total-Coloration-Tool, mean to solve ILP problems using prebuilt solvers.

## Usage
To use the server, simply run the provided jar file to run it with default settings.
The jar supports the command line arguments:
-p {port_number} and
-s {solver} where solver can be any of: {highs, glpk, any} ("any" will use either highs or glpk, depending on which is installed on the machine)

## Installing a solver
To use the server, the selected solver has to be installed

### Installing HiGHS (recommended)

- Installation instructions for HiGHS can be found [here](https://ergo-code.github.io/HiGHS/dev/interfaces/cpp/).
- HiGHS can only be built from source, so you will need CMake as well as a C-Compiler like MSVC.
- After installation, the `highs` executable must be added to your PATH. See instructions [here](https://helpdeskgeek.com/windows-10/add-windows-path-environment-variable/).

### Installing GLPK

- Prebuilt binaries for Windows can be found [here](https://sourceforge.net/projects/winglpk/).
- On Linux, GLPK can be installed via the command:
  ```bash
  sudo apt-get install glpk-utils
  ``` 
- Alternatively GLPK can also be built from [source](http://ftp.gnu.org/gnu/glpk/).
- As with HiGHS the executable has to be added to PATH
