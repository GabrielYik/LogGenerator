package loggenerator.generation.instruments.subject;

import loggenerator.generation.instruments.RandomMachine;

import java.util.List;

public class SimpleSubjectGenerator extends RandomMachine implements SubjectGenerator {
    private List<String> subjects;

    public SimpleSubjectGenerator(List<String> subjects) {
        super();
        this.subjects = subjects;
    }

    @Override
    public String generate() {
        int randomIndex = rng.nextInt(subjects.size());
        return subjects.get(randomIndex);
    }
}
