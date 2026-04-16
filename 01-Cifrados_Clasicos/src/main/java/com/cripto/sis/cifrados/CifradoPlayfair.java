package com.cripto.sis.cifrados;

import com.cripto.sis.inter.AlgoritmoCifrado;

public class CifradoPlayfair implements AlgoritmoCifrado {

    // Alfabeto clásico inglés de 25 letras (Sin la J)
    private static final String ALFABETO_PLAYFAIR = "ABCDEFGHIKLMNOPQRSTUVWXYZ";

    @Override
    public String procesar(String texto, String parametro, boolean cifrar) {
        if (parametro.isEmpty()) throw new IllegalArgumentException("La clave no puede estar vacía.");

        // 1. Limpieza estricta clásica: Cambiar J por I, y Ñ por N
        String claveLimpia = parametro.replace("J", "I").replace("Ñ", "N");
        String txt = texto.replace(" ", "").replace("J", "I").replace("Ñ", "N");

        // 2. Construir la matriz temporal sin duplicados
        StringBuilder tempMatriz = new StringBuilder();
        for (char c : (claveLimpia + ALFABETO_PLAYFAIR).toCharArray()) {
            // Ignoramos números o signos raros que se hayan colado
            if (c < 'A' || c > 'Z') continue;
            if (tempMatriz.indexOf(String.valueOf(c)) == -1) {
                tempMatriz.append(c);
            }
        }

        // 3. Llenar la matriz de 5x5
        char[][] matriz = new char[5][5];
        for (int i = 0; i < 25; i++) {
            matriz[i/5][i%5] = tempMatriz.charAt(i);
        }

        // 4. Preparar el texto en diagramas (parejas)
        StringBuilder sb = new StringBuilder();
        if (cifrar) {
            for (int i = 0; i < txt.length(); i++) {
                sb.append(txt.charAt(i));
                // Si hay dos letras iguales seguidas, insertar 'X'
                if (i + 1 < txt.length() && txt.charAt(i) == txt.charAt(i+1)) {
                    sb.append('X');
                }
            }
            // Si la longitud es impar, rellenar con 'X' al final
            if (sb.length() % 2 != 0) {
                sb.append('X');
            }
        } else {
            sb.append(txt);
        }

        // 5. Cifrar / Descifrar usando las reglas 5x5
        StringBuilder res = new StringBuilder();

        // Truco matemático en Java: Para descifrar (restar 1) en módulo 5, es más seguro sumar 4.
        int dir = cifrar ? 1 : 4;

        for (int i = 0; i < sb.length(); i += 2) {
            char a = sb.charAt(i);
            char b = (i + 1 < sb.length()) ? sb.charAt(i+1) : 'X';

            int r1 = 0, c1 = 0, r2 = 0, c2 = 0;

            // Buscar coordenadas en la matriz
            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 5; c++) {
                    if (matriz[r][c] == a) { r1 = r; c1 = c; }
                    if (matriz[r][c] == b) { r2 = r; c2 = c; }
                }
            }

            // Aplicar reglas espaciales
            if (r1 == r2) {
                // Misma fila: Movimiento horizontal (módulo 5)
                res.append(matriz[r1][(c1 + dir) % 5]).append(matriz[r2][(c2 + dir) % 5]);
            } else if (c1 == c2) {
                // Misma columna: Movimiento vertical (módulo 5)
                res.append(matriz[(r1 + dir) % 5][c1]).append(matriz[(r2 + dir) % 5][c2]);
            } else {
                // Rectángulo: Intercambiar columnas, mantener fila
                res.append(matriz[r1][c2]).append(matriz[r2][c1]);
            }
        }

        return res.toString();
    }
}