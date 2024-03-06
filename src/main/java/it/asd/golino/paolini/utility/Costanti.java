package it.asd.golino.paolini.utility;

import java.awt.*;

public class Costanti {
    public static final int MOSSA_CARDINALE = 1;
    public static final double DIAGONALE = 1.414;

    public static final Font TEXT_FONT = new Font(Font.DIALOG, Font.PLAIN, 18);
    public static final Font BUTTON_FONT = new Font(Font.DIALOG, Font.PLAIN, 15);

    public static final String OUT_PATH = "output";
    public static final String FOLDER_CREATION_SUCCESS = "Cartella di creata con successo.";
    public static final String FOLDER_CREATION_ERROR = "Errore nella creazione della cartella 'output': ";

    public static final String ERRORE = "Errore";
    public static final String ERRORE_CONVERSIONE_NUMERO = "Errore: inserisci valori numerici validi.";
    public static final String ERRORE_NUMERO_NEGATIVO = "Errore: i valori devono essere tutti positivi.";
    public static final String ERRORE_GRIGLIA_NON_COMPLETA = "Non è stato possibile creare una griglia con i parametri richiesti. Viene visualizzata la griglia che più si avvicina a quanto inserito.";
    public static final String ERRORE_NUMERO_CELLE = "Il numero delle celle non attraversabili è in conflitto con il numero degli agenti.";
    public static final String ERRORE_PERCENTUALE_MASSIMA= "La percentuale non può essere superiore al 100%";

    public static final String PATH_CONNESSIONE_CELLE_LIBERE = "output\\grafi\\grafo.txt";
    public static final String CAMMINO_AGENTI = "output\\grafi\\cammino_Agenti.txt";

    public static final String NOME_CELLA = "(%d , %d)";

    public static final String STAMPA_CELLA_GRAFO = "Vertice %s -> : { ";
    public static final String STAMPA_PESO_GRAFO =  "[%s - %.3f];  ";

    public static final String PNG = "PNG";
    public static final String PATH_ORIENTED_GRAPH_IMAGE = "output\\grafi\\grafo_dei_pesi.png";

    public static final String PATH_MST_NO_AGENTS_IMAGE = "output\\grafi\\minSpanningTreeNoAgents.png";
    public static final String PATH_MST_NO_AGENTS_TXT = "output\\grafi\\mstNoAgents.txt";

    public static final String PATH_MST_AGENTS_IMAGE = "output\\grafi\\minSpanningTreeAgents_%d.png";
    public static final String PATH_MST_AGENTS_TXT = "output\\grafi\\mstAgents_%d.txt";

}
