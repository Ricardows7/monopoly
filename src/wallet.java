public class wallet {
    private long money;
    private boolean bankruptcy;

    public wallet(long money)
    {
        this.money = money;
        bankruptcy = false;
    }
    
    private void declareBankruptcy()
    {
        bankruptcy = true;
    }

    public void receive(long value)
    {
        if (value < 0)
            return;
        this.money += value;
    }

    public boolean pay (long value)
    {
        if (value < 0) 
            return false;
        if (this.money < value)
        {
            declareBankruptcy();
            return false;
        }
        this.money -= value;
        return true;
    }
    
    public long Check()
    {
        return this.money;
    }

    public boolean checkIfBroke()
    {
        return bankruptcy;
    }
}