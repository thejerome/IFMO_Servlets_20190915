# IFMO_Servlets_20190915
[![from_flaxo with_♥](https://img.shields.io/badge/from_flaxo-with_♥-blue.svg)](https://github.com/tcibinan/flaxo)

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 4bee4d164ef9db7792be0eeb72cf258f3886956c
Create a servlet, serving expression value calculating.

It should serve `GET` requests.

Each request should have `equation` parameter containing expression to evaluate.
Expression may contain integer numbers, basic operators like `+`, `-`, `*`, `/`, `(`, `)`, variable names (latin lowercase single characters), spaces.

Each variable is served as additional request parameter with corresponding name and having value of integer number or name of another variable.
In latter case use the same value as variable having corresponding name.

Register servlet to serve at  `/calc` context path.

All calculations are supposed to be integer. 

Other implementation restrictions are covered by unit tests. 
 
<<<<<<< HEAD
=======
Some servlet examples.
Switch to task-* branch to get exercies.
>>>>>>> 1ae00de08547d5e3a2e3bf5def7bb410fbe705c2
=======
>>>>>>> 4bee4d164ef9db7792be0eeb72cf258f3886956c
