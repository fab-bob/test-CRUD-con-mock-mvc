package co.develhope.crudTest.services;

import co.develhope.crudTest.entities.Student;
import co.develhope.crudTest.repositories.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepo studentRepo;

    public boolean studentStatus(Student student){
        if(student.isWorking()){
            return true;
        }
        return false;
    }

    public Student create (Student student){
        return studentRepo.save((student));
    }

    public List<Student> read (){
        return studentRepo.findAll();
    }

    public Optional<Student> readOne (Long id){
        return studentRepo.findById(id);
    }

    public Student update(Long id, Student studentDetails) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student with id" + id + "not found"));
        student.setName(studentDetails.getName());
        student.setSurname(studentDetails.getSurname());
        return studentRepo.save(student);
    }

    public Student updateWorkingStatus(Long id, boolean isWorking) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id " + id));
        student.setWorking(isWorking);
        return studentRepo.save(student);
    }

    public void delete(Long id) {
        studentRepo.deleteById(id);
    }
}
