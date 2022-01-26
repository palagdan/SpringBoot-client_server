package com.palagincom.client.client.ui;

import com.palagincom.client.client.model.CustomerDTO;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.ExitRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Component
public class CustomerView {
    public  void printErrorGeneric(WebClientException e){
        if(e instanceof WebClientRequestException){
            System.err.println("Cannot connect to api");
            throw new ExitRequest();
        }else if(e instanceof WebClientResponseException.InternalServerError){
            System.err.println(AnsiOutput.toString(AnsiColor.MAGENTA,"Unknown technical server error",AnsiColor.DEFAULT));
        }else{
            System.err.println(AnsiOutput.toString(AnsiColor.MAGENTA,"Unknown error",AnsiColor.DEFAULT));
        }
    }

    public  void printErrorCreate(WebClientException e) {
        if(e instanceof WebClientResponseException.Conflict){
            System.err.println("Customer already exists");
        }else
            printErrorGeneric(e);
    }

    public  void printCustomer(CustomerDTO customer){
        System.out.println("-----------------------------------------------------");
        System.out.println("|                    Information                    |");
        System.out.println("-----------------------------------------------------");
        System.out.println("username: " + customer.getUsername() );
        System.out.println("-----------------------------------------------------");
        System.out.println("name: " + customer.getName() );
        System.out.println("-----------------------------------------------------");
        System.out.println("surname: " + customer.getSurname() );
        System.out.println("-----------------------------------------------------");
        System.out.println("email " + customer.getMail());
        System.out.println("-----------------------------------------------------");
    }
    public   void printAll(List<CustomerDTO> customers){
        System.out.println("-----------------------------------------------------");
        System.out.println("|                List of customers                  |");
        System.out.println("-----------------------------------------------------");
        customers.forEach(u -> System.out.println("username: " + u.getUsername() + "\n-----------------------------------------------------"));
    }
    public  void printErrorUpdate(WebClientException e) {
        if(e instanceof WebClientResponseException.NotFound){
            System.err.println(AnsiOutput.toString(AnsiColor.MAGENTA,"Cannot update, customer does not exist",AnsiColor.DEFAULT));
        }else
            printErrorGeneric(e);
    }
    public  void printErrorRead(WebClientException e) {
        if(e instanceof WebClientResponseException.NotFound){
            System.err.println(AnsiOutput.toString(AnsiColor.MAGENTA,"Customer not found",AnsiColor.DEFAULT));
        }else
            printErrorGeneric(e);
    }
    public  void printErrorDelete(WebClientException e) {
        if(e instanceof WebClientResponseException.NotFound){
            System.err.println(AnsiOutput.toString(AnsiColor.MAGENTA,"Cannot delete, customer not found",AnsiColor.DEFAULT));
        }else
            printErrorGeneric(e);
    }


    public void  printCustomerByName(List<CustomerDTO> customerByName) {
        System.out.println("---------------------------------------------------------------");
        System.out.println("|                      Customers by name                      |");
        System.out.println("---------------------------------------------------------------");
        customerByName.forEach(u -> System.out.println("username: " + " | " + u.getUsername() + " | " + " name: " + u.getName() + " | " +  " surname: "+ u.getSurname() + " | " + " mail: " + u.getMail() + "\n---------------------------------------------------------------"));
    }
}
