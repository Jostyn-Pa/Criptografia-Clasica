package com.cripto.sis.inter;

public interface AlgoritmoCifrado {
    String ALFABETO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String procesar(String texto, String parametro, boolean cifrar);
}
