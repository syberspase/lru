class LRUCache {
	private int capacity;
	private HashMap cacheMap = new HashMap();
	private Item head;
	private Item tail;

    public LRUCache(int capacity) {
		this.capacity = capacity;
    }
    
    public int get(int key) {
		if (!cacheMap.containsKey(key))
			return -1;
		touch(key);
		return ((Item)cacheMap.get(key)).value;
    }
    
    public void put(int key, int value) {
		if (cacheMap.containsKey(key)) {
			((Item)cacheMap.get(key)).value = value;
			touch(key);
			return;
		}
		removeLeaseRecent();
		putNew(new Item(key, value));
    }
	private void touch(int key) {
		if (cacheMap.size() != 1 && key != tail.key) {
			Item latest = (Item)cacheMap.get(key);

			if (key == head.key) {
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

	private void removeLeaseRecent() {
		if (cacheMap.size() == capacity) {
			if (capacity == 1) {
				cacheMap.remove(head.key);
				return;
			}
			Object k = head.key;
			head.next.prev = tail;
			tail.next = head.next;
			head = head.next;
			cacheMap.remove(k);
		}
	}

	private void putNew(Item item) {
		if (cacheMap.size() == 0) {
			head = item;
			tail = item;
		} else {
			item.prev = tail;
			item.next = head;

			tail.next = item;
			head.prev = item;

			tail = item;

			if (head.next == null)
				head.next = item;
		}

		cacheMap.put(item.key, item);
	}
    private class Item {
		public int key;
		public int value;
		public Item prev;
		public Item next;

		public Item(int key, int value) {
			this.key = key;
			this.value = value;
		}
	}

}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */