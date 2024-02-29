package it.asd.golino.paolini.utility;

import it.asd.golino.paolini.Main;
import it.asd.golino.paolini.classi.Cella;
import it.asd.golino.paolini.classi.Grafo;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Calcolatore {
    public static int calcolaNumeroCellePercentuale(int altezza, int larghezza, int percentuale) {
        return Math.round((float) ((altezza * larghezza * percentuale) / 100));
    }

    public static double calcolaEuristica(Cella init, Cella goal) {
        var distanza1 = Math.pow(init.getRiga() - goal.getRiga(),2);
        var distanza2 = Math.pow(init.getColonna() - goal.getColonna(),2);

        return Math.sqrt(distanza1+distanza2);
    }
}
