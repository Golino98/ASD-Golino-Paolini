package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.Calcolatore;
import it.asd.golino.paolini.utility.StatoCelle;

import java.util.ArrayList;
import java.util.Random;

public class Griglia {

    public static ArrayList<Agente> listaAgenti = new ArrayList<>();

    private final int altezza;
    private final int larghezza;
    private final Cella[][] griglia;

    private static int numeroCelleDaOccupare;
    private final int agglomerazione;

    private final int agenti;
    private final Random rnd = new Random();

    public Griglia(int altezza, int larghezza, int percentuale, int agglomerazione, int agenti) {
        this.altezza = altezza;
        this.larghezza = larghezza;

        this.griglia = new Cella[altezza][larghezza];
        for (int i = 0; i < altezza; i++) {
            for (int j = 0; j < larghezza; j++) {
                griglia[i][j] = new Cella(i, j, StatoCelle.LIBERA.getValore());
            }
        }

        numeroCelleDaOccupare = Calcolatore.calcolaNumeroCellePercentuale(altezza, larghezza, percentuale);
        this.agglomerazione = agglomerazione;
        this.agenti = agenti;

        generateAgents();
        generateObstacles();
    }

    public Cella[][] getGriglia() {
        return griglia;
    }

    public int getAltezza() {
        return altezza;
    }

    public int getLarghezza() {
        return larghezza;
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

            int riga;
            int colonna;
            ArrayList<Cella> disponibili = new ArrayList<>();
            ArrayList<Cella> daColorare = new ArrayList<>();
            //Prima di selezionare questa casuale, dovrei verificare se sia libera o meno e che non sia vicina cardinalmente ad altre celle
            do {
                riga = rnd.nextInt(altezza);
                colonna = rnd.nextInt(larghezza);
            } while (griglia[riga][colonna].getCellStatus() != StatoCelle.LIBERA.getValore() && !senzaVicini(griglia[riga][colonna]));

            daColorare.add(griglia[riga][colonna]); //Cambio lo stato della cella

            int counter = rnd.nextInt(agglomerazione);
            //probabilità che abbia un'agglomerazione da 1 a agglomerazione.
            boolean ok = true;

            while (counter > 0 && ok) {
                if (riga + 1 < altezza) {
                    if (senzaVicini(griglia[riga + 1][colonna]))
                        disponibili.add(griglia[riga + 1][colonna]);
                }
                if (colonna + 1 < larghezza) {
                    if (senzaVicini(griglia[riga][colonna + 1]))
                        disponibili.add(griglia[riga][colonna + 1]);
                }
                if (colonna - 1 > 0) {
                    if (senzaVicini(griglia[riga][colonna - 1]))
                        disponibili.add(griglia[riga][colonna - 1]);
                }
                if (riga - 1 > 0) {
                    if (senzaVicini(griglia[riga - 1][colonna]))
                        disponibili.add(griglia[riga - 1][colonna]);
                }

                if (disponibili.size() == 0) {
                    ok = false;
                } else {
                    Cella cella = sceltaRandom(disponibili);
                    riga = cella.getRiga();
                    colonna = cella.getColonna();
                    daColorare.add(griglia[riga][colonna]);
                    counter--;
                }
            }

            for (Cella cella : daColorare) {
                cambiaStatoCella(cella);
                numeroCelleDaOccupare--;
            }

        }
    }

    public boolean senzaVicini(Cella x) {
        if (x.getRiga() + 1 >= altezza || x.getRiga() - 1 < 0 || x.getColonna() + 1 >= larghezza || x.getColonna() - 1 < 0)
            return false;
        else if (griglia[x.getRiga() + 1][x.getColonna()].getCellStatus() != StatoCelle.LIBERA.getValore())
            return false;
        else if (griglia[x.getRiga()][x.getColonna() + 1].getCellStatus() != StatoCelle.LIBERA.getValore())
            return false;
        else if (griglia[x.getRiga() - 1][x.getColonna()].getCellStatus() != StatoCelle.LIBERA.getValore())
            return false;
        else if (griglia[x.getRiga()][x.getColonna() - 1].getCellStatus() != StatoCelle.LIBERA.getValore())
            return false;
        else return true;
    }

    public Cella sceltaRandom(ArrayList<Cella> lista) {
        int random = rnd.nextInt(lista.size());
        return lista.remove(random);
    }

    /**
     * Metodo cambia lo stato della cella da libero a occupato (dopo aver verificato che sia effettivamente libera)
     *
     * @param cella -> cella sulla quale viene effettuato il controllo e che successivamente viene cambiata
     */
    private void cambiaStatoCella(Cella cella) {
        if (cella.getCellStatus() == StatoCelle.LIBERA.getValore()) {
            cella.setStatus(StatoCelle.NON_ATTRAVERSABILE.getValore());
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
        if (griglia[riga][colonna].getCellStatus() == statoIniziale) {
            griglia[riga][colonna].setStatus(statoFinale);
            return true;
        }
        return false;
    }

    public ArrayList<Agente> getListaAgenti() {
        return listaAgenti;
    }
}
