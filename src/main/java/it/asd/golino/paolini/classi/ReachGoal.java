package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Calcolatore;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class ReachGoal {

    private static final ArrayList<Agente> sigma = new ArrayList<>();

    public static void calculateReachGoal(Graph<Cella, DefaultWeightedEdge> G, Cella init, Cella goal, int max) {
        boolean traversable;

        ArrayList<VerticeTempo> closed = new ArrayList<>(), open = new ArrayList<>();
        open.add(new VerticeTempo(init, 0));

        ArrayList<VerticeTempo> v_t = new ArrayList<>();

        for (int t = 0; t <= max; t++) {
            for (var vertex : G.vertexSet()) {
                VerticeTempo verticeTempo = new VerticeTempo(vertex, t);
                verticeTempo.setG(Double.POSITIVE_INFINITY);
                verticeTempo.setP(null);
                v_t.add(verticeTempo);
            }
        }

        int index = v_t.indexOf(new VerticeTempo(init, 0));

        // g(<init,0>) <- 0
        v_t.get(index).setG(0);

        // f(<init,0>) <- h(init, goal)
        v_t.get(index).setF(Calcolatore.calcolaEuristica(init, goal));

        while (!open.isEmpty()) {
            // <v,t> <- the state in Open with the lowest f-score
            var lowest_f_score_state = Collections.min(open, Comparator.comparingDouble(VerticeTempo::getF));
            open.remove(lowest_f_score_state);
            closed.add(lowest_f_score_state);
            if (lowest_f_score_state.getV().toString().equalsIgnoreCase(goal.toString())) {
                // sigma <- sigma + reconstruct path
            }

            int t = lowest_f_score_state.getT();
            if (t < max) {
                // foreach n in Adj[v]
                for (var edge : G.edgesOf(lowest_f_score_state.getV())) {
                    Cella n = G.getEdgeTarget(edge);

                    // if <n, t+1> not in Closed
                    if (!closed.contains(new VerticeTempo(n, t + 1))) {
                        traversable = true;
                        for (Agente a : sigma) {

                            traversable = false;
                        }

                        VerticeTempo n_t1 = null;
                        for (var control : v_t) {
                            if (control.getT() == t + 1 && control.getV().toString().equalsIgnoreCase(n.toString()))
                            {
                                n_t1 = control;
                                break;
                            }
                        }

                        var indice = v_t.indexOf(n_t1);

                        // CONTROLLARE SE FUNZIONA CON I NEW PERCHÈ HO PAURA CHE LAVORI SU INDIRIZZI E NON SU VALORI
                        if (traversable) {
                            if (lowest_f_score_state.getG() + Calcolatore.calcolaEuristica(lowest_f_score_state.getV(), n) < v_t.get(indice).getG()) {
                                // P(<n, t+1>) <- <v,t>
                                v_t.get(indice).setP(lowest_f_score_state);

                                // g(<n, t+1>) <- g(<v,t>) + w(v,n)
                                v_t.get(indice).setG(lowest_f_score_state.getG() + Calcolatore.calcolaEuristica(lowest_f_score_state.getV(), n));

                                v_t.get(indice).setF(v_t.get(indice).getG() + Calcolatore.calcolaEuristica(n, goal));
                            }

                            if (!open.contains(n_t1)) {
                                open.add(n_t1);
                            }
                        }
                    }
                }
            }
        }

        //RITORNO ERRORE
    }

    public static void aggiungiAgentePercorsoOttimo(Agente agente) {
        sigma.add(agente);
    }
}