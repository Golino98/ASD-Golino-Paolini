package it.asd.golino.paolini.utility;

public enum StatoCelle {
    LIBERA(0),
    NON_ATTRAVERSABILE(1),
    AGENTE_START(2),
    AGENTE_GOAL(3);

    private final int valore;

    StatoCelle(int valore) {
        this.valore = valore;
    }

    public int getValore() {
        return valore;
    }
}
