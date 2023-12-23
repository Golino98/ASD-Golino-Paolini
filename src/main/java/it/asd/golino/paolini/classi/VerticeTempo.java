package it.asd.golino.paolini.classi;

public class VerticeTempo {
    private Cella v;
    private int t;
    private double f;
    private double g;

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    private double h;
    private VerticeTempo p;

    public VerticeTempo getP() {
        return p;
    }

    public void setP(VerticeTempo p) {
        this.p = p;
    }

    public VerticeTempo(Cella v, int t) {
        this.v = v;
        this.t = t;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }
}
