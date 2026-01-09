import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// enum namesto tie 0,1 vrednostite
enum QuestionType {
    TRUEFALSE,
    FREEFORM
}

class TriviaQuestion {
    //site polinja private i getteri implementirani
    private final String question;
    private final String answer;
    private final int value;
    private final QuestionType type;
    //kreiranje na question direktno tuka a ne podolu
    public TriviaQuestion(String question, String answer, int value, QuestionType type) {
        this.question = question;
        this.answer = answer;
        this.value = value;
        this.type = type;
    }
    //implementacija na metodite za odreden question direktno vo Question klasata
    public void showQuestion(int index) {
        System.out.println("Question " + (index + 1) + ".  " + value + " points.");
        System.out.println(question);

        if (type == QuestionType.TRUEFALSE) {
            System.out.println("Enter 'T' for true or 'F' for false.");
        }
    }

    public boolean isCorrect(String userAnswer) {
        if (type == QuestionType.TRUEFALSE) {
            return userAnswer.charAt(0) == answer.charAt(0);
        }
        return userAnswer.equalsIgnoreCase(answer);
    }

    public int getValue() {
        return value;
    }

    public String getAnswer() {
        return answer;
    }
}

class TriviaData {

    private final List<TriviaQuestion> questions = new ArrayList<>();
    //small change direktno dodavanje na nov question bez kreiranje na promenliva,
    //mozhe da se implementira i celosen question namesto ova, so toa shto ke se kreira
    // question dolu
    public void addQuestion(String q, String a, int v, QuestionType t) {
        questions.add(new TriviaQuestion(q, a, v, t));
    }

    public int numQuestions() {
        return questions.size();
    }

    public TriviaQuestion getQuestion(int index) {
        return questions.get(index);
    }

}

public class TriviaGame {
    //private ne public, mozhe da e final? Intellij sugestija
    private final TriviaData questions = new TriviaData();

    public TriviaGame() {
        questions.addQuestion(
                "The possession of more than two sets of chromosomes is termed?",
                "polyploidy", 3, QuestionType.FREEFORM);

        questions.addQuestion(
                "Erling Kagge skiied into the north pole alone on January 7, 1993.",
                "F", 1, QuestionType.TRUEFALSE);

        questions.addQuestion(
                "1997 British band that produced 'Tub Thumper'",
                "Chumbawumba", 2, QuestionType.FREEFORM);

        questions.addQuestion(
                "I am the geometric figure most like a lost parrot",
                "polygon", 2, QuestionType.FREEFORM);

        questions.addQuestion(
                "Generics were introducted to Java starting at version 5.0.",
                "T", 1, QuestionType.TRUEFALSE);
    }

    public static void main(String[] args) {

        TriviaGame game = new TriviaGame();
        Scanner sc = new Scanner(System.in);

        int score = 0;
        int questionNum = 0;
        //simplificiran main so toa shto celata logika za proveruvanje na
        //tochnosta na odgovorot e vo TriviaQuestion klasata
        while (questionNum < game.questions.numQuestions()) {

            TriviaQuestion q = game.questions.getQuestion(questionNum);
            q.showQuestion(questionNum);

            String answer = sc.nextLine();

            if (q.isCorrect(answer)) {
                System.out.println("That is correct!  You get " + q.getValue() + " points.");
                score += q.getValue();
            } else {
                System.out.println("Wrong, the correct answer is " + q.getAnswer());
            }

            System.out.println("Your score is " + score);
            questionNum++;
        }

        System.out.println("Game over!  Thanks for playing!");
    }
}
