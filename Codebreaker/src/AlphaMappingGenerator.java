import java.util.Arrays;

/**
 * Generates possible plaintext mappings in alphabetical order.
 */
public class AlphaMappingGenerator implements MappingGenerator {

	@Override
	public Iterable<Character> getMappings ( char cipher ) {
		Character[] order =
		    { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
		      'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
		return Arrays.asList(order);
	}
}
