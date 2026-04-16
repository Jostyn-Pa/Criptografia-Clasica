package com.cripto.sis.cifrados;

import com.cripto.sis.inter.AlgoritmoCifrado;

public class CifradoRailFence implements AlgoritmoCifrado {
    @Override
    public String procesar(String texto, String parametro, boolean cifrar) {
        int rails = Integer.parseInt(parametro);
        if (rails < 2) throw new IllegalArgumentException("Se necesitan al menos 2 rieles.");
        String simple = texto.replace(" ", "");
        char[][] matrix = new char[rails][simple.length()];
        boolean bajando = false;
        int f = 0;

        if (cifrar) {
            for (int i = 0; i < simple.length(); i++) {
                matrix[f][i] = simple.charAt(i);
                if (f == 0 || f == rails - 1) bajando = !bajando;
                f += bajando ? 1 : -1;
            }
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < rails; i++) for (int j = 0; j < simple.length(); j++)
                if (matrix[i][j] != 0) res.append(matrix[i][j]);
            return res.toString();
        } else {
            for (int i = 0; i < simple.length(); i++) {
                matrix[f][i] = '*';
                if (f == 0 || f == rails - 1) bajando = !bajando;
                f += bajando ? 1 : -1;
            }
            int index = 0;
            for (int i = 0; i < rails; i++) for (int j = 0; j < simple.length(); j++)
                if (matrix[i][j] == '*' && index < simple.length()) matrix[i][j] = simple.charAt(index++);
            StringBuilder res = new StringBuilder();
            f = 0; bajando = false;
            for (int i = 0; i < simple.length(); i++) {
                res.append(matrix[f][i]);
                if (f == 0 || f == rails - 1) bajando = !bajando;
                f += bajando ? 1 : -1;
            }
            return res.toString();
        }
    }
}
