package it.asd.golino.paolini.classi;

public class VerticeTempo {
    // Cella di riferimento
    private Cella v;

    // Istante temporale
    private int t;

    // Valore della funzione score f, calcolata come la somme di g(v,t) + h(v,goal)
    private double f;

    // Valore della funzione g, il costo più basso sinora calcolato per raggiungere il vertice v dall’istante t partendo da istante 0
    private double g;

    // Padre da cui si è arrivati (nell'albero dei cammini minimi, cioè di costo minimo)
    private VerticeTempo p;

    /**
     * Metodo costruttore per la classe {@link VerticeTempo}
     * @param v cella di riferimento
     * @param t istante temporale corrente
     */
    public VerticeTempo(Cella v, int t) {
        this.v = v;
        this.t = t;
    }

    public Cella getV() {
        return v;
    }

    public int getT() {
        return t;
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
