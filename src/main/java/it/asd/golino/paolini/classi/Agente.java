package it.asd.golino.paolini.classi;

import java.awt.*;
import java.util.Random;

public class Agente {
    private Cella cellaStart, cellaGoal;
    private int indice;

    // Imposta un colore casuale per lo sfondo
    private Random rand = new Random();
    private Color randomColor;

    private static int i = 1;

    public Agente(Cella cellaStart, Cella cellaGoal) {
        this.cellaStart = cellaStart;
        this.cellaGoal = cellaGoal;
        this.indice = i++;
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
