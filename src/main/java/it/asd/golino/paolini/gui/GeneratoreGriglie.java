package it.asd.golino.paolini.gui;

import it.asd.golino.paolini.classi.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

public class GeneratoreGriglie extends JFrame {

    /**
     * Funzione che permette di stampare la griglia iniziale
     * @param griglia griglia da stampare
     */
    public static void stampaGriglia(Griglia griglia) {

        // Ottieni dimensioni della griglia
        int altezza = griglia.getAltezza();
        int larghezza = griglia.getLarghezza();

        // Ottieni la lista degli agenti
        Cella[][] grid = griglia.getGriglia();
        ArrayList<Agente> listaAgenti = griglia.getListaAgenti();

        // Crea una finestra per visualizzare la griglia
        JFrame dialogGrid = new JFrame("Griglia");
        dialogGrid.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Chiudi solo questa finestra

        dialogGrid.setVisible(true);
        StyleSystemGui.setCenterOfTheScreen(dialogGrid); // Imposta la finestra al centro dello schermo

        // Crea un pannello per la griglia con layout a griglia
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(altezza, larghezza));

        // Ciclo attraverso ogni cella nella griglia
        for (int i = 0; i < altezza; i++) {
            for (int j = 0; j < larghezza; j++) {
                Cella cella = griglia.getCella(i,j);
                JPanel cellPanel = new JPanel();
                cellPanel.setPreferredSize(new Dimension(60, 60));
                cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                // Switch basato sullo stato della cella
                switch (cella.getCellStatus().getValore()) {
                    case 0:
                        Grafo.grafo.addVertex(cella);
                        break;
                    case 1:
                        cellPanel.setBackground(Color.BLUE);
                        break;
                    case 2:

                        Grafo.grafo.addVertex(cella);
                        // Caso agente di partenza
                        int rigaStart = i;
                        int colonnaStart = j;

                        Agente agenteStart = listaAgenti.stream().filter(a -> a.getCellaStart().getRiga() == rigaStart
                                && a.getCellaStart().getColonna() == colonnaStart).findFirst().orElse(null);

                        assert agenteStart != null;
                        cellPanel.setBackground(agenteStart.getRandomColor());
                        JLabel labelStart = new JLabel("A_s_" + agenteStart.getIndice()); // Scrivi la lettera "A" al centro della cella

                        labelStart.setHorizontalAlignment(JLabel.CENTER);
                        labelStart.setVerticalAlignment(JLabel.CENTER);
                        labelStart.setForeground(Color.BLACK); // Imposta il colore del testo
                        cellPanel.add(labelStart);
                        break;
                    case 3:

                        Grafo.grafo.addVertex(cella);

                        // Caso agente di destinazione
                        int rigaGoal = i;
                        int colonnaGoal = j;
                        Agente agenteGoal = listaAgenti.stream().filter(a -> a.getCellaGoal().getRiga() == rigaGoal
                                && a.getCellaGoal().getColonna() == colonnaGoal).findFirst().orElse(null);

                        assert agenteGoal != null;
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
                gridPanel.add(cellPanel); // Aggiungi il pannello della cella al pannello della griglia
            }
        }

        // Crea un grafo basato sulla griglia
        Grafo.creaGrafo(altezza, larghezza);

        // Aggiungi il pannello della griglia alla finestra
        //dialogGrid.add(gridPanel);

        // Ridimensiona la finestra in base al contenuto
        //dialogGrid.pack();

        // Imposta la finestra al centro dello schermo
        //StyleSystemGui.setCenterOfTheScreen(dialogGrid);

        // Rendi la finestra visibile
        //dialogGrid.setVisible(true);

        // Dopo aver aggiunto i componenti al frame dialogGrid, cattura l'immagine
        //BufferedImage grigliaImage = catturaImmagineGriglia(dialogGrid);

//        try {
//            // Specifica il percorso in cui desideri salvare l'immagine
//            File output = new File("output\\griglia.png");
//
//            // Salva l'immagine
//            ImageIO.write(grigliaImage, "png", output);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
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