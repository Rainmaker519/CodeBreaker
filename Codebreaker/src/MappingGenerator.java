
/**
 * Generate plaintext possibilities for ciphertext letters.
 */
public interface MappingGenerator {

	/**
	 * Get possible mappings for the specified ciphertext character.
	 * 
	 * @param cipher
	 *          ciphertext character
	 * @return possible mappings, in the order in which they should be considered
	 */
	public Iterable<Character> getMappings ( char cipher );

}
