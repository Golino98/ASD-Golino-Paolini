package it.asd.golino.paolini.classi;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Agente {

    private final Cella cellaStart;
    private final Cella cellaGoal;
    private final int indice;

    private final Color randomColor;

    private static int i = 1;

    public Agente(Cella cellaStart, Cella cellaGoal) {
        this.cellaStart = cellaStart;
        this.cellaGoal = cellaGoal;
        this.indice = i++;
        // Imposta un colore casuale per lo sfondo
        Random rand = new Random();
        this.randomColor = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
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
