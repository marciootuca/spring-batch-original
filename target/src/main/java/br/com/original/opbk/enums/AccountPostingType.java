package br.com.original.opbk.enums;

import lombok.Getter;

@Getter
public enum AccountPostingType {
    CREDITO(2,    "Credito"),
    DEBITO(3,"Debito");

    private String description;
    private Integer code;

    AccountPostingType(Integer code ,String description){
        this.description = description;
        this.code = code;
    }
}
