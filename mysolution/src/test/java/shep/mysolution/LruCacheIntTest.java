package shep.mysolution;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LruCacheIntTest {
	private LruCacheImpl<Integer, Integer> cache;

	@Before
	public void setUp() throws Exception {
		cache = new LruCacheImpl<Integer, Integer>(3);
	}

	@Test
	public void testPutTillFull() {
		cache.put(1, 1);
		cache.put(2, 2);
		String expected = "[1, 1]-[2, 2]-";
		
		assertEquals("Expected", expected, cache.dumpByAge());
		cache.put(3, 3);
		
		expected = "[1, 1]-[2, 2]-[3, 3]-";
		assertEquals("Expected", expected, cache.dumpByAge());
	}
	
	@Test
	public void testPutMoreToFull() {
		cache.put(1, 1);
		cache.put(2, 2);
		cache.put(3, 3);
		String expected = "[1, 1]-[2, 2]-[3, 3]-";
		
		assertEquals("Expected", expected, cache.dumpByAge());
		cache.put(4, 4); // evicts key 1
		
		expected = "[2, 2]-[3, 3]-[4, 4]-";
		assertEquals("Expected", expected, cache.dumpByAge());
	}

	@Test
	public void testGet() {
		cache.put(1, 1);
		cache.put(2, 2);
		cache.put(3, 3);
		String expected = "[1, 1]-[2, 2]-[3, 3]-";
		
		assertEquals("Expected", expected, cache.dumpByAge());
		cache.put(4, 4); // evicts key 1

		// Try get(4)
		assertEquals("Expected", Integer.valueOf(4), cache.get(4));
		expected = "[2, 2]-[3, 3]-[4, 4]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(3)
		assertEquals("Expected", Integer.valueOf(3), cache.get(3));
		expected = "[2, 2]-[4, 4]-[3, 3]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(2)
		assertEquals("Expected", Integer.valueOf(2), cache.get(2));
		expected = "[4, 4]-[3, 3]-[2, 2]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(1)
		assertEquals("Expected", null, cache.get(1));
		expected = "[4, 4]-[3, 3]-[2, 2]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// More put
		cache.put(5, 5);    // evicts key 4
		expected = "[3, 3]-[2, 2]-[5, 5]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(1)
		assertEquals("Expected", null, cache.get(1));
		expected = "[3, 3]-[2, 2]-[5, 5]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(2)
		assertEquals("Expected", Integer.valueOf(2), cache.get(2));
		expected = "[3, 3]-[5, 5]-[2, 2]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(3)
		assertEquals("Expected", Integer.valueOf(3), cache.get(3));
		expected = "[5, 5]-[2, 2]-[3, 3]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(4)
		assertEquals("Expected", null, cache.get(4));
		expected = "[5, 5]-[2, 2]-[3, 3]-";
		assertEquals("Expected", expected, cache.dumpByAge());
		
		// Try get(4)
		assertEquals("Expected", Integer.valueOf(5), cache.get(5));
		expected = "[2, 2]-[3, 3]-[5, 5]-";
		assertEquals("Expected", expected, cache.dumpByAge());
	}

}
