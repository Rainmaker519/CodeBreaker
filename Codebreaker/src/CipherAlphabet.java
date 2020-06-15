import java.util.Hashtable;

/**
 * A cipher alphabet maps between plaintext and ciphertext letters.
 */
public class CipherAlphabet {
	
	public Hashtable<Character,Character> map; //In the form <cipher, plain>
	public Hashtable<Character,Character> reverseMap;

	public CipherAlphabet () {
		map = new Hashtable<>();
		reverseMap = new Hashtable<>();
	}

	/**
	 * Add a mapping to the cipher alphabet.
	 * 
	 * @param plain
	 *          plaintext character
	 * @param cipher
	 *          ciphertext character
	 */
	public void addMapping ( char cipher, char plain ) {
		map.put(cipher, plain);
		reverseMap.put(plain, cipher);
	}

	/**
	 * Remove a mapping from the cipher alphabet.
	 * 
	 * @param plain
	 *          plaintext character
	 * @param cipher
	 *          ciphertext character
	 */
	public void removeMapping ( char cipher, char plain ) {
		map.remove(cipher, plain);
	}

	/**
	 * Get the ciphertext letter corresponding to the specified plaintext letter.
	 * 
	 * @param plain
	 *          plaintext letter (must be present in the alphabet)
	 * @return corresponding ciphertext letter
	 */
	public char getCipher ( char plain ) {
		return reverseMap.get(plain);
	}

	/**
	 * Get the plaintext letter corresponding to the specified ciphertext letter.
	 * 
	 * @param cipher
	 *          ciphertext letter (must be present in the alphabet)
	 * @return corresponding plaintext letter
	 */
	public char getPlain ( char cipher ) {
		return map.get(cipher);
	}

	/**
	 * Determine if there is a ciphertext letter corresponding to the specified
	 * plaintext letter.
	 * 
	 * @param plain
	 *          plaintext letter
	 * @return if there is a corresponding ciphertext letter
	 */
	public boolean containsCipher ( char cipher ) {
		return map.containsKey(cipher);
	}

	/**
	 * Determine if there is a plaintext letter corresponding to the specified
	 * ciphertext letter.
	 * 
	 * @param cipher
	 *          ciphertext letter (must be present in the alphabet)
	 * @return if there is a corresponding plaintext letter
	 */
	public boolean containsPlain ( char plain ) {
		return map.containsValue(plain);
	}

}
