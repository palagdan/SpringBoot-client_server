package com.palagincom.client.client.ui;


import com.palagincom.client.client.data.CustomerClient;
import com.palagincom.client.client.model.CustomerDTO;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.web.reactive.function.client.WebClientException;

@ShellComponent
public class CustomerConsole {
    private final CustomerClient customerClient;
    private final CustomerView customerView;


    public CustomerConsole(CustomerClient customerClient, CustomerView customerView) {
        this.customerClient = customerClient;
        this.customerView = customerView;
    }
    @ShellMethod("Sign up new customer")
    @ShellMethodAvailability("currentCustomerNeededUnset")
    public void createCustomer(String username){
        try{
            var customer = new CustomerDTO(username, null, null,null);
            var ret = customerClient.create(customer);
            customerView.printCustomer(ret);
        }catch (WebClientException e){
            customerView.printErrorCreate(e);
        }
    }

    @ShellMethod("Find customer by name")
    @ShellMethodAvailability("currentCustomerNeededUnset")
    public void findCustomerByName(String name){
        try{
           customerView.printCustomerByName(customerClient.findCustomerByName(name));
        }catch (WebClientException e){
            customerView.printErrorRead(e);
        }
    }


    @ShellMethod("Retrieve and display list of all customers")
    @ShellMethodAvailability("currentCustomerNeededUnset")
    public void listCustomers(){
        try {
            var customers = customerClient.readAll();
            customerView.printAll(customers);
        }catch (WebClientException e){

            customerView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Set current customer")
    @ShellMethodAvailability("currentCustomerNeededUnset")
    public void setCustomer(String currentCustomer){
        try {
            customerClient.setCustomer(currentCustomer);
        }catch (WebClientException e){
            customerView.printErrorRead(e);
        }
    }
    public Availability currentCustomerNeededUnset(){
        return customerClient.getCurrentCustomer() !=  null ?
                Availability.unavailable("current customer needs to be unset")
                : Availability.available();
    }



    @ShellMethod("Unset current customer")
    @ShellMethodAvailability("currentCustomerNeededAvailability")
    public void unsetCustomer(){
        customerClient.setCustomer(null);
    }
    public Availability  currentCustomerNeededAvailability(){
        return customerClient.getCurrentCustomer() == null ?
                Availability.unavailable("current customer needs to be set")
                : Availability.available();
    }


    @ShellMethod("Update customer info")
    @ShellMethodAvailability("currentCustomerNeededAvailability")
    public void updateCustomer(String name,String surname, String mail){
        try{
            customerClient.update(new CustomerDTO(null,name,surname,mail));
        }catch (WebClientException e){
            customerView.printErrorUpdate(e);
        }
    }
    @ShellMethod("Print info of selected customer")
    @ShellMethodAvailability("currentCustomerNeededAvailability")
    public void  readOneCustomer(){
        try{
            customerView.printCustomer(customerClient.readOne());
        }catch (WebClientException e) {
            customerView.printErrorRead(e);
        }
    }

    @ShellMethod("Delete customer")
    @ShellMethodAvailability("currentCustomerNeededAvailability")
    public void deleteCustomer() {
        try {
            customerClient.delete();
        } catch (WebClientException e) {
            customerView.printErrorDelete(e);
        }

    }

    @ShellMethod("Find customer by surname")
    @ShellMethodAvailability("currentCustomerNeededUnset")
    public void findCustomerBySurname(String surname){
        try{
            customerView.printCustomerByName(customerClient.findCustomerBySurname(surname));
        }catch (WebClientException e){
            customerView.printErrorRead(e);
        }
    }



}
