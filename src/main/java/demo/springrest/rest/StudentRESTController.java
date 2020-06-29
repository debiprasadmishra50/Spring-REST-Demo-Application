package demo.springrest.rest;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import demo.springrest.entity.Student;
import demo.springrest.exception.StudentErrorResponse;
import demo.springrest.exception.StudentNotFoundException;

@RestController
@RequestMapping("/api")
public class StudentRESTController {
	
	private List<Student> students;
	
	// define @PostConstruct to load student data.. execute only once after bean creation 
	@PostConstruct
	public void loadData() {
		
		students = new ArrayList<Student>();
		
		students.add(new Student("Debi", "Mishra"));
		students.add(new Student("Debi", "Prasad"));
		students.add(new Student("Vicky", "Mishra"));
		students.add(new Student("Rog", "Debi"));
	}

	// define end-point for a "/students" - return list of students
	@GetMapping("/students")
	public List<Student> getStudents(){
		return students;
	}
	
	// define end-point for "/students/{studentId}" - return student index in ArrayList
	@GetMapping("/students/{studentId}")
	public Student getStudent(@PathVariable int studentId)
	{
		// just index it into the list 
		
		// check the studentId against the list.size()
		if((studentId >= students.size()) || (studentId<0))
			throw new StudentNotFoundException("Student Not Found with id "+studentId);
		
		return students.get(studentId);
	}
	
	// Add an exception Handler
	@ExceptionHandler
	public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException e) {
		
		// Create a new StudentErrorResponse
		StudentErrorResponse error = new StudentErrorResponse();
		
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(e.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		
		//return ResponseEntity
		return new ResponseEntity<> (error, HttpStatus.NOT_FOUND); // (body, Status Code)
	}
	
	// Add another exception Handler.. to catch any type of exception that is thrown (catch all)
	@ExceptionHandler
	public ResponseEntity<StudentErrorResponse> handleException(Exception e) {
		
		// Create a new StudentErrorResponse
		StudentErrorResponse error = new StudentErrorResponse();
				
		error.setStatus(HttpStatus.BAD_REQUEST.value());
//		error.setMessage(e.getMessage()); // Default Message of Java
		error.setMessage("Do not enter anything unrelated"); // Custom MEssage that will be displayed
		error.setTimeStamp(System.currentTimeMillis());
				
		//return ResponseEntity
		return new ResponseEntity<> (error, HttpStatus.BAD_REQUEST); // (body, Status Code)
	}
	
}












