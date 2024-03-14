package it.asd.golino.paolini.classi;

public class Stato {

    private Cella cella;
    private double costo;

    public Stato(Cella cella, double costo) {
        this.cella = cella;
        this.costo = costo;
    }

    public Cella getCella() {
        return cella;
    }

    public void setCella(Cella cella) {
        this.cella = cella;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }
}
