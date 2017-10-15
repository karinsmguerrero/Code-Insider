package pruebita.parts;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PruebaFX extends Application {
	 // Creating a static root to pass to the controller
    private static BorderPane root = new BorderPane();

    /**
     * Just a root getter for the controller to use
     */
    public static BorderPane getRoot() {
        return root;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

    	AnchorPane anchor = new AnchorPane();
    	Label lbl = new Label("Holis");
        // constructing our scene using the static root
    	anchor.getChildren().addAll(lbl);
        root.setCenter(anchor);

        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add("GUI/css/flat_ui.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Code to flow diagram");
        primaryStage.show();
    }

}
