package view;

import javax.swing.*;

public class ErrorDialog extends JDialog {
    public ErrorDialog(String warningMsg){
        JOptionPane optionPane = new JOptionPane(warningMsg, JOptionPane.ERROR_MESSAGE);
        JDialog tmpDialog = optionPane.createDialog("Invalid Move");
        tmpDialog.setAlwaysOnTop(true);
        tmpDialog.setVisible(true);
    }
}
