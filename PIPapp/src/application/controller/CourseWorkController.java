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

import application.entity.CourseWorkEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent; 
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class CourseWorkController implements Initializable {
	
	@FXML
	private TableView<CourseWorkEntity> tableView;
	
	@FXML
	private TableColumn<CourseWorkEntity, String> assignmentCol;
	
	@FXML
	private TableColumn<CourseWorkEntity, String> courseNumberCol;
	
	@FXML
	private TableColumn<CourseWorkEntity, String> instructorCol;
	
	@FXML
	private TableColumn<CourseWorkEntity, String> gradeCol;
	
	@FXML
	private TextField assignmentField;
	
	@FXML
	private TextField courseNumberField;
	
	@FXML
	private TextField instructorField;
	
	@FXML
	private TextField gradeField;
	
	@FXML
	private Button addButton;
	
	@FXML
	private TextField filterField;
	
	@FXML
	private Button deleteButton;
		
	ObservableList<CourseWorkEntity> courseworklist = FXCollections.observableArrayList();
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Load file.
		load();
		
		// Editable
		tableView.setEditable(true);
		assignmentCol.setOnEditCommit(e -> assignmentCol_OnEditCommit(e));
		courseNumberCol.setOnEditCommit(e -> courseNumber_OnEditCommit(e));
		instructorCol.setOnEditCommit(e -> instructor_OnEditCommit(e));
		gradeCol.setOnEditCommit(e -> grade_OnEditCommit(e));
		
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		// Initialize each column.
		assignmentCol.setCellFactory(TextFieldTableCell.<CourseWorkEntity>forTableColumn());
		courseNumberCol.setCellFactory(TextFieldTableCell.<CourseWorkEntity>forTableColumn());
		instructorCol.setCellFactory(TextFieldTableCell.<CourseWorkEntity>forTableColumn());
		gradeCol.setCellFactory(TextFieldTableCell.<CourseWorkEntity>forTableColumn());
		
		assignmentCol.setCellValueFactory(new PropertyValueFactory<>("assignment"));
		courseNumberCol.setCellValueFactory(new PropertyValueFactory<>("courseNumber"));
		instructorCol.setCellValueFactory(new PropertyValueFactory<>("instructor"));
		gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
		
		// Disable button when no input.
		addButton.setDisable(true);
		deleteButton.setDisable(true);
		
		// Initialize table.
		tableView.setItems(courseworklist);
		tableView.setEditable(true);
		tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		tableView.setPlaceholder(new Label("You Have No Assignment"));
		
		// Enable add and delete buttons.
		assignmentField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			
			@Override
			public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
				
				if(assignmentField.isFocused()) {
					
					addButton.setDisable(false);

				}
	
			}
			
		});
		
		tableView.focusedProperty().addListener(new ChangeListener<Boolean>() {
			
			@Override
			public void changed(ObservableValue<? extends Boolean> obervable, Boolean oldValue, Boolean newValue) {
				
				if (tableView.isFocused()) {
					
					deleteButton.setDisable(false);
					
				}
				
			}
			
		});
		
		filterData();
		
	}
	
	/* This method handle the add button -> set tableview and pass to list. */
	@FXML
	public void handleAddButton(ActionEvent event) {
		
		if (isValidInput(event)) {
			
			CourseWorkEntity courseWorkEntity = new CourseWorkEntity();
			courseWorkEntity.setAssignment(assignmentField.getText().trim());
			courseWorkEntity.setCourseNumber(courseNumberField.getText().trim());
			courseWorkEntity.setInstructor(instructorField.getText().trim());
			courseWorkEntity.setGrade(gradeField.getText().trim());
			
			courseworklist.add(courseWorkEntity);
			
			assignmentField.clear();
			courseNumberField.clear();
			instructorField.clear();
			gradeField.clear();
			
			// save to file 
			save();
			
		}
		
	}
	
	/* In case of empty fields. Gives alert for respective empty field and requests focus on that field */
	public boolean isValidInput(ActionEvent event) {
		
		String errorMessage = "";
		
		if(assignmentField.getText() == null || assignmentField.getText().length() ==0)
			errorMessage += "No valid Assignment!\n";
		
		if (courseNumberField.getText() == null || courseNumberField.getText().length() == 0) 
            errorMessage += "No valid Course Number!\n";
		
		if (instructorField.getText() == null || instructorField.getText().length() == 0) 
            errorMessage += "No valid Instructor!\n";
        
		if (gradeField.getText() == null || gradeField.getText().length() == 0) 
            errorMessage += "No valid Grade!\n";
        	
		if(errorMessage.length() ==0) {
			
			return true;
			
		}else {
			
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);
			alert.showAndWait();
			
			return false;
			
		}
		
	}
	
	/* This method handle clear button -> wipe out all text field. */
	@FXML
	public void handleClearButton(ActionEvent event) {
		
		assignmentField.clear();
		courseNumberField.clear();
		instructorField.clear();
		gradeField.clear();
		
	}
	
	@FXML
	public void handleDeleteButton(ActionEvent event) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Course Work");
		alert.setHeaderText("Remove assignment from list");
		alert.setContentText("Are you sure?");
		
		Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
		if (result.get() == javafx.scene.control.ButtonType.OK) {
			
			CourseWorkEntity selectedContact = tableView.getSelectionModel().getSelectedItem();
			
			courseworklist.remove(selectedContact);
			
			save();
			
		}
		
		tableView.getSelectionModel().clearSelection();
		
	}
	
	/* Real-Time Filter */
	public void filterData() {
		
		FilteredList<CourseWorkEntity> searchedData = new FilteredList<>(courseworklist, e -> true);
		
		// When press the keyboard, clear the tableview
		filterField.setOnKeyPressed(e ->{
			
			tableView.getSelectionModel().clearSelection();
			
		});
		
		filterField.setOnKeyReleased(e ->{
			
			filterField.textProperty().addListener((obervable, oldValue, newValue) ->{
				
				searchedData.setPredicate(coursework ->{
					
					if (newValue == null || newValue.isEmpty()) 
						
						return true;
						
					String lowerCaseFilter = newValue.toLowerCase();
					
					if (coursework.getAssignment().toLowerCase().contains(lowerCaseFilter)) { 
						return true;
					}
					else if (coursework.getCourseNumber().toLowerCase().contains(lowerCaseFilter)) {
						return true;
					}
					else if (coursework.getInstructor().toLowerCase().contains(lowerCaseFilter)) {
						return true;
					}
					else if (coursework.getGrade().toLowerCase().contains(lowerCaseFilter)) {
						return true;
					}
					return false;	
				});
				
			});
			
			SortedList<CourseWorkEntity> sortedData = new SortedList<>(searchedData);
			sortedData.comparatorProperty().bind(tableView.comparatorProperty());
			tableView.setItems(sortedData);
			
		});
		
	}
	
	@SuppressWarnings("unchecked")
	public void assignmentCol_OnEditCommit(Event e) {
		
		TableColumn.CellEditEvent<CourseWorkEntity, String> cellEditEvent;
		int courseworkID = tableView.getSelectionModel().getSelectedIndex();
		cellEditEvent = (TableColumn.CellEditEvent<CourseWorkEntity, String>) e;
		CourseWorkEntity courseWorkEntity = cellEditEvent.getRowValue();
		courseWorkEntity.setAssignment(cellEditEvent.getNewValue());
		
		courseworklist.set(courseworkID, courseWorkEntity);
		
		save();
		
	}
	
	@SuppressWarnings("unchecked")
	public void courseNumber_OnEditCommit(Event e) {
		
		TableColumn.CellEditEvent<CourseWorkEntity, String> cellEditEvent;
		int courseworkID = tableView.getSelectionModel().getSelectedIndex();
		cellEditEvent = (TableColumn.CellEditEvent<CourseWorkEntity, String>) e;
		CourseWorkEntity courseWorkEntity = cellEditEvent.getRowValue();
		courseWorkEntity.setCourseNumber(cellEditEvent.getNewValue());
		
		courseworklist.set(courseworkID, courseWorkEntity);
		
		save();
		
	}
	
	@SuppressWarnings("unchecked")
	public void instructor_OnEditCommit(Event e) {
		
		TableColumn.CellEditEvent<CourseWorkEntity, String> cellEditEvent;
		int courseworkID = tableView.getSelectionModel().getSelectedIndex();
		cellEditEvent = (TableColumn.CellEditEvent<CourseWorkEntity, String>) e;
		CourseWorkEntity courseWorkEntity = cellEditEvent.getRowValue();
		courseWorkEntity.setInstructor(cellEditEvent.getNewValue());
		
		courseworklist.set(courseworkID, courseWorkEntity);
		
		save();
		
	}
	
	@SuppressWarnings("unchecked")
	public void  grade_OnEditCommit(Event e) {
		
		TableColumn.CellEditEvent<CourseWorkEntity, String> cellEditEvent;
		int courseworkID = tableView.getSelectionModel().getSelectedIndex();
		cellEditEvent = (TableColumn.CellEditEvent<CourseWorkEntity, String>) e;
		CourseWorkEntity courseWorkEntity = cellEditEvent.getRowValue();
		courseWorkEntity.setInstructor(cellEditEvent.getNewValue());
		
		courseworklist.set(courseworkID, courseWorkEntity);
		
		save();
		
	}
	
	public void save() {
		
		try (ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get("coursework.data")))) {
            stream.writeObject(new ArrayList<CourseWorkEntity>(courseworklist));
            System.out.println("Saved!");
        } catch (Exception e) {
            System.out.println("Failed to save: " + e);
        }
		
	}
	
	@SuppressWarnings("unchecked")
	public void load() {
		
		Path file = Paths.get("courseWork.data");
		List<CourseWorkEntity> listtemp = new ArrayList<>();
		
		// Check if the file exists, if not 
		if(Files.exists(file)) {
			
			try(ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(file))){
				
				listtemp = (ArrayList<CourseWorkEntity>) stream.readObject();
				this.courseworklist = FXCollections.observableArrayList(listtemp);
				System.out.println("Loaded!");
				
			}catch(Exception e) {
				
				System.out.println("Failed to load: " + e);
				
			}
		}
	}
	
}
