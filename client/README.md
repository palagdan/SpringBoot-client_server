### About application
This application-client is used for the online shop. 
At the moment registraion only for customers is  available.

# Dependencies
- Jdk-17(to build application locally)


## How to build
If you want to build application locally, in the **_root folder_** run `./gradlew build`. 

## How to run
If you want to run application locally, in the **_root folder_** run `./gradlew bootRun`. To stop the process call `./gradlew --stop` in a Terminal and it will kill all gradle processes.


### Features
**Available:**
- create-customer
- delete-customer
- find-customer-by-name
- find-customer-by-surname
- list-customer
- read-one-customer
- set-customer
- unset-customer
- update-customer

### Using the application
Client can be accessed at: http://localhost:8080

### Ports Information

**The application needs the following ports to run:**
- 8080 Client application 