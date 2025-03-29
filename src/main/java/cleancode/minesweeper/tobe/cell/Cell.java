package cleancode.minesweeper.tobe.cell;

public interface Cell {
    String FLAG_SIGN = "⚑";
    String UNCHECKED_SIGN = "□";

    boolean isLandMine();
    boolean hasLandMineCount();
    String getSign();

    boolean isChecked();
    boolean isOpened();
    void open();
    void flag();
}
