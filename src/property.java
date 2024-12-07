public class property extends squares{
    private int value[] = new int[6];
    private int houses[] = new int[5]; //vetor com os valores de melhoria pra casas
    private int rent[] = new int[6]; //aluguel com base nas melhorias
    private int state; //estado das melhorias
    private int set;
    private boolean recent;
    private log register;

    public property(int level, int set)
    {
        this.state = level;
        this.set = set;
        this.recent = true;
    }
    
    public boolean payRent(wallet player, boolean monopoly) {
        int value = rent[state];
        if (monopoly)
            value *= 3;
        return player.pay(value);
    }

    public boolean improve(wallet character) {
        if (character.Check() >= this.houses[state]) {
            if (state < 4 || recent) {
                state++;
                character.pay(this.houses[state]);
                recent = false;
                return true;
            }
        }
        return false;
    }

    public int getValue ()
    {
        return value[state];
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
}
