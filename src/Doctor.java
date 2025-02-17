public class Person {
    private String firstName;
    private String lastName;
    private String ssn;
    private Doctor assignedDoctor;

    public Person(String firstName, String lastName, String ssn) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
    }

    public String getSSN() {
        return ssn;
    }

    public String getFirst() {
        return firstName;
    }

    public String getLast() {
        return lastName;
    }

    public Doctor getDoctor() {
        return assignedDoctor;
    }

    void setDoctor(Doctor doctor) {
        this.assignedDoctor = doctor;
    }
}
