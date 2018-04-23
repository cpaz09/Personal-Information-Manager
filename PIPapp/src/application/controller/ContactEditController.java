package application.controller;

import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import application.entity.ContactEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ContactEditController implements Initializable, ContactList{
	
	@FXML
	private TextField titleField;
	
	@FXML
	private TextField firstNameField;
	
	@FXML
	private TextField lastNameField;
	
	@FXML
	private TextField emailField;
	
	@FXML
	private TextField mobileField;
	
	@FXML
	private TextArea addressArea;
	
	@FXML
	private Button saveButton;
	
	private ContactEntity contactEntity;
	
	private int personID;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		resetValues();
		
	}
	
	/* Get the selected object from main controller */
	public void setPerson(ContactEntity contactEntity, int personID) {
		
		this.contactEntity = contactEntity;
		this.personID = personID;
		setData();
		
	}
	
	/* This method show the selected object in textfield. */
	private void setData() {
		
		titleField.setText(contactEntity.getTitle());
		firstNameField.setText(contactEntity.getFirstName());
		lastNameField.setText(contactEntity.getLastName());
		emailField.setText(contactEntity.getEmail());
		mobileField.setText(contactEntity.getMobile());
		addressArea.setText(contactEntity.getAddress());
		
	}
	
	/* This method handle save button -> write the list to file. */
	@FXML
	public void handleSave(ActionEvent event) {
		
		if (validateInput()) {
			
			// trim() will kill the head and tail spaces.
			ContactEntity editedContactEntity = new ContactEntity(titleField.getText().trim(), firstNameField.getText().trim(),
															lastNameField.getText().trim(), emailField.getText().trim(),
															mobileField.getText().trim(), addressArea.getText().trim(), contactEntity.getPicture());
			
			// selected object in the list has been modified.
			CONTACTLIST.set(personID, editedContactEntity);
			save();
			
			// Show success alert box.
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Successful");
			alert.setHeaderText("Contact is modified");
			alert.setContentText("Contact is modified sucessfully");
			alert.showAndWait();
			
			((Stage) saveButton.getScene().getWindow()).close();
			
		}
		
	}
	
	/* Write file. NOTE: file name must stay the same!(Exactly like addClass) */
	public void save() {
		
		try(ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get("contact.data")))) {
			
			stream.writeObject(new ArrayList<ContactEntity>(CONTACTLIST));
			System.out.println("Saved!");
			
		}catch(Exception e) {
			
			Alert ioAlert = new Alert(Alert.AlertType.ERROR, "OOPS!", javafx.scene.control.ButtonType.OK);
			ioAlert.setTitle("ERROR");
			ioAlert.setHeaderText("Somthing Bad Happened");
			ioAlert.setContentText(e.getMessage());
			ioAlert.showAndWait();
			
			if (ioAlert.getResult() == javafx.scene.control.ButtonType.OK) {
				
				ioAlert.close();
				
			}
			
			System.out.println("Failed to save: " + e);
			
		}
		
	}
	
	/* Any blank must be filled up */
	private boolean validateInput() {
		
		String errorMessage = "";
		
		if(titleField.getText() == null || titleField.getText().length() ==0)
			errorMessage += "No valid title!\n";
		
		if (firstNameField.getText() == null || firstNameField.getText().length() == 0) 
            errorMessage += "No valid first name!\n";
		
		if (lastNameField.getText() == null || lastNameField.getText().length() == 0) 
            errorMessage += "No valid last name!\n";
        
		if (emailField.getText() == null || emailField.getText().length() == 0) 
            errorMessage += "No valid email!\n";
        
		if (mobileField.getText() == null || mobileField.getText().length() == 0) 
            errorMessage += "No valid mobile!\n";
		
		if (addressArea.getText() == null || addressArea.getText().length() == 0) 
            errorMessage += "No valid address!\n";
		
		if(errorMessage.length() ==0) {
			
			return true;
			
		}else {
			
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);
			alert.showAndWait();
			
			return false;
			
		}
		
	}
	
	/* This method handle the cancel button -> wipe all textfields. */
	@FXML
	public void handleCancel(ActionEvent event) {
		
		resetValues();
        
    }
	
	public void resetValues() {
		
		 titleField.setText("");
	     firstNameField.setText("");
	     lastNameField.setText("");
	     emailField.setText("");
	     mobileField.setText("");
	     addressArea.setText("");
	}
	
	@FXML
    public void handleMinimize(ActionEvent event) {
		
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        if (stage.isMaximized()) {
            stage.setMaximized(false);
        } else {
            stage.setMaximized(true);
        }
    }
	
	 @FXML
	 public void handleExit(ActionEvent event) {
		 
	     ((Node) (event.getSource())).getScene().getWindow().hide();
	     
	    }

}
