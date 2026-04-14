import java.util.*;

public class CifradoClasico {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Iniciando Sistema Criptográfico...");

        while (true) {
            System.out.println("\n========================================");
            System.out.println("SISTEMA DE CIFRADO CLÁSICO");
            System.out.println("========================================");
            System.out.println("1. Cifrar un mensaje");
            System.out.println("2. Descifrar un mensaje");
            System.out.println("0. Salir del programa");
            System.out.print("Seleccione una acción: ");

            int accion;
            try {
                accion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
                continue;
            }

            if (accion == 0) {
                System.out.println("Saliendo del sistema");
                break;
            }
            if (accion != 1 && accion != 2) {
                System.out.println("Error: Acción no válida. Elija 1 o 2.");
                continue;
            }

            System.out.println("\n--- MÉTODOS DISPONIBLES ---");
            System.out.println("1. Atbash");
            System.out.println("2. César");
            System.out.println("3. Vigenère");
            System.out.println("4. Rail Fence");
            System.out.println("5. Playfair");
            System.out.print("Seleccione el algoritmo: ");

            int metodo;
            try {
                metodo = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
                continue;
            }

            System.out.print("Ingrese el texto a procesar: ");
            String texto = sc.nextLine().toUpperCase().replaceAll("[^A-Z ]", "");

            if (accion == 1) {
                switch (metodo) {
                    case 1 -> cifradoAtbash(texto);
                    case 2 -> cifradoCesar(texto);
                    default -> System.out.println("Error: Algoritmo no válido.");
                }
            } else {
                switch (metodo) {
                    case 1 -> descifrarAtbash(texto);
                    case 2 -> descifrarCesar(texto);
                    default -> System.out.println("Error: Algoritmo no válido.");
                }
            }
        }
    }

    private static void cifradoAtbash(String t) {
        System.out.println("\n[MÉTODO ATBASH]: Es un cifrado de sustitución monoalfabética donde la primera letra del alfabeto se sustituye por la última, la segunda por la penúltima y así sucesivamente (espejo).");
        StringBuilder res = new StringBuilder();
        for (char c : t.toCharArray()) {
            if (c == ' ') res.append(" ");
            else res.append((char) ('Z' - (c - 'A')));
        }
        System.out.println("Resultado: " + res);
    }

    private static void cifradoCesar(String t) {
        System.out.println("\n[MÉTODO CÉSAR]: Sustitución por desplazamiento. Cada letra se desplaza un número fijo de posiciones en el alfabeto.");
        System.out.print("Ingrese el nivel de desplazamiento (ej. 3): ");
        int shift = Integer.parseInt(sc.nextLine()) % 26;

        StringBuilder res = new StringBuilder();
        for (char c : t.toCharArray()) {
            if (c == ' ') res.append(" ");
            else res.append((char) ((c - 'A' + shift + 26) % 26 + 'A'));
        }
        System.out.println("Resultado: " + res);
    }

    private static void descifrarAtbash(String t) {
        System.out.println("\n[DESCIFRANDO ATBASH]");
        // Al ser un espejo geométrico, cifrar y descifrar es el mismo proceso
        cifradoAtbash(t);
    }

    private static void descifrarCesar(String t) {
        System.out.println("\n[DESCIFRANDO CÉSAR]");
        System.out.print("Ingrese el nivel de desplazamiento usado: ");
        int shift = Integer.parseInt(sc.nextLine()) % 26;

        StringBuilder res = new StringBuilder();
        for (char c : t.toCharArray()) {
            if (c == ' ') res.append(" ");
            else {
                res.append((char) ((c - 'A' - shift + 26) % 26 + 'A'));
            }
        }
        System.out.println("Texto Original: " + res);
    }

}
