/*
 * Name: Scott Martel
 * Course: CSI233 (Spring 2021)
 * Date: May 10, 2021
 * 
 * Description: A program used to automate the counting of votes from a .txt file.
 *              and output the results to a new txt file 
 *
 *              ** Files included to run for "parkingcommissioner" and "mayor"
 *              
 *              ** Bonus Attempted lines 118-128
 */

import java.util.*;

import java.io.*;

public class Voting {

   /** Constant for File extension (.txt) */
   static final String FILE_EXT = ".txt";
   
   /** Constant for result file name  */
   static final String _RESULTS = "_results";

   /** User input elected position, also becomes the file name to read from */
   static String electedPosition;

   /** Number of candidates in election */
   static int numCandidates;

   /** Names of candidates */
   static String[] candidates;

   /** Parrallel to candidates array that holds vote count for corresponding candidate */
   static int[] voteCount;
   
   /** ArrayList for invalid voter id's */
   static ArrayList<String> invalidVoterIds =  new ArrayList<String>();

   public static void main(String[] args) throws FileNotFoundException {

      Scanner console = new Scanner(System.in);

      // user input validation loop for elected position/name of file
      do {
         System.out.print("Enter elected position: ");
         electedPosition = console.nextLine();

         // if input has a blank space repeat else break out of loop
         if (electedPosition.contains(" "))
            System.out.println("Elected position cannot contain a space\n");
         else
            break;
      } while (true);
      
      // Combine user input for elected position with file extension 
      String fileName = electedPosition.concat(FILE_EXT);

      // Open user selected file
      Scanner inFile = new Scanner(new File(fileName));

      // Set the number of candidates based on file
      numCandidates = inFile.nextInt();
      inFile.nextLine(); // eat leftover

      // Initialize candidates array to correct size based on input file
      candidates = new String[numCandidates];

      // Save each candidate's name read from file to the candidates array
      for (int index = 0; index < numCandidates; index++)
         candidates[index] = inFile.nextLine();
      

      // Set vote counts to be parrallel to candidates array and all elements to 0
      voteCount = new int[numCandidates];
      Arrays.fill(voteCount, 0);

      // Read from file to count vote if voter id is a valid 6 digit number
      while (inFile.hasNext()) 
      {
         // Grab voter id from file
         String voterId = inFile.next();

         // If voter id is valid count vote else skip
         if (isValidId(voterId)) 
         {
            // read candidate number from file
            int candidate = inFile.nextInt();
  
            // Add vote count to corrensponding candidate (minus one to change to 0 based index)
            voteCount[candidate - 1] += 1;
         } 
         else 
         {
            // Add invalid id to invalid voter id array list
            invalidVoterIds.add(voterId);
            // Skip invalid voter ID's vote
            inFile.nextLine();
         }
      }
      
      // Close input file
      inFile.close();
      
      // filename for results output (same as input filename appended with _results)
      String resultsFile = electedPosition.concat(_RESULTS).concat(FILE_EXT);
      
      // Open output file stream to results file
      PrintWriter outFile = new PrintWriter(resultsFile);
      
      // Output fromtted results for each candidate 
      for (int index = 0; index < numCandidates; index++)
         outFile.printf("%s : %d votes%n", toFirstLast(candidates[index]), voteCount[index]);
      
      // Display results file name
      System.out.printf("%nElection results written to: %n%s%n", resultsFile);
      
      // Close output file
      outFile.close();
      
      // Display invalid voter ids if any
      if (!invalidVoterIds.isEmpty())
      {  
         System.out.printf("%nInvalid voter ids:%n");
         
         // set index to zero to iterate through invalidVoterIds ArrayList
         int index = 0;
         
         // Output voter ids in the ArrayList
         while (index < invalidVoterIds.size())
            System.out.println(invalidVoterIds.get(index++));
      }     
   }

   /** test if voter id is valid (numeric and is 6 digits long) */
   public static boolean isValidId(String voterId) {
      // Initialize result to false before testing
      boolean isValid = false;

      // convert voterId string to character array to verify each char in id string is numeric
      char[] tempId = voterId.toCharArray();

      // check if voterId contains 6 elements
      if (tempId.length == 6) {
         // check if each character is a digit
         for (int index = 0; index < tempId.length; index++) {
            // if any character in the array is not a digit immediately return false
            if (!Character.isDigit(tempId[index]))
               return false;
            else
               isValid = true;
         }
      }
      // false if voter id does not contain 6 elements 
      else 
      {
         isValid = false;
      }

      return isValid;
   }

   /** Covert name from last, first middle to first middle last */
   public static String toFirstLast(String name) {
      
      // get first name (everything after the comma)
      String firstName = name.substring(name.indexOf(",") + 1);
      
      // get last name (everything before the comma)
      String lastName = name.substring(0, name.indexOf(","));
      
      // combine first and last name with a space between and remove excess whitespace
      String fullName = firstName.concat(" ").concat(lastName).replaceAll("\\s+", " ").trim();
      
      return fullName;
   }

}