package co.develhope.crudTest.repositories;

import co.develhope.crudTest.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, Long> {
}
