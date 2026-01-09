import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i = 5; i <= 10; i++) {
            levels.add("level" + i);
        }
        Map<String, Collection<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            if (!employees.isEmpty()) {
                System.out.println("LEVEL: " + level);
                System.out.println("Employees: ");
                employees.forEach(System.out::println);
                System.out.println("------------");
            }
        });

    }
}

class EmployeeFactory {
    public static Employee createEmployee(String line, Map<String, Double> hourlyRate, Map<String, Double> ticketRate) {
        String[] tokens = line.split(";");
        String id = tokens[1];
        String level = tokens[2];
        List<Integer> list = new ArrayList<>();
        if (tokens[0].equals("F")) {
            list = Arrays.stream(tokens).skip(3).map(Integer::parseInt).collect(Collectors.toList());
            return new FreelanceEmployee(id, level, ticketRate.get(level), list);
        } else {
            double hours = Double.parseDouble(tokens[tokens.length - 1]);
            return new HourlyEmployee(id, level, hourlyRate.get(level), hours);
        }
    }
}

class PayrollSystem {
    Map<String, Double> hourlyRateByLevel;
    Map<String, Double> ticketRateByLevel;
    List<Employee> employees;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        employees = new ArrayList<>();
    }

    void readEmployees(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        this.employees = br.lines().map(line -> EmployeeFactory.createEmployee(line, hourlyRateByLevel, ticketRateByLevel)).collect(Collectors.toList());
    }

    public Map<String, Collection<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels) {
        Map<String, Collection<Employee>> tmp = new HashMap<>();
        for (String s : levels) {
            tmp.put(s, employees.stream().filter(e -> e.level.equals(s)).sorted(Comparator.comparing(Employee::calculate).thenComparing(Employee::getId).reversed()).collect(Collectors.toList()));
        }
        BufferedWriter bw = new BufferedWriter(new PrintWriter(os));
        for (String s : tmp.keySet()){
            List<Employee> employees = (List<Employee>) tmp.get(s);
            employees.forEach(e -> {
                try {
                    bw.write(e.toString());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
        return tmp;
    }
}



abstract class Employee {
    String id;
    String level;
    double rate;

    public Employee(String id, String level, double rate) {
        this.id = id;
        this.level = level;
        this.rate = rate;
    }

    abstract double calculate();

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public double getRate() {
        return rate;
    }
    @Override
    public String toString(){
        return "Employee ID: " + this.id + " Level: " + this.level + " Salary: " + String.format("%.2f",this.calculate());
    }
}

class HourlyEmployee extends Employee {
    double hours;
    double overtime;
    double regular;

    public HourlyEmployee(String id, String level, double rate, double hours) {
        super(id, level, rate);
        this.hours = hours;
        this.overtime = Math.max(0, hours - 40);
        this.regular = hours - overtime;
    }

    @Override
    double calculate() {
        return regular * rate + overtime * rate * 1.5;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" Regular hours: %.2f Overtime hours: %.2f", regular, overtime);
    }
}

class FreelanceEmployee extends Employee {
    List<Integer> tickets;

    public FreelanceEmployee(String id, String level, double rate, List<Integer> list) {
        super(id, level, rate);
        this.tickets = list;
    }

    @Override
    double calculate() {
        return tickets.stream().mapToInt(t -> t).sum() * rate;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(
                " Tickets count: %d Tickets points: %d",
                tickets.size(),
                tickets.stream().mapToInt(i -> i).sum()
        );
    }
}