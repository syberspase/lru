package shep.mysolution;

import java.util.HashMap;

/**
 * Assignment of implementation of simple LRU cache
 * 
 * @author Shep Liu
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class LruCacheImpl<K, V> implements LruCache<K, V> {
	/**
	 * Capacity of LRU cache.
	 */
	private int capacity;

	/**
	 * Cache.
	 */
	private HashMap<K, LruCacheItem<K, V>> cacheMap = new HashMap<K, LruCacheItem<K, V>>();

	/**
	 * Head of doubly linked list. Keeps the head always the least recent-used(LRU)
	 * item.
	 */
	private LruCacheItem<K, V> head;

	/**
	 * Tail of doubly linked list. Keeps tail always the most recent-used(MRU) item.
	 */
	private LruCacheItem<K, V> tail;

	/**
	 * Constructor with capacity size.
	 * 
	 * @param capacity Capacity size, it must be greater than 0.
	 */
	public LruCacheImpl(int capacity) {
		if (capacity < 1)
			throw new RuntimeException("Capacity size has to be greater than 0.");
		this.capacity = capacity;
	}

	/**
	 * Gets object from LRU cache by key.
	 */
	public V get(K key) {
		if (!cacheMap.containsKey(key))
			return null;
		touch(key);
		return cacheMap.get(key).value;
	}

	/**
	 * Adds object to LRU cache with key.
	 */
	public void put(K key, V value) {
		if (cacheMap.containsKey(key)) {
			cacheMap.get(key).value = value;
			touch(key);
			return;
		}
		removeLeaseRecent();
		putNew(new LruCacheItem<K, V>(key, value));
	}

	/**
	 * Get capacity of LRU cache.
	 */
	public int getMaxSize() {
		return capacity;
	}

	/**
	 * Dumps to string by age for unit testing.
	 * @return
	 */
	public String dumpByAge() {
        StringBuilder sb = new StringBuilder();
        LruCacheItem<K, V> item = head;
        while (item != null) {
        	sb.append(item.toString());
            sb.append('-');
            item = item.next;
            if (item == null || item.key == head.key)
                break;
        }
        return sb.toString();
	}

	/**
	 * Touches cache by key to make it latest item
	 * 
	 * @param key
	 */
	private void touch(K key) {
		if (cacheMap.size() != 1 && key != tail.key) {
			LruCacheItem<K, V> latest = cacheMap.get(key);

			// If it's head node in the linked list
			// move pointer of LRU to next of current head, MRU to current head.
			// No needs to break the chain.
			if (key.equals(head.key)) {
				head = latest.next;
				tail = latest;

			} else {
				// disconnect
				latest.prev.next = latest.next;
				latest.next.prev = latest.prev;

				// append to tail
				latest.prev = tail;
				latest.next = head;

				// update head/tail
				tail.next = latest;
				tail = latest;
				head.prev = latest;
			}
		}
	}

	/**
	 * Removes least recent-used item from cache when the cache is full. Keeps the
	 * head always the least recent-used item, tail the most.
	 */
	private void removeLeaseRecent() {
		// Cache is full.
		if (cacheMap.size() == capacity) {
			// When cache's capacity is 1, no need to bother.
			if (capacity == 1) {
				cacheMap.remove(head.key);
				return;
			}

			// Removes LRU node.
			K k = head.key;
			head.next.prev = tail;
			tail.next = head.next;
			head = head.next;
			cacheMap.remove(k);
		}
	}

	/**
	 * Adds Item instance to cache.
	 * 
	 * @param item
	 */
	private void putNew(LruCacheItem<K, V> item) {
		// First node in the cache.
		if (cacheMap.size() == 0) {
			head = item;
			tail = item;
		} else {
			// Adds new node to tail.
			item.prev = tail;
			item.next = head;

			tail.next = item;
			head.prev = item;

			tail = item;

			// Case: second node in the new constructed linked list.
			if (head.next == null)
				head.next = item;
		}

		cacheMap.put(item.key, item);
	}

	/**
	 * Main class for local dev test only.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		LruCache<Integer, Integer> cache = new LruCacheImpl<Integer, Integer>(3);
		cache.put(1, 1);
		cache.put(2, 2);
		cache.put(3, 3);
		cache.put(4, 4); // evicts key 1
		System.out.println(cache.get(4)); // returns 4
		System.out.println(cache.get(3)); // returns 3
		System.out.println(cache.get(2)); // returns 2
		System.out.println(cache.get(1)); // returns -1 (not found)
		cache.put(5, 5); // evicts key 4
		System.out.println(cache.get(1)); // returns -1 (not found)
		System.out.println(cache.get(2)); // returns 2
		System.out.println(cache.get(3)); // returns 3
		System.out.println(cache.get(4)); // returns -1 (not found)
		System.out.println(cache.get(5)); // returns 5
		System.out.println(((LruCacheImpl<Integer, Integer>) cache).dumpByAge());

	}
}
