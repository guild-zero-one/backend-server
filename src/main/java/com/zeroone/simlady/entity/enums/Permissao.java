package com.zeroone.simlady.entity.enums;

public enum Permissao {

    ADMIN,
    COMUM;


    public static Boolean isValid(String permissao) {
        if (permissao == null) {
            return false;
        }

        for (Permissao value : Permissao.values()) {
            if(permissao.equalsIgnoreCase(value.toString())) {
                return true;
            }
        }
        return false;
    }
}
