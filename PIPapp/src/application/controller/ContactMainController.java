package application.controller;

import java.io.ByteArrayInputStream;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;

import application.entity.ContactEntity;
import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ContactMainController implements Initializable, ContactList {
	
	@FXML 
	private TableView<ContactEntity> tableView;
	
	@FXML
	private Button editButton;
	
	@FXML
	private Button deleteButton;
	
	@FXML
	private TableColumn<ContactEntity, String> firstColumn;
	
	@FXML
	private TableColumn<ContactEntity, String> lastColumn;
	
	@FXML
	private TextField searchField;
	
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
	
	private double xOffset = 0;
	private double yOffset = 0;
	
	/* When restart the life-cycle of app */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setData(); // load();
		
		firstColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lastColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		
		// Bind selected tableView item with right side textfield.
		tableView.getSelectionModel().selectedItemProperty().addListener(
				
				(obervable, oldValue, newValue) ->{
					
					showDetails(newValue);
					
				});
		
		tableView.setItems(CONTACTLIST);
		tableView.setPlaceholder(new Label("You Have No Contact"));
		
		// Disable the EDIT BUTTON and DELETE BUTTON when no selecting on table.
		editButton.disableProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()));
		deleteButton.disableProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()));
		
		filterData();
		
	}
	
	/* This method load the file */
	@SuppressWarnings("unchecked")
	public void setData() {
		
		if (CONTACTLIST.isEmpty()) {
			
			Path file = Paths.get("contact.data");
			List<ContactEntity> listtemp = new ArrayList<>();
			
			// Check if the file exists, if not 
			if(Files.exists(file)) {
				
				try(ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(file))){
					
					listtemp = (ArrayList<ContactEntity>) stream.readObject();
					CONTACTLIST.addAll(listtemp);
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
	
	/* Filter */
	public void filterData() {
		
		FilteredList<ContactEntity> searchedData = new FilteredList<>(CONTACTLIST, e -> true);
		
		// When press the keyboard, clear the tableview
		searchField.setOnKeyPressed(e ->{
			
			tableView.getSelectionModel().clearSelection();
			
		});
		
		searchField.setOnKeyReleased(e ->{
			
			searchField.textProperty().addListener((obervable, oldValue, newValue) ->{
				
				searchedData.setPredicate(person ->{
					
					if (newValue == null || newValue.isEmpty()) 
						
						return true;
						
					String lowerCaseFilter = newValue.toLowerCase();
					
					if (person.getFirstName().toLowerCase().contains(lowerCaseFilter)) { 
						return true;
					}
					else if (person.getLastName().toLowerCase().contains(lowerCaseFilter)) {
						return true;
					}
					return false;	
				});
				
			});
			
			SortedList<ContactEntity> sortedData = new SortedList<>(searchedData);
			sortedData.comparatorProperty().bind(tableView.comparatorProperty());
			tableView.setItems(sortedData);
			
		});
		
	}
	
	private File streamToFile(ByteArrayInputStream in) {

        File tempFile = null;
        try {
            tempFile = File.createTempFile("picture", ".jpg");
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                IOUtils.copy(in, out);
                in.close();
                out.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
        return tempFile;
    }
	
	/* Render picture */
	public Image renderPicture(byte[] picture) {
		
		File file = null;
		
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(picture);
			file = streamToFile(inputStream);
			inputStream.close();
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return new Image(file.toURI().toString());
		
		
	}
	
	/* This method bind the selected tableView item with right side textfield. */
	public void showDetails(ContactEntity contactEntity) {
		
		if (contactEntity != null) {
			
			titleField.setText(contactEntity.getTitle());
			firstNameField.setText(contactEntity.getFirstName());
			lastNameField.setText(contactEntity.getLastName());
			emailField.setText(contactEntity.getEmail());
			mobileField.setText(contactEntity.getMobile());
			addressArea.setText(contactEntity.getAddress());
			
			if(contactEntity.getPicture() != null) {
				pictureView.setImage(renderPicture(contactEntity.getPicture()));
			}else {
				File file = new File("src/application/icons/contact.png");
				pictureView.setImage(new Image(file.toURI().toString()));
			}
			
		}else {
			
			titleField.setText("");
			firstNameField.setText("");
			lastNameField.setText("");
			emailField.setText("");
			mobileField.setText("");
			addressArea.setText("");
			File file = new File("src/application/icons/contact.png");
			pictureView.setImage(new Image(file.toURI().toString()));
			
		}
		
	}
	
	/* This method handle NEW BUTTON -> new stage for adding. */
	@FXML
	public void handleNew(ActionEvent event) throws Exception{
		
		tableView.getSelectionModel().clearSelection();
		Parent root = FXMLLoader.load(getClass().getResource("/application/fxml/add.fxml"));
		Stage stage = new Stage();
		
		// Make the new stage move when drag by mouse.
		root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
		
		Scene scene = new Scene(root);
		stage.setTitle("Add New");
		
		// Defines a Stage style with a solid white background and no decorations.
		stage.initStyle(StageStyle.UNDECORATED);

		// Defines a modal window that blocks events from being delivered to any other application window.
		stage.initModality(Modality.APPLICATION_MODAL); 
		
		stage.setScene(scene);
		stage.show();
		
	}
	
	/* This method handle EDIT BUTTON -> new stage for editing */
	@FXML
	public void handleEdit(ActionEvent event) throws Exception{
		
		// Returns the currently selected object.
		ContactEntity selectedContactEntity = tableView.getSelectionModel().getSelectedItem();
		int selectedPersonID = tableView.getSelectionModel().getSelectedIndex();
		FXMLLoader loader = new FXMLLoader((getClass().getResource("/application/fxml/edit.fxml")));
		ContactEditController controller = new ContactEditController();
		loader.setController(controller);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		
		root.setOnMousePressed((MouseEvent event1) -> {
            xOffset = event1.getSceneX();
            yOffset = event1.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event1) -> {
            stage.setX(event1.getScreenX() - xOffset);
            stage.setY(event1.getScreenY() - yOffset);
        });
        
        stage.setTitle("Edit Person Details");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
        
        // IMPORTANT!
        controller.setPerson(selectedContactEntity, selectedPersonID);
        
	}
	
	/* This method handled DELETE BUTTON -> wipe seleted item from list */
	@FXML
	public void handleDelete(ActionEvent event) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Contact");
		alert.setHeaderText("Remove Contact from list");
		alert.setContentText("Are you sure?");
		
		Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
		if (result.get() == javafx.scene.control.ButtonType.OK) {
			
			ContactEntity selectedContact = tableView.getSelectionModel().getSelectedItem();
			
			CONTACTLIST.remove(selectedContact);
			ContactAddController addController = new ContactAddController();
			addController.save();
			
		}
		
		tableView.getSelectionModel().clearSelection();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
