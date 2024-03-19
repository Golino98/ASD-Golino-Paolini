package it.asd.golino.paolini.utility;

public enum StatoCelle {
    LIBERA(0),
    NON_ATTRAVERSABILE(1),
    AGENTE_START(2),
    AGENTE_GOAL(3),
    PERCORSO(4);

    private final int valore;

    StatoCelle(int valore) {
        this.valore = valore;
    }

    public int getValore() {
        return valore;
    }

    public static StatoCelle getStatoByValore(int valoreControllo) {
        return switch (valoreControllo) {
            case 0 -> LIBERA;
            case 1 -> NON_ATTRAVERSABILE;
            case 2 -> AGENTE_START;
            case 3 -> AGENTE_GOAL;
            default -> null;
        };
    }
}
