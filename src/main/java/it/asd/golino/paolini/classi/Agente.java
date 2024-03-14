package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Costanti;
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
    private final int max;

    private final Color randomColor;

    private static int i = 1;
    private final Cella[] percorso;

    public Agente(Cella cellaStart, Cella cellaGoal, int max) {

        this.cellaStart = cellaStart;
        this.cellaGoal = cellaGoal;

        // Incremento il contatore degli agenti presenti in lista
        this.indice = i++;

        // Imposta un colore casuale per lo sfondo
        Random rand = new Random();
        this.randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

        // Il percorso è semplicemente un array di celle dove quella iniziale è l'init
        this.max = max;
        percorso = new Cella[max];
        percorso[0] = cellaStart;

        for (int i = 1; i < max; i++) {
            percorso[i] = new Cella(-1, -1, StatoCelle.LIBERA);
        }
    }

    public Cella cellaDiUnPercorso(int i) {
        return percorso[i];
    }

    public void settaCellaDiPercorso(Cella cella, int indice) {
        percorso[indice] = cella;
    }

    public Color getRandomColor() {
        return randomColor;
    }

    public int getMax() {
        return this.max;
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

    public String stampaPercorso() {
        String res = "Agente " + this.indice + ".\n";
        for (int i = 0; i < max; i++) {
            res = res.concat(percorso[i].toString());
        }
        res = res.concat("\n");
        return res;
    }

    public String percorsoInesistente() {
        return String.format(Costanti.PERCORSO_NON_TROVATO, this.indice);
    }

}
