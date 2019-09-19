# IFMO_Servlets_20190915
[![from_flaxo with_♥](https://img.shields.io/badge/from_flaxo-with_♥-blue.svg)](https://github.com/tcibinan/flaxo)

Create a webapp serving expression value calculating.

It should serve `PUT`, `GET`, `DELETE` requests and keep user sessions.

User can put expression to evaluate or variable values via `PUT` requests.

To set an expression he execute PUT request to `/calc/equation` URI setting expression as a request body.

To set a variable he execute PUT request to `/calc/<variable_name>` URI where `<variable_name>` is a variable name (latin lowercase single characters) and setting variable value as a request body.
 
User may update equation or variable value by executing another `PUT` request of the same format.
When equation or variable is set for the first time, webapp should return `201` status code. Also setting URI of created resource as a Location header value is welcome.
When equation or variable is updated, webapp should return `200` status code.
Each variable has value of integer number or name of another variable.
In latter case use the same value as variable having corresponding name.
If user has sent incorrect value (badly formatted) for expression or variable webapp should return `400` status code and reason of the error.
All variable values should be in `[-10000; 10000]` range. Exceeding values should lead to `403` status code.
User may unset equation or variable by executing `DELETE` request to corresponding resource URL.
Such requests should return `204` status codes.

User may get expression evaluated value by execution `GET` request to `/calc/result` URL.
In case expression is there and it may be evaluated, webapp should return `200` status code and calculated integer value as a response body.
In case expression may not be calculated due to lack of data webapp should return `409` status code and reason of this error.  

All calculations are supposed to be integer.
Webapp must be implemented on the basis of http servlets and servlet filters.
Other implementation restrictions are covered by unit tests. 