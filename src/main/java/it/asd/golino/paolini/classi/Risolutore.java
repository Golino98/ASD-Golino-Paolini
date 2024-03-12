package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Costanti;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

import static it.asd.golino.paolini.classi.Grafo.grafo;

public class Risolutore {
    public static void risolviProblema() {
        try {
            // Al posto di fare il ciclo potrei passarglieli tutti e lavorare uno alla volta senza dover rifare tutte le volte le inizializzazioni delle variabili
            for (Agente a : Griglia.listaAgenti) {
                // Modificare in quanto ritorna un agente
                ReachGoal.calculateReachGoal(grafo, a, a.getCellaStart(), a.getCellaGoal(), a.getMax());
            }

            // Apri il file per la scrittura
            File file = new File(String.valueOf(Path.of(Costanti.CAMMINO_AGENTI)));
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for (Agente a : Griglia.listaAgenti) {
                // Scrivi l'output sul file invece di stamparlo sulla console
                printWriter.print(a.stampaPercorso());

                printWriter.println();
            }

            // Chiudi il PrintWriter per salvare le modifiche sul file
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
