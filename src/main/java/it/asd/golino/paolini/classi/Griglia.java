package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.Main;
import it.asd.golino.paolini.gui.GuiGeneratoreGriglie;
import it.asd.golino.paolini.gui.StyleSystemGui;
import it.asd.golino.paolini.utility.Calcolatore;
import it.asd.golino.paolini.utility.StatoCelle;

import java.util.ArrayList;
import java.util.Random;

import static it.asd.golino.paolini.utility.Costanti.*;


public class Griglia {

    public static ArrayList<Agente> listaAgenti = new ArrayList<>();

    private final int altezza;
    private final int larghezza;
    private final Cella[][] griglia;

    private static int numeroCelleDaOccupare;
    private final int agglomerazione;

    private final int agenti;
    private final Random rnd = new Random();

    private final int max;

    public Griglia(int altezza, int larghezza, int percentuale, int agglomerazione, int agenti, int max) {
        this.altezza = altezza;
        this.larghezza = larghezza;
        this.max = max;

        this.griglia = new Cella[altezza][larghezza];
        for (int i = 0; i < altezza; i++) {
            for (int j = 0; j < larghezza; j++) {
                griglia[i][j] = new Cella(i, j, StatoCelle.LIBERA);
            }
        }

        numeroCelleDaOccupare = Calcolatore.calcolaNumeroCellePercentuale(altezza, larghezza, percentuale);
        this.agglomerazione = agglomerazione;
        this.agenti = agenti;

        generateObstacles();
        generateAgents(max);
    }

    public Cella[][] getGriglia() {
        return griglia;
    }

    public int getMax() {
        return max;
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
    public void generateAgents(int max) {

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

            listaAgenti.add(new Agente(new Cella(rigaStart, colonnaStart, StatoCelle.AGENTE_START), new Cella(rigaGoal, colonnaGoal, StatoCelle.AGENTE_GOAL), max));
        }
    }

    /**
     * Metodo che permette di generare degli ostacoli (celle con stato 1)
     */
    private void generateObstacles() {

        int numeroMax = 200;
        while (numeroCelleDaOccupare > 0) {

            int riga;
            int colonna;

            // Lista nella quale vengono inserite le celle adiacenti che sono dei validi vicini per l'agglomerato
            ArrayList<Cella> disponibili = new ArrayList<>();

            // Lista nella quale vengono inserite le celle che devono essere colorate (e quindi rese non attraversabili)
            ArrayList<Cella> daColorare = new ArrayList<>();

            // Prima di selezionare questa casuale, dovrei verificare se sia libera o meno e che non sia vicina cardinalmente ad altre celle

            do {
                riga = rnd.nextInt(altezza);
                colonna = rnd.nextInt(larghezza);
                numeroMax--;
            } while (griglia[riga][colonna].getCellStatus() != StatoCelle.LIBERA || !senzaVicini(griglia[riga][colonna]) && numeroMax > 0);

            if (numeroMax <= 0) {
                break;
            }

            // Aggiungo la cella alla lista delle celle da colorare
            daColorare.add(griglia[riga][colonna]);

            // Valore intero nell'intervallo [0,agglomerazione[. Indica quante celle aggiungere dalla cella selezionata per creare l'agglomerato
            int counter = rnd.nextInt(agglomerazione);

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
                if (disponibili.isEmpty()) {
                    ok = false;
                } else {
                    Cella cella = sceltaRandom(disponibili);
                    riga = cella.getRiga();
                    colonna = cella.getColonna();
                    daColorare.add(griglia[riga][colonna]);
                    counter--;
                }
            }

            // Ciclo che permette di colorare le celle
            for (Cella cella : daColorare) {
                cambiaStatoCella(cella);
                numeroCelleDaOccupare--;
            }

        }
        if (numeroMax <= 0) {
            GuiGeneratoreGriglie errore = new GuiGeneratoreGriglie();
            StyleSystemGui.setCenterOfTheScreen(errore);
            errore.showErrorDialog(ERRORE_GRIGLIA_NON_COMPLETA);
            errore.dispose();
        }
    }

    /**
     * Funzione che permette di verificare se una cella ha la possibilità di avere vicini
     *
     * @param x cella sulla quale effettuare il controllo
     * @return true se ha tutti i vicini disponibili, falso altrimenti
     */
    public boolean senzaVicini(Cella x) {

        // Controllo la riga sotto
        if (!(x.getRiga() + 1 >= altezza)) {
            if (griglia[x.getRiga() + 1][x.getColonna()].getCellStatus() != StatoCelle.LIBERA)
                return false;
        }

        // Controllo la riga a destra
        if (!(x.getColonna() + 1 >= larghezza)) {
            if (griglia[x.getRiga()][x.getColonna() + 1].getCellStatus() != StatoCelle.LIBERA)
                return false;
        }

        // Controllo la riga sopra
        if (!(x.getRiga() - 1 < 0)) {
            if (griglia[x.getRiga() - 1][x.getColonna()].getCellStatus() != StatoCelle.LIBERA)
                return false;
        }

        // Controllo la colonna a sinistra
        if (!(x.getColonna() - 1 < 0)) {
            if (griglia[x.getRiga()][x.getColonna() - 1].getCellStatus() != StatoCelle.LIBERA) return false;
        }
        return true;
    }

    /**
     * Metodo che presa una cella, verifica se i vicini sono delle possibili celle per gli agglomerati
     *
     * @param x           cella sulla quale effettuare il controllo
     * @param disponibili lista delle celle scelte per gli agglomerati
     */
    public void senzaVicini(Cella x, ArrayList<Cella> disponibili) {
        // Controllo se non sono su un bordo
        if (x.getRiga() + 1 >= altezza || x.getRiga() - 1 < 0 || x.getColonna() + 1 >= larghezza || x.getColonna() - 1 < 0)
            return;

            // Controllo la riga sotto
        else if (griglia[x.getRiga() + 1][x.getColonna()].getCellStatus() != StatoCelle.LIBERA)
            return;

            // Controllo la riga a destra
        else if (griglia[x.getRiga()][x.getColonna() + 1].getCellStatus() != StatoCelle.LIBERA)
            return;

            // Controllo la riga sopra
        else if (griglia[x.getRiga() - 1][x.getColonna()].getCellStatus() != StatoCelle.LIBERA)
            return;

            // Controllo la colonna a sinistra
        else disponibili.add(x);
    }

    /**
     * @param lista arrayList che permette di selezionare un elemento casuale da essa
     * @return l'elemento casuale della lista (e lo rimuove da essa)
     */
    public Cella sceltaRandom(ArrayList<Cella> lista) {
        int random = rnd.nextInt(lista.size());
        return lista.remove(random);
    }

    /**
     * Metodo cambia lo stato della cella da libero a occupato (dopo aver verificato che sia effettivamente libera)
     *
     * @param cella cella sulla quale viene effettuato il controllo e che successivamente viene cambiata
     */
    public void cambiaStatoCella(Cella cella) {
        if (cella.getCellStatus() == StatoCelle.LIBERA) {
            cella.setStatus(StatoCelle.NON_ATTRAVERSABILE);
        }
    }

    /**
     * Metodo che permette di modificare lo stato di una cella.
     * Viene effettuato una verifica sul valore dello stato e poi viene successivamente modificato
     *
     * @param riga          indice di riga della cella da controllare
     * @param colonna       indice di colonna della cella da controllare
     * @param statoIniziale valore rappresentante lo stato iniziale della cella
     * @param statoFinale   valore rappresentante lo stato finale della cella
     * @return -> true se viene effettuato il cambio di stato, false altrimenti.
     */
    public boolean cambiaStatoCella(int riga, int colonna, int statoIniziale, int statoFinale) {
        if (griglia[riga][colonna].getCellStatus().getValore() == statoIniziale) {

            griglia[riga][colonna].setStatus(StatoCelle.getStatoByValore(statoFinale));
            return true;
        }
        return false;
    }

    public ArrayList<Agente> getListaAgenti() {
        return listaAgenti;
    }
}
