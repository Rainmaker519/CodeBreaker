import java.util.Arrays;

public class FrequencyMappingGenerator implements MappingGenerator {

	
	public Iterable<Character> getMappings ( char cipher ) {
		Character[] order =
		    { 'e', 't', 'a', 'o', 'i', 'n', 's', 'h', 'r', 'd', 'l', 
		      'u', 'w', 'm', 'f', 'c', 'g', 'y', 'p', 'b', 'k', 'v', 'j', 'x', 'q', 'z' };
		return Arrays.asList(order);
	}
	
}
