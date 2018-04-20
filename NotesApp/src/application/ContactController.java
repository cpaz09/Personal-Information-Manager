package application;

import javafx.event.ActionEvent;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class ContactController {
	
	@FXML
	private Button add;
	
	@FXML
	private TextField name;
	
	@FXML
	private TextField phoneNum;
	
	@FXML
	private Accordion leftPane;

	@FXML
	private Button addName;
	
	@FXML
	private TitledPane firstTpane;


	
	public void addButton() {
		
		
		 add.setOnAction(new EventHandler<ActionEvent>() {
	        	
	        	@Override
	        	public void handle(ActionEvent event) {
	        		
	        		TitledPane newPane = new TitledPane();
	        		leftPane.getPanes().add(newPane);
	    
	        		
	        	}
	        	
	        });
		
		
	}
	
	public void addButtonName() {
		
		
		 addName.setOnAction(new EventHandler<ActionEvent>() {
	        	
	        	@Override
	        	public void handle(ActionEvent event) {
	        		
	        		firstTpane.setText(name.getText());
	        		
	        		
	        		
	        		firstTpane.setContent(new TextField(phoneNum.getText()));
	    
	        		
	        	}
	        	
	        });
		
		
	}
	

}
