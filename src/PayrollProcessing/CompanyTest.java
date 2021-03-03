package PayrollProcessing;

/**
 * JUnit test class for Company class
 * @author Connie Chen, Tiffany Lee
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {
   //hi
   @Test
   void add() {
      Company database = new Company();

      // test case #1: standard employee, department CS
      Date date1 = new Date("11/01/2000");
      Profile profile1 = new Profile("Chen,Connie", "CS", date1);
      Employee emp1 = new Employee(profile1);
      assertTrue(database.add(emp1));

      // test case #2: standard parttime employee, department IT
      Date date2 = new Date("01/26/2001");
      Profile profile2 = new Profile("Wang,Brian", "IT", date2);
      Parttime emp2 = new Parttime(profile2, 15, 20);
      assertTrue(database.add(emp2));

      // test case #3: Fulltime employee, no management role, department ECE
      Date date3 = new Date("08/16/2001");
      Profile profile3 = new Profile("Lee, Tiffany","ECE", date3);
      Fulltime emp3 = new Fulltime(profile3, 80030);
      assertTrue(database.add(emp3));

      // test case #4: Fulltime employee, management role, department IT
      Date date4 = new Date("12/12/2001");
      Profile profile4 = new Profile("Chen,Eva", "IT", date4);
      Management emp4 = new Management(profile4, 70000, 2);
      assertTrue(database.add(emp4));

      // test case #5: checks if company grows
      Date date5 = new Date("01/03/2001");
      Profile profile5 = new Profile("Li,Calvin", "CS", date5);
      Parttime emp5 = new Parttime(profile5, 12, 15);
      assertTrue(database.add(emp5));

      // test case #6: Employee already exists, does not add (should return false)
      Date date6 = new Date("08/16/2001");
      Profile profile6 = new Profile("Lee, Tiffany", "ECE", date6);
      Fulltime emp6 = new Fulltime(profile6, 80030);
      assertFalse(database.add(emp6));
   }

   @Test
   void remove() {
      Company database = new Company();

      // test case #1: removing an employee when company is empty (returns false)
      Date date1 = new Date("11/01/2000");
      Profile profile1 = new Profile("Chen,Connie", "CS", date1);
      Employee emp1 = new Employee(profile1);
      assertFalse(database.remove(emp1));

      // test case #2: company is not empty, removes an existing employee
      database.add(emp1);
      assertTrue(database.remove(emp1));

      // test case #3: company is not empty, removes a nonexistent employee (returns false)
      database.add(emp1);
      Date date2 = new Date("01/26/2001");
      Profile profile2 = new Profile("Wang,Brian", "IT", date2);
      Parttime emp2 = new Parttime(profile2, 15, 20);
      assertFalse(database.remove(emp2));
   }

   @Test
   void setHours() {
      Company database = new Company();

      // test case #1: setting hours for an employee in an empty company (returns false)
      Date date2 = new Date("01/26/2001");
      Profile profile2 = new Profile("Wang,Brian", "IT", date2);
      Parttime emp2 = new Parttime(profile2, 15, 20);
      assertFalse(database.setHours(emp2));

      // test case #2: setting hours for an employee that exists in company
      database.add(emp2);
      assertTrue(database.setHours(emp2));

      // test case #3: setting hours for an employee that does not exist in company
      Date date3 = new Date("02/26/2002");
      Profile profile3 = new Profile("Ross,Bob", "ECE", date3);
      Parttime emp3 = new Parttime(profile3, 15, 25);
      assertFalse(database.setHours(emp3));

      // test case #4: setting hours for an employee that is not parttime (returns false)
      Date date4 = new Date("12/12/2001");
      Profile profile4 = new Profile("Chen,Eva", "IT", date4);
      Management emp4 = new Management(profile4, 70000, 2);
      database.add(emp4);
      assertFalse(database.setHours(emp4));
   }
}