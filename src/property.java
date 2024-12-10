public class property extends squares{
    public int value;
    public int houses; //vetor com os valores de melhoria pra casas
    public int rent; //aluguel com base nas melhorias

    public float multValue;
    public float multHouses;
    public float multRent;

    private int state; //estado das melhorias
    private int set;    //monopolio a que pertence
    private boolean recent;
    private log register;

    public property(int set, int value, int houses, int rent, float multValue, float multHouses, float multRent, log logs)
    {
        this.state = 0;
        this.set = set;
        this.value = value;
        this.houses = houses;
        this.rent = rent;
        this.multValue = multValue;
        this.multHouses = multHouses;
        this.multRent = multRent;
        this.recent = true;
        this.register = logs;
    }
    
    private float estimateValue(int mode) //1 para value, 2 para houses, 3 para rent
    {
        float multiplier = 0;
        if (mode == 1)
            multiplier = state * multValue;
        else if (mode == 2)
            multiplier = state * multHouses;
        else
            multiplier = state * multRent;

        if (multiplier == 0)
            multiplier = 1;

        return multiplier;
    }
    public boolean payRent(wallet player, boolean monopoly) {
        int value = (int)(rent * estimateValue(3));
        if (monopoly)
            value *= 2;
        return player.pay(value);
    }

    public boolean improve(wallet character) {
        if (character.canAfford((long)(houses * estimateValue(2)))) 
        {
            if (isUpgradeValid()) {
                state++;
                character.pay((long)(this.houses * estimateValue(2)));
                recent = false;
                return true;
            }
        }
        return false;
    }

    public int getValue ()
    {
        return (int) (value * estimateValue(1));
    }
    
    public int getUpgradeValue()
    {
        return (int) (houses * estimateValue(2));
    }

    public int getState()
    {
        return state;
    }

    public boolean update(portfolio gamer, wallet money, bank comp)    //atualiza a hipoteca e a possibilidade de comprar hotel
    {
        recent = true;
        return updateMortgage(gamer, money, this, comp);
    }
    
    public void setSet(int value)
    {
        set = value;
    }

    public int getSet()
    {
        return set;
    }

    public boolean updateMortgage(portfolio gamer, wallet money, property land, bank comp)
    {
        return register.updateMortgage(gamer, money, land, comp);
    }

    public boolean getMortgage (wallet money)
    {
        return register.mortgage(this, money);
    }

    public boolean isUpgradeValid()
    {
        if (state > 3)
            return false;
        else if (state < 3 || recent)
        {
            return true;
        }
        return false;
    }

    public int getDebt()
    {
        return register.getDebt();
    }
}
