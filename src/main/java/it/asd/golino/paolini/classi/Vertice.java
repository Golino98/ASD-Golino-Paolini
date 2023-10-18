package it.asd.golino.paolini.classi;

import java.util.ArrayList;

public class Vertice
{
    private static ArrayList<Cella> vertici = new ArrayList<>();
    public static void aggiungiVertice(Cella vertice)
    {
        vertici.add(vertice);
    }

    public static ArrayList<Cella> getVertici() {
        return vertici;
    }
}
