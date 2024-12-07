//Arquivo que contem a classe dos players
public class player {
    private int id;
    private wallet money;
    private int position;
    private portfolio resources;

    public static final long FIRST_MONEY = 2000;    //passar isso pra initializer!
    
    public player(int id)
    {
        this.id = id;
        this.money = new wallet(FIRST_MONEY);
        this.position = 0;
        this.resources = new portfolio();
    }

    public void receive (long value)
    {
        this.money.receive(value);
    }

    public boolean pay (long value)
    {
        return this.money.pay(value);
    }

    public long Check()
    {
        return this.money.Check();
    }

    public void addProp (squares item)
    {
        this.resources.addProp(item);
    }

    public squares remProp (squares item)
    {
        this.resources.remProp(item);
        return item;
    }

    public boolean move(squares place, dice dado, int totalSquares)
    {
        if (money.checkIfBroke())
            return false;
        dado.throwDie();
        if (place instanceof special) {
            if (((special) place).getTimeOut() > 0) {
                ((special) place).updateHolyday();
                return false;
            }
        }
        int distance = dado.checkTotalValue();
        position = (position + distance) % totalSquares;

        return true;
    }
    
    private void specialMove (squares place, int distance, int totalSquares)
    {
        position = (position + distance) % totalSquares;
    }
    
    public void improveProperty(property land, bank comp)
    {
        if (comp.getOwner(position) == id)
            land.improve(money);
    }

    public boolean verifyOwnership (bank comp)
    {
        return (id == comp.getOwner(position));
    }

    public boolean update (squares place, bank comp, int totalSquares, int ownerStocks, player[] gamers, int playerAmount, int moverId)    //esse owner stock vem do board e e a quantidade de stocks do dono do quadrado
    {
        int owner = comp.getOwner(position);
        boolean value = true;

        if (place instanceof property)
        {
            if (owner != id)
                value = ((property) place).payRent(money, comp.checkMonopoly(((property)place).getSet(), owner));
        }
        else if (place instanceof stocks)
        {
            if (owner != id)
                value = ((stocks) place).payDebt(money, ownerStocks);
        }
        else //considerando que so tem 3 tipos de squares
        {
            int distance = ((special) place).fallSpecial(money, comp.getSalary(), gamers, position, id, playerAmount, totalSquares, comp);
            if (distance > 0)
                specialMove(place, distance, distance);
            else if (distance < 0)  //se falhou em pagar a casa especial, faliu
                value = false;
        }
        return value && gamers[moverId].getPortfolio().updateAll(money, comp);  //atualiza status e hipoteca de todas as casas!
    }
    public boolean bankNegotiation(bank comp, squares place, boolean sell)
    {
        return comp.sellProperties (resources, money, id, place, sell);
    }
    
    public boolean playerNegotiation(bank comp, portfolio giver, wallet owner, squares place, boolean mode) //compra forcada
    {
        return comp.sellProperties(resources, giver, owner, money, id, place, mode);
    }

    public boolean playerTrade (bank comp, portfolio gamer2, wallet player2, int player2Id, squares place1, squares place2)    //places sao os locais escolhidos pra troca!
    {
        return comp.tradeProperties(resources, gamer2, money, player2, id, player2Id, place1, place2);
    }

    public int getPosition ()
    {
        return position;
    }

    public squares currentSquare()
    {
        return this.resources.search(position);
    }

    public int getId()
    {
        return id;
    }

    public wallet getWallet()
    {
        return money;
    }

    public portfolio getPortfolio()
    {
        return resources;
    }

    public boolean getBankruptcy()
    {
        return money.checkIfBroke();
    }

    public boolean mortgage(property land)
    {
        return land.getMortgage(money);
    }

    public int checkStocks ()
    {
        return resources.checkStocks();
    }

    public squares getRandomSquares(int totalSquares)
    {
        return resources.getRandomSquares(totalSquares);
    }

    public int checkVictory (bank comp, int stocksQuantity)   //retorno da funcao update
    {
        if (money.checkIfBroke()) // 1 se venceu, -1 se perdeu, 0 se ta na mesma!
            return -1;
        if (comp.allStocks(resources, stocksQuantity) || comp.checkFullMonopoly(id))
            return 1;
        return 0;
    }
}