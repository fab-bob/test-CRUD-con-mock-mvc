package co.develhope.crudTest;

import co.develhope.crudTest.entities.Student;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
class StudentControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ObjectMapper objectMapper;


	@Test
	void createStudentWithNameTest() throws Exception {
		Student student = new Student();
		student.setName("Aurora");

		String studentJSON = objectMapper.writeValueAsString(student);
		MvcResult result = this.mockMvc.perform(post("/students")
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJSON))
						.andDo(print())
						.andExpect(status().isOk())
						.andReturn();
		Student studentResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		assertNotNull(studentResponse.getName());
		assertEquals("Aurora", studentResponse.getName());

	}

	@Test
	void readStudentListTest() throws Exception {
		Student student = new Student();
		student.setName("Aurora");
		studentService.create(student);

		MvcResult result = this.mockMvc.perform(get("/students"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		List<Student> studentResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);

		assertNotNull(studentResponse);
		assertFalse(studentResponse.isEmpty());
	}

	@Test
	void readASingleStudentTest() throws Exception {
		Student student = new Student();
		student.setName("Aurora");
		student.setSurname("Scalici");
		Student savedStudent = studentService.create(student);

		this.mockMvc.perform(get("/students/{id}", savedStudent.getId())
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.name").value("Aurora"))
						.andExpect(jsonPath("$.surname").value("Scalici"));
	}

	@Test
	void updateStudentTest() throws Exception {
		Student existingStudent = new Student();
		existingStudent.setName("Rari");
		existingStudent.setSurname("Scalicci");
		Student savedStudent = studentService.create(existingStudent);

		Student updatedStudent = new Student();
		updatedStudent.setName("Aurora");
		updatedStudent.setSurname("Scalici");

		String updatedStudentJson = objectMapper.writeValueAsString(updatedStudent);
		this.mockMvc.perform(put("/students/{id}", savedStudent.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(updatedStudentJson))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.name").value("Aurora"))
						.andExpect(jsonPath("$.surname").value("Scalici"));
	}

	@Test
	void updateStudentWorkingStatusTest() throws Exception {
		Student existingStudent = new Student();
		existingStudent.setName("Aurora");
		existingStudent.setSurname("Scalici");
		existingStudent.setWorking(false);
		Student savedStudent = studentService.create(existingStudent);
		boolean updatedWorkingStatus = true;

		this.mockMvc.perform(patch("/students/{id}/working", savedStudent.getId())
						.param("isWorking", String.valueOf(updatedWorkingStatus))
						.contentType(MediaType.APPLICATION_JSON))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.working").value(updatedWorkingStatus));

		Student updatedStudent = studentService.readOne(savedStudent.getId()).orElse(null);
		assertNotNull(updatedStudent);
		assertEquals(updatedWorkingStatus, updatedStudent.isWorking());
	}

	@Test
	void deleteStudentTest() throws Exception {
		Student student = new Student();
		student.setName("Aurora");
		student.setSurname("Scalici");
		Student savedStudent = studentService.create(student);

		this.mockMvc.perform(delete("/students/{id}", savedStudent.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		
		Optional<Student> deletedStudent = studentService.readOne(savedStudent.getId());
		assertTrue(deletedStudent.isEmpty());
	}
}