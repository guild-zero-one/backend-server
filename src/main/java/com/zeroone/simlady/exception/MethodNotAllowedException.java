package com.zeroone.simlady.exception;

public class MethodNotAllowedException extends RuntimeException{
    public MethodNotAllowedException(String mensagem){
        super(mensagem);
    }
}
