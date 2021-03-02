package auctionsniper;

public class AuctionSniper implements AuctionEventListener {

  private final Auction auction;
  private final SniperListener sniperListener;

  public AuctionSniper(Auction auction, SniperListener sniperListener) {
    this.auction = auction;
    this.sniperListener = sniperListener;
  }

  @Override
  public void currentPrice(int price, int increment, PriceSource from) {
    switch (from) {
      case FromSniper:
        sniperListener.sniperWinning();
        break;
      case FromOtherBidder:
        auction.bid(price + increment);
        sniperListener.sniperBidding();
        break;
    }
  }

  @Override
  public void auctionClosed() {
    sniperListener.sniperLost();
  }
}
