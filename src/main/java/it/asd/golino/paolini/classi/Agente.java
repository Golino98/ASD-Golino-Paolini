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

    private boolean foundBest;
    private final LinkedList<VerticeTempo> percorso = new LinkedList<>();

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

        this.percorso.add(new VerticeTempo(cellaGoal,0));
        this.percorso.add(new VerticeTempo(cellaStart,0));
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

    public Queue<VerticeTempo> getPercorso() {
        return percorso;
    }

    public void aggiungiNodoPercorso(Cella cella, int t, Griglia g) {
        percorso.remove();
        percorso.add(new VerticeTempo(cella,t));
        percorso.add(new VerticeTempo(cellaStart,0));
        percorso.getLast().setT(percorso.size()-1);

        g.cambiaStatoCella(cella.getRiga(), cella.getColonna(), StatoCelle.LIBERA.getValore(), StatoCelle.PERCORSO.getValore());

    }

    public Cella getCellaPercorso(int t)
    {
        return percorso.get(t).getV();
    }

    public String stampaPercorso()
    {
        String result = "";
        for(var cella : percorso)
        {
            result = result.concat(cella.toString() + " - - - ");
        }
        return result;
    }

}
