package it.asd.golino.paolini.classi;

import com.mxgraph.layout.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import it.asd.golino.paolini.utility.Costanti;
import it.asd.golino.paolini.utility.StatoCelle;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


import static it.asd.golino.paolini.utility.Costanti.PATH_ORIENTED_GRAPH_IMAGE;
import static it.asd.golino.paolini.utility.Costanti.PNG;

public class Grafo {

    // Creazione di un grafo direzionato pesato utilizzando JGraphT
    private static final Graph<Cella, DefaultWeightedEdge> grafo = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);

    /**
     * Metodo che permette la creazione di un grafo. Aggiunge i vertici alla variabile grafo.
     */
    public static void creaGrafo() {
        Vertice.getVertici().forEach(grafo::addVertex);
        grafo.vertexSet().forEach(Grafo::creaConnessioni);
        stampaGrafo();
    }

    /**
     * Metodo che permette di creare tutte le connessioni con celle libere adiacenti dato un vertice.
     * Sono gestiti anche i costi delle connessioni
     *
     * @param vertice vertice sul quale creare le connessioni
     */
    public static void creaConnessioni(Cella vertice) {

        ArrayList<Cella> diagonali = new ArrayList<>();
        ArrayList<Cella> cardinali = new ArrayList<>();

        // Crea le celle adiacenti in diagonale
        diagonali.add(new Cella(vertice.getRiga() - 1, vertice.getColonna() + 1, StatoCelle.LIBERA));
        diagonali.add(new Cella(vertice.getRiga() + 1, vertice.getColonna() + 1, StatoCelle.LIBERA));
        diagonali.add(new Cella(vertice.getRiga() + 1, vertice.getColonna() - 1, StatoCelle.LIBERA));
        diagonali.add(new Cella(vertice.getRiga() - 1, vertice.getColonna() - 1, StatoCelle.LIBERA));

        // Crea le celle adiacenti in modo cardinale
        cardinali.add(new Cella(vertice.getRiga() - 1, vertice.getColonna(), StatoCelle.LIBERA));
        cardinali.add(new Cella(vertice.getRiga(), vertice.getColonna() + 1, StatoCelle.LIBERA));
        cardinali.add(new Cella(vertice.getRiga() + 1, vertice.getColonna(), StatoCelle.LIBERA));
        cardinali.add(new Cella(vertice.getRiga(), vertice.getColonna() - 1, StatoCelle.LIBERA));

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

        // Creazione dei nodi cappio con sè stessi
        for (Cella vertex : grafo.vertexSet()) {
            grafo.addEdge(vertex, vertex);
            grafo.setEdgeWeight(vertex, vertex, Costanti.MOSSA_CARDINALE);
        }
    }

    /**
     * Metodo che permette di stampare a console un grafo (ovvero i vertici e le connessioni)
     * Inoltre scrive su un file di testo ciò che è stato scritto sulla console.
     */
    public static void stampaGrafo() {

        // Verifica se la cartella "output" esiste, altrimenti la crea
        Path outputFolderPath = Paths.get(Costanti.OUT_PATH);
        Path outputGrafi = Paths.get(Costanti.OUT_PATH + "\\grafi");

        creaCartella(outputFolderPath);
        creaCartella(outputGrafi);

        // Crea un oggetto PrintWriter per scrivere l'output sulla console
        try (PrintWriter consoleWriter = new PrintWriter(System.out);

             PrintWriter fileWriter = new PrintWriter(new FileWriter("output\\grafi\\grafo.txt"))) {

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

            JGraphXAdapter<Cella, DefaultWeightedEdge> graphAdapter = new JGraphXAdapter<>(grafo);

            // Personalizza il rendering del bordo
            graphAdapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_ENDARROW, mxConstants.NONE);
            graphAdapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_ROUNDED, true);
            graphAdapter.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_ORTHOGONAL, true);

            // Personalizza il rendering del vertice
            graphAdapter.getStylesheet().getDefaultVertexStyle().put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
            graphAdapter.getStylesheet().getDefaultVertexStyle().put(mxConstants.STYLE_ALIGN, mxConstants.ALIGN_CENTER);
            graphAdapter.getStylesheet().getDefaultVertexStyle().put(mxConstants.STYLE_VERTICAL_ALIGN, mxConstants.ALIGN_MIDDLE);


            mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
            layout.execute(graphAdapter.getDefaultParent());

            // Aggiungi i pesi degli archi come etichette
            mxIGraphModel model = graphAdapter.getModel();
            for (DefaultWeightedEdge edge : grafo.edgeSet()) {
                mxCell cell = (mxCell) graphAdapter.getEdgeToCellMap().get(edge);
                double weight = grafo.getEdgeWeight(edge);
                String label = String.valueOf(weight);
                model.setValue(cell, label);
                cell.setStyle(cell.getStyle() + ";" + mxConstants.STYLE_LABEL_POSITION + "=" + mxConstants.ALIGN_CENTER);
            }

            BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);

            // Salva l'immagine su disco
            try {
                File imgFile = new File(PATH_ORIENTED_GRAPH_IMAGE);
                ImageIO.write(image, PNG, imgFile);
            } catch (IOException e) {
                System.out.println("Errore nella scrittura dell'immagine su disco: " + e.getMessage());
            }

        } catch (IOException e) {
            // Gestisce eventuali eccezioni di IO (Input/Output) stampando il messaggio di errore sulla console
            System.out.println(e.getMessage());
        }
    }

    /**
     * Funzione che permette la creazione di una cartella se non esistente.
     * Verifica se esiste. Se non esiste la cartella la crea, altrimenti ritorna
     *
     * @param cartella -> path di una cartella da creare (post verifica di non esistenza)
     */
    private static void creaCartella(Path cartella) {

        if (!Files.exists(cartella)) {
            try {
                Files.createDirectories(cartella);
                System.out.println(Costanti.FOLDER_CREATION_SUCCESS);
            } catch (IOException e) {
                System.out.println(Costanti.FOLDER_CREATION_ERROR + e.getMessage());
            }
        }
    }
}