package it.asd.golino.paolini.utility;

import java.awt.*;

public class Costanti {
    public static final int MOSSA_CARDINALE = 1;
    public static final double DIAGONALE = Math.sqrt(2);

    public static final Font TEXT_FONT = new Font(Font.DIALOG, Font.PLAIN, 18);
    public static final Font BUTTON_FONT = new Font(Font.DIALOG, Font.PLAIN, 15);

    public static final String ERRORE = "Errore";
    public static final String ERRORE_CONVERSIONE_NUMERO = "Errore: inserisci valori numerici validi.";
    public static final String ERRORE_NUMERO_NEGATIVO = "Errore: i valori devono essere tutti positivi.";

    public static final String NOME_CELLA = "(%d , %d)";

    public static final String STAMPA_CELLA_GRAFO = "Vertice %s è collegato a: ";
    public static final String STAMPA_PESO_GRAFO =  "%s con peso %.3f, ";
}
