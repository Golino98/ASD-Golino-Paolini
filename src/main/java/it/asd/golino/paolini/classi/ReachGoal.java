package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Calcolatore;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class ReachGoal {

    // Lista globale degli agenti che hanno raggiunto l'obiettivo
    private static final ArrayList<Agente> sigma = new ArrayList<>();

    /**
     * Calcola il percorso ottimale da un punto di partenza a un obiettivo su una griglia utilizzando l'algoritmo A*.
     *
     * @param G       Grafo rappresentante la griglia
     * @param init    Cella di partenza
     * @param goal    Cella di destinazione
     * @param max     Massimo numero di passi consentiti
     * @param griglia Griglia di riferimento
     * @return Agente che rappresenta il percorso ottimale
     */
    public static Agente calculateReachGoal(Graph<Cella, DefaultWeightedEdge> G, Cella init, Cella goal, int max, Griglia griglia) {
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
                verticeTempo.setG(Double.POSITIVE_INFINITY);
                verticeTempo.setP(null);
                v_t.add(verticeTempo);
            }
        }

        // Inizializzazione del vertice di partenza
        for (var v : v_t) {
            if (v.getV().toString().equalsIgnoreCase(init.toString()) && v.getT() == 0) {
                index = v_t.indexOf(v);
                break;
            }
        }
        v_t.get(index).setG(0);
        v_t.get(index).setF(Calcolatore.calcolaEuristica(init, goal));

        // Algoritmo A*
        while (!(open.isEmpty())) {
            var lowest_f_score_state = Collections.min(open, Comparator.comparingDouble(VerticeTempo::getF));
            open.remove(lowest_f_score_state);
            closed.add(lowest_f_score_state);

            if (lowest_f_score_state.getV().toString().equalsIgnoreCase(goal.toString())) {
                var percorso = reconstructPath(init, goal, lowest_f_score_state.getP(), lowest_f_score_state.getT(), griglia);
                sigma.add(percorso);
                return percorso;
            }

            int t = lowest_f_score_state.getT();
            index = -1;
            Cella n;

            if (t < max) {
                for (var edge : G.edgesOf(lowest_f_score_state.getV())) {
                    if (G.getEdgeTarget(edge) != lowest_f_score_state.getV()) {
                        n = G.getEdgeTarget(edge);
                    } else {
                        n = G.getEdgeSource(edge);
                    }

                    for (var v : closed) {
                        if (v.getV().toString().equalsIgnoreCase(n.toString()) && v.getT() == t + 1) {
                            index = closed.indexOf(v);
                        }
                    }

                    if (index == -1) {
                        traversable = true;

                        // Controllo se la cella è già stata attraversata da altri agenti
                        for (Agente a : sigma) {
                            if (a.getCellaPercorso(t + 1).toString().equalsIgnoreCase(closed.get(index).getV().toString()) ||
                                    (a.getCellaPercorso(t + 1).toString().equalsIgnoreCase(lowest_f_score_state.getV().toString()) &&
                                            a.getCellaPercorso(t).toString().equalsIgnoreCase(closed.get(index).getV().toString()))) {
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
                            double w = G.getEdge(lowest_f_score_state.getV(), n) != null ?
                                    G.getEdgeWeight(G.getEdge(lowest_f_score_state.getV(), n)) :
                                    G.getEdgeWeight(G.getEdge(n, lowest_f_score_state.getV()));

                            if (traversable) {
                                if (lowest_f_score_state.getG() + w < n_t1.getG()) {
                                    n_t1.setP(lowest_f_score_state);
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
        }
        return null; // RITORNO ERRORE
    }

    /**
     * Ricostruisce il percorso ottimale da un vertice temporale iniziale al vertice di destinazione.
     *
     * @param init    Cella di partenza
     * @param goal    Cella di destinazione
     * @param P       Vertice temporale precedente
     * @param t       Tempo
     * @param griglia Griglia di riferimento
     * @return Agente rappresentante il percorso ottimale
     */
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
        return null; // Non utilizzato
    }
}