package MIDDLEWARES;

public class Campo {
    private final String name;
    private final boolean required;
    private final int minimum;
    private final int maximum;
    private final String type; // "string", "numero", "booleano"

    public Campo(String name, boolean required, int minimum, int maximum, String type) {
        this.name = name;
        this.required = required;
        this.minimum = minimum;
        this.maximum = maximum;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public String getType() {
        return type;
    }
}
