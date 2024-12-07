public class bank {
    private static final long SALARY = 2000;

    private portfolio holdings;
    private register dataBank;
  
    public bank()
    {
        this.dataBank = new register();
        this.holdings = new portfolio();
    }
  
    private boolean transfer (portfolio gamer, wallet money, int gamerId, squares place, boolean sell)
    {
        int value;

        if (place instanceof special)
            return false;

        value = place.getValue();
        
        if (sell && exchange(this, money, value))   //sell para jogador vender!
        {
            holdings.addProp(gamer.remProp(place)); //transfere propriedade
            dataBank.setOwner(place.getPosition(), 0);
        }
        else if (!sell && exchange(money, this, value))
        {
            gamer.addProp(holdings.remProp(place));
            dataBank.setOwner(place.getPosition(), gamerId);
        }
        else
            return false;
        
        return true;
    }

    private boolean thirdPartyTransfer (portfolio receiver, portfolio giver, wallet owner, wallet buyer, int buyerId, squares place, boolean mode)
    {

        int value; int position = place.getPosition();

        if (place instanceof special)
            return false;
        
        value = place.getValue();

        if (!mode || (mode && exchange(buyer, owner, value)))
        {
            receiver.addProp(place);
            giver.remProp(place);
            dataBank.setOwner(position, buyerId);
            return true;
        }
        return false;
    }
    public boolean sellProperties(portfolio gamer, wallet money, int gamerId, squares place, boolean sell)   //negocio entre banco e player
    {
        if (sell && (dataBank.getOwner(place.getPosition()) != gamerId))    //player quer vender mas nao tem propriedade
            return false;
        else if (!sell && (dataBank.getOwner(place.getPosition()) != 0))    //player quer comprar mas banco nao tem prop
            return false;
        return transfer(gamer, money, gamerId, place, sell);
    }

    public boolean sellProperties(portfolio receiver, portfolio giver, wallet owner, wallet buyer, int buyerId, squares place, boolean mode)   //negocio entre players
    {
        if (giver.search(place.getPosition()) == null)
            return false;
        return thirdPartyTransfer(receiver, giver, owner, buyer, buyerId, place, mode);  //1 para vender, 0 para trocar
    }

    public boolean tradeProperties(portfolio gamer1, portfolio gamer2, wallet player1, wallet player2, int player1Id, int player2Id, squares place1, squares place2)
    {
        return (thirdPartyTransfer(gamer1, gamer2, player2, player1, player1Id, place2, false) && thirdPartyTransfer(gamer2, gamer1, player1, player2, player2Id, place1, false));
    }

    public void seize (portfolio gamer, property land)
    {
        this.holdings.addProp(gamer.remProp(land));
    }

    public void payDay (wallet gamer, long value)   //quando da volta no tabuleiro ou cai em casa especial
    {
        gamer.receive(value);
    }
    
    public boolean payUp (wallet gamer, long value)    //quando cai em casa especial
    {
        return gamer.pay(value);
    }

    public boolean exchange (wallet giver, wallet receiver, long value)
    {
        if (giver.Check() >= value)
        {
            giver.pay(value);
            receiver.receive(value);
            return true;
        }
        return false;
    }
    
    public boolean exchange (wallet giver, bank receiver, long value)   //pagar pro banco!
    {
        if (giver.Check() >= value)
        {
            giver.pay(value);
            return true;
        }
        return false;
    }

    public boolean exchange (bank giver, wallet receiver, long value)   //receber do banco!
    {
        receiver.receive(value);
        return true;
    }
    
    public int getOwner(int position)
    {
        return dataBank.getOwner(position);
    }

    public void setOwner(int position, int owner_id)
    {
        dataBank.setOwner(position, owner_id);
    }

    public boolean checkMonopoly(int set, int owner_id)
    {
        return dataBank.checkMonopoly(set, owner_id);
    }

    public boolean checkFullMonopoly(int owner_id)
    {
        return this.checkFullMonopoly(owner_id);
    }

    public boolean allStocks (portfolio gamer, int stocksQuantity)
    {
        if (gamer.checkStocks() == stocksQuantity)
            return true;
        return false;
    }

    public long getSalary()
    {
        return SALARY;
    }
}