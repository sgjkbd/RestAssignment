In order to start this project, run mvnw spring-boot:run.
The project must be restarted to flush the database.

Included are two REST commands, both require header content-type: application/json. 

POST http://localhost:8080/transaction/ with example body

{
	"customerID": 1,
	"transactionAmount": 100,
	"transactionDate": "2020-05-08"
}

is used to create a customer.

GET http://localhost:8080/transaction/points/YYYY-MM-DD
is used to retrieve all the information of customers, points per month,
and points total all at once.
The date is the last date of the three month period the user is looking for.

Included with this project is a file "test.py."

In order to run this file, Python 3 will need to be installed, along with 
the command "pip install python-dateutil".

At the top of this file include three variables:
customers, transactions, and endDate.
The user is encouraged to play with those variables along with
the variables used to create dates to create new test data.

After running "test.py," the program will output the data
to a file called "output.py" that can then be compared with 
the output data from the GET request.

Included are testTransactionController and testTransactionService files.