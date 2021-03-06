package auctionsniper;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import auctionsniper.AuctionEventListener.PriceSource;

@ExtendWith(MockitoExtension.class)
class AuctionSniperTest {

  private static final String ITEM_ID = "1234";

  @Mock
  private SniperListener listener;

  @Mock
  private Auction auction;

  private AuctionSniper sniper;

  @BeforeEach
  void setUp() {
    sniper = new AuctionSniper(ITEM_ID, auction, listener);
  }

  @Test
  void reports_lost_if_auction_closes_immediately() {
    sniper.auctionClosed();

    verify(listener, times(1))
        .sniperStateChanged(argThat((SniperSnapshot snapshot) -> SniperState.LOST.equals(snapshot.state)));
  }

  @Test
  void reports_lost_if_auction_closes_when_bidding() {
    sniper.currentPrice(123, 45, PriceSource.FROM_OTHER_BIDDER);
    sniper.auctionClosed();

    verify(listener)
        .sniperStateChanged(argThat((SniperSnapshot snapshot) -> SniperState.BIDDING.equals(snapshot.state)));
    verify(listener)
        .sniperStateChanged(argThat((SniperSnapshot snapshot) -> SniperState.LOST.equals(snapshot.state)));
  }

  @Test
  void reports_won_if_auction_closes_when_winning() {
    sniper.currentPrice(123, 45, PriceSource.FROM_SNIPER);
    sniper.auctionClosed();

    verify(listener)
        .sniperStateChanged(refEq(new SniperSnapshot(ITEM_ID, 123, 0, SniperState.WINNING)));
    verify(listener)
        .sniperStateChanged(argThat((SniperSnapshot snapshot) -> SniperState.WON.equals(snapshot.state)));
  }

  @Test
  void bids_higher_and_reports_bidding_when_new_price_arrives() {
    final int price = 1001;
    final int increment = 25;
    final int bid = price + increment;

    sniper.currentPrice(price, increment, PriceSource.FROM_OTHER_BIDDER);

    verify(auction, times(1)).bid(bid);
    verify(listener, atLeastOnce())
        .sniperStateChanged(refEq(new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING)));
  }

  @Test
  void reports_is_winning_when_current_price_comes_from_sniper() {
    sniper.currentPrice(123, 12, PriceSource.FROM_OTHER_BIDDER);
    sniper.currentPrice(135, 45, PriceSource.FROM_SNIPER);

    verify(listener, atLeastOnce())
        .sniperStateChanged(new SniperSnapshot(ITEM_ID, 123, 135, SniperState.BIDDING));
    verify(listener, atLeastOnce())
        .sniperStateChanged(new SniperSnapshot(ITEM_ID, 135, 135, SniperState.WINNING));
  }
}