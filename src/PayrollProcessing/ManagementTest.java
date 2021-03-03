package PayrollProcessing;

/**
 * JUnit test class for calculatePayment method in Management class
 * @author Connie Chen, Tiffany Lee
 */

import org.junit.Test;
//import org.junit.jupiter.api.Test;
import java.text.DecimalFormat;
import static org.junit.jupiter.api.Assertions.*;

class ManagementTest {

   @Test
   void calculatePayment() {
      String pattern = "###0.00";
      DecimalFormat formatNum = new DecimalFormat(pattern);

      // test case 1: calculates payment for Management Employee with role department head
      Date date1 = new Date("01/26/2001");
      Profile profile1 = new Profile("Wang,Brian", "CS",date1);
      Management emp1 = new Management(profile1,75500,2);
      emp1.calculatePayment();
      assertEquals("3269.23", formatNum.format(emp1.getPayment()));

      // test case #2: calculates payment for Management Employee with role manager
      Date date2 = new Date("04/01/2000");
      Profile profile2 = new Profile("Lin,Henry", "ECE", date2);
      Management emp2 = new Management(profile2, 85000, 1);
      emp2.calculatePayment();
      assertEquals("3461.54", formatNum.format(emp2.getPayment()));

      // test case #2: calculates payment for Management Employee with role director
      Date date3 = new Date("03/12/2001");
      Profile profile3 = new Profile("Li,Andy", "IT", date3);
      Management emp3 = new Management(profile3, 96756, 3);
      emp3.calculatePayment();
      assertEquals("4182.92", formatNum.format(emp3.getPayment()));
   }
}