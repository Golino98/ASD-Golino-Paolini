package it.asd.golino.paolini.utility;

public class Calcolatore {
    public static int calcolaNumeroCellePercentuale(int altezza, int larghezza, int percentuale) {
        return Math.round((float) ((altezza * larghezza * percentuale) / 100));
    }
}
