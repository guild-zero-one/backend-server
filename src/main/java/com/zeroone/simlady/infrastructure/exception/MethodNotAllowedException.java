package com.zeroone.simlady.infrastructure.exception;

public class MethodNotAllowedException extends RuntimeException{
    public MethodNotAllowedException(String mensagem){
        super(mensagem);
    }
}
