package view;

import model.classes.cards.CardColor;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class OnTheTrackPanel extends JLayeredPane {
    private JLabel nameTag;
    private HashMap<CardColor,JLabel> cards ;
    private final int stx = 10;
    private final int sty = 5;
    private final int offsetX = 75;


    OnTheTrackPanel(String nameTag){
        this.initComponents();
        this.nameTag.setText(nameTag);
    }

    private void initComponents(){
        nameTag = new JLabel();
        cards = new HashMap<>();

        this.setLayout(null);
        this.add(nameTag);
        this.setPreferredSize(new Dimension(320,180));


        Arrays.stream(CardColor.values()).forEach(color -> {
            System.out.println(color.toString().toLowerCase());
            JLabel tmp = new JLabel(color.toString().toLowerCase()+": 0");
            if(cards.size() <= 3){
                tmp.setBounds(cards.size() * offsetX + stx,sty,70,20);
            }else if(cards.size() <= 7){
                tmp.setBounds((cards.size()-4) * offsetX + stx,sty+60,70,20);
            }else {
                tmp.setBounds(2 * offsetX - stx,sty+135,120,20);
            }


            CardLabel cardLabel = new CardLabel(color, CardLabel.Rotation.TILT);
            if(cards.size() <=3){
                cardLabel.setBounds(cards.size() * offsetX + stx,sty+17,cardLabel.getWidth(),cardLabel.getHeight());
            }else if(cards.size() <= 7){
                cardLabel.setBounds((cards.size()-4)* offsetX + stx,sty+17+60,cardLabel.getWidth(),cardLabel.getHeight());
            }else {
                cardLabel.setBounds(3*offsetX + stx,sty+17+107,cardLabel.getWidth(),cardLabel.getHeight());
            }

            this.add(cardLabel);

            cards.put(color,tmp);
            this.add(tmp);
        });

        this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        this.setOpaque(true);
    }

    private void updateLabel(CardColor cardColor,int amount){
        cards.get(cardColor).setText(cardColor.toString().toLowerCase()+": "+amount);
        this.repaint();
    }
    public void updateLabels(HashMap<CardColor, Integer> collection){
        collection.forEach(this::updateLabel);
    }

    public void removeAllCardLabels(){
        Arrays.stream(CardColor.values()).forEach(cardColor -> {
            cards.get(cardColor).setText(cardColor.toString().toLowerCase()+": 0");
            this.repaint();
        });
        this.repaint();
    }
}
