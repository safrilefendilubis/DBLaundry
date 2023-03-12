package com.juaracoding.DBLaundry.service;

import com.juaracoding.DBLaundry.model.Student;
import com.juaracoding.DBLaundry.repo.StudentRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

	private StudentRepo studentRepository;

	public StudentService(StudentRepo studentRepository) {
		this.studentRepository = studentRepository;
	}

	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	public Student saveStudent(Student student) {
		return studentRepository.save(student);
	}

	public Optional<Student> getStudentById(Long id,String x) {
//		return studentRepository.findById(id).get();
		return studentRepository.findById(id);
	}

	public Student getStudentById(Long id) {
		return studentRepository.findById(id).get();
//		return studentRepository.findById(id);
	}

	public Student updateStudent(Student student) {
		return studentRepository.save(student);
	}

	public void deleteStudentById(Long id) {
		studentRepository.deleteById(id);	
	}

}
