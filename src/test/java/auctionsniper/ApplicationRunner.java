package auctionsniper;

import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;

public class ApplicationRunner {

  public static final String SNIPER_ID = "sniper";
  public static final String SNIPER_PASSWORD = "sniper";
  public static final String SNIPER_XMPP_ID = "sniper@local/Auction";

  private AuctionSniperDriver driver;
  private String itemId;

  public void startBiddingIn(final FakeAuctionServer... auctions) {
    Thread thread = new Thread("Test Application") {
      @Override
      public void run() {
        try {
          Main.main(arguments(auctions));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    thread.setDaemon(true);
    thread.start();
    driver = new AuctionSniperDriver(1_000);
    driver.hasTitle(MainWindow.APPLICATION_TITLE);
    driver.hasColumnTitles();
    driver.showsSniperStatus(MainWindow.STATUS_JOINING);

    for (FakeAuctionServer auction : auctions) {
      driver.showsSniperStatus(auction.getItemId(), 0, 0, SnipersTableModel.textFor(SniperState.JOINING));
    }
  }

  protected static String[] arguments(FakeAuctionServer... auctions) {
    String[] arguments = new String[auctions.length + 3];
    arguments[0] = FakeAuctionServer.XMPP_HOSTNAME;
    arguments[1] = SNIPER_ID;
    arguments[2] = SNIPER_PASSWORD;
    for (int i = 0; i < auctions.length; i++) {
      arguments[i + 3] = auctions[i].getItemId();
    }
    return arguments;
  }

  public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
    driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, MainWindow.STATUS_BIDDING);
  }

  public void showsSniperHasLostAuction() {
    driver.showsSniperStatus(MainWindow.STATUS_LOST);
  }

  public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
    driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid, MainWindow.STATUS_WINNING);
  }

  public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
    driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice, MainWindow.STATUS_WON);
  }

  public void stop() {
    if (driver != null) {
      driver.dispose();
    }
  }
}
