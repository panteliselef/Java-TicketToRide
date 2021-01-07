package view;

import model.classes.cards.Card;
import model.classes.cards.CardColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CardLabel extends JLabel {
    final private CardColor cardColor;
    private Card card = null;
    enum Rotation {
        TILT,NONE
    }

    /**
     * <h2>Constructor</h2>
     * @post Creates a CardLabel based on given card with dimensions the user has decided
     * @param card the card of the cardLabel
     * @param w width of the cardLabel
     * @param h height of the cardLabel
     */
    public CardLabel(Card card, int w,int h){
        this(card);
        this.setSize(w,h);
    }

    /**
     * <h2>Constructor</h2>
     * @post Creates a CardLabel based on given card
     * @param card the card of the cardLabel
     */
    public CardLabel(Card card){
        this.setSize(80,140);
        cardColor = CardColor.LOCOMOTIVE;
        this.card = card;
        File imageFile = new File(card.getImageFileName());
        try{
            BufferedImage image = ImageIO.read(imageFile);
            Image newImg;
            newImg = image.getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(newImg);
            this.setIcon(imageIcon);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * <h2>Constructor</h2>
     * @post Creates a CardLabel based on given card with dimensions the user has decided
     * @param cardColor the cardColor of the cardLabel
     * @param rot the selected Rotation
     */
    CardLabel(CardColor cardColor,Rotation rot){
        this.cardColor = cardColor;
        this.setSize(65,45);
        File imageFile = new File("./resources/images/trainCards/"+cardColor.toString().toLowerCase()+".jpg");
        try{
            BufferedImage image = ImageIO.read(imageFile);
            Image newimg;
            if(rot == Rotation.TILT){
                BufferedImage rotated = rotateImageByDegrees(image, 270);
                newimg = rotated.getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);
            }else {
                newimg = image.getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);
            }
            ImageIcon imageIcon = new ImageIcon(newimg);
            this.setIcon(imageIcon);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * <b>Accessor Method</b>
     * @post the color of the cardLabel has been returned
     * @return the color of the cardLabel
     */
    public CardColor getCardColor() {
        return cardColor;
    }

    /**
     * <b>Transformer Method</b>
     * @post the new rotated image has been returned
     * @param img selected image
     * @param degrees degrees
     * @return rotated image
     */
    private BufferedImage rotateImageByDegrees(BufferedImage img, double degrees) {
        double rads = Math.toRadians(degrees);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / (float) 2, (newHeight - h) / (float) 2);

        int x =  w / 2;
        int y = h / 2 ;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, this);
        g2d.dispose();

        return rotated;
    }

    /**
     * <b>Accessor Method</b>
     * @post the card of the cardLabel has been returned
     * @return the card of the cardLabel
     */
    public Card getCard() {
        return card;
    }

    /**
     * <b>Transformer Method</b>
     * @post the icon of the cardLabel has become gray scaled
     */
    public void makeItGray(){
        File imageFile = new File(card.getImageFileName());
        try {
            BufferedImage image = ImageIO.read(imageFile);
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int p = image.getRGB(x, y);

                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;

                    //calculate average
                    int avg = (r + g + b) / 3;

                    //replace RGB value with avg
                    p = (a << 24) | (avg << 16) | (avg << 8) | avg;

                    image.setRGB(x, y, p);
                }
            }
            setIcon(image);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * <b>Transformer Method</b>
     * @post the selected image has been set as the icon of the cardLabel
     * @param image selected Image
     */
    private void setIcon(BufferedImage image) {
        Image newImg = image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(newImg);
        this.setIcon(imageIcon);
        this.repaint();
    }

    /**
     * <b>Transformer Method</b>
     * @post the icon of the cardLabel has become colorful
     */
    public void makeItColored(){
        File imageFile = new File(card.getImageFileName());
        try{
            BufferedImage image = ImageIO.read(imageFile);
            setIcon(image);
        }catch (IOException e){
            System.out.println(e);
        }
    }
}


