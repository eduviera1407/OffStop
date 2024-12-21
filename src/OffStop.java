
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class OffStop {

    private JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OffStop::new);
    }

    public OffStop() {
        frame = new JFrame("Control de Apagado");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mostrarPantallaPrincipal();

        frame.setVisible(true);
    }

    private void mostrarPantallaPrincipal() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(173, 216, 230));

        JLabel label = new JLabel("Selecciona la operación", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.DARK_GRAY);

        JButton botonApagar = new JButton("Apagar");
        botonApagar.setFont(new Font("Arial", Font.PLAIN, 24));
        botonApagar.setBackground(new Color(255, 99, 71));
        botonApagar.setForeground(Color.WHITE);

        JButton botonCancelar = new JButton("Cancelar apagado");
        botonCancelar.setFont(new Font("Arial", Font.PLAIN, 24));
        botonCancelar.setBackground(new Color(60, 179, 113));
        botonCancelar.setForeground(Color.WHITE);

        botonApagar.addActionListener(e -> mostrarPantallaApagar());

        botonCancelar.addActionListener(e -> {
            ProcessBuilder processBuilder = new ProcessBuilder("shutdown", "/a");
            try {
                processBuilder.start();
                JOptionPane.showMessageDialog(frame, "Apagado cancelado exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Error al intentar cancelar el apagado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(label);
        panel.add(botonApagar);
        panel.add(botonCancelar);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }

    private void mostrarPantallaApagar() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(240, 230, 140));

        JLabel label = new JLabel("Selecciona el tiempo para apagar", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setForeground(Color.DARK_GRAY);

        JPanel timePanel = new JPanel(new GridLayout(1, 3, 10, 10));
        timePanel.setBackground(new Color(240, 230, 140));

        JComboBox<Integer> horas = new JComboBox<>();
        JComboBox<Integer> minutos = new JComboBox<>();
        JComboBox<Integer> segundos = new JComboBox<>();

        for (int i = 0; i <= 23; i++) horas.addItem(i);
        for (int i = 0; i <= 59; i++) minutos.addItem(i);
        for (int i = 0; i <= 59; i++) segundos.addItem(i);

        horas.setFont(new Font("Arial", Font.PLAIN, 18));
        minutos.setFont(new Font("Arial", Font.PLAIN, 18));
        segundos.setFont(new Font("Arial", Font.PLAIN, 18));

        timePanel.add(horas);
        timePanel.add(minutos);
        timePanel.add(segundos);

        JButton botonConfirmar = new JButton("Confirmar");
        botonConfirmar.setFont(new Font("Arial", Font.PLAIN, 24));
        botonConfirmar.setBackground(new Color(255, 165, 0));
        botonConfirmar.setForeground(Color.WHITE);

        JButton botonVolver = new JButton("Volver");
        botonVolver.setFont(new Font("Arial", Font.PLAIN, 24));
        botonVolver.setBackground(new Color(100, 149, 237));
        botonVolver.setForeground(Color.WHITE);
        botonVolver.addActionListener(e -> mostrarPantallaPrincipal());

        JLabel labelCuentaAtras = new JLabel("", SwingConstants.CENTER);
        labelCuentaAtras.setFont(new Font("Arial", Font.ITALIC, 22));
        labelCuentaAtras.setForeground(Color.DARK_GRAY);

        botonConfirmar.addActionListener(new ActionListener() {
            Timer timer;
            int tiempoRestante;

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int horasSeleccionadas = (int) horas.getSelectedItem();
                    int minutosSeleccionados = (int) minutos.getSelectedItem();
                    int segundosSeleccionados = (int) segundos.getSelectedItem();

                    tiempoRestante = horasSeleccionadas * 3600 + minutosSeleccionados * 60 + segundosSeleccionados;

                    ProcessBuilder processBuilder = new ProcessBuilder("shutdown", "/s", "/t", String.valueOf(tiempoRestante));
                    processBuilder.start();

                    labelCuentaAtras.setText("Apagado en: " + tiempoRestante + " segundos");

                    timer = new Timer(1000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (tiempoRestante > 0) {
                                tiempoRestante--;
                                labelCuentaAtras.setText("Apagado en: " + tiempoRestante + " segundos");
                            } else {
                                timer.stop();
                                labelCuentaAtras.setText("El sistema se está apagando...");
                            }
                        }
                    });
                    timer.start();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error al intentar programar el apagado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(label);
        panel.add(timePanel);
        panel.add(botonConfirmar);
        panel.add(botonVolver);
        panel.add(labelCuentaAtras);

        frame.getContentPane().removeAll();
        frame.getContentPane().add(panel);
        frame.revalidate();
        frame.repaint();
    }
}
