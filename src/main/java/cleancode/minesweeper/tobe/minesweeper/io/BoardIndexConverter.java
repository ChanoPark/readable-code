package cleancode.minesweeper.tobe.minesweeper.io;

import cleancode.minesweeper.tobe.minesweeper.exception.GameException;

public class BoardIndexConverter {
    private static final char BASE_CHAR_FOR_COL = 'a';

    public int getSelectedColumnIndex(String cellInput) {
        char cellInputColumn = cellInput.charAt(0);
        return convertColFrom(cellInputColumn);
    }

    public int getSelectedRowIndex(String cellInput) {
        String cellInputRow = cellInput.substring(1);
        return convertRowFrom(cellInputRow);
    }

    private int convertRowFrom(String cellInputRow) {
        int rowIndex = Integer.parseInt(cellInputRow) - 1;

        if (rowIndex < 0) {
            throw new GameException("잘못된 입력입니다.");
        }

        return rowIndex;
    }

    private int convertColFrom(char cellInputColumn) {
        int colIndex = cellInputColumn - BASE_CHAR_FOR_COL;

        if (colIndex < 0)
            throw new GameException("잘못된 입력입니다.");

        return colIndex;
    }
}
