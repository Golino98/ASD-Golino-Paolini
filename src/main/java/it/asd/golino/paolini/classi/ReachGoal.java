package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Calcolatore;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class ReachGoal {

    private static final ArrayList<Agente> sigma = new ArrayList<>();

    public static Agente calculateReachGoal(Graph<Cella, DefaultWeightedEdge> G, Cella init, Cella goal, int max, Griglia griglia) {
        boolean traversable;
        int index = 0;

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

        for (var v : v_t) {
            if (v.getV().toString().equalsIgnoreCase(init.toString()) && v.getT() == 0) {
                index = v_t.indexOf(v);
                break;
            }
        }
        v_t.get(index).setG(0);

        // 11 -  f(<init,0>) <- h(init, goal)
        v_t.get(index).setF(Calcolatore.calcolaEuristica(init, goal));

        while (!open.isEmpty()) {
            // 13 - <v,t> <- the state in Open with the lowest f-score
            var lowest_f_score_state = Collections.min(open, Comparator.comparingDouble(VerticeTempo::getF));
            open.remove(lowest_f_score_state);
            closed.add(lowest_f_score_state);
            if (lowest_f_score_state.getV().toString().equalsIgnoreCase(goal.toString()))
            {
                var percorso = reconstructPath(init, goal, lowest_f_score_state.getP(), lowest_f_score_state.getT(), griglia);
                sigma.add(percorso);
                return percorso;
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

                            // Riga 28
                            if (a.getCellaPercorso(t + 1).toString().equalsIgnoreCase(n.toString()) ||
                                    (a.getCellaPercorso(t + 1).toString().equalsIgnoreCase(lowest_f_score_state.getV().toString())
                                            && a.getCellaPercorso(t).toString().equalsIgnoreCase(n.toString()))) {
                                traversable = false;
                            }
                        }

                        VerticeTempo n_t1 = null;
                        for (var control : v_t) {
                            if (control.getT() == t + 1 && control.getV().toString().equalsIgnoreCase(n.toString())) {
                                n_t1 = control;
                                break;
                            }
                        }

                        if (n_t1 != null) {
                            double w = 0;
                            if (G.getEdge(lowest_f_score_state.getV(), n) != null) {
                                w = G.getEdgeWeight(G.getEdge(lowest_f_score_state.getV(), n));
                            } else {
                                w = G.getEdgeWeight(G.getEdge(n, lowest_f_score_state.getV()));
                            }

                            if (traversable) {
                                if (lowest_f_score_state.getG() + w < n_t1.getG()) {
                                    // P(<n, t+1>) <- <v,t>
                                    n_t1.setP(lowest_f_score_state);

                                    // g(<n, t+1>) <- g(<v,t>) + w(v,n)
                                    n_t1.setG(lowest_f_score_state.getG() + w);
                                    n_t1.setF(n_t1.getG() + Calcolatore.calcolaEuristica(n, goal));
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
        return null;
    }

    private static Agente reconstructPath(Cella init, Cella goal, VerticeTempo P, int t, Griglia griglia) {
        for (var a : Griglia.listaAgenti) {
            if (a.getCellaStart().toString().equalsIgnoreCase(init.toString()) && a.getCellaGoal().toString().equalsIgnoreCase(goal.toString())) {
                while (P.getV().toString().equalsIgnoreCase(init.toString())) {
                    a.aggiungiNodoPercorso(P.getP().getV(), t, griglia);
                    P = P.getP();
                }
                a.setFoundBest(true);
            }
        }
        return null;
    }
}