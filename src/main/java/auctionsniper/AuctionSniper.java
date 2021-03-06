package auctionsniper;

public class AuctionSniper implements AuctionEventListener {

  private final Auction auction;
  private final SniperListener sniperListener;

  private boolean isWinning = false;

  public AuctionSniper(Auction auction, SniperListener sniperListener) {
    this.auction = auction;
    this.sniperListener = sniperListener;
  }

  @Override
  public void currentPrice(int price, int increment, PriceSource priceSource) {
    isWinning = priceSource == PriceSource.FromSniper;
    if (isWinning) {
      sniperListener.sniperWinning();
    } else {
      int bid = price + increment;
      auction.bid(bid);
      sniperListener.sniperBidding(new SniperState("", price, bid));
    }
  }

  @Override
  public void auctionClosed() {
    if (isWinning) {
      sniperListener.sniperWon();
    } else {
      sniperListener.sniperLost();
    }
  }
}
