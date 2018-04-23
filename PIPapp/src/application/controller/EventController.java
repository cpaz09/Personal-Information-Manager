package application.controller;

import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class EventController {

    @FXML
    private DatePicker picker;

    @FXML
    private TextArea notes;
    
    @FXML
    private Button updateButton;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private Label label;
    
    @FXML
    private TextField temp;

    private boolean saveConfirm = false;
    
    private boolean deleteConfirm = false;

    private Map<LocalDate, String> data = new HashMap<>();
    
    
    public void initialize() {
    	
    	// Test
    	System.out.println("initialized() is calling");
        load();

        picker.valueProperty().addListener((o, oldDate, date) -> {
            notes.setText(data.getOrDefault(date, ""));
        });

        picker.setValue(LocalDate.now());
        
 
    } 
    
    /* This method offers the alert box when click on update button. */
    /* Problem still exist: first time launch the app you will need click twice then the alert box could show up*/
    public void alertConfirmation() {
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Notes save conformation");
    	
    	alert.setHeaderText("Are you sure want to save this note?");
    	alert.setContentText("Note:( " + notes.getText() + " )" +"\n" + "For date: " + picker.getValue());
    	
    	// option button in alert box
    	Optional<ButtonType> option = alert.showAndWait();
    	
    	if(option.get() == null) {
    		
    		this.label.setTextFill(Color.FIREBRICK);
    		this.label.setText("No selection!");
    		this.saveConfirm = false;
    	
    	}else if(option.get() == ButtonType.OK) {
    		
    		this.label.setTextFill(Color.FIREBRICK);
    		this.label.setText("Note saved");
    		this.saveConfirm = true;
    	
    	}else if(option.get() == ButtonType.CANCEL) {
    		
    		this.label.setTextFill(Color.FIREBRICK);
    		this.label.setText("Canceled!");
    		this.saveConfirm = false;
    		
    	}
    }
    
    /* This method will show the alertbox when click delete button */
    public void alertConfirmationD() {
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Notes delete conformation");
    	
    	alert.setHeaderText("Are you sure want to delete this note?");
    	alert.setContentText("Note:( " + notes.getText() + " )" +"\n" + "For date: " + picker.getValue());
    	
    	// option button in alert box
    	Optional<ButtonType> option = alert.showAndWait();
    	
    	if(option.get() == null) {
    		
    		this.label.setTextFill(Color.FIREBRICK);
    		this.label.setText("No selection!");
    		this.deleteConfirm = false;
    	
    	}else if(option.get() == ButtonType.OK) {
    		
    		this.label.setTextFill(Color.FIREBRICK);
    		this.label.setText("Deleted");
    		this.deleteConfirm = true;
    	
    	}else if(option.get() == ButtonType.CANCEL) {
    		
    		this.label.setTextFill(Color.FIREBRICK);
    		this.label.setText("Canceled!");
    		this.deleteConfirm = false;
    		
    	}
    }
    
    /* This method set action on update button -> set note to map */
    public void updateNotes() {
        //data.put(picker.getValue(), notes.getText());
        
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
        	public void handle(ActionEvent event) {
        		
        		alertConfirmation();
        		
        		// If click OK in alert box. Note will be write to file.
        		if(saveConfirm) {
        			
        			data.put(picker.getValue(), notes.getText());
        			save();
        			
        		}
        		
        	}
        	
        });
        
    }
    
    /* This method set action on delete button -> delete note from map */
    public void deleteNotes() {
    	
    	deleteButton.setOnAction(new EventHandler<ActionEvent>() {
    		
    		@Override
    		public void handle(ActionEvent event) {
    			
    			alertConfirmationD();
    			
    			if(deleteConfirm) {
    				
    				data.remove(picker.getValue());
    				save();
    				notes.setText(null);
    				
    			}
    			
    		}
    		
    	});
    	
    }
    
    /* This method defines self-custom exit when click X on windows */
    public void exit() {
        save();
        Platform.exit();
    }
    
    /* This method is write the map to the disk */
    private void save() {
        try (ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(Paths.get("notes.txt")))) {
            stream.writeObject(data);
            System.out.println("Saved!");
        } catch (Exception e) {
            System.out.println("Failed to save: " + e);
        }
    }
    

    @SuppressWarnings("unchecked")
	private void load() {
        Path file = Paths.get("notes.txt");

        if (Files.exists(file)) {
            try (ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(file))) {
                data = (Map<LocalDate, String>) stream.readObject();
                System.out.println("Loaded!");
            } catch (Exception e) {
                System.out.println("Failed to load: " + e);
            }
        }
    }
}