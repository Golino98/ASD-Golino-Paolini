package it.asd.golino.paolini.gui;

import javax.swing.*;
import java.awt.*;

import static it.asd.golino.paolini.utility.Costanti.*;

public class StyleSystemGui
{
    /**
     * Metodo che permette di impostare l'aspetto della GUI principale e li imposta secondo l'aspetto richiesto
     * @param dialog variabile che estende JFrame e che contiene le variabili della GUI
     */
    public static void setSetupWindow(GuiGeneratoreGriglie dialog)
    {
        // Impostazioni di stile per i componenti dell'interfaccia utente...
        // Imposta font, allineamento, dimensioni, ecc.


        dialog.altezzaGrigliaLabel.setFont(TEXT_FONT);
        dialog.larghezzaGrigliaLabel.setFont(TEXT_FONT);
        dialog.percentualeCelleTraversabiliLabel.setFont(TEXT_FONT);
        dialog.agglomerazioneLabel.setFont(TEXT_FONT);
        dialog.numeroAgentiLabel.setFont(TEXT_FONT);

        dialog.altezzaField.setFont(BUTTON_FONT);
        dialog.larghezzaField.setFont(BUTTON_FONT);
        dialog.percentualeCelleTraversabiliField.setFont(BUTTON_FONT);
        dialog.fattoreAgglomerazioneField.setFont(BUTTON_FONT);
        dialog.numeroAgentiField.setFont(BUTTON_FONT);

        dialog.altezzaField.setHorizontalAlignment(SwingConstants.RIGHT);
        dialog.larghezzaField.setHorizontalAlignment(SwingConstants.RIGHT);
        dialog.percentualeCelleTraversabiliField.setHorizontalAlignment(SwingConstants.RIGHT);
        dialog.fattoreAgglomerazioneField.setHorizontalAlignment(SwingConstants.RIGHT);
        dialog.numeroAgentiField.setHorizontalAlignment(SwingConstants.RIGHT);

        dialog.buttonGenera.setFont(BUTTON_FONT);
        dialog.buttonReset.setFont(BUTTON_FONT);

        // Imposta le dimensioni della finestra
        dialog.setSize(590, 380);

        // Imposta la posizione della finestra
        setCenterOfTheScreen(dialog);

        dialog.setVisible(true); // Rende la finestra visibile.
    }

    public static void setCenterOfTheScreen(JFrame frame)
    {
        // Ottieni le dimensioni dello schermo
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Calcola la posizione x e y per posizionare la finestra al centro dello schermo
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;

        frame.setLocation(x,y);
    }
}
