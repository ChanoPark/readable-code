package cleancode.studycafe.tobe.service;

import cleancode.studycafe.tobe.io.InputHandler;
import cleancode.studycafe.tobe.io.OutputHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.repository.locker.LockerPassFileRepository;

import java.util.List;
import java.util.Optional;

public class LockerService {
    private final InputHandler inputHandler = new InputHandler();
    private final OutputHandler outputHandler = new OutputHandler();
    private final LockerPassFileRepository lockerPassFileRepository = new LockerPassFileRepository();

    public Optional<StudyCafeLockerPass> getStudyCafeLockerPass(StudyCafePass selectedPass) {
        if (selectedPass.isNotLockerType()) {
            return Optional.empty();
        }

        StudyCafeLockerPass lockerPassCandidate = getLockerPassFrom(selectedPass);

        if (isLockerPass(lockerPassCandidate)) {
            boolean lockerSelection = getLockerSelection(lockerPassCandidate);

            if (lockerSelection) {
                return Optional.of(lockerPassCandidate);
            }
        }

        return Optional.empty();
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
                .filter(lockerPass ->
                        lockerPass.isEqualPassType(studyCafePass.getPassType())
                     && lockerPass.isEqualDuration(studyCafePass.getDuration())
                )
                .findFirst()
                .orElse(null);
    }

    public boolean getLockerSelection(StudyCafeLockerPass lockerPass) {
        outputHandler.askLockerPass(lockerPass);
        return inputHandler.getLockerSelection();
    }
}
