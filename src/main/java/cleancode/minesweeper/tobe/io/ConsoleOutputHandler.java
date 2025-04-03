package cleancode.minesweeper.tobe.io;

import cleancode.minesweeper.tobe.GameBoard;
import cleancode.minesweeper.tobe.GameException;
import cleancode.minesweeper.tobe.cell.CellSnapshot;
import cleancode.minesweeper.tobe.cell.CellSnapshotStatus;
import cleancode.minesweeper.tobe.position.CellPosition;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConsoleOutputHandler implements OutputHandler {

    private static final String EMPTY_SIGN = "■";
    private static final String LAND_MINE_SIGN = "☼";
    private static final String FLAG_SIGN = "⚑";
    private static final String UNCHECKED_SIGN = "□";

    @Override
    public void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void showBoard(GameBoard gameBoard) {
        List<String> alphabets = IntStream.range(0, gameBoard.getColSize())
                .mapToObj(index -> (char) ('a' + index))
                .map(Object::toString)
                .collect(Collectors.toList());
        String joiningAlphabets = String.join(" ", alphabets);

        System.out.println("    " + joiningAlphabets);
        for (int row = 0; row < gameBoard.getRowSize(); row++) {
            System.out.printf("%2d  ", row + 1);
            for (int col = 0; col < gameBoard.getColSize(); col++) {
                CellPosition cellPosition = CellPosition.of(row, col);
                CellSnapshot snapshot = gameBoard.getSnapshot(cellPosition);
                String cellSign = decideCellSignFrom(snapshot);
                System.out.print(cellSign + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void showGameWinningComment() {
        System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
    }

    @Override
    public void showGameLosingComment() {
        System.out.println("지뢰를 밟았습니다. GAME OVER!");
    }

    @Override
    public void showCommentForSelectingCell() {
        System.out.println("\n선택할 좌표를 입력하세요. (예: a1)");
    }

    @Override
    public void showCommentForUserAction() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
    }

    @Override
    public void showExceptionMessage() {
        System.out.println("프로그램에 문제가 생겼습니다.");
    }

    @Override
    public void showExceptionMessage(GameException e) {
        System.out.println(e.getMessage());
    }

    private String decideCellSignFrom(CellSnapshot snapshot) {
        CellSnapshotStatus status = snapshot.getStatus();
        if (status== CellSnapshotStatus.EMPTY) {
            return EMPTY_SIGN;
        }

        if (status == CellSnapshotStatus.FLAG) {
            return FLAG_SIGN;
        }

        if (status == CellSnapshotStatus.LAND_MINE) {
            return LAND_MINE_SIGN;
        }

        if (status == CellSnapshotStatus.NUMBER) {
            return String.valueOf(snapshot.getNearbyLandMineCount());
        }

        if (status == CellSnapshotStatus.UNCHECKED) {
            return UNCHECKED_SIGN;
        }

        throw new IllegalArgumentException("확인할 수 없는 셀입니다.");
    }
}