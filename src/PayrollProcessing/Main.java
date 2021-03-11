package PayrollProcessing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main used for launching the GUI
 * @author Connie Chen, Tiffany Lee
 */
public class Main extends Application {

    /**
     * Starts the JavaFX Payroll Processing GUI
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource( "View.fxml"));
            primaryStage.setTitle("Payroll Processing");
            primaryStage.setScene(new Scene(root, 650, 550));
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls launch method for the GUI
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
