package cleancode.minesweeper.tobe.minesweeper.board;

import cleancode.minesweeper.tobe.minesweeper.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPosition;
import cleancode.minesweeper.tobe.minesweeper.board.position.CellPositions;
import cleancode.minesweeper.tobe.minesweeper.board.position.RelativePosition;
import cleancode.minesweeper.tobe.minesweeper.board.cell.*;

import java.util.List;
import java.util.stream.Collectors;

public class GameBoard {
    private final Cell[][] board;
    private final int rowSize;
    private final int colSize;
    private final int landMineCount;
    private GameStatus gameStatus;


    public GameBoard(GameLevel gameLevel) {
        this.rowSize = gameLevel.getRowSize();
        this.colSize = gameLevel.getColSize();
        this.landMineCount = gameLevel.getLandMineCount();
        this.board = new Cell[this.rowSize][this.colSize];
        initializeGameStatus();
    }

    public void initializeGame() {
        initializeGameStatus();

        CellPositions cellPositions = CellPositions.from(board);

        initializeEmptyCells(cellPositions);

        List<CellPosition> landMinePositions = cellPositions.extractRandomPositions(landMineCount);
        initializeLandMineCells(landMinePositions);

        List<CellPosition> numberPositionCandidates = cellPositions.subtract(landMinePositions);
        initializeNumberCells(numberPositionCandidates);
    }

    public void openAt(CellPosition cellPosition) {
        if (isLandMineCell(cellPosition)) {
            openOneCellAt(cellPosition);
            changeGameStatusToLose();
            return;
        }

        openSurroundedCells(cellPosition);
        checkIfGameIsOver();
    }

    public void flagAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        cell.flag();

        checkIfGameIsOver();
    }

    public boolean isInvalidCellPosition(CellPosition cellPosition) {
        return cellPosition.isRowIndexMoreThanOrEqual(rowSize)
                || cellPosition.isColIndexMoreThanOrEqual(colSize);
    }

    public boolean isInProgress() {
        return gameStatus == GameStatus.IN_PROGRESS;
    }

    public boolean isWinStatus() {
        return gameStatus == GameStatus.WIN;
    }

    public boolean isLoseStatus() {
        return gameStatus == GameStatus.LOSE;
    }

    public CellSnapshot getSnapshot(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.getSnapshot();
    }

    public int getRowSize() {
        return this.rowSize;
    }

    public int getColSize() {
        return this.colSize;
    }


    private void openOneCellAt(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        cell.open();
    }

    private void openSurroundedCells(CellPosition cellPosition) {
        if (isOpenedCell(cellPosition)) {
            return;
        }
        if (isLandMineCell(cellPosition)) {
            return;
        }

        openOneCellAt(cellPosition);

        if (doesCellHaveLandMineCount(cellPosition)) {
            return;
        }

        calculateSurroundedPositions(cellPosition).forEach(this::openSurroundedCells);
    }

    private boolean isLandMineCell(CellPosition cellPosition) {
        return board[cellPosition.getRowIndex()][cellPosition.getColIndex()].isLandMine();
    }

    private boolean isAllCellChecked() {
        Cells cells = Cells.from(board);
        return cells.isAllChecked();
    }

    private boolean isOpenedCell(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.isOpened();
    }

    private void checkIfGameIsOver() {
        if (isAllCellChecked()) {
            changeGameStatusToWin();
        }
    }

    private boolean doesCellHaveLandMineCount(CellPosition cellPosition) {
        Cell cell = findCell(cellPosition);
        return cell.hasLandMineCount();
    }

    private int countNearbyLandMines(CellPosition cellPosition) {
        long count = calculateSurroundedPositions(cellPosition).stream()
                .filter(this::isLandMineCell)
                .count();
        return (int) count;
    }

    private List<CellPosition> calculateSurroundedPositions(CellPosition cellPosition) {
        return RelativePosition.SURROUND_POSITIONS.stream()
                .filter(cellPosition::canCalculatePositionBy)
                .map(cellPosition::calculatePositionBy)
                .filter(position -> position.isRowIndexLessThanOrEqual(rowSize))
                .filter(position -> position.isColIndexLessThanOrEqual(colSize))
                .collect(Collectors.toList()); // 가독성을 위해 List로 반환
    }

    private void initializeEmptyCells(CellPositions cellPositions) {
        List<CellPosition> allPositions = cellPositions.getAllPositions();
        for (CellPosition position : allPositions) {
            updateCellAt(position, new EmptyCell());
        }
    }

    private void initializeLandMineCells(List<CellPosition> landMinePositions) {
        for (CellPosition position : landMinePositions) {
            updateCellAt(position, new LandMineCell());
        }
    }

    private void initializeNumberCells(List<CellPosition> numberPositionCandidates) {
        for (CellPosition candidatePosition : numberPositionCandidates) {
            int count = countNearbyLandMines(candidatePosition);

            if (count != 0) {
                updateCellAt(candidatePosition, new NumberCell(count));
            }
        }
    }

    private void updateCellAt(CellPosition position, Cell cell) {
        board[position.getRowIndex()][position.getColIndex()] = cell;
    }

    private void initializeGameStatus() {
        gameStatus = GameStatus.IN_PROGRESS;
    }

    private void changeGameStatusToWin() {
        gameStatus = GameStatus.WIN;
    }

    private void changeGameStatusToLose() {
        gameStatus = GameStatus.LOSE;
    }

    private Cell findCell(CellPosition cellPosition) {
        return board[cellPosition.getRowIndex()][cellPosition.getColIndex()];
    }
}
