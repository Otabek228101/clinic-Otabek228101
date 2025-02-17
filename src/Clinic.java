import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Clinic {
    private Map<String, Person> patients = new HashMap<>();
    private Map<Integer, Doctor> doctors = new HashMap<>();

    public void addPatient(String first, String last, String ssn) {
        Person patient = new Person(first, last, ssn);
        patients.put(ssn, patient);
    }

    public void addDoctor(String first, String last, String ssn, int docID, String specialization) {
        Doctor doctor = new Doctor(first, last, ssn, docID, specialization);
        doctors.put(docID, doctor);
    }

    public Person getPatient(String ssn) throws NoSuchPatient {
        Person patient = patients.get(ssn);
        if (patient == null) {
            throw new NoSuchPatient();
        }
        return patient;
    }

    public Doctor getDoctor(int docID) throws NoSuchDoctor {
        Doctor doctor = doctors.get(docID);
        if (doctor == null) {
            throw new NoSuchDoctor();
        }
        return doctor;
    }

    public void assignPatientToDoctor(String ssn, int docID) throws NoSuchPatient, NoSuchDoctor {
        Person patient = getPatient(ssn);
        Doctor doctor = getDoctor(docID);
        
        patient.setDoctor(doctor);
        doctor.addPatient(patient);
    }

    public Collection<Doctor> idleDoctors() {
        return doctors.values().stream()
            .filter(d -> d.getPatients().isEmpty())
            .sorted(Comparator.comparing(Person::getFirst))
            .collect(Collectors.toList());
    }

    public Collection<Doctor> busyDoctors() {
        double avgPatients = doctors.values().stream()
            .mapToDouble(d -> d.getPatients().size())
            .average()
            .orElse(0.0);

        return doctors.values().stream()
            .filter(d -> d.getPatients().size() > avgPatients)
            .sorted(Comparator.comparing(Person::getFirst))
            .collect(Collectors.toList());
    }

    public Collection<String> doctorsByNumPatients() {
        return doctors.values().stream()
            .map(d -> String.format("%3d: %s %s", 
                d.getPatients().size(), d.getFirst(), d.getLast()))
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
    }

    public Collection<String> countPatientsPerSpecialization() {
        Map<String, Long> countBySpec = doctors.values().stream()
            .collect(Collectors.groupingBy(
                Doctor::getSpecialization,
                Collectors.summingLong(d -> d.getPatients().size())
            ));

        return countBySpec.entrySet().stream()
            .map(e -> String.format("%3d - %s", e.getValue(), e.getKey()))
            .sorted(Comparator.<String>comparingInt(s -> -Integer.parseInt(s.substring(0, 3).trim()))
                .thenComparing(s -> s.substring(5)))
            .collect(Collectors.toList());
    }

    public void loadData(String path) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split(";");
                    if (parts[0].equals("P")) {
                        addPatient(parts[1], parts[2], parts[3]);
                    } else if (parts[0].equals("M")) {
                        addDoctor(parts[2], parts[3], parts[4], 
                            Integer.parseInt(parts[1]), parts[5]);
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }
}