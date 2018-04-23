package application.entity;

import java.io.Serializable;

public class CourseWorkEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String assignment;
	
	private String courseNumber;
	
	private String instructor;
	
	private String grade;
	
	/* Default Constructor */
	public CourseWorkEntity() {
	
	}
	
	public CourseWorkEntity(String assignment, String courseNumber, String instructor, String grade) {
		
		this.assignment = assignment;
		this.courseNumber = courseNumber;
		this.instructor = instructor;
		this.grade = grade;
		
	}
	
	/*---------------------------------------Get and Set methods---------------------------------------*/
	public String getAssignment() {
		
		return this.assignment;
		
	}
	
	public void setAssignment(String assignment) {
		
		this.assignment = assignment;
		
	}
	
	public String getCourseNumber() {
		
		return this.courseNumber;
		
	}
	
	public void setCourseNumber(String courseNumber) {
		
		this.courseNumber = courseNumber;
		
	}
	
	public String getInstructor() {
		
		return this.instructor;
		
	}
	
	public void setInstructor(String instructor) {
		
		this.instructor = instructor;
		
	}
	
	public String getGrade() {
		
		return this.grade;
		
	}
	
	public void setGrade(String grade) {
		
		this.grade = grade;
		
	}

}
