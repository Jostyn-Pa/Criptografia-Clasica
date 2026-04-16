package com.cripto.sis.cifrados;

import com.cripto.sis.inter.AlgoritmoCifrado;

public class CifradoVigenere implements AlgoritmoCifrado {
    @Override
    public String procesar(String texto, String parametro, boolean cifrar) {
        if (parametro.isEmpty()) throw new IllegalArgumentException("La clave no puede estar vacía.");
        StringBuilder res = new StringBuilder();
        int dir = cifrar ? 1 : -1;
        for (int i = 0, j = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == ' ') { res.append(" "); continue; }
            int indexTexto = ALFABETO.indexOf(c);
            int shift = ALFABETO.indexOf(parametro.charAt(j % parametro.length()));
            int newIndex = (indexTexto + (shift * dir) + 26) % 26;
            res.append(ALFABETO.charAt(newIndex));
            j++;
        }
        return res.toString();
    }
}
