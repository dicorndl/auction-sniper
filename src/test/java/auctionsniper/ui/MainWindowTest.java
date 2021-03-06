package auctionsniper.ui;


import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.objogate.wl.swing.probe.ValueMatcherProbe;

import auctionsniper.AuctionSniperDriver;

@Tag("integration")
class MainWindowTest {

  private final SnipersTableModel tableModel = new SnipersTableModel();
  private final MainWindow mainWindow = new MainWindow(tableModel);
  private final AuctionSniperDriver driver = new AuctionSniperDriver(1000);

  @Test
  void makes_user_request_when_join_button_clicked() {
    final ValueMatcherProbe<String> buttonProbe =
        new ValueMatcherProbe<>(equalTo("an item id"), "join request");

    mainWindow.addUserRequestListener(buttonProbe::setReceivedValue);

    driver.startBiddingInFor("an item id");
    driver.check(buttonProbe);
  }
}