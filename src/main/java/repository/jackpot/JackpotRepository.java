package repository.jackpot;

import connector.Connector;

public class JackpotRepository {
    protected final Connector connector;

    public JackpotRepository(Connector connector) {
        this.connector = connector;
    }
}
