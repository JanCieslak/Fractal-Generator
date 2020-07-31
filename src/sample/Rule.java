package sample;

public class Rule {
    private String from;
    private String to;

    public Rule(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String apply(String text) {
        return text.replace(from, to);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
