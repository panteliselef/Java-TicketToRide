package view;

import javax.swing.*;
import java.awt.*;

public class ImagePane extends JLayeredPane {
    Image img;
    public ImagePane(Image img) {this.img = img;}

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.img,0,0,this);
    }
}
