public class Employee {
    private String nip;
    private String name;
    private String birthDate;

    public Employee(String nip, String name, String birthDate) {
        this.nip = nip;
        this.name = name;
        this.birthDate = birthDate;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String toLine() {
        return nip + "|" + name + "|" + birthDate;
    }

    public static Employee fromLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) {
            return null;
        }
        return new Employee(parts[0].trim(), parts[1].trim(), parts[2].trim());
    }
}
