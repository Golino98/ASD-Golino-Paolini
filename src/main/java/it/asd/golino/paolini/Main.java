package it.asd.golino.paolini;

import it.asd.golino.paolini.gui.GuiGeneratoreGriglie;
import it.asd.golino.paolini.gui.StyleSystemGui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        SwingUtilities.invokeLater(() -> {
            GuiGeneratoreGriglie gui = new GuiGeneratoreGriglie();
            StyleSystemGui.setSetupWindow(gui);
        });
    }
}
