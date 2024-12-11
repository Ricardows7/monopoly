public class register {
    private static final int CAMPS = 40;
    private static final int SETS = 8;

    private int owners[] = new int[CAMPS];  //arquivo do banco que guarda quem e dono do que
    private int sets[] = new int[CAMPS];    //arquivo do banco que guarda quais propriedades pertencem a qual set
    
    public register()
    {
        // falta setar como -1 propriedades proibidas e 0 para propridades do banco
        for(int i = 0; i < CAMPS; i++){
            this.owners[i] = -1;
            this.sets[i] = -1;
        }
    }

    public int getCamps()
    {
        return CAMPS;
    }

    public void setOwner(int position, int ownerId)
    {
        owners[position] = ownerId;
    }

    public void setSet(int position, int set)
    {
        sets[position] = set;
    }
    
    public int getOwner(int position)
    {
        return owners[position];
    }

    public int getSet(int position)
    {
        return sets[position];
    }
    
    public boolean checkMonopoly(int set, int owner_id)
    {
        int counter = 0;

        for (int i = 0; i < CAMPS; i++)
        {
            if (owners[i] == owner_id && set == sets[i])
                counter++;
            if (counter == 3)
                break;
        }
        if (counter == 3)
            return true;
        return false;
    }

    public boolean checkFullMonopoly(int owner_id)
    {
        if (owner_id == -1 || owner_id == 4)
            return false;
            
        int monopoly[] = new int[SETS];
        for (int i = 0; i < SETS; i++)
        {
            monopoly[i] = 0;
        }

        for (int i = 0; i < CAMPS; i++)
        {
            if (owner_id == getOwner(i))
                monopoly[getSet(i)]++;
        }
        int count = 0;
        for (int i = 0; i < SETS; i++)
        {
            if (monopoly[i] == 3)
                count++;
        }
        
        return (count >= 3);
    }
}
