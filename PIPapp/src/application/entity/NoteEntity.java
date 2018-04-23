package application.entity;

import java.io.Serializable;

public class NoteEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String title;
	
	private String note;
	
	/* Default Constructor */
	public NoteEntity() {}
	
	public NoteEntity(String title, String note) {
		
		this.title = title;
		this.note = note;
		
	}
	
	public String getTitle() {
		
		return this.title;
		
	}
	
	public void setTitle(String title) {
		
		this.title = title;
		
	}
	
	public String getNote() {
		
		return this.note;
		
	}
	
	public void setNote(String note) {
		
		this.note = note;
		
	}

}
