package com.cripto.sis.ui;

import com.cripto.sis.cifrados.*;
import com.cripto.sis.inter.AlgoritmoCifrado;

import javax.swing.*;
import java.awt.*;

public class VentanaGUI extends JFrame {

    private static final String TEXTO_FANTASMA = "INGRESE SU TEXTO AQUÍ";

    private JComboBox<String> cbAccion, cbMetodo;
    private JTextArea txtEntrada, txtSalida, txtDescripcion;
    private JTextField txtParametro;
    private JLabel lblParametro;

    public VentanaGUI() {
        setTitle("Sistema de Cifrado Clásico");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL NORTE: Controles + Descripción ---
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));

        // 1. Controles (Listas y Textos)
        JPanel panelTop = new JPanel(new GridLayout(3, 2, 5, 5));
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        panelTop.add(new JLabel("Acción:"));
        cbAccion = new JComboBox<>(new String[]{"Cifrar", "Descifrar"});
        panelTop.add(cbAccion);

        panelTop.add(new JLabel("Método:"));
        cbMetodo = new JComboBox<>(new String[]{"Atbash", "César", "Vigenère", "Rail Fence", "Playfair"});
        panelTop.add(cbMetodo);

        lblParametro = new JLabel("Parámetro (No aplica):");
        txtParametro = new JTextField();
        txtParametro.setEnabled(false);
        panelTop.add(lblParametro);
        panelTop.add(txtParametro);

        // 2. Caja de Descripción Dinámica
        JPanel panelDesc = new JPanel(new BorderLayout());
        panelDesc.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        txtDescripcion = new JTextArea();
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setEditable(false);
        txtDescripcion.setFocusable(false);
        txtDescripcion.setBackground(new Color(230, 245, 255)); // Fondo azul muy claro
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 200, 250)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        txtDescripcion.setFont(new Font("SansSerif", Font.ITALIC, 12));
        panelDesc.add(txtDescripcion, BorderLayout.CENTER);

        panelNorte.add(panelTop);
        panelNorte.add(panelDesc);
        add(panelNorte, BorderLayout.NORTH);

        // --- PANEL CENTRAL: Áreas de Texto ---
        JPanel panelCenter = new JPanel(new GridLayout(2, 1, 5, 5));
        panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        txtEntrada = new JTextArea(TEXTO_FANTASMA);
        txtEntrada.setForeground(Color.GRAY);
        txtEntrada.setLineWrap(true);

        txtEntrada.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtEntrada.getText().equals(TEXTO_FANTASMA)) {
                    txtEntrada.setText("");
                    txtEntrada.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtEntrada.getText().trim().isEmpty()) {
                    txtEntrada.setForeground(Color.GRAY);
                    txtEntrada.setText(TEXTO_FANTASMA);
                }
            }
        });
        panelCenter.add(new JScrollPane(txtEntrada));

        txtSalida = new JTextArea();
        txtSalida.setLineWrap(true);
        txtSalida.setEditable(false);
        txtSalida.setBackground(new Color(240, 240, 240));
        panelCenter.add(new JScrollPane(txtSalida));
        add(panelCenter, BorderLayout.CENTER);

        // --- PANEL INFERIOR: Botón ---
        JButton btnProcesar = new JButton("Procesar Texto");
        btnProcesar.setFont(new Font("Arial", Font.BOLD, 14));
        btnProcesar.setBackground(new Color(50, 150, 250));
        btnProcesar.setForeground(Color.WHITE);
        add(btnProcesar, BorderLayout.SOUTH);

        // --- EVENTOS ---
        cbMetodo.addActionListener(e -> actualizarInterfaz());
        btnProcesar.addActionListener(e -> procesar());

        // Forzar la actualización inicial para que aparezca la descripción del primer método
        actualizarInterfaz();
    }

    private void actualizarInterfaz() {
        String metodo = (String) cbMetodo.getSelectedItem();
        txtParametro.setEnabled(true);

        switch (metodo) {
            case "Atbash" -> {
                lblParametro.setText("Parámetro (No aplica):");
                txtParametro.setEnabled(false);
                txtParametro.setText("");
                txtDescripcion.setText("📖 ATBASH: Cifrado de sustitución en espejo. La 'A' se vuelve 'Z', la 'B' se vuelve 'Y', etc. No requiere ninguna clave.");
            }
            case "César" -> {
                lblParametro.setText("Desplazamiento (Número):");
                txtDescripcion.setText("📖 CÉSAR: Sustitución por desplazamiento. Desplaza cada letra del texto un número fijo de posiciones en el alfabeto.");
            }
            case "Vigenère" -> {
                lblParametro.setText("Palabra Clave:");
                txtDescripcion.setText("📖 VIGENÈRE: Cifrado polialfabético. Usa una palabra clave que se repite para aplicar un desplazamiento diferente a cada letra del texto.");
            }
            case "Rail Fence" -> {
                lblParametro.setText("Número de Rieles:");
                txtDescripcion.setText("📖 RAIL FENCE: Cifrado por transposición. Escribe el mensaje en forma de zig-zag a través de múltiples 'rieles' (filas) y luego lo lee de izquierda a derecha.");
            }
            case "Playfair" -> {
                lblParametro.setText("Clave de Matriz:");
                txtDescripcion.setText("📖 PLAYFAIR: Cifrado por diagramas. Agrupa el texto en parejas de letras y usa las reglas de una matriz generada por la clave para encriptarlas.");
            }
        }
    }

    private void procesar() {
        try {
            boolean esCifrado = cbAccion.getSelectedIndex() == 0;
            String metodoStr = (String) cbMetodo.getSelectedItem();

            String textoInicial = txtEntrada.getText().toUpperCase();

            if (textoInicial.trim().isEmpty() || textoInicial.equals(TEXTO_FANTASMA.toUpperCase())) {
                throw new IllegalArgumentException("Por favor, escriba un mensaje antes de procesar.");
            }

            textoInicial = textoInicial.replace('Á', 'A').replace('É', 'E')
                    .replace('Í', 'I').replace('Ó', 'O')
                    .replace('Ú', 'U').replace('Ü', 'U')
                    .replace('Ñ', 'N');
            String texto = textoInicial.replaceAll("[^A-Z ]", "");

            if (texto.trim().isEmpty()) {
                throw new IllegalArgumentException("El texto no contiene letras válidas para cifrar.");
            }

            String paramInicial = txtParametro.getText().toUpperCase();
            paramInicial = paramInicial.replace('Á', 'A').replace('É', 'E')
                    .replace('Í', 'I').replace('Ó', 'O')
                    .replace('Ú', 'U').replace('Ü', 'U')
                    .replace('Ñ', 'N');
            String param = paramInicial.replaceAll("[^A-Z0-9]", "");

            AlgoritmoCifrado algoritmo = switch (metodoStr) {
                case "Atbash" -> new CifradoAtbash();
                case "César" -> new CifradoCesar();
                case "Vigenère" -> new CifradoVigenere();
                case "Rail Fence" -> new CifradoRailFence();
                case "Playfair" -> new CifradoPlayfair();
                default -> throw new IllegalStateException("Algoritmo no soportado");
            };

            String resultado = algoritmo.procesar(texto, param, esCifrado);
            txtSalida.setText(resultado);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: El parámetro debe ser un número entero para este algoritmo.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Aviso: " + ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado: " + ex.getMessage(), "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }
}

