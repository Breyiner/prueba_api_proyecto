package CONTROLLER;

public class Campo {
    private final String name;
    private final boolean required;
    private final int minimum;
    private final int maximum;
    private final String type; // "string", "numero", "booleano"
    private final String regExp;

    public Campo(String name, boolean required, int minimum, int maximum, String type, String regExp) {
        this.name = name;
        this.required = required;
        this.minimum = minimum;
        this.maximum = maximum;
        this.type = type;
        this.regExp = regExp;
    }

    public String getRegExp() {
        return regExp;
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
