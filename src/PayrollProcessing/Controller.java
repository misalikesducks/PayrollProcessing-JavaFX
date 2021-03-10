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
import java.util.Scanner;

public class Controller {
   private Company database = new Company();

   public static final int EMPTY = 0;
   @FXML
   private TextField empName, hours, partRate, salary;

   @FXML
   private DatePicker dateHired;

   @FXML
   private TextArea show1;

//   @FXML
//   private RadioButton fullTime, partTime, management,
//            departmentHead, manager, director;
   @FXML
   private ToggleGroup department, managementType, empType;
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
   /**
    * Clear the tab pane
    */
   void clear(ActionEvent event){
      empName.clear();
      hours.clear();
      partRate.clear();
      salary.clear();
      dateHired.getEditor().clear();

      managementType.getToggles().forEach(toggle -> {
         RadioButton tempButton = (RadioButton) toggle;
         tempButton.setSelected(false);
      });
      department.getToggles().forEach(toggle -> {
         RadioButton tempButton = (RadioButton) toggle;
         tempButton.setSelected(false);
      });
      empType.getToggles().forEach(toggle -> {
         RadioButton tempButton = (RadioButton) toggle;
         tempButton.setSelected(false);
      });
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
      if(prof == null)
         return;
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
               return;
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
               return;
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
         return;
      }
      database.print();
   }

   @FXML
   void remove(ActionEvent event){
      Profile tempProfile = createProfile();
      Employee tempEmp = new Employee(tempProfile);
      try{
         if(tempProfile != null) {
            if(database.getNumEmployee() != EMPTY){
               if(!database.remove(tempEmp))
                  throw new Exception();
               else
                  show1.appendText("Employee Removed Successfully");
            }
         }
      }catch(Exception e){
         show1.appendText("Employee does not exist");
         return;
      }
   }

   @FXML
   void setHours(ActionEvent event){
      try{
         if(Integer.parseInt(hours.getText()) < 0 || Integer.parseInt(hours.getText()) > Parttime.ACTUAL_MAX_HOURS)
            throw new Exception();
         else{
            Profile tempProf = createProfile();
            if(tempProf != null){
               Employee tempEmp = new Parttime(tempProf,EMPTY, Integer.parseInt(hours.getText()));
               database.setHours(tempEmp);
            }
            return;
         }
      }catch(Exception e){
         show1.appendText("Hours not valid");
         return;
      }
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

   // TAB TWO

   @FXML
   void importDatabase(ActionEvent event){
      FileChooser chooser = new FileChooser();
      chooser.setTitle("Open Source File for the Import");
      chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
              new ExtensionFilter("All Files", "*.*"));
      Stage stage = new Stage();
      File sourceFile = chooser.showOpenDialog(stage); // get the reference of the source file

      //write code to read from the file
      try{
         Date tempDate = null;
         Profile tempProf = null;
         Employee tempEmp = null;

         Scanner sc = new Scanner(sourceFile);
         while(sc.hasNextLine()){
            String input = sc.nextLine();
            String[] arrOfInput = input.split(",", 0);

            tempDate = new Date(arrOfInput[3]);
            tempProf = new Profile(arrOfInput[1], arrOfInput[2], tempDate);
            if(arrOfInput[0].equals("P")) // parttime
               tempEmp = new Parttime(tempProf, Double.parseDouble(arrOfInput[4]), 0);
            else if(arrOfInput[0].equals("F")) // fulltime
               tempEmp = new Fulltime(tempProf, Double.parseDouble(arrOfInput[4]));
            else // management
               tempEmp = new Management(tempProf, Double.parseDouble(arrOfInput[4]), Integer.parseInt(arrOfInput[5]));
            database.add(tempEmp);
         }
      }catch(Exception e){
         show1.appendText("Database import failed.\n");
         return;
      }

   }

   @FXML
   void exportDatabase(ActionEvent event){ // should create a new text file
      // FileChooser chooser = new FileChooser();
      // Stage stage = new Stage();
      try {
         File targetFile = new File("companyDatabase.txt"); // creates output txt file
         targetFile.createNewFile();
         targetFile.getParentFile().mkdirs();
         PrintWriter pw = new PrintWriter(targetFile);
         pw.print(database.print()); //write to file
         pw.close();
      } catch(Exception e){
         show1.appendText("Database export failed.\n");
         return;
      }
   }

   @FXML
   void printDatabase(ActionEvent event){
      if(!checkEmpty(database))
         show1.appendText(database.print());
   }

   @FXML
   void printDatabaseByDepartment(ActionEvent event){
      if(!checkEmpty(database))
         show1.appendText(database.printByDepartment());
   }

   @FXML
   void printDatabaseByDate(ActionEvent event){
      if(!checkEmpty(database))
         show1.appendText(database.printByDate());
   }

   @FXML
   void calcPayment(ActionEvent event){
      if(!checkEmpty(database)) {
         database.processPayments();
         show1.appendText("Calculation of employee payments is done.\n");
      }
   }

   //helper method
   public boolean checkEmpty(Company database){
      if(database.getNumEmployee() == 0) {
         show1.appendText("Employee database is empty.\n");
         return true;
      }
      return false;
   }
}
