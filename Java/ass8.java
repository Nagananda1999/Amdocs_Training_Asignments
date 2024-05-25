import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

class Employee {
    private String name;
    private int age;

    public Employee(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void printDetails() {
        System.out.println("Name: " + name + ", Age: " + age);
    }
}

class EmployeeTask extends RecursiveAction {
    private List<Employee> employees;

    public EmployeeTask(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    protected void compute() {
        if (employees.size() <= 1) {
            for (Employee employee : employees) {
                employee.printDetails();
            }
        } else {
            int mid = employees.size() / 2;
            List<Employee> leftEmployees = employees.subList(0, mid);
            List<Employee> rightEmployees = employees.subList(mid, employees.size());

            EmployeeTask leftTask = new EmployeeTask(leftEmployees);
            EmployeeTask rightTask = new EmployeeTask(rightEmployees);

            invokeAll(leftTask, rightTask);
        }
    }
}

public class ass8 {
    public static void main(String[] args) {
        // Create a list of employees
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("John", 25));
        employees.add(new Employee("Alice", 30));
        employees.add(new Employee("Bob", 28));
        employees.add(new Employee("Eva", 35));
        employees.add(new Employee("Mike", 32));

        // Create a ForkJoinPool with the default parallelism level
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        // Create the main task with the list of employees
        EmployeeTask mainTask = new EmployeeTask(employees);

        // Execute the main task using the ForkJoinPool
        forkJoinPool.invoke(mainTask);
    }
}
/*
Output:
Name: Mike, Age: 32
Name: Bob, Age: 28
Name: John, Age: 25
Name: Eva, Age: 35
Name: Alice, Age: 30
Press any key to continue . . .
*/