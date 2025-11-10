import java.util.Scanner;
import java.util.function.Predicate;

/**
 * I partial exam 2016
 */
public class ApplicantEvaluationTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        int creditScore = scanner.nextInt();
        int employmentYears = scanner.nextInt();
        boolean hasCriminalRecord = scanner.nextBoolean();
        int choice = scanner.nextInt();
        Applicant applicant = new Applicant(name, creditScore, employmentYears, hasCriminalRecord);
        Evaluator2.TYPE type = Evaluator2.TYPE.values()[choice];
        Evaluator2 evaluator = null;
        try {
            evaluator = EvaluatorBuilder2.build(type);
            System.out.println("Applicant");
            System.out.println(applicant);
            System.out.println("Evaluation type: " + type.name());
            if (evaluator.evaluate(applicant)) {
                System.out.println("Applicant is ACCEPTED");
            } else {
                System.out.println("Applicant is REJECTED");
            }
        } catch (InvalidEvaluation invalidEvaluation) {
            System.out.println("Invalid evaluation");
        }
    }
}

class Applicant {
    private String name;

    private int creditScore;
    private int employmentYears;
    private boolean hasCriminalRecord;

    public Applicant(String name, int creditScore, int employmentYears, boolean hasCriminalRecord) {
        this.name = name;
        this.creditScore = creditScore;
        this.employmentYears = employmentYears;
        this.hasCriminalRecord = hasCriminalRecord;
    }

    public String getName() {
        return name;
    }

    public int getCreditScore() {
        return creditScore;
    }

    public int getEmploymentYears() {
        return employmentYears;
    }

    public boolean hasCriminalRecord() {
        return hasCriminalRecord;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nCredit score: %d\nExperience: %d\nCriminal record: %s\n",
                name, creditScore, employmentYears, hasCriminalRecord ? "Yes" : "No");
    }
}

interface Evaluator2 {
    enum TYPE {
        NO_CRIMINAL_RECORD,
        MORE_EXPERIENCE,
        MORE_CREDIT_SCORE,
        NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE,
        MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE,
        NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE,
        INVALID // should throw exception
    }

    boolean evaluate(Applicant applicant);
}

class EvaluatorBuilder2 {
    static Predicate<Applicant> noCriminalRecord = a -> !a.hasCriminalRecord();
    static Predicate<Applicant> moreExperience = a -> a.getEmploymentYears() >= 10;
    static Predicate<Applicant> moreCreditScore = a -> a.getCreditScore() >= 500;


    public static Evaluator2 build(Evaluator2.TYPE type) throws InvalidEvaluation {
        // вашиот код овде
        Predicate<Applicant> predicate;
        switch (type) {
            case NO_CRIMINAL_RECORD:
                predicate = noCriminalRecord;
                break;
            case MORE_EXPERIENCE:
                predicate = moreExperience;
                break;
            case MORE_CREDIT_SCORE:
                predicate = moreCreditScore;
                break;
            case NO_CRIMINAL_RECORD_AND_MORE_EXPERIENCE:
                predicate = noCriminalRecord.and(moreExperience);
                break;
            case MORE_EXPERIENCE_AND_MORE_CREDIT_SCORE:
                predicate = moreExperience.and(moreCreditScore);
                break;
            case NO_CRIMINAL_RECORD_AND_MORE_CREDIT_SCORE:
                predicate = noCriminalRecord.and(moreCreditScore);
                break;
            default:
                throw new InvalidEvaluation("Invalid type");
        }
        return predicate::test;
    }
}

class InvalidEvaluation extends Exception {
    public InvalidEvaluation(String message) {
        super(message);
    }
}

// имплементација на евалуатори овде


