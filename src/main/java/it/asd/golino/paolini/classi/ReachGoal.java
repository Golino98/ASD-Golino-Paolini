package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Calcolatore;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class ReachGoal {

    public static void calculateReachGoal(Graph<Cella, DefaultWeightedEdge> G, Agente ag, Cella init, Cella goal, int max, Griglia griglia) {
        // Liste per la gestione degli stati aperti e chiusi
        ArrayList<VerticeTempo> closed = new ArrayList<>(), open = new ArrayList<>();
        open.add(new VerticeTempo(init, 0));

        // Lista di vertici temporali (v_t)
        ArrayList<VerticeTempo> v_t = initializeVerticeTempoList(G, max);

        // Inizializzazione del vertice di partenza
        initializeStartingVertex(open, v_t, init, goal);

        // Algoritmo A*
        while (!open.isEmpty()) {
            // Prendo il VerticeTempo con il minor valore della funzione f
            var lowest_f_score_state = Collections.min(open, Comparator.comparingDouble(VerticeTempo::getF));
            open.remove(lowest_f_score_state);
            closed.add(lowest_f_score_state);

            if (lowest_f_score_state.getV().toString().equalsIgnoreCase(goal.toString())) {
                reconstructPath(lowest_f_score_state, ag);
                break; // Termina il ciclo quando raggiungi la destinazione
            }

            expandState(G, ag, lowest_f_score_state, max, griglia, v_t, open, closed);
        }
    }

    private static ArrayList<VerticeTempo> initializeVerticeTempoList(Graph<Cella, DefaultWeightedEdge> G, int max) {
        ArrayList<VerticeTempo> v_t = new ArrayList<>();

        // Creazione di vertici temporali per ogni vertice e tempo
        for (int t = 0; t <= max; t++) {
            for (var vertex : G.vertexSet()) {
                VerticeTempo verticeTempo = new VerticeTempo(vertex, t);
                verticeTempo.setG(Double.POSITIVE_INFINITY);
                verticeTempo.setP(null);
                v_t.add(verticeTempo);
            }
        }

        return v_t;
    }

    private static void initializeStartingVertex(ArrayList<VerticeTempo> open, ArrayList<VerticeTempo> v_t, Cella init, Cella goal) {
        for (var v : v_t) {
            if (v.getV().equals(init) && v.getT() == 0) {
                open.getFirst().setG(0);
                open.getFirst().setF(Calcolatore.calcolaEuristica(init, goal));
                v.setG(0);
                v.setF(Calcolatore.calcolaEuristica(init, goal));
                break;
            }
        }
    }

    private static void expandState(Graph<Cella, DefaultWeightedEdge> G, Agente ag, VerticeTempo currentState,
                                    int max, Griglia griglia, ArrayList<VerticeTempo> v_t, ArrayList<VerticeTempo> open, ArrayList<VerticeTempo> closed) {
        int t = currentState.getT();

        if (t < max) {
            for (var edge : G.edgesOf(currentState.getV())) {
                Cella n = G.getEdgeTarget(edge);

                if (!isCellInClosedList(n, t + 1, closed) && isTraversable(ag, n, currentState, t + 1, griglia)) {
                    VerticeTempo n_t1 = findVerticeTempo(v_t, n, t + 1);
                    assert n_t1 != null;

                    // Calcolare w
                    double costo_edge_v_n = G.getEdgeWeight(edge);
                    if (currentState.getG() + costo_edge_v_n < n_t1.getG()) {
                        n_t1.setP(currentState);
                        n_t1.setG(currentState.getG() + costo_edge_v_n);
                        n_t1.setF(n_t1.getG() + Calcolatore.calcolaEuristica(n, currentState.getV()));
                        updateOpenList(open, n_t1);
                    }
                }
            }
        }
    }

    private static boolean isCellInClosedList(Cella cella, int t, ArrayList<VerticeTempo> closed) {
        return closed.stream().anyMatch(v -> v.getV().equals(cella) && v.getT() == t);
    }

    private static boolean isTraversable(Agente ag, Cella n, VerticeTempo currentState, int t, Griglia griglia) {
        for (Agente a : griglia.getListaAgenti()) {
            try {
                if (a.cellaDiUnPercorso(t).equals(n) ||
                        (a.cellaDiUnPercorso(t).equals(currentState.getV()) &&
                                a.cellaDiUnPercorso(t - 1).equals(n))) {
                    return false;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                break;
            }
        }
        return true;
    }

    private static VerticeTempo findVerticeTempo(ArrayList<VerticeTempo> v_t, Cella cella, int t) {
        return v_t.stream().filter(v -> v.getV().equals(cella) && v.getT() == t).findFirst().orElse(null);
    }

    private static void updateOpenList(ArrayList<VerticeTempo> open, VerticeTempo n_t1) {
        boolean inOpen = open.stream().anyMatch(vertice -> vertice.getV().equals(n_t1.getV()) && vertice.getT() == n_t1.getT());
        if (!inOpen) open.add(n_t1);
    }

    private static void reconstructPath(VerticeTempo verticeTempo, Agente agente) {
        for (int i = verticeTempo.getT(); i < agente.getMax(); i++) {
            agente.settaCellaDiPercorso(verticeTempo.getV(), i);
        }

        for (int i = verticeTempo.getT() - 1; i >= 0; i--) {
            agente.settaCellaDiPercorso(verticeTempo.getP().getV(), i);
            verticeTempo = verticeTempo.getP();
        }
    }
}
