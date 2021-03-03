package PayrollProcessing;

import java.util.Calendar;

/**
 * Date class represents String of Date object and implements the Java Interface Comparable.
 * Each Date object contains day, month, and year.
 * @author Connie Chen, Tiffany Lee
 */
public class Date implements Comparable<Date>{
   private int year;
   private int month;
   private int day;

   public static final int EQUAL = 0;
   public static final int LESS_THAN = -1;
   public static final int GREATER_THAN = 1;

   //Constants for the months and years
   public static final int QUADRENNIAL = 4;
   public static final int CENTENNIAL = 100;
   public static final int QUATERCENTENNIAL = 400;

   public static final int MAX_YEAR = 1900;

   public static final int JANUARY = 1;
   public static final int FEBRUARY = 2;
   public static final int MARCH = 3;
   public static final int APRIL = 4;
   public static final int MAY = 5;
   public static final int JUNE = 6;
   public static final int JULY = 7;
   public static final int AUGUST = 8;
   public static final int SEPTEMBER = 9;
   public static final int OCTOBER = 10;
   public static final int NOVEMBER = 11;
   public static final int DECEMBER = 12;

   //Constants for testing .isValid()
   public static final int LONG_MONTH = 31;
   public static final int SHORT_MONTH = 30;
   public static final int LEAP = 29;
   public static final int NOT_LEAP = 28;

   /**
    * Create an object with a given String.
    * @param date in String format of MM/DD/YYYY.
    */
   public Date(String date) {
      String[] arrOfDate = date.split("/", 0);
      year = Integer.parseInt(arrOfDate[2]);
      month = Integer.parseInt(arrOfDate[0]);
      day = Integer.parseInt(arrOfDate[1]);
   }

   //ACCESSOR METHODS

   /**
    * Accesses a Date object's data member month.
    * @return int representing the object's month.
    */
   public int getMonth() {
      return this.month;
   }

   /**
    * Accesses a Date object's data member year.
    * @return int representing the object's year.
    */
   public int getYear() {
      return this.year;
   }

   /**
    * Accesses a Date object's data member Day.
    * @return int representing the object's day.
    */
   public int getDay() {
      return this.day;
   }

   /**
    * Returns the date of an object in String format.
    * @return String of the date of the object in MM/DD/YYYY format.
    */
   public String toString() {
      return this.getMonth() + "/" + this.getDay() + "/" + this.getYear();
   }

   /**
    * Checks if a Date object has a valid date.
    * A valid date has the year after 1900 and does not surpass current date.
    * Leap year and valid days of a month is checked also.
    * @return true if the date is valid, false otherwise.
    */
   public boolean isValid() { //checks if a Date is valid
      Calendar c = Calendar.getInstance();
      int currMonth = c.get(Calendar.MONTH) + GREATER_THAN;

      if(this.year < MAX_YEAR || this.year > c.get(Calendar.YEAR))
         return false;

      if(this.month > DECEMBER || this.month < JANUARY)
         return false;

      //checks if the date is after our current date
      if((this.year == c.get(Calendar.YEAR) && this.month > currMonth) //same year, month is later
              || (this.year == c.get(Calendar.YEAR) && this.month == currMonth && this.day > c.get(Calendar.DATE))) //same year + month, day is later
         return false;

      if(this.year == c.get(Calendar.YEAR) && this.month == currMonth && this.day == c.get(Calendar.DATE)) //it's the current date
         return true;

      switch(this.month) {
         case APRIL:
         case JUNE:
         case SEPTEMBER:
         case NOVEMBER:
            if(this.day > SHORT_MONTH)
               return false;
            break;
         case JANUARY:
         case MARCH:
         case MAY:
         case JULY:
         case AUGUST:
         case OCTOBER:
         case DECEMBER:
            if(this.day > LONG_MONTH)
               return false;
            break;
         case FEBRUARY:
            if(this.year % QUADRENNIAL == 0){
               if(this.year % CENTENNIAL == 0) {
                  if(this.year % QUATERCENTENNIAL == 0) {
                     if(this.day > LEAP){
                        return false;
                     }
                  }
               } else {
                  if(this.day > LEAP) {
                     return false;
                  }
               }
            } else {
               if(this.day > NOT_LEAP)
                  return false;
            }
         default:
            break;
      }
      return true;
   }

   //0 if equals
   //1 if obj 1 > obj 2
   //-1 if obj1 < obj 2

   /**
    * Overrides default compareTo method from Comparable.
    * @param date object to be compared to.
    * @return 1, 0, or -1 if Date object is greater than, equals, or less than other Date object.
    */
   @Override
   public int compareTo(Date date) { // return 1, 0, or -1
      Calendar date1 = Calendar.getInstance();
      Calendar date2 = Calendar.getInstance();

      date1.set(Calendar.MONTH, this.getMonth());
      date1.set(Calendar.DATE, this.getDay());
      date1.set(Calendar.YEAR, this.getYear());

      date2.set(Calendar.MONTH, date.getMonth());
      date2.set(Calendar.DATE, date.getDay());
      date2.set(Calendar.YEAR, date.getYear());

      int compareDates = date1.compareTo(date2);
      if(this.getMonth() == date.getMonth() && this.getDay() == this.getDay() && this.getYear() == date.getYear())
         return EQUAL;
      else if(compareDates == LESS_THAN)
         return LESS_THAN;
      else
         return GREATER_THAN;
   }
}
