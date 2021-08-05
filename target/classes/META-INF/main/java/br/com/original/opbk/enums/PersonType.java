package br.com.original.opbk.enums;

import lombok.Getter;

@Getter
public enum PersonType {
    PJ("PESSOA JURIDICA"),
    PF("PESSOA NATURAL");

    private String description;
    PersonType(String description){
        this.description = description;
    }

}
