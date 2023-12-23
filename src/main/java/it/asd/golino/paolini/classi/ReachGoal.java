package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Calcolatore;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReachGoal {
    public ReachGoal(Graph<Cella, DefaultWeightedEdge> G, Cella init, Cella goal, int max) {
        ArrayList<Stato> closed, open = new ArrayList<>();
        open.add(new Stato(init, 0));

        ArrayList<VerticeTempo> v_t = new ArrayList<>();

        for (int t = 0; t <= max; t++) {
            for (var vertex : G.vertexSet())
            {
                VerticeTempo verticeTempo = new VerticeTempo(vertex, t);
                verticeTempo.setG(Double.POSITIVE_INFINITY);
                verticeTempo.setP(null);
                v_t.add(verticeTempo);
            }
        }

        int index = v_t.indexOf(new VerticeTempo(init,0));
        v_t.get(index).setG(0);
        v_t.get(index).setH(Calcolatore.calcolaEuristica(init, goal));

        while(!open.isEmpty())
        {

        }
    }
}
