package PayrollProcessing;

import java.io.File;
import java.io.PrintWriter;

/**
 * Company class representing database of Employees.
 * @author Connie Chen, Tiffany Lee
 */

public class Company {
   private Employee[] emplist;
   private int numEmployee;

   public static final int NOT_FOUND = -1;
   public static final int CONTAINER_SIZE = 4;
   public static final int NOT_TRUE = 0;
   public static final boolean BY_DATE = true;
   public static final boolean BY_DEPARTMENT = false;

   //Constructor

   /**
    * Default Company constructor to create an empty Employee database.
    */
   public Company(){
      emplist = new Employee[CONTAINER_SIZE];
      numEmployee = 0;
   }

   //ACCESSOR METHOD

   /**
    * Accesses the numEmployee data member of a Company.
    * @return int representing the number of Employees in the Company.
    */
   public int getNumEmployee(){
      return numEmployee;
   }

   //HELPER METHODS

   /**
    * Finds the index number of the given Employee in the Company object.
    * @param employee being searched for.
    * @return int representing the index number of the Employee in the empList array.
    */
   private int find(Employee employee){
      int employeeInd = NOT_FOUND;
      for(int i = 0; i < numEmployee; i++){ //traverses through emplist array to find given employee
         if(emplist[i].getProfile().equals(employee.getProfile())){
            employeeInd = i;
            break;
         }
      }
      return employeeInd;
   }

   /**
    * Increases the Employee database capacity (empList) by 4.
    */
   private void grow(){
      int newMaxSize = emplist.length + CONTAINER_SIZE; //creates new arraySize
      Employee[] newEmpList = new Employee[newMaxSize];
      for(int i = 0; i < numEmployee; i++) //copies employees into new array
         newEmpList[i] = emplist[i];
      emplist = newEmpList;
   }

   /**
    * Inserts an Employee Object in the Company database.
    * @param employee to be added to the empList array of Company.
    * @return true if the Employee was added, false otherwise.
    */
   public boolean add(Employee employee){
      for(int i = 0; i < numEmployee; i++) {
         if(emplist[i].getProfile().equals(employee.getProfile()))
            return false;
      }
      if(numEmployee == emplist.length)
         grow();
      emplist[numEmployee] = employee;
      numEmployee++;
      return true;
   }

   /**
    * Removes the given Employee from the Company database.
    * @param employee to be removed.
    * @return true if the Employee was successfully removed, false otherwise.
    */
   public boolean remove(Employee employee){ //maintain the original sequence
      int targetInd = find(employee);
      if(targetInd == NOT_FOUND) //employee doesn't exist
         return false;

      for(int i = targetInd; i < numEmployee - 1; i++)
      {
         emplist[i] = emplist[i + 1];
         targetInd++;
      }

      emplist[numEmployee-1] = null;
      numEmployee--;
      return true;
   }

   /**
    * Modifies the hoursWorked for a Parttime Employee.
    * @param employee to be modified.
    * @return true if the working hours were successfully modified, false otherwise.
    */
   public boolean setHours(Employee employee){ //set working hours for a part time
      int foundIndex = find(employee);

      if(foundIndex == NOT_FOUND || !(employee instanceof  Parttime))
         return false;

      if(emplist[foundIndex] instanceof Parttime && employee instanceof Parttime) {
         ((Parttime) emplist[foundIndex]).setHoursWorked(((Parttime) employee).getHoursWorked());
         return true;
      }
      return false;
   }

   /**
    * Processes the payment of all Employees in the Company database.
    */
   public void processPayments(){ //process payments for all employees
      for(int i = 0; i < numEmployee; i++){
         emplist[i].calculatePayment();
      }
   }

   /**
    * Returns a String of all Employee Objects in the Company database as is.
    */
   public String print(){ //print earning statements for all employees
      String allEmps = "";

      for(int i = 0; i < numEmployee; i++)
         allEmps += emplist[i].toString() + "\n";

      return allEmps;
   }

   /**
    * Returns a String of all Employee Objects in the Company database sorted by Department.
    */
   public String printByDepartment(){ //print earning statements by department
      quickSort(this.emplist, 0, this.numEmployee - 1, BY_DEPARTMENT);
      return print();
   }

   /**
    * Returns a String of the list of Employee Objects in the Company by dateHired.
    */
   public String printByDate(){ //print earning statements by date hired
      quickSort(this.emplist, 0, this.numEmployee - 1, BY_DATE);
      return print();
   }

   /**
    * Sorts the empList array in ascending order depending on the sortType using the Quick Sort algorithm.
    * @param database of Employees.
    * @param low the beginning index of the array to be sorted.
    * @param high the ending index of the array to be sorted.
    * @param sortType defines whether sorting BY_DATE or BY_DEPARTMENT.
    */
   public void quickSort(Employee[] database, int low, int high, boolean sortType) {
      if(low < high) {
         int partitionIndex = partition(database, low, high, sortType);
         quickSort(database, low, partitionIndex - 1, sortType);
         quickSort(database, partitionIndex + 1, high, sortType);
      }
   }

   /**
    * Helper method for Quick Sort Algorithm that partitions the array to swap elements.
    * @param database of Employees.
    * @param low the beginning index of the array to partition.
    * @param high the ending index of the array to partition.
    * @param sortType defines whether sorting BY_DATE or BY_DEPARTMENT.
    * @return index of element to be partitioned.
    */
   public int partition(Employee[] database, int low, int high, boolean sortType) {
      Employee pivot = database[high];
      int small = (low - 1);

      Employee temp = null;

      for(int j = low; j < high; j++) {
         if(sortType == BY_DATE) {
            if(database[j].getProfile().getDate().compareTo(pivot.getProfile().getDate()) == NOT_FOUND) {
               small++;
               temp = database[small];
               database[small] = database[j];
               database[j] = temp;
            }
         } else {
            if(compareDepartment(database[j], pivot)) {
               small++;
               temp = database[small];
               database[small] = database[j];
               database[j] = temp;
            }
         }
      }
      temp = database[small + 1];
      database[small + 1] = database[high];
      database[high] = temp;
      return small + 1;
   }

   /**
    * Compares the department type of two Employee objects.
    * @param emp1 to be compared.
    * @param emp2 to be compared.
    * @return true if emp1 department type is alphabetically earlier than emp2's, false otherwise.
    */
   public boolean compareDepartment(Employee emp1, Employee emp2) {
      String department1 = emp1.getProfile().getDepartment();
      String department2 = emp2.getProfile().getDepartment();

      int compareDates = department1.compareTo(department2);

      return compareDates < NOT_TRUE;
   }

   /**
    * Writes the employee database from the array to given text file.
    * @param exportFile to be written to
    * @return true if export is successful, false otherwise
    */
   public boolean exportDatabase(File exportFile){
      try{
         PrintWriter pw = new PrintWriter(exportFile);
         pw.print(print()); //write to file
         pw.close();
         return true;
      }catch(Exception e){
         return false;
      }
   }
}
