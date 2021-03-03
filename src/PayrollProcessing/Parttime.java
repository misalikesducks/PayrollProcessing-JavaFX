package PayrollProcessing;

/**
 * Parttime extends Employee and includes specific data and operations for a Parttime Employee.
 * @author Connie Chen, Tiffany Lee
 */

public class Parttime extends Employee{

   private double hourlyRate;
   private int hoursWorked;

   public static final int MAX_HOURS = 80;
   public static final int ACTUAL_MAX_HOURS = 100;
   public static final double OVERTIME = 1.5;

   //CONSTRUCTOR

   /**
    * Creates a Parttime Object.
    * @param profile of the Parttime Employee.
    * @param hourlyRate of the Parttime Employee.
    * @param hoursWorked of the Parttime Employee.
    */
   public Parttime(Profile profile, double hourlyRate, int hoursWorked) {
      super(profile);
      this.hourlyRate = hourlyRate;
      this.hoursWorked = hoursWorked;
   }

   //ACCESSOR METHODS

   /**
    * Accesses the hourlyRate data member of Parttime object.
    * @return double representing the Parttime Employee's hourly wage.
    */
   public double getHourlyRate() {
      return hourlyRate;
   }

   /**
    * Accesses the hoursWorked data member of Parttime object.
    * @return int representing the hours worked of Parttime Employee.
    */
   public int getHoursWorked() {
      return hoursWorked;
   }

   //MODIFIER METHOD

   /**
    * Modifies the hoursWorked data member of a Parttime Employee.
    * @param hours a Parttime Employee has worked.
    */
   public void setHoursWorked(int hours){
      hoursWorked = hours;
   }

   //HELPER METHODS

   /**
    * Overrides the default toString method of Java.Lang*.
    * @return String stating the Profile, payment, hourly rate, and hours worked of the Parttime Employee.
    */
   @Override
   public String toString() {
      return super.toString() + "PART TIME::Hourly Rate " + convertToMoney(getHourlyRate()) +
              "::Hours worked this period: " + hoursWorked;
   }

   /**
    * Overrides the default equals method from Java.Lang*.
    * @param obj that could be an instance of a Parttime Employee.
    * @return true if two objects are both Parttime objects and have the same profile, false otherwise.
    */
   @Override
   public boolean equals(Object obj) {
      if(super.equals(obj) && (obj instanceof Parttime)){
         Parttime comparingParttime = (Parttime) obj;
         return this.getProfile().equals(comparingParttime.getProfile());
      }
      return false;
   }

   /**
    * Overrides the calculatePayment method in Employee.
    * Calculates the payment that a Parttime Employee should receive.
    * Sets payment data member.
    */
   @Override
   public void calculatePayment() {
      if(hoursWorked > MAX_HOURS)
         setPayment(hourlyRate * MAX_HOURS + (hourlyRate * OVERTIME * (hoursWorked - MAX_HOURS)));
      else
         setPayment(hourlyRate * hoursWorked);
   }
}
