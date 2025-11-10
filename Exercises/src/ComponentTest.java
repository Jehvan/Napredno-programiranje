import java.util.*;

public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if (what == 4) {
                    break;
                }
            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.print(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.print(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        try {
            window.swichComponents(pos1, pos2);
        } catch (InvalidPositionException e) {}
        System.out.print(window);
    }
}

class Component {
    String color;
    int weight;
    List<Component> components = new ArrayList<>();

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
    }

    void addComponent(Component component) {
        components.add(component);
        components.sort(Comparator.comparingInt((Component c) -> c.weight)
                .thenComparing(c -> c.color));
    }

    void changeColor(int weight, String color) {
        for (Component component : components) {
            if (component == null) continue;
            if (component.weight < weight) {
                component.color = color;
                component.changeColor(weight, color);
            }
            component.changeColor(weight, color);
        }
    }

    String toStringHelper(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append(weight).append(":").append(color).append("\n");
        for (Component component : components) {
            if (component != null) {
                sb.append(component.toStringHelper(indent + "---"));
            }
        }
        return sb.toString();
    }
}

class Window {
    String name;
    List<Component> components = new ArrayList<>();

    public Window(String name) {
        this.name = name;
    }

    void addComponent(int position, Component component) throws InvalidPositionException {
        if (position < 0) throw new InvalidPositionException("Invalid position");
        while (components.size() <= position) components.add(null);
        if (components.get(position) == null) {
            components.set(position, component);
        } else {
            throw new InvalidPositionException("Invalid position " + position + ", alredy taken!");
        }
    }

    void changeColor(int weight, String color) {
        for (Component component : components) {
            if (component == null) continue;
            if (component.weight < weight) {
                component.color = color;
            }
            component.changeColor(weight, color);
        }
    }

    void swichComponents(int pos1, int pos2) throws InvalidPositionException {
        if (pos1 >= components.size() || pos2 >= components.size() || pos1 < 0 || pos2 < 0) {
            throw new InvalidPositionException("Invalid position!");
        }
        Component tmp = components.get(pos1);
        components.set(pos1, components.get(pos2));
        components.set(pos2, tmp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WINDOW ").append(this.name).append("\n");
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            if (component == null) continue;
            // Position number + weight + color
            sb.append(i).append(":").append(component.weight).append(":").append(component.color).append("\n");
            // Nested components
            for (Component nested : component.components) {
                sb.append(nested.toStringHelper("---"));
            }
        }
        sb.append("\n"); // Single blank line after the entire list
        return sb.toString();
    }

}

class InvalidPositionException extends Exception {
    public InvalidPositionException(String message) {
        super(message);
    }
}
