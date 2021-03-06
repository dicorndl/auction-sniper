package auctionsniper;

public class AuctionSniper implements AuctionEventListener {

  private final Auction auction;
  private final SniperListener sniperListener;

  private SniperSnapshot snapshot;

  public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
    this.auction = auction;
    this.sniperListener = sniperListener;
    this.snapshot = SniperSnapshot.joining(itemId);
  }

  @Override
  public void currentPrice(int price, int increment, PriceSource priceSource) {
    switch (priceSource) {
      case FROM_SNIPER:
        snapshot = snapshot.winning(price);
        break;
      case FROM_OTHER_BIDDER:
        int bid = price + increment;
        auction.bid(bid);
        snapshot = snapshot.bidding(price, bid);
        break;
    }
    notifyChanged();
  }

  @Override
  public void auctionClosed() {
    snapshot = snapshot.closed();
    notifyChanged();
  }

  private void notifyChanged() {
    sniperListener.sniperStateChanged(snapshot);
  }
}
