
/**
 * A collection of words.
 */
public interface Lexicon {

	/**
	 * Return true if the specified string is a word.
	 * 
	 * @param str
	 *          string
	 * @return true if the specified string is a word, false if not
	 */
	public boolean isWord ( String str );

	/**
	 * Return true if the specified string is a proper prefix of a word. (Returns
	 * false if 'str' is a word that is not itself a prefix of another word.)
	 * 
	 * @param str
	 *          string
	 * @return true if the specified string is a proper prefix of a word, false if
	 *         not
	 */
	public boolean isPrefix ( String str );

}
