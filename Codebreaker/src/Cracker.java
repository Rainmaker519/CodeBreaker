import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Cracker {

	private static long start_; // starting timestamp
	private static boolean testEnd = false;
	private static ArrayList<CipherAlphabet> valid = new ArrayList<>();
	/**
	 * Read in a ciphertext file.
	 * 
	 * @param filename
	 *          filename
	 * @return contents of the file, stripped of any non-letter characters and in
	 *         all caps
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String readFile ( String filename )
	    throws FileNotFoundException, IOException {
		Reader reader = new FileReader(filename);
		StringBuffer buffer = new StringBuffer();
		for ( ; true ; ) {
			int ch = reader.read();
			if ( ch == -1 ) {
				break;
			}
			if ( !Character.isLetter(ch) ) {
				continue;
			}
			buffer.append((char) ch);
		}
		reader.close();

		return buffer.toString().toUpperCase();
	}

	/**
	 * Crack the code - find and print all valid decryptions for the ciphertext. A
	 * valid decryption results in a collection of legal words.
	 * 
	 * @param ciphertext
	 *          the ciphertext
	 * @param generator
	 *          generator for cipher-to-plain mappings
	 * @param wordlist
	 *          legal words
	 */
	public static void crack ( String ciphertext, MappingGenerator generator,
	                           Lexicon wordlist ) {
		crack(new CipherAlphabet(),ciphertext,generator,wordlist);
	}

	/**
	 * Crack the code - find and print all valid decryptions for the ciphertext
	 * consistent with the mappings in the current cipher alphabet. A valid
	 * decryption results in a collection of legal words.
	 * 
	 * @param alphabet
	 *          cipher alphabet (may be partial)
	 * @param ciphertext
	 *          the ciphertext
	 * @param generator
	 *          generator for cipher-to-plain mappings
	 * @param wordlist
	 *          legal words
	 */
	private static void crack ( CipherAlphabet alphabet, String ciphertext,
	                            MappingGenerator generator, Lexicon wordlist ) {
		// no point in continuing to build this cipher alphabet if it is already not
		// a valid decryption
		if ( !isPlausible(alphabet,ciphertext,wordlist) ) {
			System.out.println("NOT PLAUSIBLE: " + decrypt(ciphertext,alphabet));
			return;
		}
		
		System.out.println("PLAUSIBLE: " + decrypt(ciphertext,alphabet));
		
		// next ciphertext letter to add to the alphabet
		char nextcipher = getNextUnmapped(ciphertext,alphabet);
		System.out.println("nextcip " + nextcipher);
		
		// if we have a complete mapping (all ciphertext letters are mapped), we are
		// done
		if ( nextcipher == 0) { // no unmapped ciphertext letters
			
			long elapsed = System.currentTimeMillis() - start_;
			System.out.println("elapsed time: " + elapsed);
			
			// decrypt the cipher text using the cipher alphabet
			String plain = decrypt(ciphertext,alphabet);
			
			// output the decryption nicely formatted (i.e. with spaces and
			// word-wrapped at 80 characters)
			System.out.println(alphabet.map);
			System.out.println("Final: ");
			output(spacify(plain,wordlist),80);
			valid.add(alphabet);
			testEnd = true;
			return;
		}

		// expand cipher alphabet by one mapping - try each possibility
		for ( char nextplain : generator.getMappings(nextcipher) ) {
			//if only testing for an encryption
			if (testEnd == true) {
				return;
			}
			
			//System.out.println("EEEEEEEEEEEEEE");
			// add the mapping to the cipher alphabet - only if not already being used
			//System.out.println("nextplain1: " + nextplain + ", " + "nextcipher1: " + nextcipher);
			if ( alphabet.containsPlain(nextplain) ) {
				continue;
			}

			System.out.println("nextplain: " + nextplain + ", " + "nextcipher: " + nextcipher);
			alphabet.addMapping(nextcipher,nextplain);

			// attempt to crack with the new mapping
			System.out.println(alphabet.map);
			crack(alphabet,ciphertext,generator,wordlist);

			// remove the mapping from the cipher alphabet
			alphabet.removeMapping(nextcipher,nextplain);
		}
		System.out.println("Num of valid solutions: " + valid.size());
	}

	/**
	 * Determine if the cipher alphabet is a plausible alphabet for the
	 * ciphertext, that is, if it could lead to a valid decryption of the
	 * ciphertext.
	 * 
	 * @param alphabet
	 *          cipher alphabet (may be partial)
	 * @param ciphertext
	 *          text to decrypt (all caps, letters only)
	 * @param wordlist
	 *          legal words
	 * @return true if the cipher alphabet is plausible, false otherwise
	 */
	public static boolean isPlausible ( CipherAlphabet alphabet,
	                                    String ciphertext, Lexicon wordlist ) {
		return isPlausible(alphabet,ciphertext,0,0,wordlist);
	}

	/**
	 * Determine if the cipher alphabet is a plausible alphabet for the
	 * ciphertext, that is, if it could lead to a valid decryption of the
	 * ciphertext. Assumes that the ciphertext up to start can be legally
	 * decrypted, and that from start to curpos-1 (inclusive) is consistent with a
	 * word or prefix of a word.
	 * 
	 * @param alphabet
	 *          cipher alphabet (may be partial)
	 * @param ciphertext
	 *          text to decrypt (all caps, letters only)
	 * @param start
	 *          starting index of current word-in-progress
	 * @param curpos
	 *          current position in the ciphertext string
	 * @param wordlist
	 *          legal words
	 * @return true if the cipher alphabet is plausible, false otherwise
	 */
	private static boolean isPlausible ( CipherAlphabet alphabet,
	                                     String ciphertext, int start, int curpos,
	                                     Lexicon wordlist ) {
		// if current position is at the end of the text, the alphabet is plausible
		if ( curpos == ciphertext.length() ) {
			return true;
		}

		// if there is not a mapping for the current ciphertext character, stop
		// checking - it's plausible so far...
		if ( !alphabet.containsCipher(ciphertext.charAt(curpos)) ) {
			System.out.println("newMappingThatsWhy");
			return true;
		}

		// decrypt the current string according to the cipher alphabet, since it is
		// the decryption that needs to be checked for being a word or prefix
		String substr = decrypt(ciphertext.substring(start,curpos + 1),alphabet);
		/*
		System.out.println("Substr for word check:  " + substr);
		String[] testCases = {"absolute", "baggage", "crustacean", "destitute", "erroneous"};
		CharTest wl = (CharTest) wordlist;
		System.out.println("/////////////// TestIndex: " + wl.runBinarySearchIteratively(testCases, "baggage", 0, 4));
		*/
		// should be able to decrypt everything if we passed the previous step
		assert substr.length() == curpos + 1 - start : "can't decrypt "
		    + ciphertext.substring(start,curpos + 1);

		// if we have a word, start a new word
		if ( wordlist.isWord(substr) ) {
			System.out.println("WORD!--------------------------------------------");
			boolean plausible =
			    isPlausible(alphabet,ciphertext,curpos + 1,curpos + 1,wordlist);
			if ( plausible ) {
				return plausible;
			}
		}

		// if we have a prefix, keep going with this word
		if ( wordlist.isPrefix(substr) ) {
			boolean plausible =
			    isPlausible(alphabet,ciphertext,start,curpos + 1,wordlist);//curpos + 1, just testing something
			if ( plausible ) {
				return plausible;
			}
		}

		// neither word nor prefix
		return false;
	}

	/**
	 * Apply the cipher alphabet to decrypt the specified ciphertext. Decryption
	 * stops if a ciphertext character is encountered that doesn't have a mapping
	 * in the cipher alphabet.
	 * 
	 * @param ciphertext
	 *          ciphertext to decrypt
	 * @param alphabet
	 *          cipher alphabet (may be partial)
	 * @return the decrypted text, up to the first character that doesn't have a
	 *         mapping in the cipher alphabet
	 */
	public static String decrypt ( String ciphertext, CipherAlphabet alphabet ) {
		StringBuffer plaintext = new StringBuffer();

		for ( int i = 0 ; i < ciphertext.length() ; i++ ) {
			char ch = ciphertext.charAt(i);
			if ( !alphabet.containsCipher(ch) ) {
				break;
			}
			plaintext.append(alphabet.getPlain(ch));
		}

		return plaintext.toString();
	}

	/**
	 * Get the next unmapped ciphertext character.
	 * 
	 * @param ciphertext
	 *          ciphertext
	 * @param alphabet
	 *          cipher alphabet (likely partial)
	 * @return the next ciphertext character without a mapping in the cipher
	 *         alphabet, or 0 if there is no such character
	 */
	public static char getNextUnmapped ( String ciphertext,
	                                     CipherAlphabet alphabet ) {
		// find first ciphertext character without a mapping
		for ( int i = 0 ; i < ciphertext.length() ; i++ ) {
			if ( !alphabet.containsCipher(ciphertext.charAt(i)) ) {
				System.out.println("nextunmapped: " + ciphertext.charAt(i));
				return ciphertext.charAt(i);
			}
		}
		// have a complete mapping for this ciphertext
		return 0;
	}

	/**
	 * Pretty-print text, wrapping lines so that no line is longer than the
	 * specified width.
	 * 
	 * @param text
	 *          text to print
	 * @param width
	 *          maximum line length
	 */
	public static void output ( String text, int width ) {
		String[] words = text.split(" ");
		int curwidth = 0;
		for ( int i = 0 ; i < words.length ; i++ ) {
			if ( curwidth + words[i].length() + 1 > width ) {
				System.out.print("\n");
				curwidth = 0;
			} else if ( i > 0 ) {
				System.out.print(" ");
			}
			System.out.print(words[i]);
			curwidth += words[i].length();
		}
		System.out.println();
	}

	/**
	 * Add spaces to a text so that (only) legal words are formed. If there is
	 * more than one way to do this, only one version is returned.
	 * 
	 * @param text
	 *          text (plaintext)
	 * @param wordlist
	 *          legal words
	 * @return a copy of the text with spaces inserted to separate words, or null
	 *         if that is not possible
	 */
	public static String spacify ( String text, Lexicon wordlist ) {
		return spacify(text,0,0,"",wordlist);
	}

	/**
	 * Add spaces to a text so that (only) legal words are formed. If there is
	 * more than one way to do this, only one version is returned.
	 * 
	 * @param text
	 *          text (plaintext)
	 * @param start
	 *          starting index of current word-in-progress
	 * @param curpos
	 *          current position in the text string
	 * @param result
	 *          space-ified version of text up to position 'start'
	 * @param wordlist
	 *          legal words
	 * @return a copy of the text with spaces inserted to separate words, or null
	 *         if that is not possible
	 */
	private static String spacify ( String text, int start, int curpos,
	                                String result, Lexicon wordlist ) {
		// if current position is at the end of the text, the task has been achieved
		if ( curpos == text.length() ) {
			return result.toString();
		}

		// get the current word-in-progress
		String substr = text.substring(start,curpos + 1);

		// if we have a word, add a space
		if ( wordlist.isWord(substr) ) {
			String spaced =
			    spacify(text,curpos + 1,curpos + 1,result + substr + " ",wordlist);
			if ( spaced != null ) {
				return spaced;
			}
		}

		// if we have a prefix, don't add a space
		if ( wordlist.isPrefix(substr) ) {
			String spaced = spacify(text,start,curpos + 1,result,wordlist);
			if ( spaced != null ) {
				return spaced;
			}
		}

		return null;
	}

	public static void main ( String[] args ) {
		Scanner scanner = new Scanner(System.in);

		try {
			// determine filenames for ciphertext and wordlist
			String ciphertextName;
			if ( args.length == 0 ) {
				System.out.print("enter filename for ciphertext: ");
				ciphertextName = scanner.next();
			} else {
				ciphertextName = args[0];
			}

			String wordlistName;
			if ( args.length < 2 ) {
				System.out.print("enter filename for word list: ");
				wordlistName = scanner.next();
			} else {
				wordlistName = args[1];
			}

			// read encrypted file
			String ciphertext = readFile(ciphertextName);
			System.out.println("ciphertext: " + ciphertext.length() + " characters");

			// decrypt
			start_ = System.currentTimeMillis();
			crack(ciphertext,new FrequencyMappingGenerator(),
			      new CharTest(wordlistName));

			long elapsed = System.currentTimeMillis() - start_;
			System.out.println("elapsed time: " + elapsed);

		} catch ( FileNotFoundException e ) {
			System.out.println(e.getMessage() + ": " + args[0] + " or " + args[1]);
		} catch ( IOException e ) {
			System.out.println(e.getMessage());
		}
		
		scanner.close();
	}

}
