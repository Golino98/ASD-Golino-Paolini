package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Calcolatore;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class ReachGoal {

    /**
     * Calcola il percorso ottimale da un punto di partenza a un obiettivo su una griglia utilizzando l'algoritmo A*.
     *
     * @param G    Grafo rappresentante la griglia
     * @param init Cella di partenza
     * @param goal Cella di destinazione
     * @param max  Massimo numero di passi consentiti
     */
    public static boolean calculateReachGoal(Graph<Cella, DefaultWeightedEdge> G, Agente ag, Cella init, Cella goal, int max) {
        boolean traversable;
        int index = 0;

        // Liste per la gestione degli stati aperti e chiusi
        ArrayList<VerticeTempo> closed = new ArrayList<>(), open = new ArrayList<>();
        open.add(new VerticeTempo(init, 0));

        // Lista di vertici temporali (v_t)
        ArrayList<VerticeTempo> v_t = new ArrayList<>();

        // Creazione di vertici temporali per ogni vertice e tempo
        for (int t = 0; t <= max; t++) {
            for (var vertex : G.vertexSet()) {
                VerticeTempo verticeTempo = new VerticeTempo(vertex, t);
                if (t == 0 && vertex.toString().equalsIgnoreCase(init.toString())) {
                    verticeTempo.setG(0);
                    verticeTempo.setF(Calcolatore.calcolaEuristica(init, goal));
                } else {
                    verticeTempo.setG(Double.POSITIVE_INFINITY);
                    verticeTempo.setP(null);
                }
                v_t.add(verticeTempo);
            }
        }

        // Algoritmo A*
        while (!(open.isEmpty())) {

            // Prendo il VerticeTempo con il minor valore della funzione f
            var lowest_f_score_state = Collections.min(open, Comparator.comparingDouble(VerticeTempo::getF));
            open.remove(lowest_f_score_state);
            closed.add(lowest_f_score_state);

            if (lowest_f_score_state.getV().toString().equalsIgnoreCase(goal.toString())) {
                reconstructPath(lowest_f_score_state, ag);
                return true;
            }

            int t = lowest_f_score_state.getT();
            index = -1;
            Cella n;

            if (t < max) {
                if (G.containsVertex(lowest_f_score_state.getV())) {
                    for (var edge : G.edgesOf(lowest_f_score_state.getV())) {
                        n = G.getEdgeTarget(edge);
                        Grafo.creaConnessioni(n, G);

                        for (var v : closed) {
                            if (v.getV().toString().equalsIgnoreCase(n.toString()) && v.getT() == t + 1) {
                                index = closed.indexOf(v);
                                break;
                            }
                        }

                        if (index == -1) {
                            traversable = true;

                            for (Agente a : Griglia.listaAgenti) {
                                try {

                                    if (a.cellaDiUnPercorso(t + 1).toString().equalsIgnoreCase(n.toString()) ||
                                            (a.cellaDiUnPercorso(t + 1).toString().equalsIgnoreCase(lowest_f_score_state.getV().toString())
                                                    && a.cellaDiUnPercorso(t).toString().equalsIgnoreCase(n.toString()))) {
                                        traversable = false;
                                    }
                                } catch (ArrayIndexOutOfBoundsException ex) {
                                    break;
                                }
                            }

                            if (traversable) {
                                VerticeTempo n_t1 = null;
                                for (var control : v_t) {
                                    if (control.getT() == t + 1 && control.getV().toString().equalsIgnoreCase(n.toString())) {
                                        n_t1 = control;
                                        break;
                                    }
                                }

                                assert n_t1 != null;
                                // calcolare w
                                var edge_v_n = Grafo.grafo.getEdge(lowest_f_score_state.getV(), n);
                                var costo_edge_v_n = Grafo.grafo.getEdgeWeight(edge_v_n);
                                if (lowest_f_score_state.getG() + costo_edge_v_n < n_t1.getG()) {
                                    n_t1.setP(lowest_f_score_state);
                                    n_t1.setG(lowest_f_score_state.getG() + costo_edge_v_n);
                                    n_t1.setF(n_t1.getG() + Calcolatore.calcolaEuristica(n, goal));
                                }

                                boolean inOpen = false;

                                for (var vertice : open) {
                                    if (vertice.getV().toString().equalsIgnoreCase(n_t1.getV().toString()) && vertice.getT() == t + 1) {
                                        inOpen = true;
                                        break;
                                    }
                                }
                                if (!inOpen) open.add(n_t1);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static void reconstructPath(VerticeTempo verticeTempo, Agente agente) {

        for (int i = verticeTempo.getT(); i < agente.getMax(); i++) {
            agente.settaCellaDiPercorso(verticeTempo.getV(), i);
        }

        while (verticeTempo.getP() != null) {
            agente.settaCellaDiPercorso(verticeTempo.getP().getV(), verticeTempo.getP().getT());
            verticeTempo = verticeTempo.getP();
        }
    }
}