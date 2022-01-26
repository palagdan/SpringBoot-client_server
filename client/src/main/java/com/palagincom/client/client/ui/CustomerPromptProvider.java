package com.palagincom.client.client.ui;

import com.palagincom.client.client.data.CustomerClient;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;


@Component
public class CustomerPromptProvider implements PromptProvider{

    private final CustomerClient customerClient;

    public CustomerPromptProvider(CustomerClient customerClient) {
        this.customerClient = customerClient;
    }


    @Override
    public AttributedString getPrompt() {
        if(customerClient.getCurrentCustomer() == null){
          return new AttributedString("database:>");
        }
        return new AttributedString("customer=" + customerClient.getCurrentCustomer() + ":>", AttributedStyle.DEFAULT.foreground(AttributedStyle.BRIGHT));
    }
}
