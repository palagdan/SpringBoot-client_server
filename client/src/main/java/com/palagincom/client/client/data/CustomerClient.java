package com.palagincom.client.client.data;

import com.palagincom.client.client.model.CustomerDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Duration;
import java.util.List;

@Component
public class CustomerClient {
        private final WebClient customerWebClient;
        private String currentCustomer;



    public CustomerClient(@Value("${backend_url}") String backendUrl) {
        this.customerWebClient = WebClient.create(backendUrl + "/v1/customer");
    }
    public String getCurrentCustomer(){
        return this.currentCustomer;
    }

    public void setCustomer(String currentCustomer){
        this.currentCustomer = currentCustomer;
        if(currentCustomer != null){
            try{
                readOne();
            }catch (WebClientException e){
                this.currentCustomer = null;
                throw e;
            }
        }

    }
    public CustomerDTO create(CustomerDTO customerDTO){
        return customerWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(customerDTO)
                .retrieve()
                .bodyToMono(CustomerDTO.class)
                .block(Duration.ofSeconds(5));
    }
    public List<CustomerDTO> readAll(){
        return customerWebClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CustomerDTO.class)
                .collectList()
                .block();
    }
    public CustomerDTO readOne(){
        if(currentCustomer == null || currentCustomer.isBlank())
            throw new IllegalStateException("current customer not set");

        return customerWebClient.get()
                .uri("/{id}",currentCustomer)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CustomerDTO.class)
                .block();
    }

    public void update(CustomerDTO customerDTO){
       customerDTO.setUsername(currentCustomer);
       customerWebClient.put()
               .uri("/{id}",currentCustomer)
               .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(customerDTO)
                .retrieve()
                .toBodilessEntity()
                .block(Duration.ofSeconds(5));

    }
    public void delete(){
        if(currentCustomer == null || currentCustomer.isBlank()){
            throw new IllegalStateException("Customer doesn't exist");
        }
        customerWebClient.delete()
                .uri("/{id}",currentCustomer)
                .retrieve()
                .toBodilessEntity()
                .block(Duration.ofSeconds(5));
    }


    public List<CustomerDTO> findCustomerByName(String name) {
        return customerWebClient.get()
                .uri("/name/{name}", name)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CustomerDTO.class)
                .collectList()
                .block();
    }

    public List<CustomerDTO> findCustomerBySurname(String surname) {
        return customerWebClient.get()
        .uri("/surname/{surname}", surname)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CustomerDTO.class)
                .collectList()
                .block();
    }
}
