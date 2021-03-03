package PayrollProcessing;

/**
 * Fulltime class extends the Employee class and includes specific data and operations to a full-time employee.
 * @author Connie Chen, Tiffany Lee
 */

public class Fulltime extends Employee {

   private double annualSalary;

   public static final int PAY_PERIOD = 26;

   /**
    * Creates a Fulltime object.
    * @param profile of the Fulltime employee.
    * @param annualSalary of the Fulltime employee.
    */
   public Fulltime(Profile profile, double annualSalary){
      super(profile);
      this.annualSalary = annualSalary;
   }

   //ACCESSOR METHOD

   /**
    * Accesses the annualSalary data member in Fulltime.
    * @return double representing annualSalary of Fulltime employee.
    */
   public double getAnnualSalary(){
      return annualSalary;
   }

   /**
    * Overrides default method of toString from Java.Lang*.
    * @return String
    */
   @Override
   public String toString() {
      return super.toString() + "FULL TIME::Annual Salary " + convertToMoney(annualSalary);
   }

   /**
    * Overrides default equals method from Java.Lang*.
    * @param obj that could be an instance of Employee.
    * @return true if two Fulltime employees are the same, false otherwise.
    */
   @Override
   public boolean equals(Object obj) {
      if(super.equals(obj) && (obj instanceof Fulltime)){
         Fulltime comparingFulltime = (Fulltime) obj;
         return this.getProfile().equals(comparingFulltime.getProfile());
      }
      return false;
   }

   /**
    * Overrides calculatePayment method from Employee class.
    * Sets payment data member.
    */
   @Override
   public void calculatePayment() {
      setPayment(annualSalary/PAY_PERIOD);
   }
}
