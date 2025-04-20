package cleancode.studycafe.tobe;

import cleancode.studycafe.tobe.exception.AppException;
import cleancode.studycafe.tobe.io.InputHandler;
import cleancode.studycafe.tobe.io.OutputHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;
import cleancode.studycafe.tobe.service.LockerService;
import cleancode.studycafe.tobe.service.StudyCafePassService;

import java.util.List;

public class StudyCafePassMachine {

    private final InputHandler inputHandler = new InputHandler();
    private final OutputHandler outputHandler = new OutputHandler();
    private final StudyCafePassService studyCafePassService = new StudyCafePassService();
    private final LockerService lockerService = new LockerService();

    public void run() {
        try {
            outputHandler.showWelcomeMessage();
            outputHandler.showAnnouncement();

            StudyCafePassType studyCafePassType = getStudyCafePassTypeFromUser();

            List<StudyCafePass> passes = studyCafePassService.getPassesFrom(studyCafePassType);
            StudyCafePass selectedPass = getStudyCafePassFromUser(passes);

            StudyCafeLockerPass lockerPass = lockerService.getStudyCafeLockerPass(selectedPass);

            outputHandler.showPassOrderSummary(selectedPass, lockerPass);
        } catch (AppException e) {
            outputHandler.showSimpleMessage(e.getMessage());
        } catch (Exception e) {
            outputHandler.showSimpleMessage("알 수 없는 오류가 발생했습니다.");
        }
    }

    private StudyCafePassType getStudyCafePassTypeFromUser() {
        outputHandler.askPassTypeSelection();
        return inputHandler.getPassTypeSelectingUserAction();
    }

    private StudyCafePass getStudyCafePassFromUser(List<StudyCafePass> hourlyPasses) {
        outputHandler.showPassListForSelection(hourlyPasses);

        return inputHandler.getSelectPass(hourlyPasses);
    }
}
