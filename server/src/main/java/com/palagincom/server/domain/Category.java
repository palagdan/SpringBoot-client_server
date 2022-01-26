package com.palagincom.server.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public enum Category {
   Computer("Computer"),SmartPhone("Smartphone"), Screen("Screen"),TV("Tv");

    private final String name;

    public String getName() {
        return name;
    }

    Category(String name) {
        this.name = name;
    }



}
