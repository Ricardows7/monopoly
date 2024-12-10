public class log {
    private boolean mortgaged;
    private int parcelas;   //contador ou prazo ate pagamento
    private int debt;
    private int mortgage; //vetor com os valores de hipoteca dependendo das melhorias
    private float multMortgage;

    private static final int DURATION = 5;

    public log(int mortgage, float multMortgage)
    {
        this.mortgaged = false;
        this.parcelas = 0;
        this.debt = 0;
        this.mortgage = mortgage;
        this.multMortgage = multMortgage;
        //Falta vetor mortgage precisa de arquivo
    }

    private float estimateMortgage(int state)
    {
        float value = state * multMortgage;
        if (value == 0)
            value = 1;
        return value;
    }

    public int getMortgageValue(int state)
    {
        return (int) estimateMortgage(state);
    }
    public boolean mortgage(property land, wallet money)
    {
        if (mortgaged)
            return false;
        mortgaged = true;
        parcelas = DURATION;
        debt = (int)(mortgage * estimateMortgage(land.getState()));
        money.receive(debt);///hipoteca com a prop ou o banco?

        return true;
    }

    public boolean payMortgage (portfolio gamer, wallet money, property land, bank comp)
    {
        if (!mortgaged)
            return true;
        if (money.pay(debt)) {
            mortgaged = false;
            debt = 0;
            return true;
        }
        comp.seize(gamer, land);
        return false;
    }
    
    public int getDebt()
    {
        return debt;
    }

    public int getDuration()
    {
        return parcelas;
    }
    
    public void setDebt(int value)
    {
        this.debt = value;
    }

    public void setDuration(int value)
    {
        this.parcelas = value;
    }

    public void setMortgage(boolean value)
    {
        this.mortgaged = value;
    }

    private boolean update ()
    {
        if (parcelas > 0)
            parcelas -= 1;
        if (parcelas == 0 && mortgaged)     //retorna 1 caso esteja na hora de quitar divida
            return true;
        return false;
    }

    public boolean updateMortgage(portfolio gamer, wallet money, property land, bank comp)
    {
        if (update())
            return payMortgage(gamer, money, land, comp);
        return true;
    }

    public boolean isMortgaged ()
    {
        return mortgaged;
    }
}
