package co.develhope.crudTest.controllers;

import co.develhope.crudTest.entities.Student;
import co.develhope.crudTest.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @PostMapping
    public Student createStudent (@RequestBody Student student){
        return studentService.create(student);
    }

    @GetMapping
    public List<Student> showStudents (){
        return studentService.read();
    }

    @GetMapping("/{id}")
    public Optional<Student> showASingleStudent (@PathVariable Long id){
        return studentService.readOne(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.update(id, student));
    }

    @PatchMapping("/{id}/working")
    public ResponseEntity<Student> updateStudentWorkingStatus (@PathVariable Long id, @RequestParam boolean isWorking){
        return ResponseEntity.ok(studentService.updateWorkingStatus(id, isWorking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}