import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;

public class CharTest implements Lexicon {
	Hashtable<Integer,String> table;
	String[] sortTable;
	int left;
	int right;
	int middle;
	
	/**
	 * The constructor for the object which holds the lexicon
	 * 
	 * @param filename The string name of the file which has the potential words
	 */
	public CharTest (String filename) {
		table = new Hashtable<>();
		try {
			Reader reader = new FileReader(filename);
			Scanner s = new Scanner(reader);
			//sortTable = new String[s.];
			int count = 1; 
			while (s.hasNextLine()) {
				String str = s.nextLine();
				table.put(count,str);
				count++;
			}
			s.close();
			sortAlphabetical();
		} catch (FileNotFoundException e) {
			System.out.println("failed file import");
		}
	}

	/**
	 * Returns a list of all the words which the prefix matches, with the prefix removed from the start
	 * 
	 * @param pre The prefix to check
	 * @return A list of all the words which the prefix matches, with the prefix removed from the start
	 */
	public ArrayList<String> prefixList(String pre) {
		ArrayList<String> list = new ArrayList<>();
			
		if (pre == "") {
		}
		else {
			for (int i = 1; i < table.size() + 1; i++) {
				if (table.get(i).length() >= pre.length() && table.get(i).substring(0, pre.length()).equals(pre)) {
					list.add(table.get(i).substring(pre.length()));
				}
			}
		}
		return list;
		}

	public boolean isWord(String word) {//B.txt-1000 for an hour didn't finish
		System.out.println("Is Word?: " + word);
		// - Use this to test iterative implementation rather than binaryCheck implementation (runs better on small wordlists)
		for (int i =1; i < table.size() + 1; i++) {
			if (table.get(i).equals(word)) {
				return true;
			}
		}
		return false;
		/*
		return isWordBinaryCheck(word);//the binarysearch version which works poorly on the sets 100 words or less
		*/
	}
	
	public boolean isWordBinaryCheck(String word) {//somehow this is slower i dont understand
		int binaryCheck = runBinarySearchIteratively(sortTable, word, 0, table.size()-1);
		if (binaryCheck == 2147483647) {
			return false;
		}
		System.out.println("binaryCheck: " + table.get(binaryCheck + 1) + " || " + "word: " + word);
		System.out.println("FOUND-A-WORD");
		System.out.println("bcheck-index: " + binaryCheck);
		
		System.out.println(binaryCheck + "-----------------------------------------------------------------------");
		if (table.get(binaryCheck+1).equals(word)) {
			return true;
		}
		else {
			return false; 
		}
	}
	
	public int runBinarySearchIteratively( String[] sortedArray, String key, int low, int high) {
		int index = Integer.MAX_VALUE;
		System.out.println("Strt"); 
		while (low <= high) {
			System.out.println(low);
			System.out.println(high);
			int mid = (low + high) / 2;
			System.out.println(mid);
			System.out.println("key: " + key);
			System.out.println(sortedArray[mid]);
			if (alphabCheck(sortedArray[mid],key)) {
				low = mid + 1;
			} else if (alphabCheck(key,sortedArray[mid])) {
			    high = mid - 1;
			} else {
			    index = mid;
			    System.out.println("MATCH");
			    break;
			}
			//System.out.println("missed but heres mid " + mid);
		}
		return index;
	}
	
	public int runPrefixBinarySearch( String[] sortedArray, String key, int low, int high) {
		int index = Integer.MAX_VALUE;
		System.out.println("Strt"); 
		while (low <= high) {
			System.out.println(low);
			System.out.println(high);
			int mid = (low + high) / 2;
			System.out.println(mid);
			System.out.println("key: " + key);
			System.out.println(sortedArray[mid]);
			if (alphabCheck(sortedArray[mid].substring(0, key.length()),key)) {
				low = mid + 1;
			} else if (alphabCheck(key,sortedArray[mid].substring(0, key.length()))) {
			    high = mid - 1;
			} else {
			    index = mid;
			    System.out.println("MATCH");
			    break;
			}
			//System.out.println("missed but heres mid " + mid);
		}
		return index;
	}

	private boolean alphabCheck(String first, String second) { //returns true if the first is greater than the second
		if (first.length() == 1 || second.length() == 1) {
			if (first.compareTo(second) < 0) {
				return true;
			}
		}
		else {
			if (first.substring(0, 1).compareTo(second.substring(0,1)) < 0) {
				return true;
			}
			else {
				if (first.substring(0, 1).compareTo(second.substring(0,1)) == 0) {
					return alphabCheck(first.substring(1, first.length()),second.substring(1,second.length()));
				}
				return false;
			}
		}
		return false;
	}

	public boolean isPrefix(String pre) {
		if (pre.equals("")) {
			return true;
		}
		else {
			for (int i = 1; i < table.size() + 1; i++) {
				if (table.get(i).length() >= pre.length() && table.get(i).substring(0, pre.length()).equals(pre)) {
					return true;
				}	
			}
			return false;
		}
		
		//return isPrefixBinary(pre);
	}
	/*
	public boolean isPrefixBinary(String preSmaller) {
		if (preSmaller.equals("")) {
			return true;
		}
		else {
			if (table.get(runPrefixBinarySearch(sortTable, preSmaller, 0, table.size()-1)) == null) {
				return false;
			}
			else if (table.get(runPrefixBinarySearch(sortTable, preSmaller, 0, table.size()-1)).substring(0, preSmaller.length()).equals(preSmaller)) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	*/
	
	public void sortAlphabetical() {
		Hashtable<Integer,String> newTable = new Hashtable<>();
		String[] temp = new String[table.size()];
		for (int i = 1; i < table.size()+1; i++) {
			temp[i-1] = table.get(i);
		}
		Arrays.sort(temp);
		for (int i = 1; i < table.size()+1; i++) {
			//System.out.println(temp[i-1]);
			newTable.put(i,temp[i-1]);
		}
		sortTable = temp;
		table = newTable;
	}
	/*
	public String spacify(String text, int curpos, int start, String result) {
		//try to make so it recognizes
		//longer words with higher priority than for instance single letter words like "a"
		System.out.println("Result: " + result);
		if (curpos >= text.length()) {//if the current position is at the end of the text return
			return result;
		}
		else if (isWord(text.substring(start, curpos))){//if text[start..curpos] is a word
			System.out.println("is word");
			spacify(text,curpos,curpos,result + text.substring(start, curpos) +" ");
		}
		else if (!(prefixList(text.substring(start, curpos)).isEmpty())) {//if text[start..curpos] is a prefix of a word
			System.out.println("is pre");
			spacify(text,curpos+1,start,result);
		}
		return result;
	}
	*/
}


	

