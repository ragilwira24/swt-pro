<a name="br1"></a>Dear Candidate,

Please kindly create a RESTful API using Spring Boot with PostgreSQL database that provides
following functionalities:

### API Registration:

- Request:
  - Phone number
  - Name
  - Password 


- Validation:
  - Phone number mandatory min 10 max 13 must started with 08 
  - Name mandatory max 60
  - Password mandatory min 6, max 16, containing at least 1 capital letter and 1 number.


- Process:
  - Store in database with password salted and hashed


- Response:
  - Success 

### API login:

- Request:
  - Phone number 
  - Password (hashed & salted)
  

- Process:
  - Check phone number and password exists in database


- Response:
  - JWT with alg “RS256”

### API Get Name

- Request:
  - Header → Authorization: Bearer Token using JWT


- Process:
  - Check JWT valid
  - Get phone number from JWT
  - Check phone number exists 
  - Get the name


- Response:
  - Name 

### API Update Name

- Request:

  - Header → Authorization: Bearer Token using JWT


- Process:
  - Check JWT valid
  - Get phone number from JWT
  - Check phone number exists 
  - Update the name


- Response:
  - Success

### Non functional requirements:

- Local properties files to configure database connection and asymmetric keys
- Unit Test (positive scenario only) for those 4 functionalities
- Docker file to run this application
- Swagger UI (OpenAPI) to test this application

Make sure the application can be built, run, and tested easily by the reviewer.
Commit everything into your GitHub.
