package auctionsniper;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;

public class Main {

  private static final int ARG_HOSTNAME = 0;
  private static final int ARG_USERNAME = 1;
  private static final int ARG_PASSWORD = 2;
  private static final int ARG_ITEM_ID = 3;

  public static final String AUCTION_RESOURCE = "Auction";
  public static final String ITEM_ID_AS_LOGIN = "auction-%s";
  public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/" + AUCTION_RESOURCE;

  public static final String JOIN_COMMAND_FORMAT = "";
  public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID, Price: %d;";

  private final SnipersTableModel snipers = new SnipersTableModel();
  private MainWindow ui;
  private Set<Chat> notToBeGCd = new HashSet<>();

  public Main() throws InvocationTargetException, InterruptedException {
    SwingUtilities.invokeAndWait(() -> ui = new MainWindow(snipers));
  }

  public static void main(String... args) throws InvocationTargetException, InterruptedException, XMPPException {
    Main main = new Main();
    XMPPConnection connection = connection(args[ARG_HOSTNAME], args[ARG_USERNAME], args[ARG_PASSWORD]);
    main.disconnectWhenUICloses(connection);

    for (int i = 3; i < args.length; i++) {
      main.joinAuction(connection, args[i]);
    }
  }

  private void joinAuction(XMPPConnection connection, String itemId)
      throws InvocationTargetException, InterruptedException {
    safelyAddItemToModel(itemId);

    Chat chat = connection.getChatManager().createChat(auctionId(itemId, connection), null);

    notToBeGCd.add(chat);

    Auction auction = new XMPPAuction(chat);
    chat.addMessageListener(new AuctionMessageTranslator(
        connection.getUser(),
        new AuctionSniper(itemId, auction, new SwingThreadSniperListener(snipers))));
    auction.join();
  }

  private void disconnectWhenUICloses(final XMPPConnection connection) {
    ui.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        connection.disconnect();
      }
    });
  }

  private void safelyAddItemToModel(final String itemId) throws InvocationTargetException, InterruptedException {
    SwingUtilities.invokeAndWait(() -> snipers.addSniper(SniperSnapshot.joining(itemId)));
  }

  private static XMPPConnection connection(String hostname, String username, String password) throws XMPPException {
    XMPPConnection connection = new XMPPConnection(hostname);
    connection.connect();
    connection.login(username, password, AUCTION_RESOURCE);

    return connection;
  }

  private static String auctionId(String itemId, XMPPConnection connection) {
    return String.format(AUCTION_ID_FORMAT, itemId, connection.getServiceName());
  }

  public static class XMPPAuction implements Auction {

    private final Chat chat;

    public XMPPAuction(Chat chat) {
      this.chat = chat;
    }

    @Override
    public void bid(int amount) {
      sendMessage(String.format(BID_COMMAND_FORMAT, amount));
    }

    @Override
    public void join() {
      sendMessage(JOIN_COMMAND_FORMAT);
    }

    private void sendMessage(final String message) {
      try {
        chat.sendMessage(message);
      } catch (XMPPException e) {
        e.printStackTrace();
      }
    }
  }


  public class SwingThreadSniperListener implements SniperListener {

    private final SnipersTableModel snipers;

    public SwingThreadSniperListener(SnipersTableModel snipers) {
      this.snipers = snipers;
    }

    @Override
    public void sniperStateChanged(SniperSnapshot snapshot) {
      SwingUtilities.invokeLater(() -> ui.sniperStatusChanged(snapshot));
    }
  }
}
