package view;

import model.classes.cards.Card;
import model.classes.cards.CardColor;
import model.classes.cards.TrainCard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RailYardPanel extends JLayeredPane {

    private JLabel nameTag;
    private HashMap<CardColor,ArrayList<CardButton>> trainCardButtonsOnRailYard ;
    private ArrayList<ColorTextLabel> colors;
    private JButton button = new JButton();

    private int stx = 10;
    private int sty = 5;

    RailYardPanel(String nameTag){
        int offsetX = 60;

        this.initComponents();
        this.nameTag.setText(nameTag);
        this.setLayout(null);
        this.setSize(400,400);
        this.setPreferredSize(new Dimension(500,180));
        Arrays.stream(CardColor.values())
                .filter(color -> color != CardColor.LOCOMOTIVE).forEach( color ->  colors.add(new ColorTextLabel(color.toString().toLowerCase(),color)));
        Arrays.stream(CardColor.values()).forEach(color-> trainCardButtonsOnRailYard.put(color,new ArrayList<>()));

        int index = 0;
        for (JLabel colorLabel: colors
             ) {
            colorLabel.setBounds(index * offsetX + stx, sty,50,20 );
            this.add(colorLabel);
            index++;
        }

        this.button.setBounds(240,157,200,20);
        this.nameTag.setBounds(stx,160,150,20);
        this.add(this.nameTag);
        this.add(this.button);

        this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLACK));
        this.setOpaque(true);

    }


    public void setTrainCardButtonsOnRailYard(HashMap<CardColor,ArrayList<TrainCard>> hm) {

        int selectedColorPosX = this.stx;
        int selectedColorPosY = this.sty;
        int offsetY = 15;

        this.removeAllCardButtons();

        for (CardColor cardColor: CardColor.values()){

            for(ColorTextLabel colorTextLabel: colors){

                if(colorTextLabel.getCardColor() == cardColor){
                    selectedColorPosX = colorTextLabel.getX();
                    selectedColorPosY = colorTextLabel.getY() + 20;
                }
            }

            int index = 0;
            for (TrainCard trainCard: hm.get(cardColor)
            ) {
                CardButton tmp = new CardButton(trainCard);
                tmp.setBounds( selectedColorPosX,index * offsetY + selectedColorPosY);
                trainCardButtonsOnRailYard.get(cardColor).add(tmp);
                this.add(tmp);
                index++;

            }
        }


    }


    public JButton getButton() {
        return button;
    }


    public void removeAllCardButtons(){
        Arrays.stream(CardColor.values()).forEach(cardColor -> {
            trainCardButtonsOnRailYard.get(cardColor).forEach(this::remove);
            trainCardButtonsOnRailYard.get(cardColor).clear();
            this.repaint();
        });
        this.repaint();
    }

    private void initComponents(){
        nameTag = new JLabel();
        trainCardButtonsOnRailYard = new HashMap<CardColor,ArrayList<CardButton>>();
        colors = new ArrayList<ColorTextLabel>();
        button = new JButton("Move Cards to On-The-Track");
    }
}
