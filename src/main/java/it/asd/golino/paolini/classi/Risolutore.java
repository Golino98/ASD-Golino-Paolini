package it.asd.golino.paolini.classi;

import static it.asd.golino.paolini.classi.Grafo.grafo;
public class Risolutore
{
    public static void risolviProblema(Griglia griglia)
    {
        // Al posto di fare il ciclo potrei passarglieli tutti e lavorare uno alla volta senza dover rifare tutte le volte le inizializzazione delle variabili
        for(Agente a : Griglia.listaAgenti)
        {
            // Modificare in quanto ritorna un agente
            Agente ag = ReachGoal.calculateReachGoal(grafo, a.getCellaStart(), a.getCellaGoal(), griglia.getMax(), griglia);
            System.out.println(a.stampaPercorso());
        }
    }
}
