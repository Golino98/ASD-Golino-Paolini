package it.asd.golino.paolini.gui;

import it.asd.golino.paolini.classi.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class GeneratoreGriglie extends JFrame {

    /**
     * Funzione che permette di stampare la griglia iniziale
     * @param griglia griglia da stampare
     */
    public static void stampaGriglia(Griglia griglia) {

        int altezza = griglia.getAltezza();
        int larghezza = griglia.getLarghezza();

        Cella[][] grid = griglia.getGriglia();

        ArrayList<Agente> listaAgenti = new ArrayList<>(griglia.getListaAgenti());

        JFrame dialogGrid = new JFrame("Griglia");
        dialogGrid.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chiudi solo questa finestra

        dialogGrid.setVisible(true);
        StyleSystemGui.setCenterOfTheScreen(dialogGrid);

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(altezza, larghezza));

        for (int i = 0; i < altezza; i++) {
            for (int j = 0; j < larghezza; j++) {
                Cella cella = grid[i][j];
                JPanel cellPanel = new JPanel();
                cellPanel.setPreferredSize(new Dimension(60, 60));
                cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                switch (cella.getCellStatus().getValore()) {
                    case 0:
                        Vertice.aggiungiVertice(cella);
                        break;
                    case 1:
                        cellPanel.setBackground(Color.BLUE);
                        break;
                    case 2:
                        int rigaStart = i;
                        int colonnaStart = j;

                        Agente agenteStart = listaAgenti.stream().filter(a -> a.getCellaStart().getRiga() == rigaStart
                                && a.getCellaStart().getColonna() == colonnaStart).findFirst().orElse(null);

                        cellPanel.setBackground(agenteStart.getRandomColor());
                        JLabel labelStart = new JLabel("A_s_" + agenteStart.getIndice()); // Scrivi la lettera "A" al centro della cella

                        labelStart.setHorizontalAlignment(JLabel.CENTER);
                        labelStart.setVerticalAlignment(JLabel.CENTER);
                        labelStart.setForeground(Color.BLACK); // Imposta il colore del testo
                        cellPanel.add(labelStart);
                        break;
                    case 3:
                        int rigaGoal = i;
                        int colonnaGoal = j;
                        Agente agenteGoal = listaAgenti.stream().filter(a -> a.getCellaGoal().getRiga() == rigaGoal
                                && a.getCellaGoal().getColonna() == colonnaGoal).findFirst().orElse(null);

                        cellPanel.setBackground(agenteGoal.getRandomColor());
                        JLabel labelEnd = new JLabel("A_g_" + agenteGoal.getIndice()); // Scrivi la lettera "A" al centro della cella
                        labelEnd.setHorizontalAlignment(JLabel.CENTER);
                        labelEnd.setVerticalAlignment(JLabel.CENTER);
                        labelEnd.setForeground(Color.BLACK); // Imposta il colore del testo
                        cellPanel.add(labelEnd);
                        break;
                    default:
                        break;
                }
                gridPanel.add(cellPanel);
            }
        }

        Grafo.creaGrafo();
        dialogGrid.add(gridPanel);
        dialogGrid.pack();
        StyleSystemGui.setCenterOfTheScreen(dialogGrid);
        dialogGrid.setVisible(true);

        // Dopo aver aggiunto i componenti al frame dialogGrid, cattura l'immagine
        BufferedImage grigliaImage = catturaImmagineGriglia(dialogGrid);

        try {
            // Specifica il percorso in cui desideri salvare l'immagine
            File output = new File("output\\griglia.png");

            // Salva l'immagine
            ImageIO.write(grigliaImage, "png", output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo che permette di effettuare lo screen del frame che gli si passa
     * @param frame sul quale effettuare lo screen
     * @return lo screen appena fatto
     */
    public static BufferedImage catturaImmagineGriglia(JFrame frame) {
        BufferedImage image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
        frame.paint(image.getGraphics());
        return image;
    }
}