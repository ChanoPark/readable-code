package cleancode.studycafe.tobe.repository.locker;

import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.model.StudyCafeLockerPass;

import java.util.List;

public class LockerPassFileRepository {
    private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

    public List<StudyCafeLockerPass> getStudyCafeLockerPasses() {
        return studyCafeFileHandler.readLockerPasses();
    }
}