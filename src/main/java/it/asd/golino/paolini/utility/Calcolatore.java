package it.asd.golino.paolini.utility;

import it.asd.golino.paolini.Main;
import it.asd.golino.paolini.classi.Cella;
import it.asd.golino.paolini.classi.Grafo;
import it.asd.golino.paolini.classi.Griglia;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Calcolatore {
    public static int calcolaNumeroCellePercentuale(int altezza, int larghezza, int percentuale) {
        return Math.round((float) ((altezza * larghezza * percentuale) / 100));
    }

    public static double calcolaEuristica(Cella init, Cella goal) {
        var grafoAgente = Grafo.grafo;

        for (var agente : Griglia.listaAgenti) {
            if (!(agente.getCellaStart().getRiga() == init.getRiga() &&
                    agente.getCellaStart().getColonna() == init.getColonna() &&
                    agente.getCellaGoal().getRiga() == goal.getRiga() &&
                    agente.getCellaGoal().getColonna() == goal.getColonna()))
            {
                grafoAgente.addVertex(agente.getCellaStart());
                grafoAgente.addVertex(agente.getCellaGoal());
                Grafo.creaConnessioni(agente.getCellaStart(), grafoAgente);
                Grafo.creaConnessioni(agente.getCellaGoal(), grafoAgente);
            }
        }

        DijkstraShortestPath<Cella, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(grafoAgente);
        return dijkstraShortestPath.getPathWeight(init, goal);
    }
}
