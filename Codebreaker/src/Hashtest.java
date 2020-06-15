import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Hashtest {
	
	public static Hashtable<Integer,String> table;
	
	
	//I need:
	//1. A representation of the cipher alphabet
	//2. A test for if the cipher alphabet is plausible
	//3. A way to determine the next possible mappings to add to the current cipher alphabet
	

	public static void main(String[] args) {
		CharTest test = new CharTest("words-10000");
		String alltext = "aisthewordheusthe ";//needs a space at end rn for some reason
		String finishedresult = "";
	
		//System.out.println(spacify(alltext,0,0,finishedresult));
		System.out.println(test.isPrefix("abs"));
	}
	
	

}
