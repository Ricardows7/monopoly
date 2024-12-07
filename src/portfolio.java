import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class portfolio{
    private List<squares> properties;

    public portfolio(){
        properties = new ArrayList<>();
    }

    public void addProp(squares item) {
        properties.add(item);
    }

    public squares remProp(squares item) {
        properties.remove(item);
        return item;
    }
    
    public squares search (int location)
    {
        for (squares place : properties)
        {
            if (place.getPosition() == location)
                return place;
        }

        return null;
    }

    private squares randomSquare(int totalSquares)
    {
        Random random = new Random();
        int randomInt = random.nextInt(totalSquares);
        return search(randomInt);
    }

    public squares getRandomSquares(int totalSquares)
    {
        if (this.properties.isEmpty())
            return null;

        squares land = null;
        while (land == null)
            land = randomSquare(totalSquares);
        return land;
    }
    
    public int checkStocks ()
    {
        int count = 0;
        for (squares place : properties)
            if (place instanceof stocks)
                count++;
        return count;
    }

    public boolean updateAll(wallet money, bank comp)
    {
        boolean value = true;
        for (squares place : properties)
            if (place instanceof property)
                value = value && ((property) place).update(this, money, comp);
        return value;
    }
}