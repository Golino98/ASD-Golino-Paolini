package it.asd.golino.paolini.gui;

import it.asd.golino.paolini.classi.Griglia;
import it.asd.golino.paolini.classi.Risolutore;
import it.asd.golino.paolini.utility.Calcolatore;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
    JLabel maxLabel;
    JTextField maxField;

    public GuiGeneratoreGriglie() {
        setContentPane(contentPane);
        getRootPane().setDefaultButton(buttonGenera);
        initComponents();
        setVisible(true);
        setWindowBehavior();
        handleGeneraButton();
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

            // File per i risultati della prima funzione
            String firstFunctionOutputFile = "output//first_function_output.txt";
            // File per i risultati della seconda funzione
            String secondFunctionOutputFile = "output//relaxed_output.txt";

//            int altezza = Integer.parseInt(altezzaField.getText());
//            int larghezza = Integer.parseInt(larghezzaField.getText());
//            int percentage = Integer.parseInt(percentualeCelleTraversabiliField.getText());
//            int agglomerazione = Integer.parseInt(fattoreAgglomerazioneField.getText());
//            int agenti = Integer.parseInt(numeroAgentiField.getText());
//            int max = Integer.parseInt(maxField.getText());

            int altezza = 10;
            int larghezza = 20;
            int percentage = 9;
            int agglomerazione = 5;
            int agenti = 3;
            int max = 15;


            // Controllo sulla positività degli input
            if (altezza <= 0 || larghezza <= 0 || percentage < 0 || agglomerazione <= 0 || agenti < 1 || max < 1) {
                showErrorDialog(ERRORE_NUMERO_NEGATIVO);
                if (altezza <= 0) {
                    resetTextField(altezzaField);
                } else if (larghezza <= 0) {
                    resetTextField(larghezzaField);
                } else if (percentage < 0 || percentage > 100) {
                    resetTextField(percentualeCelleTraversabiliField);
                } else if (agglomerazione <= 0) {
                    resetTextField(fattoreAgglomerazioneField);
                } else if (max < 1) {
                    resetTextField(maxField);
                } else {
                    resetTextField(numeroAgentiField);
                }
            } else if (percentage > 100) {
                showErrorDialog(ERRORE_PERCENTUALE_MASSIMA);
                resetTextField(percentualeCelleTraversabiliField);

            } else if (Calcolatore.calcolaNumeroCellePercentuale(altezza, larghezza, percentage) > altezza * larghezza - agenti * 2 || 2 * agenti > altezza * larghezza) {
                showErrorDialog(ERRORE_NUMERO_CELLE);
                resetTextField(percentualeCelleTraversabiliField);
                resetTextField(numeroAgentiField);
            } else {
                dispose();

                //Dopo aver effettuato tutti i controlli sui dati avvio effettivamente il programma

                // Creo la griglia, generando automaticamente gli ostacoli e gli agenti nel percorso
                Griglia griglia = new Griglia(altezza, larghezza, percentage, agglomerazione, agenti, max);

                // Stampo la griglia
                GeneratoreGriglie.stampaGriglia(griglia);

                FileWriter firstFunctionWriter = new FileWriter(firstFunctionOutputFile);
                BufferedWriter firstFunctionBufferedWriter = new BufferedWriter(firstFunctionWriter);

                // Misura il tempo e la memoria prima dell'esecuzione della prima funzione
                long startTime = System.currentTimeMillis();
                long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                // Risolvo il problema
                Risolutore.risolviProblema(false);

                // Misura il tempo e la memoria dopo l'esecuzione della prima funzione
                long endTime = System.currentTimeMillis();
                long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                // Calcola il tempo e la memoria utilizzata dalla prima funzione
                long elapsedTime = endTime - startTime;
                long usedMemory = endMemory - startMemory;

                // Scrivi i risultati della prima funzione sul file
                firstFunctionBufferedWriter.write("Tempo impiegato dalla prima funzione: " + elapsedTime + " millisecondi\n");
                firstFunctionBufferedWriter.write("Memoria utilizzata dalla prima funzione: " + usedMemory + " byte\n");

                firstFunctionBufferedWriter.close();

                FileWriter secondFunctionWriter = new FileWriter(secondFunctionOutputFile);
                BufferedWriter secondFunctionBufferedWriter = new BufferedWriter(secondFunctionWriter);

                // Misura il tempo e la memoria prima dell'esecuzione della seconda funzione
                startTime = System.currentTimeMillis();
                startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();


                for (var a : Griglia.listaAgenti) {
                    a.resetAgente();
                }

                Risolutore.risolviProblema(true);

                endTime = System.currentTimeMillis();
                endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

                // Calcola il tempo e la memoria utilizzata dalla seconda funzione
                elapsedTime = endTime - startTime;
                usedMemory = endMemory - startMemory;

                // Scrivi i risultati della seconda funzione sul file
                secondFunctionBufferedWriter.write("Tempo impiegato dalla seconda funzione: " + elapsedTime + " millisecondi\n");
                secondFunctionBufferedWriter.write("Memoria utilizzata dalla seconda funzione: " + usedMemory + " byte\n");

                secondFunctionBufferedWriter.close();

            }
        } catch (NumberFormatException e) {
            showErrorDialog(ERRORE_CONVERSIONE_NUMERO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleResetButton() {
        // Resetta i campi di input
        resetTextField(altezzaField);
        resetTextField(larghezzaField);
        resetTextField(percentualeCelleTraversabiliField);
        resetTextField(fattoreAgglomerazioneField);
        resetTextField(numeroAgentiField);
        resetTextField(maxField);
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

    public void showErrorDialog(String errorMessage) {
        // Mostra una finestra di dialogo con il messaggio di errore
        JLabel erroreLabel = new JLabel(errorMessage);
        erroreLabel.setFont(BUTTON_FONT);
        JOptionPane.showMessageDialog(this, erroreLabel, ERRORE, JOptionPane.ERROR_MESSAGE);
    }
}