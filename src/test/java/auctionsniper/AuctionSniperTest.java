package auctionsniper;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import auctionsniper.AuctionEventListener.PriceSource;

@ExtendWith(MockitoExtension.class)
class AuctionSniperTest {

  private enum SniperState {
    idle,
    winning,
    bidding;
  }


  private SniperListener sniperListenerSpy = spy(new SniperListenerStub());

  private Auction auction = mock(Auction.class);

  private AuctionSniper sniper = new AuctionSniper(auction, sniperListenerSpy);

  private SniperState sniperState = SniperState.idle;

  @Test
  void reports_lost_if_auction_closes_immediately() {
    sniper.auctionClosed();

    verify(sniperListenerSpy, times(1)).sniperLost();
  }

  @Test
  void reports_lost_if_auction_closes_when_bidding() {
    sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
    sniper.auctionClosed();

    verify(sniperListenerSpy, atLeastOnce()).sniperLost();
    assertThat(sniperState).isEqualTo(SniperState.bidding);
  }

  @Test
  void bids_higher_and_reports_bidding_when_new_price_arrives() {
    final int price = 1001;
    final int increment = 25;

    sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);

    verify(auction, times(1)).bid(price + increment);
    verify(sniperListenerSpy, atLeastOnce()).sniperBidding();
  }

  @Test
  void reports_is_winning_when_current_price_comes_from_sniper() {
    sniper.currentPrice(123, 45, PriceSource.FromSniper);

    verify(sniperListenerSpy, atLeastOnce()).sniperWinning();
  }

  private class SniperListenerStub implements SniperListener {

    @Override
    public void sniperLost() {
    }

    @Override
    public void sniperBidding() {
      sniperState = SniperState.bidding;
    }

    @Override
    public void sniperWinning() {
    }
  }
}