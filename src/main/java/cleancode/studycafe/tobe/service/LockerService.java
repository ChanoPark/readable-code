package cleancode.studycafe.tobe.service;

import cleancode.studycafe.tobe.io.InputHandler;
import cleancode.studycafe.tobe.io.OutputHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.repository.locker.LockerPassFileRepository;

import java.util.List;

public class LockerService {
    private final InputHandler inputHandler = new InputHandler();
    private final OutputHandler outputHandler = new OutputHandler();
    private final LockerPassFileRepository lockerPassFileRepository = new LockerPassFileRepository();

    public StudyCafeLockerPass getStudyCafeLockerPass(StudyCafePass selectedPass) {
        StudyCafeLockerPass lockerPass = getLockerPassFrom(selectedPass);

        if (isLockerPass(lockerPass)) {
            boolean lockerSelection = getLockerSelection(lockerPass);

            if (lockerSelection) {
                return lockerPass;
            }
        }

        return null;
    }

    private static boolean isLockerPass(StudyCafeLockerPass lockerPass) {
        return lockerPass != null;
    }

    public List<StudyCafeLockerPass> getStudyCafeLockerPasses() {
        return lockerPassFileRepository.getStudyCafeLockerPasses();
    }

    public StudyCafeLockerPass getLockerPassFrom(StudyCafePass studyCafePass) {
        List<StudyCafeLockerPass> lockerPasses = getStudyCafeLockerPasses();

        return lockerPasses.stream()
                .filter(option ->
                        studyCafePass.isEqualPassType(option.getPassType())
                                && studyCafePass.isEqualDuration(option.getDuration())
                )
                .findFirst()
                .orElse(null);
    }

    public boolean getLockerSelection(StudyCafeLockerPass lockerPass) {
        outputHandler.askLockerPass(lockerPass);
        return inputHandler.getLockerSelection();
    }
}
