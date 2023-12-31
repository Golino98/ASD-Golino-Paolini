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
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import static it.asd.golino.paolini.utility.Costanti.*;
import static it.asd.golino.paolini.utility.GestoreCartelle.creaCartella;

public class Grafo {

    // Creazione di un grafo direzionato pesato utilizzando JGraphT
    private static final Graph<Cella, DefaultWeightedEdge> grafo = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);

    /**
     * Metodo che permette la creazione di un grafo. Aggiunge i vertici alla variabile grafo.
     */
    public static void creaGrafo() {
        Vertice.getVertici().forEach(grafo::addVertex);
        for (var s : grafo.vertexSet()) {
            creaConnessioni(s, grafo);
        }
        stampaGrafo(grafo, PATH_CONNESSIONE_CELLE_LIBERE, PATH_ORIENTED_GRAPH_IMAGE);

        // Creo l'albero dei cammini minimi delle cella senza considerare gli agenti
        creaAlberoCamminiMinimi(grafo, PATH_MST_NO_AGENTS_TXT, PATH_MST_NO_AGENTS_IMAGE);

        var grafoConAgenti = grafo;

        // Ordina la lista di agenti in base all'indice
        Griglia.listaAgenti.sort(Comparator.comparingInt(Agente::getIndice));

        for (Agente entryAgent : Griglia.listaAgenti) {
            grafoConAgenti.addVertex(entryAgent.getCellaStart());
            grafoConAgenti.addVertex(entryAgent.getCellaGoal());
            creaConnessioni(entryAgent.getCellaStart(), grafoConAgenti);
            creaConnessioni(entryAgent.getCellaGoal(), grafoConAgenti);
            creaAlberoCamminiMinimi(grafoConAgenti, String.format(PATH_MST_AGENTS_TXT, entryAgent.getIndice()), String.format(PATH_MST_AGENTS_IMAGE, entryAgent.getIndice()));

        }
    }

    /**
     * Metodo che permette di creare tutte le connessioni con celle libere adiacenti dato un vertice.
     * Sono gestiti anche i costi delle connessioni
     *
     * @param vertice vertice sul quale creare le connessioni
     */
    public static void creaConnessioni(Cella vertice, Graph<Cella, DefaultWeightedEdge> graph) {

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
        for (Cella vertex : graph.vertexSet()) {
            graph.addEdge(vertex, vertex);
            graph.setEdgeWeight(vertex, vertex, Costanti.MOSSA_CARDINALE);
        }
    }

    /**
     * Creazione dell'albero dei cammini minimi tramite Kruskal (solo delle celle attraversabili)
     */
    public static void creaAlberoCamminiMinimi(Graph<Cella, DefaultWeightedEdge> graph, String path_txt, String path_image) {

        KruskalMinimumSpanningTree<Cella, DefaultWeightedEdge> kmst = new KruskalMinimumSpanningTree<>(graph);
        var spanningTree = kmst.getSpanningTree();

        // Creare un nuovo grafo per rappresentare l'MST
        Graph<Cella, DefaultWeightedEdge> mst = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);

        // Aggiungere vertici e archi all'MST
        for (DefaultWeightedEdge edge : spanningTree.getEdges()) {
            Cella source = graph.getEdgeSource(edge);
            Cella target = graph.getEdgeTarget(edge);
            double weight = graph.getEdgeWeight(edge);

            mst.addVertex(source);
            mst.addVertex(target);
            DefaultWeightedEdge mstEdge = mst.addEdge(source, target);
            mst.setEdgeWeight(mstEdge, weight);
        }
        // Salva l'immagine dell'MST su disco
        stampaGrafo(mst, path_txt, path_image);
    }

    public static void creaPercorsoMinimo(Graph<Cella, DefaultWeightedEdge> graph, String path_txt, String path_image) {

    }

    /**
     * Metodo che permette di stampare a console un grafo (ovvero i vertici e le connessioni)
     * Inoltre scrive su un file di testo ciò che è stato scritto sulla console.
     */
    public static void stampaGrafo(Graph<Cella, DefaultWeightedEdge> grafo, String path, String nomeImmagine) {

        // Verifica se la cartella "output" esiste, altrimenti la crea
        Path outputFolderPath = Paths.get(Costanti.OUT_PATH);
        Path outputGrafi = Paths.get(Costanti.OUT_PATH + "\\grafi");

        creaCartella(outputFolderPath);
        creaCartella(outputGrafi);

        // Crea un oggetto PrintWriter per scrivere l'output sulla console
        try (PrintWriter consoleWriter = new PrintWriter(System.out);
             PrintWriter fileWriter = new PrintWriter(new FileWriter(path))) {

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
                File imgFile = new File(nomeImmagine);
                ImageIO.write(image, PNG, imgFile);
            } catch (IOException e) {
                System.out.println("Errore nella scrittura dell'immagine su disco: " + e.getMessage());
            }

        } catch (IOException e) {
            // Gestisce eventuali eccezioni di IO (Input/Output) stampando il messaggio di errore sulla console
            System.out.println(e.getMessage());
        }
    }
}