package PayrollProcessing;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Alert.AlertType;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Controller {
   private Company database = new Company();

   public static final int EMPTY = 0;
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
   private ToggleGroup department, managementType, empType;
   @FXML
   private GridPane pane;
   private int manageRole = EMPTY;

   @FXML
   void fullTimeClicked(ActionEvent event) {
      hours.setDisable(true);
      partRate.setDisable(true);
      salary.setDisable(false);
      managementType.getToggles().forEach(toggle -> {
         RadioButton tempButton = (RadioButton) toggle;
         tempButton.setDisable(true);
      });
   }
   @FXML
   void clear(ActionEvent event){
      pane.getChildren().clear();
   }
   @FXML
   void partTimeClicked(ActionEvent event) {
      managementType.getToggles().forEach(toggle -> {
         RadioButton tempButton = (RadioButton) toggle;
         tempButton.setDisable(true);
      });
      hours.setDisable(false);
      partRate.setDisable(false);
      salary.setDisable(true);
   }

   @FXML
   void manageClicked(ActionEvent event){
      hours.setDisable(true);
      partRate.setDisable(true);
      salary.setDisable(false);
      managementType.getToggles().forEach(toggle -> {
         RadioButton tempButton = (RadioButton) toggle;
         tempButton.setDisable(false);
      });
   }

   @FXML
   /**
    * Event Handler for the add Employee button
    * @param event
    */
   void add(ActionEvent event) {
      show1.clear();
      Profile prof = createProfile();
      Employee tempEmp = null;
      String selectedEmp = null;
      try{
         if(empType.getSelectedToggle() != null)
            selectedEmp= ((RadioButton) empType.getSelectedToggle()).getText();
         else
            throw new Exception();
      }catch(Exception e){
         show1.appendText("Invalid Select Employee Type");
      }

      switch(selectedEmp) {
         case "Full Time":
         case "Management":
            double annualSalary = EMPTY;
            try{
               annualSalary = Double.parseDouble(salary.getText());
            }catch(Exception e){
               show1.appendText("Invalid Salary");
            }
            if(selectedEmp.equals("Full Time")) {
               tempEmp = new Fulltime(prof, annualSalary);
               break;
            }
            int manageCode = EMPTY;
            if(((RadioButton)managementType.getSelectedToggle()).getText().equals("Director"))
               manageCode = Management.DIRECTOR;
            else if(((RadioButton)managementType.getSelectedToggle()).getText().equals("Department Head"))
               manageCode = Management.DEPARTMENT_HEAD;
            else
               manageCode = Management.MANAGER;
            tempEmp = new Management(prof, annualSalary, manageCode);
            break;
         case "Part Time":
            double partTimeRate = EMPTY;
            try{
               partTimeRate = Double.parseDouble(partRate.getText());
            }catch(Exception e){
               show1.appendText(partTimeRate + "Invalid Hourly Rate for Partime Employee");
            }
            tempEmp = new Parttime(prof, partTimeRate, EMPTY);
         default:
            break;
      }
      try{
         if(!database.add(tempEmp))
            throw new Exception();
         else
            show1.appendText("Added Employee Successfully");
      }catch(Exception e){
         show1.appendText("Could not add Employee, Employee exists or invalid inputs");
      }
   }

   void remove(ActionEvent event){

   }
   /**
    *
    * @return
    */
   Profile createProfile() {
      String date = dateHired.getValue().toString();
      Date tempDate = new Date(date.substring(5,7) + "/" + date.substring(8) + "/" + date.substring(0, 4));
      Profile tempProf = null;
      String name = empName.getText().trim().toUpperCase();
      try{
         if(tempDate.isValid() && name.length() != EMPTY) {
            tempProf = new Profile(empName.getText(), ((RadioButton) department.getSelectedToggle()).getText(),
                              tempDate);
         }else{
            throw new IllegalArgumentException();
         }
      }catch(IllegalArgumentException e) {
         show1.appendText("Date hired or Name is invalid");
      }
      return tempProf;
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
