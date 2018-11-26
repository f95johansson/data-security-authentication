# data-security-authentication
Assignment 2 of Data Security

# Password file is formatted as follows
One line represents a user, formatted like below  
name, role, salt, hashedPassword

the hashedPassword gets the following information as input
* secret
* salt
* password in clear text

Which methods a role can access are set in the Role-policy.txt document
Each line is formatted
Role enum, Method.enum, Method.enum, Method.en...

Example
USER, PRINT, QUEUE

To run the test suite, make sure to install the external libraries junit 4 and org.assertj

