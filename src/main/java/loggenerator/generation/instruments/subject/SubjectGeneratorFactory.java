package loggenerator.generation.instruments.subject;

import java.util.List;

public class SubjectGeneratorFactory {
    public static SubjectGenerator getSubjectGenerator(String type, List<String> subjects) {
        switch(type) {
            case "simple":
                return new SimpleSubjectGenerator(subjects);
        }
        return null;
    }
}
