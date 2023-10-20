package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Calcolatore;
import it.asd.golino.paolini.utility.StatoCelle;

import java.util.ArrayList;
import java.util.Random;

public class Griglia {

    private final ArrayList<ArrayList<Cella>> griglia = new ArrayList<>();
    public static ArrayList<Agente> listaAgenti = new ArrayList<>();

    private final int altezza;
    private final int larghezza;

    private static int numeroCelleDaOccupare;
    private final int agglomerazione;
    private final int agenti;
    private Random rnd = new Random();

    public Griglia(int altezza, int larghezza, int percentuale, int agglomerazione, int agenti) {
        this.altezza = altezza;
        this.larghezza = larghezza;
        numeroCelleDaOccupare = Calcolatore.calcolaNumeroCellePercentuale(altezza, larghezza, percentuale);
        this.agglomerazione = agglomerazione;
        this.agenti = agenti;
        initializeGrid();
        generateAgents();
        generateObstacles();
    }

    public ArrayList<ArrayList<Cella>> getGriglia() {
        return griglia;
    }

    public int getAltezza() {
        return altezza;
    }

    public int getLarghezza() {
        return larghezza;
    }

    /**
     * Metodo che permette la creazione di tutte le celle (inizialmente tutte libere, quindi con stato = 0)
     */
    private void initializeGrid() {
        for (int i = 0; i < altezza; i++) {
            ArrayList<Cella> riga = new ArrayList<>();
            for (int j = 0; j < larghezza; j++) {
                riga.add(new Cella(i, j, StatoCelle.LIBERA.getValore()));
            }
            griglia.add(riga);
        }
    }

    /**
     * Metodo che permette di generare degli agenti (agente start stato 2, agente goal stato 3)
     */
    public void generateAgents() {

        int rigaStart = 0;
        int colonnaStart = 0;

        int rigaGoal = 0;
        int colonnaGoal = 0;

        for (int i = 1; i <= agenti; i++) {
            boolean foundStart = false;
            while (!foundStart) {
                rigaStart = rnd.nextInt(altezza);
                colonnaStart = rnd.nextInt(larghezza);

                foundStart = cambiaStatoCella(rigaStart, colonnaStart, StatoCelle.LIBERA.getValore(), StatoCelle.AGENTE_START.getValore());
            }

            boolean foundGoal = false;
            while (!foundGoal) {
                rigaGoal = rnd.nextInt(altezza);
                colonnaGoal = rnd.nextInt(larghezza);

                foundGoal = cambiaStatoCella(rigaGoal, colonnaGoal, StatoCelle.LIBERA.getValore(), StatoCelle.AGENTE_GOAL.getValore());
            }

            listaAgenti.add(new Agente(new Cella(rigaStart, colonnaStart, StatoCelle.AGENTE_START.getValore()), new Cella(rigaGoal, colonnaGoal, StatoCelle.AGENTE_GOAL.getValore())));
        }
    }

    /**
     * Metodo che permette di generare degli ostacoli (celle con stato 1)
     */
    private void generateObstacles() {

        while (numeroCelleDaOccupare > 0) {

            int riga = rnd.nextInt(altezza);
            int colonna = rnd.nextInt(larghezza);

            cambiaStatoCella(griglia.get(riga).get(colonna));

            int nextCell = rnd.nextInt(9);
            switch (nextCell) {
                case 1:
                    muoviNord(riga, colonna);
                    muoviOvest(riga, colonna);
                    break;
                case 2:
                    muoviNord(riga, colonna);
                    break;
                case 3:
                    muoviNord(riga, colonna);
                    muoviEast(riga, colonna);
                    break;
                case 4:
                    muoviEast(riga, colonna);
                    break;
                case 5:
                    muoviEast(riga, colonna);
                    muoviSud(riga, colonna);
                    break;
                case 6:
                    muoviSud(riga, colonna);
                    break;
                case 7:
                    muoviSud(riga, colonna);
                    muoviOvest(riga, colonna);
                    break;
                case 8:
                    muoviOvest(riga, colonna);
                    break;
            }
        }
    }

    public void muoviNord(int riga, int colonna) {
        if (riga < 1) return;
        cambiaStatoCella(griglia.get(riga - 1).get(colonna));
    }

    public void muoviEast(int riga, int colonna) {
        if (colonna >= this.larghezza - 1) return;
        cambiaStatoCella(griglia.get(riga).get(colonna + 1));
    }

    public void muoviSud(int riga, int colonna) {
        if (riga >= this.altezza - 1) return;
        cambiaStatoCella(griglia.get(riga + 1).get(colonna));
    }

    public void muoviOvest(int riga, int colonna) {
        if (colonna < 1) return;
        cambiaStatoCella(griglia.get(riga).get(colonna - 1));
    }

    /**
     * Metodo cambia lo stato della cella da libero a occupato (dopo aver verificato che sia effettivamente libera)
     *
     * @param cella -> cella sulla quale viene effettuato il controllo e che successivamente viene cambiata
     */
    private void cambiaStatoCella(Cella cella) {
        if (cella.getCellStatus() == StatoCelle.LIBERA.getValore()) {
            cella.setStatus(StatoCelle.NON_ATTRAVERSABILE.getValore());
            numeroCelleDaOccupare--;
        }
    }

    /**
     * Metodo che permette di modificare lo stato di una cella.
     * Viene effettuato una verifica sul valore dello stato e poi viene successivamente modificato
     *
     * @param riga          -> indice di riga della cella da controllare
     * @param colonna       -> indice di colonna della cella da controllare
     * @param statoIniziale -> valore rappresentante lo stato iniziale della cella
     * @param statoFinale   -> valore rappresentante lo stato finale della cella
     * @return -> true se viene effettuato il cambio di stato, false altrimenti.
     */
    private boolean cambiaStatoCella(int riga, int colonna, int statoIniziale, int statoFinale) {
        if (griglia.get(riga).get(colonna).getCellStatus() == statoIniziale) {
            griglia.get(riga).get(colonna).setStatus(statoFinale);
            return true;
        }
        return false;
    }

    public ArrayList<Agente> getListaAgenti() {
        return listaAgenti;
    }
}