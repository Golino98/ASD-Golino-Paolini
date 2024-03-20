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
            // Apri il file per la scrittura
            File file = new File(String.valueOf(Path.of(Costanti.CAMMINO_AGENTI)));
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);


            File file2 = new File(String.valueOf(Path.of(Costanti.CAMMINO_AGENTI_RELAXED)));
            FileWriter fileWriter2 = new FileWriter(file2);
            PrintWriter printWriter2 = new PrintWriter(fileWriter2);

            // Al posto di fare il ciclo potrei passarglieli tutti e lavorare uno alla volta senza dover rifare tutte le volte le inizializzazioni delle variabili
            for (Agente a : Griglia.listaAgenti) {
                // Modificare in quanto ritorna un agente
                if (ReachGoal.calculateReachGoal(grafo, a, a.getCellaStart(), a.getCellaGoal(), a.getMax(), false)) {
                    printWriter.print(a.stampaPercorso());
                    printWriter.println();
                } else {
                    printWriter.print(a.percorsoInesistente());
                    printWriter.println();
                }
            }

            for (Agente ag : Griglia.listaAgenti) {
                ag.resetAgente();
            }
            for (Agente a : Griglia.listaAgenti) {
                // Modificare in quanto ritorna un agente
                if (ReachGoal.calculateReachGoal(grafo, a, a.getCellaStart(), a.getCellaGoal(), a.getMax(), true)) {
                    printWriter2.print(a.stampaPercorso());
                    printWriter2.println();
                } else {
                    printWriter2.print(a.percorsoInesistente());
                    printWriter2.println();
                }
            }

            printWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}