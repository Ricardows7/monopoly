import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.Math;

public class cartas {

    private static int numCards;
    private static List<String> allCards = new ArrayList<String>();
    private int currentCard;

    public cartas() {

        if (allCards.isEmpty()) {
            try {
            allCards = Files.readAllLines(Paths.get("../assets/cards.csv"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            numCards = allCards.size();
        }

    }
    private void setCurrentCard(int n) {

        currentCard = n;
        
    }
    private int getCurrentCard() {

        return currentCard;
        
    }
    public void setCurCard(int n) {

        setCurrentCard(n);
        
    }
    public int getCurCard() {

        return getCurrentCard();
        
    }
    private String getCard(int n) {

        return allCards.get(n);

    }
    private int getRandomPlayer(int currentPlayer, int playerAmount) {
        
        int randomPlayer = currentPlayer;
        while (randomPlayer == currentPlayer)
            randomPlayer = (int)(Math.random()*playerAmount);
        return randomPlayer;
        
    }
    public int randomCard() {
        return (int)(Math.random()*numCards);
    }
    public String getCardEffect(int n) {
        String card = getCard(n);
        String[] parts = card.split("[,]");
        return parts[0];
    }
    public int drawACard(int currentPlayer, player[] gamers, int playerAmount, int totalSquares, bank comp, int rand) {

        String card = getCard(rand);
        return manageCard(card,currentPlayer,gamers, playerAmount, totalSquares, comp);

    }

    private int movementCard(String[] parts, player[] gamers, int currentPlayer) {

        if (parts[4].equals("0"))
            return Integer.parseInt(parts[5]);
        return Integer.parseInt(parts[5]) - gamers[currentPlayer].getPosition() + 40;

    }
    private int moneyCard(String[] parts, player[] gamers, int currentPlayer) {

        boolean success = true;
        String origin = parts[2], destination = parts[3];
        int amount = Integer.parseInt(parts[4]);

        if (origin.equals("0"))
            success = gamers[currentPlayer].pay(amount);
        else if (origin.equals("2"))
            for (int i = 0; i < gamers.length; i++)
                if (i != currentPlayer)
                    success = gamers[i].pay(amount);


        if (destination.equals("0"))
            gamers[currentPlayer].receive(amount);
            if (origin.equals("2"))
                gamers[currentPlayer].receive((amount)*(gamers.length - 2));

        else if (destination.equals("2")) {
            int randomPlayer = getRandomPlayer(currentPlayer, gamers.length);
            gamers[randomPlayer].receive(amount);
        }

        if (success == false)
            return -1;
        return 0;

    }
    private void propertyCard(String[] parts, player[] gamers, int currentPlayer, int playerAmount, int totalSquares, bank comp) {
        
        int rivalId = getRandomPlayer(currentPlayer, playerAmount);
        player rival = gamers[rivalId];

        squares place1 = gamers[currentPlayer].getRandomSquares(totalSquares);
        squares place2 = rival.getRandomSquares(totalSquares);

        if ((place1 == null) || (place2 == null))
            return;

        if (parts[4].equals("0"))
            gamers[currentPlayer].playerTrade(comp, rival.getPortfolio(), rival.getWallet(), rival.getId(), place1, place2);    //tem que colocar os parametros nessa chamada!
        else if (parts[5].equals("0"))
            rival.bankNegotiation(comp,place2,true);
        else if (parts[5].equals("1"))
            gamers[currentPlayer].bankNegotiation(comp,place1,true);

    }
    private int manageCard(String card, int currentPlayer, player[] gamers, int playerAmount, int totalSquares, bank comp) {

        int retValue = 0;
        String[] parts = card.split("[,]");

        if (parts[1].equals("0"))         //Handles cards related to the tansfer of money
            retValue = moneyCard(parts,gamers,currentPlayer);
        else if (parts[1].equals("1"))    //Handles cards related to moving around the board
            retValue = movementCard(parts,gamers,currentPlayer);
        else if (parts[1].equals("2"))       //Handle cards related to property management
            propertyCard(parts,gamers,currentPlayer, playerAmount, totalSquares, comp);

        return retValue;


    }   
}
