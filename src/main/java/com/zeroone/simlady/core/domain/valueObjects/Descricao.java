package com.zeroone.simlady.core.domain.valueObjects;

import java.util.regex.Pattern;

public class Descricao {
    private String value;
    private static final Pattern DESCRICAO_REGEX = Pattern.compile("^.{3,60}$");

    public static Descricao of(String value){
        if (value == null || !isDescricaoValid(value)){
            throw new IllegalArgumentException("Descrição Inválida: " + value);
        }
        return new Descricao(value);
    }

    private static Boolean isDescricaoValid(String value){
        return value != null && DESCRICAO_REGEX.matcher(value).matches();
    }

    private Descricao(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
