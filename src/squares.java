public class squares {
    private int type;
    private int position;

    public void setType (int type)
    {
        this.type = type;
    }

    public int getType ()
    {
        return type;
    }

    public int getPosition ()
    {
        return position;
    }

    public int getValue()
    {
        if (this instanceof special)
            return 0;
        else if (this instanceof property)
            return ((property) this).getValue();
        else
            return ((stocks) this).getValue();
    }

    public boolean getMortgage(wallet money)
    {
        if (this instanceof property) {
            return ((property) this).getMortgage(money);
        }
        return false;
    }
    
    public boolean improve(wallet money)
    {
        if (this instanceof property)
        {
            return ((property) this).improve(money);
        }
        return false;
    }
}