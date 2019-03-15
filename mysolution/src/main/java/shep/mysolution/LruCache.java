package shep.mysolution;

public interface LruCache<K, V> {
	public V get(K key);

	public void put(K key, V value);

	public int getMaxSize();
}
