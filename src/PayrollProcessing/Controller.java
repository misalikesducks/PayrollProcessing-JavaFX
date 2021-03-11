package PayrollProcessing;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Controller is the operational class for the JavaFX Payroll Processing GUI
 * @author Connie Chen, Tiffany Lee
 */
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
   private ToggleGroup department, managementType, empType;

   @FXML
   /**
    * Clears the Employee Management tab pane
    * @param event
    */
   void clear(ActionEvent event){
      empName.clear();
      hours.clear();
      partRate.clear();
      salary.clear();
      dateHired.setValue(null);
      show1.clear();

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
   /**
    * Disable not applicable items on Employee Management tab pane for Fulltime Employee
    * @param event
    */
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
    * Disable not applicable items on Employee Management tab pane for Parttime Employee
    * @param event
    */
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
   /**
    * Disable not applicable items on Employee Management tab pane for Maanagement Employee
    * @param event
    */
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
    * Event Handler for the Add Employee button
    * Adds Employee using user input to Company database
    * @param event
    */
   void add(ActionEvent event) {
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
         show1.appendText("Missing employee type.\n");
         return;
      }

      switch(selectedEmp) {
         case "Full Time":
         case "Management":
            double annualSalary = EMPTY;
            try{
               annualSalary = Double.parseDouble(salary.getText());
            }catch(Exception e){
               show1.appendText("Invalid salary.\n");
               return;
            }
            if(selectedEmp.equals("Full Time")) {
               tempEmp = new Fulltime(prof, annualSalary);
               break;
            }
            int manageCode = EMPTY;
            String manageToggle = null;
            try{
               if((RadioButton)managementType.getSelectedToggle() != null)
                  manageToggle = ((RadioButton)managementType.getSelectedToggle()).getText();
               else
                  throw new Exception();
            }catch(Exception e){
               show1.appendText("Management Type is not selected\n");
               return;
            }
            if(manageToggle.equals("Director"))
               manageCode = Management.DIRECTOR;
            else if(manageToggle.equals("Department Head"))
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
               show1.appendText("Invalid hourly rate for parttime employee.\n");
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
            show1.appendText("Employee added successfully.\n");
      }catch(Exception e){
         show1.appendText("Could not add employee, employee exists or invalid input(s).\n");
         return;
      }
      database.print();
   }

   @FXML
   /**
    * Event handler for the Remove Employee button
    * Removes specified Employee from Company database, unless Employee does not exist
    * @param event
    */
   void remove(ActionEvent event){
      Profile tempProfile = createProfile();
      Employee tempEmp = new Employee(tempProfile);
      try{
         if(tempProfile != null) {
            if(checkEmpty(database))
               return;
            if(database.getNumEmployee() != EMPTY){
               if(!database.remove(tempEmp))
                  throw new Exception();
               else
                  show1.appendText("Employee removed successfully.\n");
            }
         }
      }catch(Exception e){
         show1.appendText("Employee does not exist.\n");
         return;
      }
   }

   @FXML
   /**
    * Event handler for the Set Hours button
    * Sets hours of specified Employee with given user input
    * @param event
    */
   void setHours(ActionEvent event){
      try{
         if(Integer.parseInt(hours.getText()) < 0 || Integer.parseInt(hours.getText()) > Parttime.ACTUAL_MAX_HOURS)
            throw new Exception();
         else{
            Profile tempProf = createProfile();
            if(tempProf != null){
               if(checkEmpty(database))
                  return;
               Employee tempEmp = new Parttime(tempProf,EMPTY, Integer.parseInt(hours.getText()));
               database.setHours(tempEmp);
            }
            return;
         }
      }catch(Exception e){
         show1.appendText("Invalid hours.\n");
         return;
      }
   }

   /**
    * Creates a Profile object with user input from GUI
    * @return Profile of employee
    */
   Profile createProfile() {
      String date = null;
      Date tempDate = null;
      Profile tempProf = null;
      String name = empName.getText().trim().toUpperCase();
      try{
         for(int i = 0; i < name.length(); i++){
            if(Character.isDigit(name.charAt(i)))
               throw new Exception();
         }
      }catch(Exception e){
         show1.appendText("Please enter a valid name.\n");
         return tempProf;
      }

      try{
         if(dateHired.getValue() != null && name.length() != EMPTY) {
            date = dateHired.getValue().toString();
            tempDate = new Date(date.substring(5, 7) + "/" + date.substring(8) + "/" + date.substring(0, 4));
         }else
            throw new Exception();
      }catch(Exception e){
         show1.appendText("Date picker or name is empty.\n");
         return tempProf;
      }
      String selectedTog = null;
      try{
         if(department.getSelectedToggle() != null)
            selectedTog = ((RadioButton) department.getSelectedToggle()).getText();
         else
            throw new Exception();
      }catch(Exception e){
         show1.appendText("Employee department is missing.\n");
         return tempProf;
      }


      try{
         if(tempDate.isValid() && selectedTog != null) {
            tempProf = new Profile(empName.getText(), selectedTog,
                              tempDate);
         }else{
            throw new IllegalArgumentException();
         }
      }catch(IllegalArgumentException e) {
         show1.appendText("Profile information input is invalid or missing");
      }
      return tempProf;
   }

   @FXML
   /**
    * Event handler for Import Menu Item
    * Imports list of Employees from a selected text file
    * @param event
    */
   void importDatabase(ActionEvent event){
      FileChooser chooser = new FileChooser();
      chooser.setTitle("Open Source File for the Import");
      chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"),
              new ExtensionFilter("All Files", "*.*"));
      Stage stage = new Stage();
      File sourceFile = chooser.showOpenDialog(stage); // get the reference of the source file

      try{
         Date tempDate = null;
         Profile tempProf = null;
         Employee tempEmp = null;

         //read from the file
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
         show1.appendText("Database import successful.\n");
      }catch(Exception e){
         show1.appendText("Database import failed.\n");
         return;
      }
   }

   @FXML
   /**
    * Event handler for Export Menu Item
    * Exports current Company database into a new text file in user's chosen directory
    * @param event
    */
   void exportDatabase(ActionEvent event){
      if(!checkEmpty(database)) {
         try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open Target Directory for the Export");
            chooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
            File exportFile = chooser.showSaveDialog(new Stage());
            if(exportFile != null){
               PrintWriter pw = new PrintWriter(exportFile);
               pw.print(database.print()); //write to file
               pw.close();
               show1.appendText("Database export successful.\n");
            }
         } catch (Exception e) {
            show1.appendText("Database export failed.\n");
            return;
         }
      }
   }

   @FXML
   /**
    * Prints all Employee objects in the Company database as is to TextArea
    * @param event
    */
   void printDatabase(ActionEvent event){
      if(!checkEmpty(database)){
         show1.appendText("--Printing earning statements for all employees--\n");
         show1.appendText(database.print());
      }
   }

   @FXML
   /**
    * Prints all Employee objects in the Company database sorted by Department to TextArea
    * @param event
    */
   void printDatabaseByDepartment(ActionEvent event) {
      if (!checkEmpty(database)) {
         show1.appendText("--Printing earning statements by date hired--\n");
         show1.appendText(database.printByDepartment());
      }
   }

   @FXML
   /**
    * Prints all Employee objects in the Company database sorted by Date to TextArea
    * @param event
    */
   void printDatabaseByDate(ActionEvent event){
      if(!checkEmpty(database)){
         show1.appendText("--Printing earning statements by department--\n");
         show1.appendText(database.printByDate());
      }
   }

   @FXML
   /**
    * Calculates payment for all Employee objects in the Company database
    * @param event
    */
   void calcPayment(ActionEvent event){
      if(!checkEmpty(database)) {
         database.processPayments();
         show1.appendText("Calculation of employee payments is done.\n");
      }
   }

   /**
    * Returns whether a Company database is empty or not, and prints to TextArea if it is empty
    * @param database of the Employee objects
    * @return boolean, true if Company is empty, false otherwise
    */
   public boolean checkEmpty(Company database){
      if(database.getNumEmployee() == 0) {
         show1.appendText("Employee database is empty.\n");
         return true;
      }
      return false;
   }
}
