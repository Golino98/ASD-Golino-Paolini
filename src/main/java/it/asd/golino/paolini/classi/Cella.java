package it.asd.golino.paolini.classi;

import static it.asd.golino.paolini.utility.Costanti.NOME_CELLA;

public class Cella {
    private int x, y;
    private int status;

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
    public Cella(int x, int y, int status) {
        this.x = x;
        this.y = y;
        this.status = status;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCellStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format(NOME_CELLA,x,y);
    }
}
