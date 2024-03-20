package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Calcolatore;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.*;

public class ReachGoal {

    /**
     * Metodo per calcolare il percorso ottimale utilizzando l'algoritmo A*.
     *
     * @param graph       Grafo rappresentante la griglia
     * @param agent       L'agente che si muove sulla griglia
     * @param initialCell Cella di partenza
     * @param goalCell    Cella di destinazione
     * @param maxSteps    Massimo numero di passi consentiti
     * @param <V>         Tipo generico per i vertici del grafo
     * @param <E>         Tipo generico per gli archi del grafo
     * @return True se è stato trovato un percorso, altrimenti False
     */
    public static <V extends Cella, E extends DefaultWeightedEdge> boolean calculateReachGoal(Graph<V, E> graph, Agente agent, V initialCell, V goalCell, int maxSteps, boolean relaxed) {
        List<VerticeTempo> closed = new ArrayList<>(); // Lista degli stati chiusi
        List<VerticeTempo> open = new ArrayList<>(); // Lista degli stati aperti
        List<VerticeTempo> vTempoList = new ArrayList<>(); // Lista dei vertici temporali

        // Inizializzazione dei vertici temporali per ogni vertice e tempo
        for (int time = 0; time <= maxSteps; time++) {
            for (V vertex : graph.vertexSet()) {
                VerticeTempo verticeTempo = new VerticeTempo(vertex, time);
                if (time == 0 && vertex.toString().equalsIgnoreCase(initialCell.toString())) {
                    verticeTempo.setG(0);
                    verticeTempo.setF(Calcolatore.calcolaEuristica(initialCell, goalCell));
                    open.add(verticeTempo); // Aggiungi il vertice temporale iniziale agli stati aperti
                } else {
                    verticeTempo.setG(Double.POSITIVE_INFINITY);
                    verticeTempo.setP(null);
                }
                vTempoList.add(verticeTempo); // Aggiungi il vertice temporale alla lista vTempoList
            }
        }

        // Algoritmo A*
        while (!open.isEmpty()) {
            VerticeTempo lowestFScoreState = Collections.min(open, Comparator.comparingDouble(VerticeTempo::getF)); // Trova lo stato con il punteggio F minimo
            int currentTime = lowestFScoreState.getT(); // Ottieni il tempo corrente
            open.remove(lowestFScoreState); // Rimuovi lo stato aperto corrente dalla lista degli stati aperti
            closed.add(lowestFScoreState); // Aggiungi lo stato corrente alla lista degli stati chiusi

            // Se lo stato corrente corrisponde alla cella di destinazione, ricostruisci il percorso e restituisci true
            if (lowestFScoreState.getV().toString().equalsIgnoreCase(goalCell.toString())) {
                reconstructPath(lowestFScoreState, agent);
                return true;
            }

            // Inserimento della strategia alternativa

            // Il percorso rilassto è un percorso ottimo in cui si tiene conto della presenza degli ostacoli
            // ma si ignora la presenza di altri agenti.
            if (relaxed) {
                if (relaxedPath(agent, lowestFScoreState)) return true;
            }

            V currentVertex = null;
            boolean foundSelf = false;

            if (currentTime < maxSteps) {
                // Trova il vertice corrispondente allo stato corrente
                for (V vertex : graph.vertexSet()) {
                    if (vertex.toString().equalsIgnoreCase(lowestFScoreState.getV().toString())) {
                        currentVertex = vertex;
                        break;
                    }
                }

                // Esamina i vicini dello stato corrente
                for (E edge : graph.edgesOf(currentVertex)) {
                    int index = -1;
                    V neighbor = graph.getEdgeTarget(edge); // Ottieni il vicino corrente

                    if (foundSelf) {
                        if (neighbor == currentVertex) {
                            neighbor = graph.getEdgeSource(edge);
                        }
                    }

                    if (neighbor == currentVertex) {
                        foundSelf = true;
                    }

                    // Controlla se il vicino è già stato considerato in uno stato chiuso
                    for (VerticeTempo v : closed) {
                        if (v.getV().toString().equalsIgnoreCase(neighbor.toString()) && v.getT() == currentTime + 1) {
                            index = closed.indexOf(v);
                            break;
                        }
                    }

                    // Se il vicino non è stato considerato, verifica se è attraversabile
                    if (index == -1) {
                        boolean traversable = isTraversable(agent, currentTime, neighbor, lowestFScoreState);
                        if (traversable) {
                            // Trova il vertice temporale corrispondente al vicino
                            VerticeTempo neighborTempo = findVertexTempo(vTempoList, currentTime + 1, neighbor);

                            assert neighborTempo != null;

                            E edgeVn = graph.getEdge(currentVertex, neighbor);
                            if (edgeVn == null) {
                                edgeVn = graph.getEdge(neighbor, currentVertex);
                            }

                            double edgeWeight = graph.getEdgeWeight(edgeVn); // Ottieni il peso dell'arco tra il vertice corrente e il vicino

                            // Aggiorna i valori G, P e F se si trova un percorso migliore verso il vicino
                            if (lowestFScoreState.getG() + edgeWeight < neighborTempo.getG()) {
                                neighborTempo.setP(lowestFScoreState);
                                neighborTempo.setG(lowestFScoreState.getG() + edgeWeight);
                                neighborTempo.setF(neighborTempo.getG() + Calcolatore.calcolaEuristica(neighbor, goalCell));
                            }

                            // Aggiungi il vicino agli stati aperti se non è già presente
                            if (!open.contains(neighborTempo)) {
                                open.add(neighborTempo);
                            }
                        }
                    }
                }
            }
        }
        return false; // Non è stato trovato alcun percorso
    }

    /**
     * Metodo per verificare se un vicino è attraversabile.
     *
     * @param agent        L'agente che si muove sulla griglia
     * @param time         Tempo corrente
     * @param neighbor     Vicino da controllare
     * @param currentState Stato corrente
     * @return True se il vicino è attraversabile, altrimenti False
     */
    private static boolean isTraversable(Agente agent, int time, Cella neighbor, VerticeTempo currentState) {
        boolean traversable = true;
        for (Agente a : Griglia.listaAgenti) {
            try {
                if (a != agent) {
                    Cella cellAtTime = a.cellaDiUnPercorso(time + 1); // Ottieni la cella dell'agente al tempo successivo
                    Cella cellAtPreviousTime = a.cellaDiUnPercorso(time); // Ottieni la cella dell'agente al tempo corrente
                    if (cellAtTime.toString().equalsIgnoreCase(neighbor.toString()) ||
                            (cellAtTime.toString().equalsIgnoreCase(currentState.getV().toString()) &&
                                    cellAtPreviousTime.toString().equalsIgnoreCase(neighbor.toString()))) {
                        traversable = false; // Il vicino non è attraversabile se corrisponde alla cella dell'agente
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
        return traversable;
    }

    /**
     * Metodo per trovare il vertice temporale corrispondente a un determinato tempo e cella.
     *
     * @param vTempoList Lista dei vertici temporali
     * @param time       Tempo da cercare
     * @param cell       Cella da cercare
     * @return Il vertice temporale corrispondente, se trovato, altrimenti null
     */
    private static VerticeTempo findVertexTempo(List<VerticeTempo> vTempoList, int time, Cella cell) {
        for (VerticeTempo vertexTempo : vTempoList) {
            if (vertexTempo.getT() == time && vertexTempo.getV().toString().equalsIgnoreCase(cell.toString())) {
                return vertexTempo; // Restituisci il vertice temporale corrispondente
            }
        }
        return null; // Non è stato trovato il vertice temporale corrispondente
    }

    /**
     * Metodo per ricostruire il percorso ottimale utilizzando i predecessori memorizzati nei vertici temporali.
     *
     * @param verticeTempo Il vertice temporale finale
     * @param agente       L'agente che si muove sulla griglia
     */
    private static void reconstructPath(VerticeTempo verticeTempo, Agente agente) {
        // Imposta le celle del percorso nell'agente a partire dal tempo corrente
        for (int i = verticeTempo.getT(); i < agente.getMax(); i++) {
            agente.settaCellaDiPercorso(verticeTempo.getV(), i);
        }

        // Risale i predecessori fino a raggiungere la cella di partenza
        while (verticeTempo.getP() != null) {
            agente.settaCellaDiPercorso(verticeTempo.getP().getV(), verticeTempo.getP().getT());
            verticeTempo = verticeTempo.getP(); // Passa al predecessore successivo
        }
    }

    private static boolean relaxedPath(Agente agente, VerticeTempo vt) {

        var v = vt.getV();
        // Calcola il percorso minimo dall'inizio alla fine
        var percorso = Calcolatore.calcolaPercorsoMinimo(v, agente.getCellaGoal());
        var listaCelle = percorso.getVertexList();

        // Verifica se gli altri agenti condividono celle con l'agente corrente lungo il percorso
        for (int i = vt.getT(); i < percorso.getLength(); i++) {
            for (Agente a : Griglia.listaAgenti) {
                if (a != agente) {
                    if (a.cellaDiUnPercorso(i).equals(listaCelle.get(i))) {
                        return false; // Se c'è una condivisione, restituisci false
                    }
                }
            }
        }

        for (int i = vt.getT(); i < percorso.getLength(); i++) {
            agente.settaCellaDiPercorso(listaCelle.get(i), i);
        }

        // Imposta le celle rimanenti del percorso come celle di percorso per l'agente corrente
        for (int i = percorso.getLength(); i < agente.getMax(); i++) {
            agente.settaCellaDiPercorso(listaCelle.getLast(), i);
        }

        for (int i = vt.getT(); i >= 0; i--) {
            agente.settaCellaDiPercorso(vt.getV(), i);
            vt = vt.getP();
        }

        return true; // Restituisci true se tutto è andato bene
    }

}