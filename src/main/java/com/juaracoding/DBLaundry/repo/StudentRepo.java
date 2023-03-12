package com.juaracoding.DBLaundry.repo;

import com.juaracoding.DBLaundry.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, Long>{

}
