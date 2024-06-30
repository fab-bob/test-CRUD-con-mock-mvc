package co.develhope.crudTest;

import co.develhope.crudTest.entities.Student;
import co.develhope.crudTest.repositories.StudentRepo;
import co.develhope.crudTest.services.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class StudentServiceTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StudentRepo studentRepo;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void studentServiceLoadsTest() {
        assertThat(studentRepo).isNotNull();
    }

    @Test
    void createStudentWithNameTest() throws Exception {
        Student student = new Student();
        student.setName("Aurora");

        String studentJSON = objectMapper.writeValueAsString(student);
        //json è il parser da json-oggetto e viceversa
        MvcResult result = this.mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Student studentResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
        assertNotNull(studentResponse.getName());
    }

    @Test
    void readStudentsTest() {
        Student student1 = new Student();
        student1.setName("Aurora");
        student1.setSurname("Scalici");

        Student student2 = new Student();
        student2.setName("Veronica");
        student2.setSurname("Rametta");

        studentRepo.save(student1);
        studentRepo.save(student2);

        List<Student> students = studentService.read();
        assertEquals(2, students.size());
    }

    @Test
    void UpdateStudentTest() {
        Student existingStudent = new Student();
        existingStudent.setId(1);
        existingStudent.setName("Rari");
        existingStudent.setSurname("Scalicci");

        String updatedName = "Aurora";
        String updatedSurname = "Scalici";

        Student updatedStudent = new Student();
        updatedStudent.setName(updatedName);
        updatedStudent.setSurname(updatedSurname);

        studentRepo.save(existingStudent);
        Student rightStudent = studentService.update(existingStudent.getId(), updatedStudent);

        Optional<Student> retrievedUpdatedStudent = studentRepo.findById(existingStudent.getId());
        assertTrue(retrievedUpdatedStudent.isPresent());
        Student retrievedStudent = retrievedUpdatedStudent.get();

        assertEquals(existingStudent.getId(), retrievedStudent.getId());
        assertEquals(updatedName, retrievedStudent.getName());
        assertEquals(updatedSurname, retrievedStudent.getSurname());
    }

    @Test
    void UpdateWorkingStatusTest() {
        Student existingStudent = new Student();
        long studentId = 1;
        boolean newWorkingStatus = true;
        existingStudent.setId(studentId);
        existingStudent.setWorking(false);
        studentRepo.save(existingStudent);

        Student updatedStatusStudent = studentService.updateWorkingStatus(studentId, newWorkingStatus);
        Optional<Student> retrievedUpdatedStudent = studentRepo.findById(studentId);
        assertTrue(retrievedUpdatedStudent.isPresent());
        Student retrievedStudent = retrievedUpdatedStudent.get();
        assertEquals(newWorkingStatus, retrievedStudent.isWorking());
    }

    @Test
    void DeleteStudentTest() {
        Student existingStudent = new Student();
        long studentId = 1;
        existingStudent.setId(studentId);
        existingStudent.setName("Rari");
        existingStudent.setSurname("Scalicci");
        studentRepo.save(existingStudent);
        studentService.delete(studentId);

        Optional<Student> deletedStudent = studentRepo.findById(studentId);
        assertFalse(deletedStudent.isPresent());
    }
}