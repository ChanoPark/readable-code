package cleancode.studycafe.tobe.service;

import cleancode.studycafe.tobe.io.StudyCafeFileHandler;
import cleancode.studycafe.tobe.model.StudyCafePass;
import cleancode.studycafe.tobe.model.StudyCafePassType;

import java.util.List;
import java.util.stream.Collectors;

public class StudyCafePassService {

    private final StudyCafeFileHandler studyCafeFileHandler = new StudyCafeFileHandler();

    public List<StudyCafePass> getPassesFrom(StudyCafePassType studyCafePassType) {
        List<StudyCafePass> studyCafePasses = getStudyCafeAllPasses();

        return studyCafePasses.stream()
                .filter(studyCafePass -> studyCafePass.getPassType() == studyCafePassType)
                .collect(Collectors.toList());
    }

    public List<StudyCafePass> getStudyCafeAllPasses() {
        return studyCafeFileHandler.readStudyCafePasses();
    }
}
