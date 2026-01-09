import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SetsTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Faculty faculty = new Faculty();

        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String[] tokens = input.split("\\s+");
            String command = tokens[0];

            switch (command) {
                case "addStudent":
                    String id = tokens[1];
                    List<Integer> grades = new ArrayList<>();
                    for (int i = 2; i < tokens.length; i++) {
                        grades.add(Integer.parseInt(tokens[i]));
                    }
                    try {
                        faculty.addStudent(id, grades);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case "addGrade":
                    String studentId = tokens[1];
                    int grade = Integer.parseInt(tokens[2]);
                    faculty.addGrade(studentId, grade);
                    break;

                case "getStudentsSortedByAverageGrade":
                    System.out.println("Sorting students by average grade");
                    Set<Student> sortedByAverage = faculty.getStudentsSortedByAverageGrade();
                    for (Student student : sortedByAverage) {
                        System.out.println(student);
                    }
                    break;

                case "getStudentsSortedByCoursesPassed":
                    System.out.println("Sorting students by courses passed");
                    Set<Student> sortedByCourses = faculty.getStudentsSortedByCoursesPassed();
                    for (Student student : sortedByCourses) {
                        System.out.println(student);
                    }
                    break;

                default:
                    break;
            }
        }

        scanner.close();
    }
}

class Student {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = new ArrayList<>(grades); // Defensive copy
    }

    public String getId() {
        return id;
    }

    public List<Integer> getGrades() {
        return grades;
    }

    @Override
    public String toString(){
        return "Student{id='" + this.id + "', grades=" + this.grades + "}";
    }
}

class Faculty {
    Map<String, List<Integer>> map;

    public Faculty() {
        map = new HashMap<>();
    }

    public void addStudent(String id, List<Integer> grades) throws StudentAlreadyExistsException {
        if (map.containsKey(id))
            throw new StudentAlreadyExistsException("Student with ID " + id + " already exists");
        else {
            map.put(id, new ArrayList<>(grades));
        }
    }

    public void addGrade(String id, int grade){
        if(map.containsKey(id)) {
            map.get(id).add(grade);
        }
    }

    public Set<Student> getStudentsSortedByAverageGrade() {
        Comparator<Student> comparator = (s1, s2) -> {
            double avg1 = s1.getGrades().stream().mapToInt(Integer::intValue).average().orElse(0);
            double avg2 = s2.getGrades().stream().mapToInt(Integer::intValue).average().orElse(0);
            int cmp = Double.compare(avg2, avg1);
            if (cmp != 0) return cmp;
            cmp = Integer.compare(s2.getGrades().size(), s1.getGrades().size());
            if (cmp != 0) return cmp;
            return s2.getId().compareTo(s1.getId());
        };

        TreeSet<Student> sortedStudents = new TreeSet<>(comparator);

        for (Map.Entry<String, List<Integer>> entry : map.entrySet()){
            sortedStudents.add(new Student(entry.getKey(), entry.getValue()));
        }
        return sortedStudents;
    }

    public Set<Student> getStudentsSortedByCoursesPassed() {
        Comparator<Student> comparator = (s1,s2) -> {
            int numGrades1 = s1.grades.size();
            int numGrades2 = s2.grades.size();
            double avg1 = s1.getGrades().stream().mapToInt(Integer::intValue).average().orElse(0);
            double avg2 = s2.getGrades().stream().mapToInt(Integer::intValue).average().orElse(0);
            int cmp = Integer.compare(numGrades2,numGrades1);
            if(cmp != 0) return cmp;
            cmp = Double.compare(avg2,avg1);
            if(cmp != 0) return cmp;
            return s2.id.compareTo(s1.id);
        };

        TreeSet<Student> sortedStudents = new TreeSet<>(comparator);
        for (Map.Entry<String, List<Integer>> mapEntry : map.entrySet()){
            sortedStudents.add(new Student(mapEntry.getKey(),mapEntry.getValue()));
        }
        return sortedStudents;
    }
}

class StudentAlreadyExistsException extends Exception{
    public StudentAlreadyExistsException(String message) {
        super(message);
    }
}
