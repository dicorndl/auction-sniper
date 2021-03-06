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
import auctionsniper.ui.MainWindow;
import auctionsniper.ui.SnipersTableModel;

@ExtendWith(MockitoExtension.class)
public class SniperTableModelTest {

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
    model.sniperStatusChanged(new SniperState("item id", 555, 666), MainWindow.STATUS_BIDDING);

    verify(listener, times(1)).tableChanged(refEq(aRowChangedEvent()));

    assertColumnEquals(Column.ITEM_IDENTIFIER, "item id");
    assertColumnEquals(Column.LAST_PRICE, 555);
    assertColumnEquals(Column.LAST_BID, 666);
    assertColumnEquals(Column.SNIPER_STATUS, MainWindow.STATUS_BIDDING);
  }

  private void assertColumnEquals(Column column, Object expected) {
    final int rowIndex = 0;
    final int columnIndex = column.ordinal();
    assertThat(expected).isEqualTo(model.getValueAt(rowIndex, columnIndex));
  }

  private TableModelEvent aRowChangedEvent() {
    return new TableModelEvent(model, 0);
  }
}
