package com.test.occ;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Class to read a names file and compute the score for each name 
 * Requirment is as below 
   To score a list of names, you must sort it alphabetically and sum the individual scores for all the names.
   To score a name, sum the alphabetical value of each letter (A=1, B=2, C=3, etc...) and multiply the sum
   by the name’s position in the list (1-based).
   For example, when the sample data below is sorted into alphabetical order, LINDA, which is worth 12 +
   9 + 14 + 4 + 1 = 40, is the 4th name in the list. So, LINDA would obtain a score of 40 x 4 = 160. The
   correct score for the entire list is 3194. The correct score for the attached names.txt file is 871198282.
 * @author maneesh
 *
 */
public class OccNameScore {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Map<Character, Integer> charToMap = getCharToIntMap();
		List<String> inputList = readFileToNamesList(args[0]);
		System.out.println(" \n--- InputList Count ==== "+inputList.size());


		List<String> sortedList = inputList.parallelStream().sorted().collect(Collectors.toList());
		System.out.println("Sorted List Count "+sortedList.size());

		Map<String,Integer> listAndPositions =  new HashMap<String, Integer>();
		sortedList.forEach( name->{
			listAndPositions.put(name, sortedList.indexOf(name)+1);
		});


		long totalScoreForEntireList = sortedList.parallelStream().mapToLong(name -> {
			long scoreForName = getScoreForName(charToMap, listAndPositions, name);
			return scoreForName;
		}).sum();

		System.out.println(" --- Total Score for List = "+totalScoreForEntireList+"  ");

	}

	/**
	 * Reads the names from the file content
	 * @param fileName
	 * @return
	 */
	private static List<String> readFileToNamesList(String fileName) {
		List<String> inputList = new ArrayList<String>();
		try{
			Scanner sc = new Scanner(new File(fileName));  
			sc.useDelimiter(",");   //sets the delimiter pattern  
			while (sc.hasNext())    
			{  
				String name = sc.next();
				inputList.add(name.replaceAll("\"", ""));
				System.out.print(name);    
			}   
			sc.close(); 
		}catch(Exception e){

		}

		return inputList;
	}

	/**
	 * Computes the Base score and multiplies it with the position in the list
	 * @param charToMap
	 * @param listAndPositions
	 * @param fName
	 * @return
	 */
	private static long getScoreForName(Map<Character, Integer> charToMap, Map<String, Integer> listAndPositions,
			String fName) {
		long baseScore = getScoreForName(charToMap, fName);
		//System.out.println(" Position for "+fName+" is "+listAndPositions.get(fName));
		long nameScore = baseScore*listAndPositions.get(fName);
		return nameScore;
	}

	/**
	 * Computes the Base Score for any given name 
	 * @param charToMap
	 * @param fName
	 * @return
	 */
	private static long getScoreForName(Map<Character, Integer> charToMap, String fName) {
		//System.out.println(" ---------------------------------- Printing name "+fName);
		long nameScoreValue = 0;
		for (char nameChar : fName.toCharArray()) {
			//System.out.println("\t ************** Score for char "+nameChar+" = "+charToMap.get(Character.toLowerCase(nameChar)));
			nameScoreValue+=charToMap.get(Character.toLowerCase(nameChar));

		}
		//System.out.println("Base Score for "+fName+" = "+nameScoreValue);
		return nameScoreValue;
	}

	/**
	 * Utility Function to print any Map entry
	 * @param entry - Input map entry to print
	 */
	public static void printMap(Entry entry){
		System.out.println("Key - "+entry.getKey()+ " Value - "+
				entry.getValue());
	}

	/**
	 * Returns Character , Integer Map - This contains alphabetical sequenze and it's number 1- 26
	 * @return
	 */
	public static Map<Character, Integer> getCharToIntMap(){
		char curChar = 'a';
		Map<Character, Integer> numberToChar = new HashMap<Character, Integer>();
		for (int i = 1; i <= 26; i++) {
			numberToChar.put(curChar,i);
			curChar++;
		}
		return numberToChar;
	}

}
