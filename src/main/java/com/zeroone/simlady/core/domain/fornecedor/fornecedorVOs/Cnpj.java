package com.zeroone.simlady.core.domain.fornecedor.fornecedorVOs;

public class Cnpj {
    private String numero;

    private Cnpj(String numero) {
        this.numero = numero;
    }

    public static Cnpj of(String numero) {
        if (!isCnpjValido(numero)) {
            throw new IllegalArgumentException("CNPJ invalido");
        }

        return new Cnpj(numero);
    }

    private static boolean isCnpjValido(String cnpj) {
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Verifica se o CNPJ tem 14 dígitos e se não é uma sequência de números repetidos
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            // Calcula o primeiro dígito verificador
            int digito1 = calcularDigito(cnpj.substring(0, 12), 5);
            // Calcula o segundo dígito verificador
            int digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, 6);

            // Compara os dígitos calculados com os originais
            return (Integer.parseInt(cnpj.substring(12, 13)) == digito1)
                    && (Integer.parseInt(cnpj.substring(13, 14)) == digito2);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static int calcularDigito(String str, int pesoInicial) {
        int soma = 0;
        int peso = pesoInicial;
        for (int i = 0; i < str.length(); i++) {
            soma += Integer.parseInt(str.substring(i, i + 1)) * peso;
            peso--;
            if (peso == 1) {
                peso = 9;
            }
        }
        int resto = soma % 11;
        return (resto < 2) ? 0 : (11 - resto);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cnpj)) return false;
        Cnpj cnpj = (Cnpj) o;
        return numero.equals(cnpj.numero);
    }

    @Override
    public String toString() {
        return numero;
    }

}
