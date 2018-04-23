package application.controller;

import java.io.File;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ContactAddController implements Initializable, ContactList {
	
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
	private ImageView pictureView;
	
	@FXML
	private Button saveButton;
	
	private FileChooser fileChooser;
	
	private File selectedFile;
	
	private byte[] bFile;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	private void fileChoosing() {
		
		fileChooser = new FileChooser();
		selectedFile = fileChooser.showOpenDialog(null);
		
		fileChooser.setTitle("Choose Picture");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG", "*.jpg"));
		
	}
	
	/* This method handle the browse button -> open filechooser. */
	@FXML
	public void handleBrowse(ActionEvent event) {
		
		fileChoosing();
		
		bFile = new byte[(int) selectedFile.length()];
		try {
		
			FileInputStream fileInputStream = new FileInputStream(selectedFile);
			fileInputStream.read(bFile);
			fileInputStream.close();
			
		}catch (FileNotFoundException e) {
			Alert ioAlert = new Alert(Alert.AlertType.ERROR, "OOPS!", javafx.scene.control.ButtonType.OK);
			ioAlert.setTitle("ERROR");
			ioAlert.setHeaderText("Somthing Bad Happened");
			ioAlert.setContentText(e.getMessage());
			ioAlert.showAndWait();
			
			if (ioAlert.getResult() == javafx.scene.control.ButtonType.OK) {
				
				ioAlert.close();
				
			}
			System.out.println(e.getMessage());
		}catch (IOException e) {
			Alert ioAlert = new Alert(Alert.AlertType.ERROR, "OOPS!", javafx.scene.control.ButtonType.OK);
			ioAlert.setTitle("ERROR");
			ioAlert.setHeaderText("Somthing Bad Happened");
			ioAlert.setContentText(e.getMessage());
			ioAlert.showAndWait();
			
			if (ioAlert.getResult() == javafx.scene.control.ButtonType.OK) {
				
				ioAlert.close();
				
			}
			
			System.out.println(e.getMessage());
		}
		
		pictureView.setImage(new Image(selectedFile.toURI().toString()));
		
	}
	
	/* This method handle save button -> write the list to file. */
	@FXML
	public void handleSave(ActionEvent event) {
		
		if (validateInput()) {
			
			// trim() will kill the head and tail spaces.
			ContactEntity contactEntity = new ContactEntity(titleField.getText().trim(), firstNameField.getText().trim(),
															lastNameField.getText().trim(), emailField.getText().trim(),
															mobileField.getText().trim(), addressArea.getText().trim(), bFile);
			
			CONTACTLIST.add(contactEntity);
			save();
			
			// Show success alert box.
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Successful");
			alert.setHeaderText("Contact is added");
			alert.setContentText("Contact is added sucessfully");
			alert.showAndWait();
			
			((Stage) saveButton.getScene().getWindow()).close();
			
		}
		
	}
	
	/* Write file  */
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
		
        titleField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        mobileField.setText("");
        addressArea.setText("");
        pictureView.setImage(null);
        
    }
	
	/* This method handle the minimize button */
	@FXML
	public void handleMinimize(ActionEvent event) {
		
		Stage stage = (Stage)((Button) event.getSource()).getScene().getWindow();
		stage.setIconified(true);
		
	}
	
	/* This method handle the exit button */
	@FXML
	public void handleExit(ActionEvent event) {
		
		((Node) (event.getSource())).getScene().getWindow().hide();
		
	}

}
