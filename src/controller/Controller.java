package controller;

import model.classes.cards.*;
import model.classes.game.Player;
import model.classes.regions.Deck;
import model.classes.regions.OnTheTrack;
import model.classes.regions.RailYard;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import view.*;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Controller {

    private final View view;
    private final Deck deck;
    private Player[] players = new Player[2];

    private final int maxScore = 100;
    private CardColor mainColor = CardColor.LOCOMOTIVE;
    private boolean phase1Completed = false;
    private boolean phase2Completed = false;
    private boolean isTrainRobbing = false;
    private ArrayList<CardColor> trainRobbingColors = new ArrayList<>();
    private int trainCardsTaken = 0;

    /**
     * <h2>Constructor</h2>
     * @post Creates the interface between the view package and the model package. Basically implements the functionality
     */
    private Controller(){
        deck = new Deck();
        players[0] = new Player("Player 1");
        players[1] = new Player("Player 2");
        String[] names = new String[2];
        names[0] = players[0].getName();
        names[1] = players[1].getName();
        view = new View(names);
        this.initializeGame();
        this.setListeners();
        view.setTitle("Ticket to Ride Card Game");
        view.pack();
        view.setVisible(true);
    }

    /**
     * <b>Accessor Method</b>
     * @param pl the player whom the opponent you want to find
     * @return the opponent of the player
     */
    private Player getOpponent(Player pl){
        if(pl == players[0]) return players[1];
        else return players[0];
    }

    /**
     * <b>Accessor Method</b>
     * @param pl the player whom the opponent panel you want to find
     * @return the panel of the opponent player
     */
    private PlayerPanel getOpponentPanel(Player pl){
        if(pl == players[0]) return view.getPlayerPanel2();
        else return view.getPlayerPanel1();
    }

    /**
     * <b>Transformer Method</b>
     * @pre all the selected card the player has should be allowed to be player
     * @post new cards will be moved to player's railyard
     * @param pl player
     * @param playerPanel playerPanel
     */
    private void moveCardsToRailYard(Player pl, PlayerPanel playerPanel){
        ArrayList<TrainCard> tmpTrainCards = new ArrayList<>();
        HashMap<CardColor,ArrayList<TrainCard>> tmpHashMap = new HashMap<>();
        Arrays.stream(CardColor.values()).forEach( cardColor -> tmpHashMap.put(cardColor,new ArrayList<>()));
        boolean isValid = false;
        boolean firstResult = false;
        int locomotiveCards = 0;
        if(pl.hasTurn()) {
            ArrayList<CardButton> tmp = playerPanel.getTrainCardPanel().getTrainCardButtonsOnHands();
            tmp = tmp.stream().filter(AbstractButton::isSelected).collect(Collectors.toCollection(ArrayList::new));
            tmp.forEach(cardButton -> {
                TrainCard tmpCard = (TrainCard)cardButton.getCard();
                tmpTrainCards.add(tmpCard);
                tmpHashMap.get(tmpCard.getColor()).add(tmpCard);
            });


            for (CardColor cardC:CardColor.values()
            ) {
                if(cardC != CardColor.LOCOMOTIVE && tmpHashMap.get(cardC).size() >0) {
                    mainColor = cardC;
                }
            }


            if(tmpTrainCards.size() == 3){
                CardColor[] tmparr = new CardColor[3];
                isValid = true;
                for (TrainCard card:tmpTrainCards) {

                    if(canCardBePlayed(card,pl,tmpTrainCards.stream().filter(trainCard -> trainCard.getColor()==card.getColor()).collect(Collectors.toCollection(ArrayList::new))))isValid=false;
                    CardColor cd = card.getColor();
                    if(cd == CardColor.LOCOMOTIVE){
                        isValid = false;
                    }
                    else {
                        for (int i = 0; i < tmparr.length; i++) {
                            if(tmparr[i]!=null){
                                if(cd == tmparr[i]){
                                    isValid = false;
                                }
                            }else {
                                tmparr[i] = cd;
                            }
                        }
                    }

                }
                firstResult = isValid;
            }
            if (!firstResult && tmpTrainCards.size()>=2) {
                //CASE 1: THROW CARDS
                isValid = false;

                for (TrainCard card:tmpTrainCards) {
                    if(canCardBePlayed(card,pl,tmpTrainCards.stream().filter(trainCard -> trainCard.getColor()==card.getColor()).collect(Collectors.toCollection(ArrayList::new))))isValid=false;
                }

                for (CardColor cd: CardColor.values()
                ) {

                    if(cd == CardColor.LOCOMOTIVE && tmpHashMap.get(cd).size() >0){
                        locomotiveCards = tmpHashMap.get(cd).size();
                        isValid = true;
                    }else if(tmpHashMap.get(cd).size()>0 && isValid){
                        isValid = false;
                        break;
                    }
                    else if(tmpHashMap.get(cd).size()>0 && !isValid){
                        isValid = true;
                    }

                }




                if(locomotiveCards > tmpTrainCards.size()-2) isValid = false;
            }

            if(isValid){
                if(isTrainRobbing){
                    trainRobbingColors.forEach(color -> getOpponent(pl).getRailYard().getCardsByColor(color).clear());
                    getOpponentPanel(pl).getRailYardPanel().setTrainCardButtonsOnRailYard(getOpponent(pl).getRailYard().getCards());
                }



                final CardColor cdFinal = mainColor;
                for (TrainCard trainCard:tmpTrainCards
                     ) {
                    pl.removeCardFromHands(trainCard);

                    if(firstResult){
                        pl.getRailYard().addCard(trainCard);
                    }else {
                        pl.getRailYard().addCard(trainCard,cdFinal);
                    }

                    playerPanel.getTrainCardPanel().setTrainCardButtonsOnHands(pl.getCardsOnHands());
                    playerPanel.getRailYardPanel().setTrainCardButtonsOnRailYard(pl.getRailYard().getCards());
                }
                setTurn();
            }else {
                new ErrorDialog("Looks like someone doesn't know how to play");
            }
        } else  {
            new ErrorDialog("It's not your turn");
        }
        tmpTrainCards.clear();
        tmpHashMap.clear();

    }

    /**
     * <b>Transformer Method</b>
     * @post the last card of each color in railyard's player will be moved to his track
     * @param pl player
     * @param playerPanel playerPanel
     */
    private void moveCardsToTrack(Player pl, PlayerPanel playerPanel){
        if(pl.hasTurn()) {
            HashMap<CardColor,ArrayList<TrainCard>> tmp = pl.getRailYard().getCards();
            Arrays.stream(CardColor.values()).forEach(cardColor -> {
                if(tmp.get(cardColor).size() > 0 ){
                    pl.getTrack().addCard(tmp.get(cardColor).get(tmp.get(cardColor).size() -1).getColor());
                    tmp.get(cardColor).remove(tmp.get(cardColor).size() -1);
                }
            });
            playerPanel.getRailYardPanel().setTrainCardButtonsOnRailYard(tmp);
            playerPanel.getOnTheTrackPanel().updateLabels(pl.getTrack().getCards());
            phase1Completed = true;
        }else {
            new ErrorDialog("It's not your turn");
        }
    }

    /**
     * <b>Transformer Method</b>
     * @post the player has taken the card which is on the top of trainCardDeck
     * @param pl player
     * @param playerPanel playerPanel
     */
    private void giveCardFromTrainDeck(Player pl, PlayerPanel playerPanel){
        pl.addCardOnHands(deck.removeTrainCardFromDeck());
        playerPanel.getTrainCardPanel().setTrainCardButtonsOnHands(pl.getCardsOnHands());
        view.getGameDeckPanel().updateLabel(deck.getTrainCardsDeck().size(),deck.getDestinationCardsDeck().size());
        trainCardsTaken++;
        if(isGameFinished(pl)) finishGame();
    }

    /**
     * <b>Transformer Method</b>
     * @post the player has taken one card from table
     * @param pl player
     * @param playerPanel playerPanel
     */
    private void giveCardFromTable(Player pl, PlayerPanel playerPanel){
        CardButton[] tmp = view.getGameDeckPanel().getCardButtonsOnTable();
        TrainCard[] tmpArrayTrain = deck.getCardsOnTable();
        for (int i = 0; i < tmp.length; i++) {
            if(tmp[i].isShouldBeTaken()){
                tmp[i].setShouldBeTaken(false);
                pl.addCardOnHands((TrainCard) tmp[i].getCard());
                playerPanel.getTrainCardPanel().setTrainCardButtonsOnHands(pl.getCardsOnHands());
                trainCardsTaken++;
                tmpArrayTrain[i] = null;
            }
        }
        TrainCard[] newTmpArrayTrain = new TrainCard[5];
        for (int i = 0; i < tmpArrayTrain.length; i++){

            if(tmpArrayTrain[i]!=null) newTmpArrayTrain[i] = tmpArrayTrain[i];
            else {
                if(deck.getTrainCardsDeck().size()>0){
                    newTmpArrayTrain[i] = deck.removeTrainCardFromDeck();

                }
                else {
                    newTmpArrayTrain[i] = null;
                }
            }

        }
        deck.setCardsOnTable(newTmpArrayTrain);
        view.getGameDeckPanel().removeAllTrainButtons();
        view.getGameDeckPanel().setCardButtonsOnTable(deck.getCardsOnTable());
        this.setListenerToDeckTrainCards();
        view.getGameDeckPanel().updateLabel(deck.getTrainCardsDeck().size(),deck.getDestinationCardsDeck().size());
        if(isGameFinished(pl)) finishGame();
    }


    /**
     * <b>Transformer Method</b>
     * @post the cards on the table have now an actionListener
     */
    private void setListenerToDeckTrainCards(){
        CardButton[] tmp = view.getGameDeckPanel().getCardButtonsOnTable();
        for (CardButton cardButton : tmp) {
            cardButton.addActionListener(e -> {
                if (canPhaseTwoHappen()) {
                    cardButton.setShouldBeTaken(true);
                    if (players[0].hasTurn() && trainCardsTaken < 2) {
                        giveCardFromTable(players[0], view.getPlayerPanel1());
                    } else if (players[1].hasTurn() && trainCardsTaken < 2) {
                        giveCardFromTable(players[1], view.getPlayerPanel2());
                    }
                    if (trainCardsTaken == 2) {
                        phase2Completed = true;
                        setTurn();
                    }
                } else {
                    new ErrorDialog("Complete Phase 1 first");
                }
            });
        }
    }

    /**
     * <b>Accessor Method</b>
     * @param tc a train card
     * @param pl a player
     * @param selCardsByPlayer a list of trainCards
     * @post whether or not a card can be player has bee returned
     * @return whether or not a card can be player
     */
    private boolean canCardBePlayed(TrainCard tc,Player pl,ArrayList<TrainCard> selCardsByPlayer){
        boolean canBePlayed = false;
        if(pl.canCardBePlayed(tc)) {
            canBePlayed = true;
        }
        else if(getOpponent(pl).getRailYard().getCardsByColor(tc.getColor()).size() !=0 &&
                getOpponent(pl).getRailYard().getCardsByColor(tc.getColor()).size() < selCardsByPlayer.size()){
            trainRobbingColors.add(tc.getColor());
            isTrainRobbing = true;
            canBePlayed = true;
        }else if(getOpponent(pl).getRailYard().getCardsByColor(tc.getColor()).size() > 0){
            canBePlayed = true;
        }
        return canBePlayed;
    }

    /**
     * <b>Observer Method</b>
     * @return true if the phase2 has not finished yet
     */
    private boolean isPhase2Happening(){
        return trainCardsTaken == 1;
    }
    /**
     * <b>Observer Method</b>
     * @return true if the phase1 can happen
     */
    private boolean canPhaseOneHappen(){
        return !phase1Completed;
    }

    /**
     * <b>Observer Method</b>
     * @return true if the phase2 can happen
     */
    private boolean canPhaseTwoHappen(){
        if(phase1Completed) return true;
        if(players[0].hasTurn()){
            if(!phase1Completed && players[0].getRailYard().isEmpty()) return true;
        }if(players[1].hasTurn()&& trainCardsTaken<2){
            return !phase1Completed && players[1].getRailYard().isEmpty();
        }
        return false;
    }

    /**
     * <b>Transformer Method</b>
     * @param pl a player
     * @param cd the destination card the player wants to buy off
     * @param plp the panel of player
     * @post the player buys off the cards, gets its points and gets one step close to acquire a bonus cards
     * @pre phase2 is allowed to happen,phase2 is not happening,it's the player's turn.
     */
    private void buyOffCard(Player pl,PlayerPanel plp,DestinationCard cd){
        if(canPhaseTwoHappen()){
            if(!isPhase2Happening()){
                if(pl.hasTurn()){
                    JDialog popupDialog = new JDialog(view,"Destination Cards",true);
                    CardLabel cl = new CardLabel(cd);
                    JLabel jl = new JLabel();
                    JButton yesButton = new JButton("Yes");
                    JButton noButton = new JButton("No");
                    jl.setText("<html>Do you want to buy off this card? <br/> <br/> BuyCard</html>");
                    jl.setBounds(140,40,250,50);
                    yesButton.setBounds(140,110,50,20);
                    noButton.setBounds(210,110,50,20);


                    popupDialog.add(yesButton);
                    popupDialog.add(noButton);

                    noButton.addActionListener(e-> popupDialog.dispose());
                    yesButton.addActionListener(e->{
                        String arCity = cd.getArrivalCity();
                        String depCity = cd.getDepartureCity();
                        boolean uCan = true;
                        int usedLocomotive = 0;
                        ArrayList<CardColor> cdAr = new ArrayList<>();
                        for (CardColor cc:cd.getColors()
                        ) {
                            if(pl.getTrack().getNumberOfCardsByColor(cc) == 0){
                                uCan = false;
                                if(pl.getTrack().getNumberOfCardsByColor(CardColor.LOCOMOTIVE) - usedLocomotive > 0){
                                    usedLocomotive++;
                                    uCan = true;
                                    cdAr.add(CardColor.LOCOMOTIVE);
                                }else {
                                    cdAr.add(cc);
                                }
                            }
                        }

                        if(uCan){
                            for (CardColor cc:cdAr
                            ) {
                                pl.getTrack().removeCard(cc);
                            }
                            takeBigCityCard(arCity,pl);
                            takeBigCityCard(depCity,pl);

                            pl.updateScore(cd.getPoints() * 2);
                            pl.getDestinationCardsAcquired().add(cd);
                            pl.removeDestinationCard(cd);
                            plp.getOnTheTrackPanel().updateLabels(pl.getTrack().getCards());
                            plp.getPlayerInfoPanel().updateLabels(pl.getScore(),pl.hasTurn());
                            plp.getDestinationTicketPanel().setDestinationCardButtonsOnHands(pl.getDestinationCards());
                            view.getGameDeckPanel().setBonusCardLabels(deck.getBigCitiesOnTable());
                            this.setListenerToDestCardsOnHands(pl,plp);
                            popupDialog.dispose();
                            setTurn();
                            if(isGameFinished(pl)) finishGame();

                        }else {
                            new ErrorDialog("You cannot buy off this card now. You don't have enough cards.");
                        }
                    });
                    popupDialog.setLayout(null);
                    cl.setBounds(50,15,cl.getWidth(),cl.getHeight());
                    popupDialog.add(cl);
                    popupDialog.add(jl);

                    popupDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                    popupDialog.setSize(400,210);
                    popupDialog.setResizable(false);
                    popupDialog.setLocationRelativeTo(view);
                    popupDialog.setVisible(true);
                    popupDialog.pack();
                }else {
                    new ErrorDialog("It's not your turn");
                }
            }else {
                new ErrorDialog("You decided to get cards..... DO IT");
            }
        }else {
            new ErrorDialog("Complete Phase 1 first");
        }
    }

    /**
     * <b>Transformer Method</b>
     * @post the player now owns the selected big city
     * @param s is the name of the city
     * @param pl the player
     * @pre the city should be a big city and it should not be already taken and the player has to already visit the city 2 times
     */
    private void takeBigCityCard(String s,Player pl){
        if(isBigCity(s)) {
            if(!getBigCityC(s).isTaken()){
                pl.addBigCitiesTimeVisited(toEnum(s));
                if(pl.getBigCitiesTimeVisited(toEnum(s)) >=3){

                    for (int i = 0; i < deck.getBigCitiesOnTable().length; i++) {
                        if(deck.getBigCitiesOnTable()[i].getCity().equals(toEnum(s))){
                            deck.getBigCitiesOnTable()[i].setTaken(true);
                        }
                    }
                    pl.addBonusCard(getBigCityC(s));
                }
            }else {
                pl.addBigCitiesTimeVisited(toEnum(s));
            }
        }
    }

    /**
     * <b>Accessor Method</b>
     * @param s the name to the city
     * @return the card that matches the given name
     */
    private BigCitiesCard getBigCityC(String s){
        return Arrays.stream(deck.getBigCitiesOnTable()).filter(bigCitiesCard -> bigCitiesCard.getCity().equals(toEnum(s))).findFirst().orElse(null);
    }
    /**
     * <b>Observer Method</b>
     * @param str the name to the city
     * @return true if only one match has came up
     */
    private boolean isBigCity(String str){
        int i =(int) Arrays.stream(BigCities.values()).map(bigCities -> bigCities.toString().toLowerCase()).filter(x->x.equals(str.toLowerCase().replace(" ",""))).count();
        return i==1;
    }

    /**
     * <b>Accessor Method</b>
     * @param str the name to the city
     * @return the enum that matches the given name
     */
    private BigCities toEnum(String str) throws EnumConstantNotPresentException{
        return Arrays.stream(BigCities.values()).filter(bigCities -> bigCities.toString().toLowerCase().equals(str.toLowerCase().replace(" ",""))).findFirst().orElse(null);
    }

    /**
     * <b>Transformer Method</b>
     * @post all the destination cards of a player now have an actionListener
     * @param player the player who owns the cards
     * @param pl his panel
     */
    private void setListenerToDestCardsOnHands(Player player,PlayerPanel pl){
        ArrayList<CardButton> tmp = pl.getDestinationTicketPanel().getDestinationCardButtonsOnHands();
        tmp.forEach(cardButton -> cardButton.addActionListener(e->{
            if(cardButton.isSelected()){
                cardButton.setBounds(cardButton.getX(),cardButton.getY()-10);
            }else {
                cardButton.setBounds(cardButton.getX(),cardButton.getY()+10);
            }
            buyOffCard(player,pl,(DestinationCard) cardButton.getCard());
        }));
    }


    /**
     * <b>Accessor Method</b>
     * @return a list of the saved game inside of a specific folder
     */
    private ArrayList<String> getSavedGames(){
        File folder= new File("./SavedGames");
        if(folder.exists()){
            ArrayList<File> files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(folder.listFiles())));
            ArrayList<String> fileNames = new ArrayList<>();
            files.forEach(file -> fileNames.add(file.getName().replaceFirst("[.][^.]+$", "")));
            return fileNames;
        }
        else return new ArrayList<>();
    }
    /**
     * <b>Transformer Method</b>
     * @post set all listeners to every button from the view class
     */
    private void setListeners(){
        this.setListenerToDeckTrainCards();
        view.getLoadGame().addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                ArrayList<String> fileNames = getSavedGames();
                String s = (String)JOptionPane.showInputDialog(
                        view,
                        "Choose which saved game you want to play:\n",
                        "Load Game",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        fileNames.toArray(),
                        fileNames.isEmpty()?"":fileNames.get(0));
                if( (s!=null )&& fileNames.size() > 0) loadGame(s);
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });

        view.getNewGame().addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                int reply = JOptionPane.showConfirmDialog(view, "Are you sure you want to create a new game?\nAll unsaved progress will be lost", "New Game", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    newGame();
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        view.getExitGame().addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                int reply = JOptionPane.showConfirmDialog(view, "Are you sure you want to exit?\nAll unsaved progress will be lost", "Exit Game", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        view.getSaveGame().addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                String fname = JOptionPane.showInputDialog(view, "Write a name for your saved game");
                ArrayList<String> fileNames = getSavedGames();
                boolean canBeSaved = true;
                if( fname!=null ){

                    for (String fileName:fileNames
                         ) {
                        if(fname.equals(fileName)){
                            int reply = JOptionPane.showConfirmDialog(view, "A game has already been saved with that name\n Do you want to override it?", "Overriding Saved Game", JOptionPane.YES_NO_OPTION);
                            if (reply == JOptionPane.NO_OPTION) {
                                canBeSaved = false;
                            }
                        }
                    }

                    if(canBeSaved){
                        saveGame(fname);
                    }
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        view.getPlayerPanel1().getTrainCardPanel().getButton().addActionListener(e->{
            if(canPhaseTwoHappen()){
                if(!isPhase2Happening()){
                    moveCardsToRailYard(players[0],view.getPlayerPanel1());
                }else {
                    new ErrorDialog("You decided to get cards..... DO IT");
                }
            }else {
                new ErrorDialog("Complete Phase 1 first");
            }

        });

        view.getPlayerPanel2().getTrainCardPanel().getButton().addActionListener(e->{
            if(canPhaseTwoHappen()){
                if(!isPhase2Happening()){
                    moveCardsToRailYard(players[1],view.getPlayerPanel2());
                }else {
                    new ErrorDialog("You decided to get cards..... DO IT");
                }
            }else {
                new ErrorDialog("Complete Phase 1 first");
            }
        });

        view.getPlayerPanel1().getRailYardPanel().getButton().addActionListener(e->{
            if(canPhaseOneHappen() && !players[0].getRailYard().isEmpty()) moveCardsToTrack(players[0],view.getPlayerPanel1());
            else {
                new ErrorDialog("Phase 1 Already Completed OR No Cards On RailYard");
            }
        });
        view.getPlayerPanel2().getRailYardPanel().getButton().addActionListener(e->{

            if(canPhaseOneHappen() && !players[1].getRailYard().isEmpty())  moveCardsToTrack(players[1],view.getPlayerPanel2());
            else {
                new ErrorDialog("Phase 1 Already Completed OR No Cards On RailYard");
            }
        });

        view.getGameDeckPanel().getTrainDeckCardButton().addActionListener(e->{

            if(canPhaseTwoHappen()){
                if(players[0].hasTurn() && trainCardsTaken<2){
                    giveCardFromTrainDeck(players[0],view.getPlayerPanel1());
                }else if(players[1].hasTurn()&& trainCardsTaken<2){
                    giveCardFromTrainDeck(players[1],view.getPlayerPanel2());
                }
                if(trainCardsTaken == 2){
                    phase2Completed = true;
                    setTurn();
                }
            }else {
                new ErrorDialog("Complete Phase 1 first");
            }

        });

        view.getGameDeckPanel().getDestinationDeckCardButton().addActionListener(e->{

            if(canPhaseTwoHappen()){
                if(players[0].hasTurn()) showDestCardPicker(4,players[0],view.getPlayerPanel1(),true);
                else if(players[1].hasTurn()) showDestCardPicker(4,players[1],view.getPlayerPanel2(),true);
            }else {
                new ErrorDialog("Complete Phase 1 first");
            }


        });


        view.getPlayerPanel1().getPlayerInfoPanel().getBonusCardsButton().addActionListener(e-> showBigCitiesCardAcquired(players[0]));
        view.getPlayerPanel2().getPlayerInfoPanel().getBonusCardsButton().addActionListener(e-> showBigCitiesCardAcquired(players[1]));
        view.getPlayerPanel1().getPlayerInfoPanel().getDestTicketsButton().addActionListener(e-> showDestTicketsAcquired(players[0]));
        view.getPlayerPanel2().getPlayerInfoPanel().getDestTicketsButton().addActionListener(e-> showDestTicketsAcquired(players[1]));

    }


    /**
     * <b>Transformer Method</b>
     * @post all selected destination cards have been moved to player's hands
     * @param max_cards number of max card a user can take
     * @param pl player
     * @param plp his panel
     * @param allowNotCardsTaken whether or not a player should take at least one card
     */
    private void showDestCardPicker(int max_cards,Player pl,PlayerPanel plp,boolean allowNotCardsTaken){
        JDialog popupDialog = new JDialog(view,pl.getName()+" Destination Cards",true);
        popupDialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        popupDialog.setSize(max_cards*100+100,300);
        popupDialog.setResizable(false);
        popupDialog.setLayout(new FlowLayout(FlowLayout.CENTER,20,10));
        ArrayList<DestinationCard> tmp = new ArrayList<>();
        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();

        popupDialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                if(allowNotCardsTaken){
                    for (int i = 0; i < checkBoxes.size() ; i++) {
                        deck.addDestinationCardOnDeck(tmp.get(i));
                    }
                    setTurn();
                    popupDialog.dispose();
                }else {
                    new ErrorDialog("You have to take at least one card");
                }
            }
        });

        for (int i = 0; i <max_cards ; i++) {
            DestinationCard tmpCard = deck.removeDestCardFromDeck();
            tmp.add(tmpCard);
            CardLabel tmpCardLabel = new CardLabel(tmpCard);
            popupDialog.add(tmpCardLabel);
        }

        tmp.forEach(destinationCard -> {
            JCheckBox tmpCheckBox = new JCheckBox("Keep Card");
            checkBoxes.add(tmpCheckBox);
            popupDialog.add(tmpCheckBox);
        });


        JButton submit = new JButton("Submit");
        submit.addActionListener(event->{
            if (allowNotCardsTaken || checkBoxes.stream().filter(AbstractButton::isSelected).count() >= 1){
                for (int i = 0; i < checkBoxes.size() ; i++) {
                    if(checkBoxes.get(i).isSelected()){
                        pl.addDestinationCard(tmp.get(i));
                        pl.updateScore(tmp.get(i).getPoints() * -1);
                    }else {
                        deck.getDestinationCardsDeck().add(0,tmp.get(i));
                    }
                }
                plp.getPlayerInfoPanel().updateLabels(pl.getScore(),pl.hasTurn());
                view.getGameDeckPanel().updateLabel(deck.getTrainCardsDeck().size(),deck.getDestinationCardsDeck().size());
                plp.getDestinationTicketPanel().setDestinationCardButtonsOnHands(pl.getDestinationCards());
                this.setListenerToDestCardsOnHands(pl,plp);
                popupDialog.dispose();
                setTurn();
            }else {
                new ErrorDialog("You have to take at least one card");
            }

        });
        popupDialog.add(submit);


        popupDialog.setLocationRelativeTo(view);
        popupDialog.setVisible(true);
        popupDialog.pack();
    }

    /**
     * <b>Accessor Method</b>
     * @post a JDialog that shows the acquired destination cards of the player
     * @param pl player
     */
    private void showDestTicketsAcquired(Player pl){
        JDialog popupDialog = new JDialog(view,"Dest Tickets "+pl.getName()+" owns",true);
        popupDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        popupDialog.setSize(
                pl.getDestinationCardsAcquired().size() <= 4? 408:pl.getDestinationCardsAcquired().size() * 102
                ,200);
        popupDialog.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));

        for (int i = 0; i <pl.getDestinationCardsAcquired().size(); i++) {
            CardLabel tmp = new CardLabel(pl.getDestinationCardsAcquired().get(i),97,150);
            popupDialog.add(tmp);
        }


        popupDialog.setLocationRelativeTo(view);
        popupDialog.setVisible(true);
        popupDialog.pack();
    }

    /**
     * <b>Accessor Method</b>
     * @post a JDialog that shows the acquired bonus cards of the player
     * @param pl player
     */
    private void showBigCitiesCardAcquired(Player pl){
        JDialog popupDialog = new JDialog(view,"Big Cities "+pl.getName()+" owns",true);
        popupDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        popupDialog.setSize(630,200);
        popupDialog.setResizable(false);
        popupDialog.setLayout(new FlowLayout(FlowLayout.LEFT,5,0));

        for (int i = 0; i <deck.getBigCitiesOnTable().length ; i++) {
            CardLabel tmp = new CardLabel(deck.getBigCitiesOnTable()[i],97,150);
            tmp.makeItGray();

            for (BigCitiesCard bgC:pl.getBonusCardsAcquired()
                 ) {
                if(bgC.getCity().equals(deck.getBigCitiesOnTable()[i].getCity())){
                    tmp.makeItColored();

                }
            }
            popupDialog.add(tmp);
        }
        for (int i = 0; i <deck.getBigCitiesOnTable().length ; i++) {
            popupDialog.add(new JLabel("Visited: "+pl.getBigCitiesTimeVisited(toEnum(deck.getBigCitiesOnTable()[i].getCityName()))+" times"));
        }

        popupDialog.setLocationRelativeTo(view);
        popupDialog.setVisible(true);
        popupDialog.pack();
    }

    /**
     * <b>Transformer Method</b>
     * @post all decks with the cards have been filled with cards
     */
    private void fillDecks(){
        deck.getTrainCardsDeck().clear();
        deck.getDestinationCardsDeck().clear();
        Arrays.stream(CardColor.values()).forEach(cardColor -> {
            if(cardColor == CardColor.LOCOMOTIVE) {
                for (int i = 0; i < 16; i++) {
                    deck.addTrainCardOnDeck(cardColor);
                }
            }else {
                for (int i = 0; i < 10; i++) {
                    deck.addTrainCardOnDeck(cardColor);
                }
            }
        });


        players[0].addCardOnHands(deck.getTrainCardsDeck().remove(deck.getTrainCardsDeck().size()-1));
        players[1].addCardOnHands(deck.getTrainCardsDeck().remove(deck.getTrainCardsDeck().size()-1));




         BigCitiesCard[] tmp = new BigCitiesCard[6];

         tmp[0] = new BigCitiesCard(BigCities.Miami,8,"./resources/images/bigCitiesCards/Miami.jpg");
         tmp[1] = new BigCitiesCard(BigCities.Seattle,8,"./resources/images/bigCitiesCards/Seattle.jpg");
         tmp[2] = new BigCitiesCard(BigCities.Dallas,10,"./resources/images/bigCitiesCards/Dallas.jpg");
         tmp[3] = new BigCitiesCard(BigCities.Chicago,12,"./resources/images/bigCitiesCards/Chicago.jpg");
         tmp[4] = new BigCitiesCard(BigCities.LosAngeles,12,"./resources/images/bigCitiesCards/LosAngeles.jpg");
         tmp[5] = new BigCitiesCard(BigCities.NewYork,15,"./resources/images/bigCitiesCards/NewYork.jpg");


         deck.setBigCitiesOnTable(tmp);

        view.getGameDeckPanel().setBonusCardLabels(deck.getBigCitiesOnTable());



        try {
            BufferedReader br = new BufferedReader(new FileReader("./resources/files/destinationCards.csv"));
            String sCurrentLine;
            int i = -1;
            while ((sCurrentLine = br.readLine()) != null) {
                if (i == -1) {
                    i = 0;
                    continue;
                }
                String[] splitLine = sCurrentLine.split(",");
                String id = splitLine[0];
                String from = splitLine[1];
                String to = splitLine[2];
                int score = Integer.parseInt(splitLine[3]);
                String colorsList = splitLine[4];
                String[] splitColors = colorsList.split("-");
                ArrayList<String> colors = new ArrayList<>(Arrays.asList(splitColors));
                String imagePath = splitLine[5];
                imagePath = "./resources/images/destination_Tickets/" + imagePath;

                deck.getDestinationCardsDeck().add(new DestinationCard(Integer.parseInt(id),from,to,score,colors,imagePath));
            }
        }
        catch ( IOException e){
            e.printStackTrace();
        }
    }

    /**
     * <b>Accessor Method</b>
     * @param min the min value
     * @param max the min value
     * @return  the generated random number between min and max
     */
    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }


    /**
     * <b>Transformer Method</b>
     * @post This method will initialize the players, view and cards.
     */
    private void initializeGame(){
        this.fillDecks();
        this.shuffleCards();
        this.assignCards();
        this.assignTurn();
    }

    /**
     * <b>Transformer Method</b>
     * @post all cards have been initialized and shuffled
     */
    private void shuffleCards(){
        for (int i = 0; i < deck.getTrainCardsDeck().size() - 1; i++) {
            int tmpRand = getRandomNumberInRange(i,deck.getTrainCardsDeck().size() - 1);
            TrainCard cd = deck.getTrainCardsDeck().get(i);
            deck.getTrainCardsDeck().set(i,deck.getTrainCardsDeck().get(tmpRand));
            deck.getTrainCardsDeck().set(tmpRand,cd);
        }

        for (int i = 0; i < deck.getDestinationCardsDeck().size() - 1; i++) {
            int tmpRand = getRandomNumberInRange(i,deck.getDestinationCardsDeck().size() - 1);
            DestinationCard cd = deck.getDestinationCardsDeck().get(i);
            deck.getDestinationCardsDeck().set(i,deck.getDestinationCardsDeck().get(tmpRand));
            deck.getDestinationCardsDeck().set(tmpRand,cd);
        }
    }


    /**
     * <b>Transformer Method</b>
     * @post each player's turn has been set
     */
    private void assignTurn(){
        int i = getRandomNumberInRange(0,1);
        if(i == 0){
            players[0].setTurn(true);
            players[1].setTurn(false);
        }
        else {
            players[0].setTurn(false);
            players[1].setTurn(true);
        }

        view.getPlayerPanel1().getPlayerInfoPanel().updateLabels(players[0].getScore(),players[0].hasTurn());
        view.getPlayerPanel2().getPlayerInfoPanel().updateLabels(players[1].getScore(),players[1].hasTurn());
    }

    /**
     * <b>Transformer Method</b>
     * @post the player who's playing first has been decided
     */
    private void setTurn(){
        phase1Completed = false;
        phase2Completed = false;
        trainCardsTaken = 0;
        trainRobbingColors.clear();

        players[0].setTurn(!players[0].hasTurn());
        players[1].setTurn(!players[1].hasTurn());

        view.getPlayerPanel1().getPlayerInfoPanel().updateLabels(players[0].getScore(),players[0].hasTurn());
        view.getPlayerPanel2().getPlayerInfoPanel().updateLabels(players[1].getScore(),players[1].hasTurn());

    }


    private void clearPlayer(Player pl){
        pl.getCardsOnHands().clear();
        pl.getDestinationCards().clear();
        pl.getDestinationCardsAcquired().clear();
        pl.getBonusCardsAcquired().clear();
        pl.getRailYard().empty();
        pl.getTrack().empty();
        pl.setScore(0);
        pl.setTurn(false);
    }

    /**
     * <b>Transformer Method</b>
     * @post the players have received their cards
     */
    private void assignCards(){

        for (int i = 0; i < 7; i++) {
            players[0].addCardOnHands(deck.removeTrainCardFromDeck());
            players[1].addCardOnHands(deck.removeTrainCardFromDeck());
        }

        showDestCardPicker(6,players[0],view.getPlayerPanel1(),false);
        showDestCardPicker(6,players[1],view.getPlayerPanel2(),false);

        view.getPlayerPanel1().getTrainCardPanel().setTrainCardButtonsOnHands(players[0].getCardsOnHands());
        view.getPlayerPanel2().getTrainCardPanel().setTrainCardButtonsOnHands(players[1].getCardsOnHands());



        TrainCard[] tmp = new TrainCard[5];
        for (int i = 0; i < 5; i++) {
            tmp[i] = deck.getTrainCardsDeck().remove(deck.getTrainCardsDeck().size()-1);
        }
        deck.setCardsOnTable(tmp);

        view.getGameDeckPanel().setCardButtonsOnTable(deck.getCardsOnTable());

        view.getGameDeckPanel().updateLabel(deck.getTrainCardsDeck().size(),deck.getDestinationCardsDeck().size());
    }


    /**
     * <b>Observer Method</b>
     * @return true/false depending on whether the game has finished
     */
    private boolean isGameFinished(Player pl){

        if(pl.getScore()>=maxScore) return true;
        return deck.getTrainCardsDeck().size() == 0;
    }

    /**
     * <b>Transformer Method</b>
     * @post a dialog with the name of the winner has been appeared
     */
    private void finishGame(){

        if(players[0].getScore() >= maxScore) {
            new ErrorDialog(players[0].getName()+" won");
        }else if(players[1].getScore() >=maxScore){
            new ErrorDialog(players[1].getName()+ " won");
        }else if(deck.getTrainCardsDeck().size() == 0){
            if(players[0].getScore() != players[1].getScore()){
                Player winner = players[0].compareByScore(players[1]);
                new ErrorDialog(winner.getName()+ " won");
            }else {
                if(players[0].getDestinationCardsAcquired().size()!=players[1].getDestinationCardsAcquired().size()){
                    new ErrorDialog(players[0].compareByDestTickets(players[1]) + " won");
                }else if(players[0].getBonusCardsAcquired().size() != players[1].getBonusCardsAcquired().size()){
                    new ErrorDialog(players[0].compareByBonusCards(players[1]) + " won");
                }else {
                    new ErrorDialog("It's a tie");
                }
            }
        }

        view.dispose();
        System.exit(0);
    }

    private void newGame(){
        clearPlayer(players[0]);
        clearPlayer(players[1]);
        view.getPlayerPanel1().getRailYardPanel().removeAllCardButtons();
        view.getPlayerPanel1().getOnTheTrackPanel().removeAllCardLabels();
        view.getPlayerPanel2().getRailYardPanel().removeAllCardButtons();
        view.getPlayerPanel2().getOnTheTrackPanel().removeAllCardLabels();
        this.initializeGame();
        this.setListenerToDeckTrainCards();
        isTrainRobbing = false;
        phase1Completed = false;
        phase2Completed = false;
        trainCardsTaken = 0;
        trainRobbingColors.clear();
    }

    private void buildGUIForLoadedDeck(){
        view.getGameDeckPanel().setCardButtonsOnTable(deck.getCardsOnTable());
        view.getGameDeckPanel().setBonusCardLabels(deck.getBigCitiesOnTable());
        view.getGameDeckPanel().updateLabel(deck.getTrainCardsDeck().size(),deck.getDestinationCardsDeck().size());
        this.setListenerToDeckTrainCards();
    }
    private void buildGUIForLoadedPlayer(Player pl,PlayerPanel plp){

        plp.getDestinationTicketPanel().setDestinationCardButtonsOnHands(pl.getDestinationCards());
        plp.getOnTheTrackPanel().updateLabels(pl.getTrack().getCards());
        plp.getRailYardPanel().setTrainCardButtonsOnRailYard(pl.getRailYard().getCards());
        plp.getTrainCardPanel().setTrainCardButtonsOnHands(pl.getCardsOnHands());
        plp.getPlayerInfoPanel().updateLabels(pl.getScore(),pl.hasTurn());
        this.setListenerToDestCardsOnHands(pl,plp);
    }
    private void buildGUIForLoadedGame(Player[] players){
        buildGUIForLoadedPlayer(players[0],view.getPlayerPanel1());
        buildGUIForLoadedPlayer(players[1],view.getPlayerPanel2());
        buildGUIForLoadedDeck();
    }

    private void loadGame(String fileName){
        File f= new File("./SavedGames/"+fileName+".json");
        JSONParser parser = new JSONParser();
        ArrayList<Player> pl = new ArrayList<>();
        try {
            Object obj = parser.parse(new FileReader(f));
            JSONObject mainObj = (JSONObject) obj;

            JSONArray playersJSON = (JSONArray) mainObj.get("players");
            Iterator<JSONObject> iterator = playersJSON.iterator();
            while (iterator.hasNext()) {
                JSONObject curPlJSON = iterator.next();
                String name = (String) curPlJSON.get("name");
                String score = curPlJSON.get("score").toString();
                boolean turn = (boolean) curPlJSON.get("turn");

                JSONObject trainCardsJSON = (JSONObject) curPlJSON.get("trainCardsOnHands");
                ArrayList<TrainCard> trainCards = new ArrayList<>();
                Arrays.asList(CardColor.values()).forEach(cardColor -> {
                    for (int i = 0; i < Integer.parseInt(trainCardsJSON.get(cardColor.toString()).toString()); i++) {
                        trainCards.add(new TrainCard("./resources/images/trainCards/"+cardColor.toString().toLowerCase()+".jpg",cardColor));
                    }
                });

                JSONObject railYardJSON = (JSONObject) curPlJSON.get("railYard");
                RailYard railYard = new RailYard();
                Arrays.asList(CardColor.values()).forEach(cardColor -> {
                    for (int i = 0; i < Integer.parseInt(railYardJSON.get(cardColor.toString()).toString()); i++) {
                        railYard.addCard(new TrainCard("./resources/images/trainCards/"+cardColor.toString().toLowerCase()+".jpg",cardColor));
                    }
                });

                JSONObject onTheTrackJSON = (JSONObject) curPlJSON.get("track");
                OnTheTrack track = new OnTheTrack();
                Arrays.asList(CardColor.values()).forEach(cardColor -> {
                    for (int i = 0; i < Integer.parseInt(onTheTrackJSON.get(cardColor.toString()).toString()); i++) {
                        track.addCard(cardColor);
                    }
                });

                JSONObject bigCitiesTimesVisitedJSON = (JSONObject) curPlJSON.get("bigCitiesTimesVisited");
                HashMap<BigCities,Integer> bigCitiesTimesVisited = new HashMap<>();
                Arrays.stream(BigCities.values()).forEach(bigCities -> bigCitiesTimesVisited.put(bigCities,0));
                Arrays.stream(BigCities.values()).forEach(bigCities -> {
                    for (int i = 0; i < Integer.parseInt(bigCitiesTimesVisitedJSON.get(bigCities.toString()).toString()); i++) {
                        bigCitiesTimesVisited.replace(bigCities,bigCitiesTimesVisited.get(bigCities)+1);
                    }
                });

                JSONArray destinationCardsOnHandsJSON = (JSONArray) curPlJSON.get("destinationCardsOnHands");
                ArrayList<DestinationCard> destinationCardsOnHands = new ArrayList<>();
                Iterator<JSONObject> destCardIterator= destinationCardsOnHandsJSON.iterator();
                while (destCardIterator.hasNext()) {
                    JSONObject destCardJSON = destCardIterator.next();
                    String depCity = destCardJSON.get("departureCity").toString();
                    String arrCity = destCardJSON.get("arrivalCity").toString();
                    String imagePath = destCardJSON.get("imageFile").toString();
                    int id = Integer.parseInt(destCardJSON.get("id").toString());
                    int points = Integer.parseInt(destCardJSON.get("points").toString());
                    ArrayList<String> cardColors = new ArrayList<>();
                    JSONArray colorsJSON = (JSONArray) destCardJSON.get("colors");
                    Iterator<String> colorIterator= colorsJSON.iterator();
                    while (colorIterator.hasNext()){
                        String color  = colorIterator.next();
                        cardColors.add(color);
                    }
                    destinationCardsOnHands.add(new DestinationCard(id,depCity,arrCity,points,cardColors,imagePath));
                }


                JSONArray destinationCardsAcquiredJSON = (JSONArray) curPlJSON.get("destinationCardsAcquired");
                ArrayList<DestinationCard> destinationCardsAcquired = new ArrayList<>();
                destCardIterator= destinationCardsAcquiredJSON.iterator();
                while (destCardIterator.hasNext()) {
                    JSONObject destCardJSON = destCardIterator.next();
                    String depCity = destCardJSON.get("departureCity").toString();
                    String arrCity = destCardJSON.get("arrivalCity").toString();
                    String imagePath = destCardJSON.get("imageFile").toString();
                    int id = Integer.parseInt(destCardJSON.get("id").toString());
                    int points = Integer.parseInt(destCardJSON.get("points").toString());
                    ArrayList<String> cardColors = new ArrayList<>();
                    JSONArray colorsJSON = (JSONArray) destCardJSON.get("colors");
                    Iterator<String> colorIterator= colorsJSON.iterator();
                    while (colorIterator.hasNext()){
                        String color  = colorIterator.next();
                        cardColors.add(color);
                    }
                    destinationCardsAcquired.add(new DestinationCard(id,depCity,arrCity,points,cardColors,imagePath));
                }

                JSONArray bonusCardsAcquiredJSON = (JSONArray) curPlJSON.get("bonusCards");
                ArrayList<BigCitiesCard> bonusCardsAcquired = new ArrayList<>();
                Iterator<JSONObject> bonusCardsIterator= bonusCardsAcquiredJSON.iterator();
                while (bonusCardsIterator.hasNext()) {
                    JSONObject card = bonusCardsIterator.next();
                    String cityName = card.get("cityName").toString();
                    String imageFile = card.get("imageFile").toString();
                    boolean isTaken = (boolean) card.get("isTaken");
                    int points = Integer.parseInt(card.get("points").toString());
                    BigCitiesCard tmpCard = new BigCitiesCard(toEnum(cityName),points,imageFile);
                    tmpCard.setTaken(isTaken);
                    bonusCardsAcquired.add(tmpCard);
                }


                Player curPlayer = new Player(name);
                curPlayer.setScore(Integer.parseInt(score));
                curPlayer.setTurn(turn);
                curPlayer.setTrainCardsOnHands(trainCards);
                curPlayer.setRailYard(railYard);
                curPlayer.setTrack(track);
                curPlayer.setDestinationCardsOnHands(destinationCardsOnHands);
                curPlayer.setDestinationCardsAcquired(destinationCardsAcquired);
                curPlayer.setBigCitiesTimesVisited(bigCitiesTimesVisited);
                curPlayer.setBonusCardsAcquired(bonusCardsAcquired);
                pl.add(curPlayer);
            }

            JSONObject deckJSON = (JSONObject) mainObj.get("deck");


            JSONArray bigCitiesOnTableJSON = (JSONArray) deckJSON.get("bigCitiesOnTable");
            ArrayList<BigCitiesCard> bigCitiesOnTable = new ArrayList<>();
            Iterator<JSONObject> bigCitiesIterator= bigCitiesOnTableJSON.iterator();
            while (bigCitiesIterator.hasNext()) {
                JSONObject card = bigCitiesIterator.next();
                String cityName = card.get("cityName").toString();
                String imageFile = card.get("imageFile").toString();
                boolean isTaken = (boolean) card.get("isTaken");
                int points = Integer.parseInt(card.get("points").toString());
                BigCitiesCard tmpCard = new BigCitiesCard(toEnum(cityName),points,imageFile);
                tmpCard.setTaken(isTaken);
                bigCitiesOnTable.add(tmpCard);
            }

            JSONArray trainCardsdeckJSON = (JSONArray) deckJSON.get("trainCardsDeck");
            Iterator<String> trainCardIterator= trainCardsdeckJSON.iterator();
            deck.getTrainCardsDeck().clear();
            while (trainCardIterator.hasNext()) {
                String color = trainCardIterator.next();
                deck.addTrainCardOnDeck(Arrays.stream(CardColor.values()).filter(cardColor -> cardColor.toString().toLowerCase().equals(color.toLowerCase().replace(" ",""))).findFirst().get());
            }


            JSONArray destinationCardsDeckJSON = (JSONArray) deckJSON.get("destinationCardsDeck");
            ArrayList<DestinationCard> destinationCardsDeck = new ArrayList<>();
            Iterator<JSONObject> destCardIterator= destinationCardsDeckJSON.iterator();
            while (destCardIterator.hasNext()) {
                JSONObject destCardJSON = destCardIterator.next();
                String depCity = destCardJSON.get("departureCity").toString();
                String arrCity = destCardJSON.get("arrivalCity").toString();
                String imagePath = destCardJSON.get("imageFile").toString();
                int id = Integer.parseInt(destCardJSON.get("id").toString());
                int points = Integer.parseInt(destCardJSON.get("points").toString());
                ArrayList<String> cardColors = new ArrayList<>();
                JSONArray colorsJSON = (JSONArray) destCardJSON.get("colors");
                Iterator<String> colorIterator= colorsJSON.iterator();
                while (colorIterator.hasNext()){
                    String color  = colorIterator.next();
                    cardColors.add(color);
                }
                destinationCardsDeck.add(new DestinationCard(id,depCity,arrCity,points,cardColors,imagePath));
            }



            JSONArray cardsOnTableJSON = (JSONArray) deckJSON.get("cardsOnTable");
            ArrayList<TrainCard> cardsOnTable = new ArrayList<>();
            Iterator<String> colorIterator =  cardsOnTableJSON.iterator();
            while (colorIterator.hasNext()) {
                String color = colorIterator.next();
                CardColor cd = Arrays.stream(CardColor.values()).filter(cardColor -> cardColor.toString().toLowerCase().equals(color.toLowerCase().replace(" ",""))).findFirst().get();
                cardsOnTable.add(new TrainCard("./resources/images/trainCards/"+cd.toString().toLowerCase()+".jpg",cd));
            }

            JSONObject gameJSON = (JSONObject) mainObj.get("game");
            trainCardsTaken = Integer.parseInt(gameJSON.get("trainCardsTaken").toString());
            phase1Completed = (boolean) gameJSON.get("phase1Completed");
            phase2Completed = (boolean) gameJSON.get("phase2Completed");
            isTrainRobbing = (boolean) gameJSON.get("isTrainRobbing");
            JSONArray trainRobbingColorsJSON = (JSONArray) gameJSON.get("trainRobbingColors");
            ArrayList<CardColor> tmpTrainRColors = new ArrayList<>();
            colorIterator = trainRobbingColorsJSON.iterator();
            while (colorIterator.hasNext()) {
                String color = colorIterator.next();
                CardColor cd = Arrays.stream(CardColor.values()).filter(cardColor -> cardColor.toString().toLowerCase().equals(color.toLowerCase().replace(" ",""))).findFirst().get();
                tmpTrainRColors.add(cd);
            }

            trainRobbingColors = tmpTrainRColors;

            TrainCard[] i = new TrainCard[cardsOnTable.size()];
            cardsOnTable.toArray(i);
            BigCitiesCard[] o = new BigCitiesCard[bigCitiesOnTable.size()];
            bigCitiesOnTable.toArray(o);
            deck.setBigCitiesOnTable(o);
            deck.setDestinationCardsDeck(destinationCardsDeck);
            deck.setCardsOnTable(i);




            Player[] a = new Player[pl.size()];
            pl.toArray(a);
            players = a;
            buildGUIForLoadedGame(players);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveGame(String fileName){
        JSONArray players = new JSONArray();
        for (Player pl:getPlayers()
             ) {
            JSONObject player = new JSONObject();

            JSONObject trainCardsOnHands = new JSONObject();
            Arrays.stream(CardColor.values()).forEach(cardColor -> trainCardsOnHands.put(cardColor,pl.getCardsOnHands().stream().filter(card-> card.getColor()==cardColor).count()));
            JSONArray destinationCardsOnHands = new JSONArray();
            pl.getDestinationCards().stream().forEach(destinationCard -> {
                JSONObject destCard = new JSONObject();
                destCard.put("id",destinationCard.getUniqueId());
                destCard.put("arrivalCity",destinationCard.getArrivalCity());
                destCard.put("departureCity",destinationCard.getDepartureCity());
                destCard.put("imageFile",destinationCard.getImageFileName());
                destCard.put("points",destinationCard.getPoints());

                JSONArray tmpArr = new JSONArray();
                destinationCard.getColors().forEach(cardColor -> tmpArr.add(cardColor.toString()));
                destCard.put("colors",tmpArr);
                destinationCardsOnHands.add(destCard);
            });
            JSONArray destinationCardsAc = new JSONArray();
            pl.getDestinationCardsAcquired().stream().forEach(destinationCard -> {
                JSONObject destCard = new JSONObject();
                destCard.put("id",destinationCard.getUniqueId());
                destCard.put("arrivalCity",destinationCard.getArrivalCity());
                destCard.put("departureCity",destinationCard.getDepartureCity());
                destCard.put("imageFile",destinationCard.getImageFileName());
                destCard.put("points",destinationCard.getPoints());
                JSONArray tmpArr = new JSONArray();
                destinationCard.getColors().forEach(cardColor -> tmpArr.add(cardColor.toString()));
                destCard.put("colors",tmpArr);
                destinationCardsAc.add(destCard);
            });
            JSONArray bonusCards = new JSONArray();
            pl.getBonusCardsAcquired().forEach(bCard -> {
                JSONObject bonusCard = new JSONObject();

                bonusCard.put("cityName",bCard.getCity().toString());
                bonusCard.put("isTaken",bCard.isTaken());
                bonusCard.put("imageFile",bCard.getImageFileName());
                bonusCard.put("points",bCard.getPoints());
                bonusCards.add(bonusCard);
            });


            JSONObject bigCitiesTimesVisited = new JSONObject();
            Arrays.stream(BigCities.values()).forEach( bigCity-> bigCitiesTimesVisited.put(bigCity,pl.getBigCitiesTimeVisited(bigCity)));

            JSONObject railYard = new JSONObject();
            pl.getRailYard().getCards().forEach( (cardColor, trainCards) -> railYard.put(cardColor,trainCards.size()));
            JSONObject track = new JSONObject();
            pl.getTrack().getCards().forEach(((cardColor, integer) -> track.put(cardColor,integer)));

            player.put("track",track);
            player.put("railYard",railYard);
            player.put("bigCitiesTimesVisited",bigCitiesTimesVisited);
            player.put("bonusCards",bonusCards);
            player.put("destinationCardsAcquired",destinationCardsAc);
            player.put("score",pl.getScore());
            player.put("name",pl.getName());
            player.put("turn",pl.hasTurn());
            player.put("destinationCardsOnHands",destinationCardsOnHands);
            player.put("trainCardsOnHands",trainCardsOnHands);
            players.add(player);
        }

        JSONObject deckObj = new JSONObject();
        JSONArray cardsOnTable = new JSONArray();

        Arrays.stream(deck.getCardsOnTable()).forEach(card->{
            cardsOnTable.add(card.getColor().toString());
        });


        JSONArray bigCitiesOnTable = new JSONArray();
        Arrays.stream(deck.getBigCitiesOnTable()).forEach(card->{
            JSONObject bonusCard = new JSONObject();
            bonusCard.put("cityName",card.getCityName());
            bonusCard.put("isTaken",card.isTaken());
            bonusCard.put("imageFile",card.getImageFileName());
            bonusCard.put("points",card.getPoints());
            bigCitiesOnTable.add(bonusCard);
        });
        JSONArray trainCardsDeck = new JSONArray();
        deck.getTrainCardsDeck().forEach(trainCard -> trainCardsDeck.add(trainCard.getColor().toString()));
        JSONArray destinationCardsDeck = new JSONArray();
        deck.getDestinationCardsDeck().forEach(destinationCard -> {
            JSONObject destCard = new JSONObject();
            destCard.put("id",destinationCard.getUniqueId());
            destCard.put("arrivalCity",destinationCard.getArrivalCity());
            destCard.put("departureCity",destinationCard.getDepartureCity());
            destCard.put("imageFile",destinationCard.getImageFileName());
            destCard.put("points",destinationCard.getPoints());
            JSONArray tmpArr = new JSONArray();
            destinationCard.getColors().forEach(cardColor -> tmpArr.add(cardColor.toString()));
            destCard.put("colors",tmpArr);
            destinationCardsDeck.add(destCard);
        });

        deckObj.put("destinationCardsDeck",destinationCardsDeck);
        deckObj.put("trainCardsDeck",trainCardsDeck);
        deckObj.put("cardsOnTable",cardsOnTable);
        deckObj.put("bigCitiesOnTable",bigCitiesOnTable);


        JSONObject gameObj= new JSONObject();
        gameObj.put("isTrainRobbing",isTrainRobbing);
        gameObj.put("phase1Completed",phase1Completed);
        gameObj.put("phase2Completed",phase2Completed);
        gameObj.put("trainCardsTaken",trainCardsTaken);
        JSONArray trainRobbingColorsJSON = new JSONArray();
        trainRobbingColors.forEach(cardColor -> {
            trainRobbingColorsJSON.add(cardColor.toString());
        });
        gameObj.put("trainRobbingColors",trainRobbingColorsJSON);


        JSONObject mainObj = new JSONObject();
        mainObj.put("game", gameObj);
        mainObj.put("players", players);
        mainObj.put("deck", deckObj);
        File f= new File("./SavedGames/"+fileName+".json");
        f.getParentFile().mkdirs();

        try (FileWriter file = new FileWriter(f)) {
            file.write(mainObj.toJSONString());
            file.flush();
            System.out.println("Successfully Copied JSON Object to File...");
            System.out.println("\nJSON Object: " + mainObj);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * <b>Accessor Method</b>
     * @post all players have been returned
     * @return an array of players
     */
    public Player[] getPlayers() {
        return players;
    }


    public static void main(String[] args){
        new Controller();
    }
}

