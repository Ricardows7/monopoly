public class initializer {
    // Isso precisa do arquivo para criar listas de propriedades
    private propInfo data;
    private propInfo specifications;

    public monopoly.board startBoard(int playerAmount, int squaresQuantity)
    {
        //monopoly jogo = new monopoly();
        monopoly.board tabuleiro = new monopoly.board(playerAmount);
        data = new propInfo("../assets/propertySet.csv");
        specifications = new propInfo("../assets/propertyInfo.csv");

        prepareMap(tabuleiro, data, squaresQuantity);
        setar(tabuleiro, specifications, squaresQuantity);
        initialBank(tabuleiro.getMap(), specifications, tabuleiro.getBank());
        return tabuleiro;
    }

    public void prepareMap(monopoly.board tabuleiro, propInfo data, int squaresQuantity)
    {
        for (int i = 0; i < squaresQuantity; i++) {
            int value = data.getLine(i);
            if (value == -2) {
                stocks acao = new stocks(0);
                tabuleiro.addLocation(acao);
            } else if (value == -1) {
                log register = new log(0, 0);
                property land = new property(0, 0, 0, 0, 0, 0, 0, register);
                tabuleiro.addLocation(land);
            } else {
                cartas news = new cartas();
                special space = new special(value, 0, news);
                tabuleiro.addLocation(space);
            }
        }
    }
    
    public void setar(monopoly.board tabuleiro, propInfo specifications, int squaresQuantity)
    {
        for (int i = 0; i < squaresQuantity; i++) {
            squares land = tabuleiro.getLocation(i);
            if (land != null) {
                land.setPosition(i);
                if (land instanceof property) {
                    land.setType(1);
                    int value1 = specifications.getStartingMortgage(i);
                    float value2 = specifications.getMultiplier(i, 3);
                    log register = new log(value1, value2);
                    land = new property(specifications.getSetType(i), specifications.getStartingValue(i),
                            specifications.getStartingHouses(i), specifications.getStartingRent(i),
                            specifications.getMultiplier(i, 1), specifications.getMultiplier(i, 2),
                            specifications.getMultiplier(i, 3), register);
                } else if (land instanceof stocks) {
                    land.setType(2);
                    land = new stocks(5000);
                } else
                    land.setType(3);
            }
        }
    }
    
    public void initialBank(portfolio map, propInfo specification, bank comp)
    {
        for (int i = 0; i < map.size(); i++)
        {
            squares land = map.search(i);
            if (land instanceof property) {
                comp.addProp(land);
                comp.setOwner(i, 4);
                comp.setSet(i, specification.getSetType(i));
            } else if (land instanceof stocks) {
                comp.addProp(land);
                comp.setOwner(i, 4);
                comp.setSet(i, -1);
            }
            else
            {
                comp.setOwner(i, -1);
                comp.setSet(i, -1);
            }
        }
    }
}
