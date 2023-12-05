package it.asd.golino.paolini.classi;

import it.asd.golino.paolini.utility.StatoCelle;

import static it.asd.golino.paolini.utility.Costanti.NOME_CELLA;

public class Cella {
    private int riga, colonna;
    private StatoCelle status;

    /**
     * Metodo costruttore
     *
     * @param x      -> ascisse
     * @param y      -> ordinate
     * @param status -> stato della cella:
     *               0 -> attraversabile
     *               1 -> non attraversabile
     *               2 -> start per agent
     *               3 -> end per agent
     */
    public Cella(int x, int y, StatoCelle status) {
        this.riga = x;
        this.colonna = y;
        this.status = status;
    }

    public int getRiga() {
        return riga;
    }

    public int getColonna() {
        return colonna;
    }

    public void setStatus(StatoCelle status) {
        this.status = status;
    }

    public StatoCelle getCellStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format(NOME_CELLA, riga, colonna);
    }
}
