package it.asd.golino.paolini.classi;

import java.util.ArrayList;

import static it.asd.golino.paolini.classi.Grafo.grafo;

public class Risolutore
{
    public static void risolviProblema(Griglia griglia)
    {
        for(Agente a : Griglia.listaAgenti)
        {
            // Modificare in quanto ritorna un agente
            Agente ag = ReachGoal.calculateReachGoal(grafo, a.getCellaStart(), a.getCellaGoal(), griglia.getMax(), griglia);
            System.out.println(a.stampaPercorso());
        }
    }
}
