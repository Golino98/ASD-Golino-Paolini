package it.asd.golino.paolini.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GestoreCartelle {

    /**
     * Funzione che permette la creazione di una cartella se non esistente.
     * Verifica se esiste. Se non esiste la cartella la crea, altrimenti ritorna
     *
     * @param cartella -> path di una cartella da creare (post verifica di non esistenza)
     */
    public static void creaCartella(Path cartella) {

        if (!Files.exists(cartella)) {
            try {
                Files.createDirectories(cartella);
                System.out.println(Costanti.FOLDER_CREATION_SUCCESS);
            } catch (IOException e) {
                System.out.println(Costanti.FOLDER_CREATION_ERROR + e.getMessage());
            }
        }
    }
}
