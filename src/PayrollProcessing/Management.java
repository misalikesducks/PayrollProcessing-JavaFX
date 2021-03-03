package PayrollProcessing;

/**
 * Management class implements objects that inherit properties of the Fulltime class.
 * Includes data and operations specific to Fulltime Employees with management roles.
 * @author Connie Chen, Tiffany Lee
 */

public class Management extends Fulltime {
   private double compensation;
   private int managementType;

   //Constants
   public static final int MANAGER = 1;
   public static final int DEPARTMENT_HEAD = 2;
   public static final int DIRECTOR = 3;
   public static final double MANAGER_EXTRA_COMP = 5000; //192.31
   public static final double DEPHEAD_EXTRA_COMP = 9500; //365.38
   public static final double DIRECTOR_EXTRA_COMP = 12000; //461.54

   //CONSTRUCTOR

   /**
    * Creates a Management object.
    * @param profile of the Fulltime Management Employee.
    * @param annualSalary of the Fulltime Management Employee.
    * @param managementType of the Fulltime Management Employee.
    */
   public Management(Profile profile, double annualSalary, int managementType){
      super(profile, annualSalary);

      this.managementType = managementType;

      if(this.managementType == MANAGER)
         compensation = MANAGER_EXTRA_COMP;
      else if(this.managementType == DEPARTMENT_HEAD)
         compensation = DEPHEAD_EXTRA_COMP;
      else if(this.managementType == DIRECTOR)
         compensation = DIRECTOR_EXTRA_COMP;
   }

   //HELPER METHODS

   /**
    * Overrides the default toString method from Java.Lang*.
    * @return String stating the Profile, payment, department type, and additional compensation.
    */
   @Override
   public String toString() {
      String compType = "";
      if(managementType == MANAGER)
         compType = "Manager Compensation";
      else if(managementType == DEPARTMENT_HEAD)
         compType = "DepartmentHead Compensation";
      else if(managementType == DIRECTOR)
         compType = "Director Compensation";

      return super.toString() + "::" + compType + " " + convertToMoney(compensation/Fulltime.PAY_PERIOD);
   }

   /**
    * Overrides the default equals method from Java.Lang*.
    * @param obj that could be an instance of Management.
    * @return true if Object is an instance of Management and has the same profile, false otherwise.
    */
   @Override
   public boolean equals(Object obj) { //compare name, department and dateHired
      if(super.equals(obj) && (obj instanceof Management)){
         Management comparingManagement = (Management) obj;
         return this.getProfile().equals(comparingManagement.getProfile());
      }
      return false;
   }

   /**
    * Overrides calculatePayment method from Fulltime.
    * Calculates the payment of the Fulltime Management Employee with the addition of their compensation.
    * Sets payments data member.
    */
   @Override
   public void calculatePayment() {
      setPayment((getAnnualSalary()+compensation)/Fulltime.PAY_PERIOD);
   }
}
