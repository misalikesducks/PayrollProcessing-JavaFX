package PayrollProcessing;

import java.text.DecimalFormat;

/**
 * Employee class implements the general employee of a Company.
 * Stored in Company class array.
 * @author Connie Chen, Tiffany Lee
 */
public class Employee {
   private Profile profile;
   private double payment;

   //CONSTRUCTOR

   /**
    * Creates an Employee object.
    * @param profile identifying the Employee.
    */
   public Employee(Profile profile) {
      this.profile = profile;
      this.payment = 0; //set payment = 0
   }

   //ACCESSOR METHODS

   /**
    * Accesses the Profile data member of Employee.
    * @return Profile of the Employee object.
    */
   public Profile getProfile(){
      return profile;
   }

   /**
    * Accesses the payment data member of Employee.
    * @return payment of the Employee object.
    */
   public double getPayment() {
      return payment;
   }

   //MODIFIER METHOD

   /**
    * Modifies the payment data member of Employee.
    * @param pay of the Employee to be modified.
    */
   public void setPayment(double pay){
      payment = pay;
   }


   //HELPER METHODS

   /**
    * Overrides default toString method from Java.Lang*.
    * @return String stating the Profile and payment of the Employee.
    */
   @Override
   public String toString() {
      return profile.toString() + "::Payment " + convertToMoney(payment) + "::";
   }

   /**
    * Overrides the equals method from Java.Lang*.
    * Checks if the current instance of Employee is equal to the given Object.
    * @param obj that could be an instance of Employee.
    * @return true if Object is an instance of Employee and has the same profile, false otherwise.
    */
   @Override
   public boolean equals(Object obj) {
      if(obj instanceof Employee) {
         Employee comparedEmp = (Employee)obj;
         if(comparedEmp.profile.equals(this.profile))
            return true;
      }
      return false;
   }

   /**
    * General method for sub-classes to implement.
    * Calculates Employee's payment.
    */
   public void calculatePayment() {
   }

   /**
    * Converts a Double object into formatted Object that represents currency.
    * @param number to be converted to money.
    * @return String representing the amount in currency format.
    */
   public String convertToMoney(double number){
      String pattern = "$#,##0.00";
      DecimalFormat formatMoney = new DecimalFormat(pattern);
      return formatMoney.format(number);
   }
}
