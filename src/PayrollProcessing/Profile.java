package PayrollProcessing;

/**
 * Profile class represents the identifying profile for employees.
 * Each profile contains name, dateHired, and department.
 * @author Connie Chen, Tiffany Lee
 */
public class Profile {
   private String name; //employee's name in the form "lastname,firstname"
   private String department; //department code: CS, ECE, IT
   private Date dateHired; //the date the employee was hired as a Date object

   /**
    * Creates a Profile object.
    * @param name of the employee in the form of "lastname, firstname".
    * @param department where the employee was hired to.
    * @param dateHired of the employee.
    */
   public Profile(String name, String department, Date dateHired){
      this.name = name;
      this.department = department;
      this.dateHired = dateHired;
   }

   //ACCESSOR METHODS

   /**
    * Accesses the dateHired data member in Profile.
    * @return dateHired as Date object of the corresponding profile.
    */
   public Date getDate() {
      return dateHired;
   }

   /**
    * Accesses the department data member in Profile.
    * @return String representing the department the Profile is in.
    */
   public String getDepartment() {
      return department;
   }

   //HELPER METHODS

   /**
    * Overrides default method of toString from Java.Lang*.
    * @return String stating the name, department, and dateHired of the Profile
    */
   @Override
   public String toString() {
      return name + "::" + department + "::" + dateHired.toString();
   }

   /**
    * Overrides default equals method from Java.Lang*.
    * Checks if the current instance of Profile is equal to an object.
    * @param obj that could be an instance of Profile
    * @return true if the name, department, and dateHired of the two profiles are the same, false otherwise.
    */
   @Override
   public boolean equals(Object obj) { //compare name, department and dateHired
      if(obj instanceof Profile) {
         Profile comparingProfile = (Profile) obj;

         if(comparingProfile.dateHired.compareTo(this.dateHired) == 0
                 && comparingProfile.name.equals(this.name)
                 && comparingProfile.department.equals(this.department)){
            return true;
         }

      }
      return false;
   }
}
