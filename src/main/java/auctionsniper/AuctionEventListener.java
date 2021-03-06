package auctionsniper;

public interface AuctionEventListener {

  enum PriceSource {
    FROM_SNIPER,
    FROM_OTHER_BIDDER
  }

  void auctionClosed();

  void currentPrice(int price, int increment, PriceSource from);
}
