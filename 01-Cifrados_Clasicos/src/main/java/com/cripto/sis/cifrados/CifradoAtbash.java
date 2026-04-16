package com.cripto.sis.cifrados;

import com.cripto.sis.inter.AlgoritmoCifrado;

public class CifradoAtbash implements AlgoritmoCifrado {
    @Override
    public String procesar(String texto, String parametro, boolean cifrar) {
        StringBuilder res = new StringBuilder();
        for (char c : texto.toCharArray()) {
            if (c == ' ') res.append(" ");
            else {
                int index = ALFABETO.indexOf(c);
                res.append(ALFABETO.charAt(25 - index));
            }
        }
        return res.toString();
    }
}
