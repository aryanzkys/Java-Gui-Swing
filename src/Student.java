public class Student {
    private String nis;
    private String name;
    private String address;

    public Student(String nis, String name, String address) {
        this.nis = nis;
        this.name = name;
        this.address = address;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toLine() {
        return nis + "|" + name + "|" + address;
    }

    public static Student fromLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) {
            return null;
        }
        return new Student(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }
}
