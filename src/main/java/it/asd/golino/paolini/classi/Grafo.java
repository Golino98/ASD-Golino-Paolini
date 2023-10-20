package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Costanti;
import it.asd.golino.paolini.utility.StatoCelle;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Grafo {

    // Creazione di un grafo direzionato pesato utilizzando JGraphT
    private static Graph<Cella, DefaultWeightedEdge> grafo = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    // Metodo per creare il grafo
    public static void creaGrafo() {
        // Aggiungi i vertici al grafo
        for (Cella vertice : Vertice.getVertici()) {
            grafo.addVertex(vertice);
        }
        // Crea i collegamenti tra i vertici
        for (Cella vertice : grafo.vertexSet()) {
            creaConnessioni(vertice);
        }
        // Stampa il grafo
        stampGrafo();
    }

    // Metodo per creare i collegamenti tra i vertici
    public static void creaConnessioni(Cella vertice) {

        ArrayList<Cella> diagonali = new ArrayList<>();
        ArrayList<Cella> cardinali = new ArrayList<>();

        // Crea le celle adiacenti in diagonale
        diagonali.add(new Cella(vertice.getRiga() - 1, vertice.getColonna() + 1, StatoCelle.LIBERA.getValore()));
        diagonali.add(new Cella(vertice.getRiga() + 1, vertice.getColonna() + 1, StatoCelle.LIBERA.getValore()));
        diagonali.add(new Cella(vertice.getRiga() + 1, vertice.getColonna() - 1, StatoCelle.LIBERA.getValore()));
        diagonali.add(new Cella(vertice.getRiga() - 1, vertice.getColonna() - 1, StatoCelle.LIBERA.getValore()));

        // Crea le celle adiacenti in modo cardinale
        cardinali.add(new Cella(vertice.getRiga() - 1, vertice.getColonna(), StatoCelle.LIBERA.getValore()));
        cardinali.add(new Cella(vertice.getRiga(), vertice.getColonna() + 1, StatoCelle.LIBERA.getValore()));
        cardinali.add(new Cella(vertice.getRiga() + 1, vertice.getColonna(), StatoCelle.LIBERA.getValore()));
        cardinali.add(new Cella(vertice.getRiga(), vertice.getColonna() - 1, StatoCelle.LIBERA.getValore()));

        // Crea i collegamenti tra il vertice corrente e le celle adiacenti in diagonale
        for (Cella diagonale : diagonali) {
            for (Cella vertex : grafo.vertexSet()) {
                if (diagonale.toString().equalsIgnoreCase(vertex.toString()) && !grafo.containsEdge(vertex, vertice)) {
                    grafo.addEdge(vertice, vertex);
                    grafo.setEdgeWeight(vertice, vertex, Costanti.DIAGONALE);
                }
            }
        }

        // Crea i collegamenti tra il vertice corrente e le celle adiacenti in modo cardinale
        for (Cella cardinale : cardinali) {
            for (Cella vertex : grafo.vertexSet()) {
                if (cardinale.toString().equalsIgnoreCase(vertex.toString()) && !grafo.containsEdge(vertex, vertice)) {
                    grafo.addEdge(vertice, vertex);
                    grafo.setEdgeWeight(vertice, vertex, Costanti.MOSSA_CARDINALE);
                }
            }
        }
    }

    // Metodo per stampare il grafo
    public static void stampGrafo() {
        // Crea un oggetto PrintWriter per scrivere l'output sulla console
        PrintWriter consoleWriter = new PrintWriter(System.out);

        try {
            // Crea un oggetto PrintWriter per scrivere l'output su un file di testo chiamato "output.txt"
            PrintWriter fileWriter = new PrintWriter(new FileWriter("output\\grafo.txt"));

            for (Cella vertice : grafo.vertexSet()) {
                // Stampa il contenuto del vertice sia sulla console che nel file di testo
                consoleWriter.format(Costanti.STAMPA_CELLA_GRAFO, vertice);
                fileWriter.format(Costanti.STAMPA_CELLA_GRAFO, vertice);

                for (DefaultWeightedEdge edge : grafo.edgesOf(vertice)) {
                    Cella target = grafo.getEdgeTarget(edge);
                    Cella source = grafo.getEdgeSource(edge);
                    Cella altroVertice = target.equals(vertice) ? source : target;
                    double peso = grafo.getEdgeWeight(edge);

                    // Stampa le informazioni relative al collegamento sia sulla console che nel file di testo
                    consoleWriter.format(Costanti.STAMPA_PESO_GRAFO, altroVertice, peso);
                    fileWriter.format(Costanti.STAMPA_PESO_GRAFO, altroVertice, peso);
                }

                // Stampa una parentesi graffa chiusa sia sulla console che nel file di testo per chiudere la definizione del vertice
                consoleWriter.println("}");
                fileWriter.println("}");
            }

            // Flusha e chiude il fileWriter per garantire che i dati siano scritti correttamente nel file
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            // Gestisce eventuali eccezioni di IO (Input/Output) stampando il messaggio di errore sulla console
            System.out.println(e.getMessage());
        } finally {
            // Flusha e chiude il consoleWriter per assicurarsi che i dati siano correttamente scritti sulla console
            consoleWriter.flush();
            consoleWriter.close();
        }
    }
}
