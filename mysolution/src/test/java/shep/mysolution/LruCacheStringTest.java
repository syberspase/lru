package shep.mysolution;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LruCacheStringTest {
	private LruCacheImpl<String, String> cache;

	@Before
	public void setUp() throws Exception {
		cache = new LruCacheImpl<String, String>(3);
	}

	@Test
	public void testPutTillFull() {
		cache.put("a", "a");
		cache.put("b", "b");
		String expected = "[a, a]-[b, b]-";
		
		assertEquals("Expected", expected, cache.dumpByAge());
		cache.put("c", "c");
		
		expected = "[a, a]-[b, b]-[c, c]-";
		assertEquals("Expected", expected, cache.dumpByAge());
	}
	
	@Test
	public void testPutMoreToFull() {
		cache.put("a", "a");
		cache.put("b", "b");
		cache.put("c", "c");
		String expected = "[a, a]-[b, b]-[c, c]-";
		
		assertEquals("Expected", expected, cache.dumpByAge());
		cache.put("d", "d"); // evicts key 1
		
		expected = "[b, b]-[c, c]-[d, d]-";
		assertEquals("Expected", expected, cache.dumpByAge());
	}

	@Test
	public void testGet() {
		cache.put("a", "a");
		cache.put("b", "b");
		cache.put("c", "c");
		String expected = "[a, a]-[b, b]-[c, c]-";
		
		assertEquals("Expected", expected, cache.dumpByAge());
		cache.put("d", "d"); // evicts key 1

		// Try get(4)
		assertEquals("Expected", "d", cache.get("d"));
		expected = "[b, b]-[c, c]-[d, d]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(3)
		assertEquals("Expected", "c", cache.get("c"));
		expected = "[b, b]-[d, d]-[c, c]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(2)
		assertEquals("Expected", "b", cache.get("b"));
		expected = "[d, d]-[c, c]-[b, b]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(1)
		assertEquals("Expected", null, cache.get("a"));
		expected = "[d, d]-[c, c]-[b, b]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// More put
		cache.put("e", "e");    // evicts key 4
		expected = "[c, c]-[b, b]-[e, e]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(1)
		assertEquals("Expected", null, cache.get("a"));
		expected = "[c, c]-[b, b]-[e, e]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(2)
		assertEquals("Expected", "b", cache.get("b"));
		expected = "[c, c]-[e, e]-[b, b]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(3)
		assertEquals("Expected", "c", cache.get("c"));
		expected = "[e, e]-[b, b]-[c, c]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(4)
		assertEquals("Expected", null, cache.get("d"));
		expected = "[e, e]-[b, b]-[c, c]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(4)
		assertEquals("Expected", "e", cache.get("e"));
		expected = "[b, b]-[c, c]-[e, e]-";
		assertEquals("Expected", expected, cache.dumpByAge());
	}

}
