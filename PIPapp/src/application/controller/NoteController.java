package application.controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.entity.NoteEntity;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class NoteController implements Initializable{
	
	@FXML
	private TableView<NoteEntity> tableView;
	
	@FXML
	private TableColumn<NoteEntity, String> noteListCol;
	
	@FXML
	private TextField titleField;
	
	@FXML
	private TextArea noteField;
	
	@FXML
	private Button addButton;
	
	@FXML
	private Button deleteButton;
	
	ObservableList<NoteEntity> noteentitylist = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		load();
		
		noteListCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		
		tableView.getSelectionModel().selectedItemProperty().addListener(
				
				(obervable, oldValue, newValue) ->{
					
					showDetails(newValue);
					
				});
		 
		tableView.setItems(noteentitylist);
		tableView.setPlaceholder(new Label("You Have No Note"));
		
		deleteButton.disableProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()));		
	}
	
	@FXML
	public void handleAdd(ActionEvent event) {
		
		if (validateInput()) {
			
			// trim() will kill the head and tail spaces.
			NoteEntity noteEntity = new NoteEntity(titleField.getText().trim(), noteField.getText());
				
			noteentitylist.add(noteEntity);
			
			save();
				
		
			// Show success alert box.
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Successful");
			alert.setHeaderText("Note is added");
			alert.setContentText("Note is added sucessfully");
			alert.showAndWait();
			
		}
		
	}
	
	@FXML
	public void handleDelete(ActionEvent event) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Note");
		alert.setHeaderText("Remove Note from list");
		alert.setContentText("Are you sure?");
		
		Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
		if (result.get() == javafx.scene.control.ButtonType.OK) {
			
			NoteEntity selectedNote = tableView.getSelectionModel().getSelectedItem();
			
			noteentitylist.remove(selectedNote);
			
			save();
			
		}
		
		tableView.getSelectionModel().clearSelection();
		
	}
	
	@FXML
	public void handleClear(ActionEvent event) {
		
		titleField.clear();
		noteField.clear();
	}
	
	public void showDetails(NoteEntity noteEntity) {
		
		if (noteEntity != null) {
			
			titleField.setText(noteEntity.getTitle());
			
			if (noteEntity.getNote() != null) {
				noteField.setText(noteEntity.getNote());
			}else {
				noteField.setText("");
			}
			
		}else {
			
			titleField.setText("");
			noteField.setText("");
			
		}
		
	}
	
	public void save() {
		
		try(ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get("note.data")))) {
			
			stream.writeObject(new ArrayList<NoteEntity>(noteentitylist));
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
	
	@SuppressWarnings("unchecked")
	public void load() {
		
		if (noteentitylist.isEmpty()) {
			
			Path file = Paths.get("note.data");
			List<NoteEntity> listtemp = new ArrayList<>();
			
			// Check if the file exists, if not 
			if(Files.exists(file)) {
				
				try(ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(file))){
					
					listtemp = (ArrayList<NoteEntity>) stream.readObject();
					noteentitylist.addAll(listtemp);
					System.out.println("Loaded!");
					
				}catch(Exception e) {
					
					Alert ioAlert = new Alert(Alert.AlertType.ERROR, "OOPS!", javafx.scene.control.ButtonType.OK);
					ioAlert.setTitle("ERROR");
					ioAlert.setHeaderText("Somthing Bad Happened");
					ioAlert.setContentText(e.getMessage());
					ioAlert.showAndWait();
					
					if (ioAlert.getResult() == javafx.scene.control.ButtonType.OK) {
						
						ioAlert.close();
						
					}
					
					System.out.println("Failed to load: " + e);
					
				}
				
			}
			
		}
		
	}
	
	private boolean validateInput() {
		
		String errorMessage = "";
		
		if(titleField.getText() == null || titleField.getText().length() ==0)
			errorMessage += "No valid title!\n";
		
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

}
