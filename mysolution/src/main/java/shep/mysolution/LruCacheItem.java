package shep.mysolution;

/**
 * Private class to represent linked list node.
 * 
 * @author Shep Liu
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class LruCacheItem<K, V> {
	public K key;
	public V value;
	public LruCacheItem<K, V> prev;
	public LruCacheItem<K, V> next;

	public LruCacheItem(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return "[" + key + ", " + value +"]";
	}
}
