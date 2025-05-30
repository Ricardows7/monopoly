import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class propInfo {

    int numLines;
    List<String> allLines = new ArrayList<String>();

    public propInfo(String name) {

        try {
        this.allLines = Files.readAllLines(Paths.get(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.numLines = allLines.size();

    }
    public String getPropertyInfo(int pos) {

        return this.allLines.get(pos);

    }
    public int getStartingValue(int pos) {

        String propInfo = getPropertyInfo(pos);
        String[] parts = propInfo.split("[,]");
        return Integer.parseInt(parts[0]);

    }    
    public int getStartingHouses(int pos) {

        String propInfo = getPropertyInfo(pos);
        String[] parts = propInfo.split("[,]");
        return Integer.parseInt(parts[1]);

    }    
    public int getStartingRent(int pos) {

        String propInfo = getPropertyInfo(pos);
        String[] parts = propInfo.split("[,]");
        return Integer.parseInt(parts[2]);

    }
    public int getStartingMortgage(int pos) {

        String propInfo = getPropertyInfo(pos);
        String[] parts = propInfo.split("[,]");
        return Integer.parseInt(parts[3]);
        
    }
    public float getMultiplier(int pos, int which) {
        
        String propInfo = getPropertyInfo(pos);
        String[] parts = propInfo.split("[,]");
        return Float.parseFloat(parts[4 + which]);      //0 pra prop, 1 casas, 2Rent, 3 mortgage
        
    }
    public int getSetType(int pos) {
            
        String propInfo = getPropertyInfo(pos);
        String[] parts = propInfo.split("[,]");
        return Integer.parseInt(parts[8]);
        
    }

    public int getLine (int pos)
    {
        String propInfo = getPropertyInfo(pos);
        return Integer.parseInt(propInfo);
    }
}
