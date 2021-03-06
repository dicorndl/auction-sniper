package auctionsniper.ui;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

import auctionsniper.UserRequestListener;

public class MainWindow extends JFrame {

  public static final String MAIN_WINDOW_NAME = "Auction Sniper";

  public static final String STATUS_JOINING = "Joining";
  public static final String STATUS_BIDDING = "Bidding";
  public static final String STATUS_WINNING = "Winning";
  public static final String STATUS_LOST = "Lost";
  public static final String STATUS_WON = "Won";

  public static final String APPLICATION_TITLE = "Auction Sniper";
  public static final String SNIPER_TABLE_NAME = "Snipers";
  public static final String NEW_ITEM_ID_NAME = "item id";
  public static final String JOIN_BUTTON_NAME = "join button";

  private final Set<UserRequestListener> userRequests = new HashSet<>();

  public MainWindow(SnipersTableModel snipers) {
    super(APPLICATION_TITLE);
    setName(MAIN_WINDOW_NAME);
    fillContentPane(makeSnipersTable(snipers), makeControls());
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  private void fillContentPane(JTable snipersTable, JPanel controlPanel) {
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());

    contentPane.add(controlPanel, BorderLayout.PAGE_START);
    contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
  }


  private JTable makeSnipersTable(SnipersTableModel snipers) {
    final JTable snipersTable = new JTable(snipers);
    snipersTable.setName(SNIPER_TABLE_NAME);
    return snipersTable;
  }

  private JPanel makeControls() {
    JPanel controls = new JPanel(new FlowLayout());
    final JTextField itemIdField = new JTextField();
    itemIdField.setColumns(25);
    itemIdField.setName(NEW_ITEM_ID_NAME);
    controls.add(itemIdField);

    JButton joinAuctionButton = new JButton("Join Auction");
    joinAuctionButton.setName(JOIN_BUTTON_NAME);
    controls.add(joinAuctionButton);

    joinAuctionButton.addActionListener(
        e -> userRequests.forEach(userRequestListener ->
            userRequestListener.joinAuction(itemIdField.getText())));

    return controls;
  }

  public void addUserRequestListener(UserRequestListener userRequestListener) {
    userRequests.add(userRequestListener);
  }
}
