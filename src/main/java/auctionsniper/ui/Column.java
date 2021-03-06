package auctionsniper.ui;

import auctionsniper.SniperSnapshot;

public enum Column {

  ITEM_IDENTIFIER("Item") {
    @Override
    public Object valueIn(SniperSnapshot snapshot) {
      return snapshot.itemId;
    }
  },
  LAST_PRICE("Last Price") {
    @Override
    public Object valueIn(SniperSnapshot snapshot) {
      return snapshot.lastPrice;
    }
  },
  LAST_BID("Last Bid") {
    @Override
    public Object valueIn(SniperSnapshot snapshot) {
      return snapshot.lastBid;
    }
  },
  SNIPER_STATUS("State") {
    @Override
    public Object valueIn(SniperSnapshot snapshot) {
      return SnipersTableModel.textFor(snapshot.state);
    }
  };

  public final String name;

  Column(String name) {
    this.name = name;
  }

  public static Column at(int offset) {
    return values()[offset];
  }

  public abstract Object valueIn(SniperSnapshot snapshot);
}
