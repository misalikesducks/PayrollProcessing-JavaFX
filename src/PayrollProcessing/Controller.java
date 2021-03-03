package PayrollProcessing;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Alert.AlertType;
import java.io.File;

public class Controller {

   @FXML
   private TextField empName, hours, partRate;
   @FXML
   private DatePicker dateHired;
   @FXML
   private TextArea show1;
   @FXML
   void get(ActionEvent event) {
      try {
         Date day = new Date("11/01/2000");
         Profile prof = new Profile(empName.getText(), "IT", day);
         show1.appendText(prof.toString());
      }
      catch(NumberFormatException e) {
         Alert alert = new Alert(AlertType.WARNING);
         alert.setTitle("WARNING!");
         alert.setHeaderText("YOURE A POO");
         alert.setContentText("HAHAHHAHAHA idk what im doing ");
         alert.showAndWait();
      }
   }

}
