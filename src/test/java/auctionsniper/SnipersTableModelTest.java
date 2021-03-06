package auctionsniper;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import auctionsniper.ui.Column;
import auctionsniper.ui.SnipersTableModel;

@ExtendWith(MockitoExtension.class)
public class SnipersTableModelTest {

  private TableModelListener listener = mock(TableModelListener.class);
  private final SnipersTableModel model = new SnipersTableModel();

  @BeforeEach
  void attachModelListener() {
    model.addTableModelListener(listener);
  }

  @Test
  void has_enough_columns() {
    assertThat(model.getColumnCount()).isEqualTo(Column.values().length);
  }

  @Test
  void sets_sniper_values_in_columns() {
    SniperSnapshot joining = SniperSnapshot.joining("item id");
    SniperSnapshot bidding = joining.bidding(555, 666);

    model.addSniper(joining);
    model.sniperStatusChanged(bidding);

    assertRowMatchesSnapshot(0, bidding);
    verify(listener, times(1)).tableChanged(refEq(aRowChangedEvent()));
  }

  @Test
  void notifies_listeners_when_adding_a_sniper() {
    assertThat(model.getRowCount()).isEqualTo(0);

    SniperSnapshot joining = SniperSnapshot.joining("item123");
    model.addSniper(joining);

    assertThat(model.getRowCount()).isEqualTo(1);
    assertRowMatchesSnapshot(0, joining);
  }

  @Test
  void holds_sniper_in_addition_order() {
    model.addSniper(SniperSnapshot.joining("item 0"));
    model.addSniper(SniperSnapshot.joining("item 1"));

    assertColumnEquals(0, Column.ITEM_IDENTIFIER, "item 0");
    assertColumnEquals(1, Column.ITEM_IDENTIFIER, "item 1");
  }

  private void assertColumnEquals(int rowIndex, Column column, Object expected) {
    final int columnIndex = column.ordinal();
    assertThat(expected).isEqualTo(model.getValueAt(rowIndex, columnIndex));
  }

  private void assertRowMatchesSnapshot(int rowIndex, SniperSnapshot snapshot) {
    assertColumnEquals(rowIndex, Column.ITEM_IDENTIFIER, snapshot.itemId);
    assertColumnEquals(rowIndex, Column.LAST_PRICE, snapshot.lastPrice);
    assertColumnEquals(rowIndex, Column.LAST_BID, snapshot.lastBid);
    assertColumnEquals(rowIndex, Column.SNIPER_STATE, SnipersTableModel.textFor(snapshot.state));
  }


  private TableModelEvent aRowChangedEvent() {
    return new TableModelEvent(model, 0);
  }
}
