package it.asd.golino.paolini.classi;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

public class Agente {

    private final Cella cellaStart;
    private final Cella cellaGoal;
    private final int indice;

    private final Color randomColor;

    private boolean foundBest;
    private Stack<Cella> percorso;

    private static int i = 1;

    public Agente(Cella cellaStart, Cella cellaGoal) {

        this.cellaStart = cellaStart;
        this.cellaGoal = cellaGoal;

        // Incremento il contatore degli agenti presenti in lista
        this.indice = i++;

        // Imposta un colore casuale per lo sfondo
        Random rand = new Random();
        this.randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

        foundBest = false;

        this.percorso.push(cellaStart);
        this.percorso.push(cellaGoal);
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

    public boolean isFoundBest() {
        return foundBest;
    }

    public void setFoundBest(boolean foundBest) {
        this.foundBest = foundBest;
    }

    public Stack<Cella> getPercorso() {
        return percorso;
    }

    public void aggiungiNodoPercorso(Cella cella)
    {
        percorso.pop();
        percorso.push(cella);
        percorso.push(cellaGoal);
    }
}
