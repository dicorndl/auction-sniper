package auctionsniper.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import auctionsniper.Defect;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;

public class SnipersTableModel extends AbstractTableModel {

  private final static String[] STATUS_TEXT = {
      "Joining", "Bidding", "Winning", "Lost", "Won"
  };
  private final List<SniperSnapshot> snapshots = new ArrayList<>();

  @Override
  public int getRowCount() {
    return snapshots.size();
  }

  @Override
  public int getColumnCount() {
    return Column.values().length;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
  }

  @Override
  public String getColumnName(int column) {
    return Column.at(column).name;
  }

  public static String textFor(SniperState state) {
    return STATUS_TEXT[state.ordinal()];
  }

  public void sniperStatusChanged(SniperSnapshot newSnapshot) {
    int row = rowMatching(newSnapshot);
    snapshots.set(row, newSnapshot);
    fireTableRowsUpdated(row, row);
  }

  private int rowMatching(SniperSnapshot newSnapshot) {
    for (int i = 0; i < snapshots.size(); i++) {
      if (newSnapshot.isForSameItemAs(snapshots.get(i))) {
        return i;
      }
    }

    throw new Defect("Cannot find match for " + newSnapshot);
  }

  public void addSniper(SniperSnapshot snapshot) {
    snapshots.add(snapshot);
  }
}
