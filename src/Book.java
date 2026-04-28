public class Book {
    private String code;
    private String title;
    private String type;

    public Book(String code, String title, String type) {
        this.code = code;
        this.title = title;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toLine() {
        return code + "|" + title + "|" + type;
    }

    public static Book fromLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) {
            return null;
        }
        return new Book(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }
}
