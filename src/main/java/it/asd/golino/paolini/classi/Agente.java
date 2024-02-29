package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.StatoCelle;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class Agente {

    private final Cella cellaStart;
    private final Cella cellaGoal;
    private final int indice;

    private final Color randomColor;

    private static int i = 1;
    private Cella[] percorso;

    public Agente(Cella cellaStart, Cella cellaGoal, int max) {

        this.cellaStart = cellaStart;
        this.cellaGoal = cellaGoal;

        // Incremento il contatore degli agenti presenti in lista
        this.indice = i++;

        // Imposta un colore casuale per lo sfondo
        Random rand = new Random();
        this.randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

        // Il percorso è semplicemente un array di celle dove quella iniziale è l'init
        this.percorso = new Cella[max];
        percorso[0] = cellaStart;

    }

    public Color getRandomColor() {
        return randomColor;
    }

    public Cella getCellaStart() {
        return cellaStart;
    }

    public Cella getCellaGoal() {
        return cellaGoal;
    }

    public int getIndice() {
        return indice;
    }
}
