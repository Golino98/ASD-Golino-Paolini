package it.asd.golino.paolini.gui;

import it.asd.golino.paolini.classi.Griglia;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static it.asd.golino.paolini.utility.Costanti.*;

public class GuiGeneratoreGriglie extends JFrame {
    private JPanel contentPane;
    JButton buttonGenera;
    JButton buttonReset;
    JTextField altezzaField;
    JTextField larghezzaField;
    JTextField percentualeCelleTraversabiliField;
    JLabel altezzaGrigliaLabel;
    JLabel larghezzaGrigliaLabel;
    JLabel percentualeCelleTraversabiliLabel;
    JLabel agglomerazioneLabel;
    JTextField fattoreAgglomerazioneField;
    JLabel numeroAgentiLabel;
    JTextField numeroAgentiField;

    public GuiGeneratoreGriglie() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonGenera);
        initComponents();
        setVisible(true);
        setWindowBehavior();
    }

    private void initComponents() {
        // Imposta gli ActionListener per i pulsanti
        setButtonListeners();
    }

    private void setButtonListeners() {
        // Imposta gli ActionListener per i pulsanti "Genera" e "Reset"
        buttonGenera.addActionListener(e -> handleGeneraButton());
        buttonReset.addActionListener(e -> handleResetButton());
    }

    private void setWindowBehavior() {
        // Imposta il comportamento della finestra
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });

        contentPane.registerKeyboardAction(e -> handleEscapeKey(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void handleGeneraButton() {
        try {

            int altezza = Integer.parseInt(altezzaField.getText());
            int larghezza = Integer.parseInt(larghezzaField.getText());
            int percentage = Integer.parseInt(percentualeCelleTraversabiliField.getText());
            int agglomerazione = Integer.parseInt(fattoreAgglomerazioneField.getText());
            int agenti = Integer.parseInt(numeroAgentiField.getText());

            // Controllo sulla positività degli input
            if (altezza <= 0 || larghezza <= 0 || percentage < 0 || percentage > 100 || agglomerazione <= 0 || agenti < 1)
            {
                showErrorDialog(ERRORE_NUMERO_NEGATIVO);
                if (altezza <= 0) {
                    resetTextField(altezzaField);
                } else if (larghezza <= 0) {
                    resetTextField(larghezzaField);
                } else if (percentage < 0 || percentage > 100) {
                    resetTextField(percentualeCelleTraversabiliField);
                } else if (agglomerazione <= 0){
                    resetTextField(fattoreAgglomerazioneField);
                }else
                {
                    resetTextField(numeroAgentiField);
                }
            } else {
                dispose();
                Griglia griglia = new Griglia(altezza, larghezza, percentage, agglomerazione, agenti);
                GeneratoreGriglie.stampaGriglia(griglia);
            }
        } catch (NumberFormatException e)
        {
            showErrorDialog(ERRORE_CONVERSIONE_NUMERO);
        }
    }

    private void handleResetButton() {
        // Resetta i campi di input
        resetTextField(altezzaField);
        resetTextField(larghezzaField);
        resetTextField(percentualeCelleTraversabiliField);
        resetTextField(fattoreAgglomerazioneField);
        resetTextField(numeroAgentiField);
    }

    private void handleWindowClose() {
        // Chiude la finestra corrente
        dispose();
    }

    private void handleEscapeKey() {
        // Chiude la finestra corrente
        dispose();
    }

    private void resetTextField(JTextField textField) {
        // Imposta il testo del campo a una stringa vuota
        textField.setText("");
    }

    private void showErrorDialog(String errorMessage) {
        // Mostra una finestra di dialogo con il messaggio di errore
        JLabel erroreLabel = new JLabel(errorMessage);
        erroreLabel.setFont(BUTTON_FONT);
        JOptionPane.showMessageDialog(this, erroreLabel, ERRORE, JOptionPane.ERROR_MESSAGE);
    }
}