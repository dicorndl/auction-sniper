package auctionsniper;

import static org.mockito.Mockito.*;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.packet.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import auctionsniper.AuctionEventListener.PriceSource;

@ExtendWith(MockitoExtension.class)
public class AuctionMessageTranslatorTest {

  public static final Chat UNUSED_CHAT = null;

  private final AuctionEventListener lister = mock(AuctionEventListener.class);

  private final AuctionMessageTranslator translator = new AuctionMessageTranslator(ApplicationRunner.SNIPER_ID, lister);

  @Test
  void notifiesAuctionClosedWhenCloseMessageReceived() {
    Message message = new Message();
    message.setBody("SOLVersion: 1.1; Event: CLOSE;");

    translator.processMessage(UNUSED_CHAT, message);

    verify(lister, times(1)).auctionClosed();
  }

  @Test
  void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromOtherBidder() {
    Message message = new Message();
    message.setBody("SOLVersion: 1.1; Event: PRICE; CurrentPrice: 192; Increment: 7; Bidder: Someone else;");

    translator.processMessage(UNUSED_CHAT, message);

    verify(lister, times(1)).currentPrice(192, 7, PriceSource.FromOtherBidder);
  }

  @Test
  void notifiesBidDetailsWhenCurrentPriceMessageReceivedFromSniper() {
    Message message = new Message();
    message.setBody(
        "SOLVersion: 1.1; Event: PRICE; CurrentPrice: 234; Increment: 5; Bidder: " + ApplicationRunner.SNIPER_ID + ";");

    translator.processMessage(UNUSED_CHAT, message);

    verify(lister, times(1)).currentPrice(234, 5, PriceSource.FromSniper);
  }
}
