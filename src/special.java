public class special extends squares{
    private int category;   //0 para
    private int timeOut;

    private cartas news;

    private static final int JAIL_LOCATION = 10;

    public special(int category, int timeOut, cartas news){
        this.timeOut = timeOut;
        this.category = category;
        if (category == 5)
            this.news = news;
    }

    public int getCategory ()
    {
        return category;
    }

    public void setCategory (int category)
    {
        this.category = category;
    }

    public void activateHolyday ()  //dado tem que pedir permissao da casa para poder andar!
    {
        if (category == 1)  //casa feriado
            timeOut++;
    }
    
    public void activatePrison(int position)
    {
        if (category == 2)  //casa go to prison
        {
            timeOut += 3;
            position = JAIL_LOCATION;
        }
    }
    public void updateHolyday ()    //atualiza se player nao pode se mexer
    {
        if (timeOut > 0)
            timeOut--;
    }

    public int getTimeOut ()
    {
        return timeOut;
    }

    public void finishLine(wallet money, long value)
    {
        if (money == null)
            return;
        if (category == 3) //evento de ganhar dinheiro
            money.receive(value);
    }
    
    public int taxes (wallet money, long value)
    {
        if (money == null)
            return -1;

        if (category == 4)
        {
            if (!money.pay(value))
                return -1;
        }
        return 0;
    }

    public String getEffect(int cardId) {

        return news.getCardEffect(cardId);
        
    }

    public cartas getNews() {

        return news;
        
    }
    
    public int fallSpecial (wallet money, long value, player[] gamers, int position, int gamer_id, int playerAmount, int totalSquares, bank comp)
    {
        int signal = 0;
        activateHolyday();  //funcao de feriao
        activatePrison(position);   //funcao de ir pra prisao
        finishLine(money, value);   //funcao de ganhar dinheiro
        signal = taxes(money, value);   //funcao de perder dinheiro
        if (category == 5) {
            news.setCurCard(news.randomCard());
            signal = news.drawACard(gamer_id, gamers, playerAmount, totalSquares, comp,news.getCurCard());  //noticias
        }
        return signal;
    }
}
