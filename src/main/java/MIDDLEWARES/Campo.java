package MIDDLEWARES;

public class Campo {
    private String name;
    private boolean required;
    private int minimum;
    private int maximum;
    private String type; // "string", "numero", "booleano"

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
