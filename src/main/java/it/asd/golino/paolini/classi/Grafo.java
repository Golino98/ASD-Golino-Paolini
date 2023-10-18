package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Costanti;
import it.asd.golino.paolini.utility.StatoCelle;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;

public class Grafo {

    private static Graph<Cella, DefaultWeightedEdge> grafo = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    public static void creaGrafo() {
        for (Cella vertice : Vertice.getVertici()) {
            grafo.addVertex(vertice);
        }
        for (Cella vertice : grafo.vertexSet()) {
            creaConnessioni(vertice);
        }
        stampGrafo();
    }

    public static void creaConnessioni(Cella vertice) {

        ArrayList<Cella> diagonali = new ArrayList<>();
        ArrayList<Cella> cardinali = new ArrayList<>();

        //CELLA NORD EST
        diagonali.add(new Cella(vertice.getRiga() - 1, vertice.getColonna() + 1, StatoCelle.LIBERA.getValore()));

        //CELLA SUD EST
        diagonali.add(new Cella(vertice.getRiga() + 1, vertice.getColonna() + 1, StatoCelle.LIBERA.getValore()));

        //CELLA SUD OVEST
        diagonali.add(new Cella(vertice.getRiga() + 1, vertice.getColonna() - 1, StatoCelle.LIBERA.getValore()));

        //CELLA NORD OVEST
        diagonali.add(new Cella(vertice.getRiga() - 1, vertice.getColonna() - 1, StatoCelle.LIBERA.getValore()));

        // CELLA NORD
        cardinali.add(new Cella(vertice.getRiga() - 1, vertice.getColonna(), StatoCelle.LIBERA.getValore()));

        // CELLA EST
        cardinali.add(new Cella(vertice.getRiga(), vertice.getColonna() + 1, StatoCelle.LIBERA.getValore()));

        // CELLA SUD
        cardinali.add(new Cella(vertice.getRiga() + 1, vertice.getColonna(), StatoCelle.LIBERA.getValore()));

        // CELLA OVEST
        cardinali.add(new Cella(vertice.getRiga(), vertice.getColonna() - 1, StatoCelle.LIBERA.getValore()));


        //RISOLVERE LA STAMPA DOPPIA
        for (Cella diagonale : diagonali) {
            for (Cella vertex : grafo.vertexSet())
            {
                if (diagonale.toString().equalsIgnoreCase(vertex.toString()))
                {
                    grafo.addEdge(vertice, vertex);
                    grafo.setEdgeWeight(vertice, vertex, Costanti.DIAGONALE);
                }
            }
        }

        for (Cella cardinale : cardinali)
        {
            for (Cella vertex : grafo.vertexSet()) {
                if (cardinale.toString().equalsIgnoreCase(vertex.toString())) {
                    grafo.addEdge(vertice, vertex);
                    grafo.setEdgeWeight(vertice, vertex, Costanti.MOSSA_CARDINALE);
                }
            }
        }
    }

    public static void stampGrafo() {
        for (Cella vertice : grafo.vertexSet()) {
            System.out.format(Costanti.STAMPA_CELLA_GRAFO, vertice);
            for (DefaultWeightedEdge edge : grafo.edgesOf(vertice)) {
                Cella target = grafo.getEdgeTarget(edge);
                Cella source = grafo.getEdgeSource(edge);
                Cella altroVertice = target.equals(vertice) ? source : target;
                double peso = grafo.getEdgeWeight(edge);
                System.out.format(Costanti.STAMPA_PESO_GRAFO, altroVertice, peso);
            }
            System.out.println();
        }
    }
}