package auctionsniper.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import auctionsniper.Main;
import auctionsniper.SniperStatus;

public class MainWindow extends JFrame {

  public static final String SNIPER_STATUS_NAME = "sniper status";
  private final JLabel sniperStatus = createLabel(SniperStatus.STATUS_JOINING);

  public MainWindow() {
    super("Auction Sniper");
    setName(Main.MAIN_WINDOW_NAME);
    add(sniperStatus);
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  private static JLabel createLabel(String initialText) {
    JLabel result = new JLabel(initialText);
    result.setName(SNIPER_STATUS_NAME);
    result.setBorder(new LineBorder(Color.BLACK));
    return result;
  }
}
