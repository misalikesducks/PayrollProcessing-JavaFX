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
         show1.appendText("Invalid Select Employee Type\n");
      }

      switch(selectedEmp) {
         case "Full Time":
         case "Management":
            double annualSalary = EMPTY;
            try{
               annualSalary = Double.parseDouble(salary.getText());
            }catch(Exception e){
               show1.appendText("Invalid Salary\n");
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
               show1.appendText("Invalid Hourly Rate for Partime Employee\n");
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
            show1.appendText("Added Employee Successfully\n");
      }catch(Exception e){
         show1.appendText("Could not add Employee, Employee exists or invalid inputs\n");
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
                  show1.appendText("Employee Removed Successfully\n");
            }
         }
      }catch(Exception e){
         show1.appendText("Employee does not exist\n");
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
         show1.appendText("Hours not valid\n");
         return;
      }
   }

   /**
    *
    * @return
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
         show1.appendText("Please Enter Valid Name!\n");
         return tempProf;
      }

      try{
         if(dateHired.getValue() != null && name.length() != EMPTY) {
            date = dateHired.getValue().toString();
            tempDate = new Date(date.substring(5, 7) + "/" + date.substring(8) + "/" + date.substring(0, 4));
         }else
            throw new Exception();
      }catch(Exception e){
         show1.appendText("Date Picker or Name is Empty!\n");
         return tempProf;
      }

      try{
         if(tempDate.isValid()) {
            tempProf = new Profile(empName.getText(), ((RadioButton) department.getSelectedToggle()).getText(),
                              tempDate);
         }else{
            throw new IllegalArgumentException();
         }
      }catch(IllegalArgumentException e) {
         show1.appendText("Date hired or Name is invalid\n");
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
      if(!checkEmpty(database)) {
         try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open Target File for the Export");
            chooser.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
            File exportFile = chooser.showSaveDialog(new Stage());
            if(exportFile != null){
               PrintWriter pw = new PrintWriter(exportFile);
               pw.print(database.print()); //write to file
               pw.close();
            }
            /*FileChooser chooser = new FileChooser();
            chooser.setTitle("Open Target File for the Export");
            chooser.getExtensionFilters().addAll(new ExtensionFilter("Text Files", "*.txt"));
            Stage stage = new Stage();
            File targetFile = chooser.showSaveDialog(stage); //get the reference of the target file

            PrintWriter pw = new PrintWriter(targetFile);
            pw.print(database.print()); //write to file
            pw.close();*/
         } catch (Exception e) {
            show1.appendText("Database export failed.\n");
            return;
         }
      }
   }

   @FXML
   void printDatabase(ActionEvent event){
      if(!checkEmpty(database)){
         show1.appendText("--Printing earning statements for all employees--\n");
         show1.appendText(database.print());
      }
   }

   @FXML
   void printDatabaseByDepartment(ActionEvent event) {
      if (!checkEmpty(database)) {
         show1.appendText("--Printing earning statements by date hired--\n");
         show1.appendText(database.printByDepartment());
      }
   }

   @FXML
   void printDatabaseByDate(ActionEvent event){
      if(!checkEmpty(database)){
         show1.appendText("--Printing earning statements by department--\n");
         show1.appendText(database.printByDate());
      }
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
