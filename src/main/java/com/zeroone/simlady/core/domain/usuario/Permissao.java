package com.zeroone.simlady.core.domain.usuario;

public enum Permissao {
    ADMIN,
    COMUM;

    public static boolean isValid(String permissao) {
        if (permissao == null) {
            return false;
        }

        for (Permissao value : Permissao.values()) {
            if (permissao.equalsIgnoreCase(value.toString())) {
                return true;
            }
        }
        return false;
    }
}
