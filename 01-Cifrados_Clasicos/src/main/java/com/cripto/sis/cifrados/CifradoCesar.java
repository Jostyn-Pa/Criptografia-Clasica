package com.cripto.sis.cifrados;

import com.cripto.sis.inter.AlgoritmoCifrado;

public class CifradoCesar implements AlgoritmoCifrado {
    @Override
    public String procesar(String texto, String parametro, boolean cifrar) {
        int shift = Integer.parseInt(parametro);
        StringBuilder res = new StringBuilder();
        int dir = cifrar ? 1 : -1;
        for (char c : texto.toCharArray()) {
            if (c == ' ') res.append(" ");
            else {
                int index = ALFABETO.indexOf(c);
                int newIndex = (index + (shift * dir) + 2600) % 26;
                res.append(ALFABETO.charAt(newIndex));
            }
        }
        return res.toString();
    }
}
