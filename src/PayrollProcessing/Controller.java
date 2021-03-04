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
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Controller {
   private Company database = new Company();

   @FXML
   private TextField empName, hours, partRate, salary;

   @FXML
   private DatePicker dateHired;

   @FXML
   private TextArea show1;

   @FXML
   private RadioButton itDepartment, csDepartment, eceDepartment, fullTime, partTime, management,
            departmentHead, manager, director;

   @FXML
   /**
    * Event Handler for the add Employee button
    * @param event
    */
   void add(ActionEvent event) {
      ToggleGroup departments = new ToggleGroup();
      itDepartment.setToggleGroup(departments);
      csDepartment.setToggleGroup(departments);
      eceDepartment.setToggleGroup(departments);

      ToggleGroup empType = new ToggleGroup();
      fullTime.setToggleGroup(empType);
      partTime.setToggleGroup(empType);
      management.setToggleGroup(empType);

      ToggleGroup managementType = new ToggleGroup();
      departmentHead.setToggleGroup(managementType);
      manager.setToggleGroup(managementType);
      director.setToggleGroup(managementType);

      //Date tempDate = new Date(dateHired.getValue().toString());
      //NEED TO CHECK FOR VALID DATE ERROR CONDITION !!!!!!!
      String date = dateHired.getValue().toString();
      Date tempDate = new Date(date.substring(5,7) + "/" + date.substring(8) + "/" + date.substring(0, 4));
      RadioButton selectedDepartment = (RadioButton) departments.getSelectedToggle();
      Profile tempProf = new Profile(empName.getText(), selectedDepartment.getText(), tempDate);
      Employee tempEmp;
      // DISABLE SUTPID STUFF !!!! ! ! ! ! ! ! ! !  ! ! ! ! ! ! !  ! ! !
      //NEED TO CHECK FOR PROPER SALARY INPUT (NUMBER EXCEPTION) !!!!!!
      RadioButton selectedEmpType = (RadioButton) empType.getSelectedToggle();
      if(selectedEmpType.getText().equals("Fill Time")) {
         tempEmp = new Fulltime(tempProf, Double.parseDouble(salary.getText()));
      } else if(selectedEmpType.getText().equals("Management")) {
         RadioButton selectedManagement = (RadioButton) managementType.getSelectedToggle();
         int manageType = 0;
         //FIX MAGIC NUMBERS SDJFSDF !!!!!
         //ERROR CATCH !!!!!!
         if(selectedManagement.getText().equals("Department Head"))
            manageType = 2;
         else if(selectedManagement.getText().equals("Manager"))
            manageType = 1;
         else
            manageType = 3;
         tempEmp = new Management(tempProf, Double.parseDouble(salary.getText()), manageType);
      } else {
         tempEmp = new Parttime(tempProf, Double.parseDouble(partRate.getText()), 0);
      }
      //show1.appendText(tempEmp.toString());
      //ADD RETURN FALSE ERROR CHECK !!!!!
      database.add(tempEmp);
   }
   Employee inputting() {
      return null;
   }

   @FXML
   void importDatabase(ActionEvent event){
      FileChooser chooser = new FileChooser();
      chooser.setTitle("Open Source File for the Import");
      chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
              new ExtensionFilter("All Files", "*.*"));
      Stage stage = new Stage();
      File sourceFile = chooser.showOpenDialog(stage); // get the reference of the source file
      //write code to read from the file

   }

   @FXML
   void exportDatabase(ActionEvent event){ // should create a new text file
      FileChooser chooser = new FileChooser();
      Stage stage = new Stage();
      File targetFile = new File("companyDatabase.txt"); // creates output txt file
      targetFile.getParentFile().mkdirs();
      if(targetFile.exists()){
         //print in textarea that file already exists
      }
      try {
         PrintWriter pw = new PrintWriter(targetFile);
         pw.print(database.print()); //write to file
         pw.close();
      } catch(FileNotFoundException ex){

      }
   }
}
