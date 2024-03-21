package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Costanti;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

import static it.asd.golino.paolini.classi.Grafo.grafo;

public class Risolutore {
    public static void risolviProblema(Boolean relaxed) {
        try {
            // Apri il file per la scrittura
            File file = new File(String.valueOf(Path.of(Costanti.CAMMINO_AGENTI)));
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Al posto di fare il ciclo potrei passarglieli tutti e lavorare uno alla volta senza dover rifare tutte le volte le inizializzazioni delle variabili
            for (Agente a : Griglia.listaAgenti) {
                // Modificare in quanto ritorna un agente
                if (ReachGoal.calculateReachGoal(grafo, a, a.getCellaStart(), a.getCellaGoal(), a.getMax(), relaxed)) {
                    printWriter.print(a.stampaPercorso());
                    printWriter.println();
                } else {
                    printWriter.print(a.percorsoInesistente());
                    printWriter.println();
                }
            }

            printWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}