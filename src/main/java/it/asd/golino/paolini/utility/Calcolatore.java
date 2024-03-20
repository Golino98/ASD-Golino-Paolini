package it.asd.golino.paolini.utility;

import it.asd.golino.paolini.classi.Cella;
import it.asd.golino.paolini.classi.Grafo;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Calcolatore {
    public static int calcolaNumeroCellePercentuale(int altezza, int larghezza, int percentuale) {
        return Math.round((float) ((altezza * larghezza * percentuale) / 100));
    }

    public static double calcolaEuristica(Cella init, Cella goal) {
        var grafoAgente = Grafo.grafo;
        DijkstraShortestPath<Cella, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(grafoAgente);
        return dijkstraShortestPath.getPathWeight(init, goal);
    }

    public static GraphPath<Cella, DefaultWeightedEdge> calcolaPercorsoMinimo(Cella init, Cella goal) {
        var grafoAgente = Grafo.grafo;
        DijkstraShortestPath<Cella, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(grafoAgente);
        return dijkstraShortestPath.getPath(init, goal);
    }
}