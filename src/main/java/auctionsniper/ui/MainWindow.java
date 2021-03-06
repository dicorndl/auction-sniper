package auctionsniper.ui;

import java.awt.*;

import javax.swing.*;

import auctionsniper.Main;

public class MainWindow extends JFrame {

  public static final String STATUS_JOINING = "Joining";
  public static final String STATUS_BIDDING = "Bidding";
  public static final String STATUS_WINNING = "Winning";
  public static final String STATUS_LOST = "Lost";
  public static final String STATUS_WON = "Won";

  public static final String SNIPER_TABLE_NAME = "sniper status";

  private final SnipersTableModel snipers = new SnipersTableModel();

  public MainWindow() {
    super("Auction Sniper");
    setName(Main.MAIN_WINDOW_NAME);
    fillContentPane(makeSnipersTable());
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  private void fillContentPane(JTable snipersTable) {
    final Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
  }

  private JTable makeSnipersTable() {
    final JTable snipersTable = new JTable(snipers);
    snipersTable.setName(SNIPER_TABLE_NAME);
    return snipersTable;
  }

  public void showStatusText(String statusText) {
    snipers.setStatusText(statusText);
  }
}
