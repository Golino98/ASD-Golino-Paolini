package it.asd.golino.paolini.classi;

public class VerticeTempo {
    private Cella v;
    private int t;
    private double f;
    private double g;
    private VerticeTempo p;

    public VerticeTempo(Cella v, int t) {
        this.v = v;
        this.t = t;
    }

    public Cella getV() {
        return v;
    }

    public void setV(Cella v) {
        this.v = v;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
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

    public VerticeTempo getP() {
        return p;
    }

    public void setP(VerticeTempo p) {
        this.p = p;
    }
}
