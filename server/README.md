## About application
This application-server is used for the online shop.

## Dependencies
- Jdk-17(to build application locally)


## How to build
If you want to build application locally, in the **_root folder_** run `./gradlew build`. 

## How to run
If you want to run application locally, in the **_root folder_** run `./gradlew bootRun`. To stop the process call `./gradlew --stop` in a Terminal and it will kill all gradle processes.
## Features
**Customer:**
- create-customer
- delete-customer
- find-customer-by-name
- find-customer-by-surname
- list-customer
- read-one-customer
- set-customer
- unset-customer
- update-customer

**Product:**
- create-product
- delete-product
- find-product-by-id
- find-product-by-name
- find-product-by-category
- find-product-by-price
- find-product-by-discount
- read-all-products
- update-product
- delete-product

**Order:**
- create-order
- update-order
- delete-order
- read-all-orders
- find-order-by-id

