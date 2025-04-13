package cleancode.minesweeper.tobe.minesweeper.board.cell;

public interface Cell {

    boolean isLandMine();
    boolean hasLandMineCount();
    CellSnapshot getSnapshot();
    boolean isChecked();
    boolean isOpened();
    void open();
    void flag();
}
