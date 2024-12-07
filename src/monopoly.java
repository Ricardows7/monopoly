public class monopoly {
    public static class board {
        private dice dado;
        private bank comp;
        private player gamers[];
        private portfolio map;

        private int numPlayers;

        private static final int SQUARES_QUANTITY = 40;
        private static final int MAX_PLAYERS = 4;
        private static final int STOCKS_QUANTITY = 8;

        public board(int playerQnt) {
            this.dado = new dice(playerQnt);
            this.comp = new bank();
            this.map = new portfolio();
            this.gamers = new player[playerQnt];

            for (int i = 0; i < playerQnt; i++) {
                gamers[i] = new player(i);
            }

            setPlayers(playerQnt);
        }

        public void setPlayers(int value) //numero maximo de players
        {
            if (value < MAX_PLAYERS)
                numPlayers = value;
        }

        public int getPlayers() {
            return numPlayers;
        }

        public void setPlayers(player gamer, int id) {
            gamers[id] = gamer;
        }

        public player getPlayer(int id) //pegar qualquer player especifico
        {
            return gamers[id];
        }

        public bank getBank() {
            return comp;
        }

        public dice getDie() {
            return dado;
        }

        public squares getPlace(int position) {
            return map.search(position);
        }

        public player getOwner(int position) {
            int id = comp.getOwner(position);
            return getPlayer(id);
        }

        public int getSquaresQuantity() {
            return SQUARES_QUANTITY;
        }

        public int getStocksQuantity() {
            return STOCKS_QUANTITY;
        }

        public player[] getGamers() {
            return gamers;
        }

        public squares getLocation(int position) {
            return map.search(position);
        }
    }
}
