package application;

import javafx.application.Application;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class NotesApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
    	
    	//Test
    	System.out.println("start() is calling now");
    	
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Tabpane.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


        /*stage.setOnCloseRequest(e -> {
        	
        	// Test
        	System.out.println("exit() is calling");
        	
            e.consume();
            loader.<NotesController>getController().exit();
        });*/
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
    	launch(args);
    }
    
}

