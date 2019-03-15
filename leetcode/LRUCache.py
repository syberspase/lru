class Item(object):
    def __init__(self, key, value):
        self.key = key
        self.value = value
        
        self.prev = None
        self.next = None

class LRUCache(object):

    def __init__(self, capacity):
        """
        :type capacity: int
        """
        self.capacity = capacity
        self.cacheMap = {}
        self.head = None
        self.tail = None
        

    def get(self, key):
        """
        :type key: int
        :rtype: int
        """
        if key not in self.cacheMap:
            return -1
        self.touch(key)
        return self.cacheMap[key].value
    
    def touch(self, key):
        if self.size() != 1 and key != self.tail.key:
            latest = self.cacheMap[key]
            
            if key == self.head.key:
                self.head = latest.next
                self.tail = latest
                
            else:
                
                # disconnect
                latest.prev.next = latest.next
                latest.next.prev = latest.prev
                
                # append to tail
                latest.prev = self.tail
                latest.next = self.head
                
                # update head/tail
                self.tail.next = latest
                self.tail = latest
                self.head.prev = latest

    def put(self, key, value):
        """
        :type key: int
        :type value: int
        :rtype: None
        """
        if key in self.cacheMap:
            self.cacheMap[key].value = value
            self.touch(key)
            return

        self.remove_least()
        self.put_new(Item(key, value))
            
    def remove_least(self):
        if self.size() == self.capacity:
            if self.capacity == 1:
                del self.cacheMap[self.head.key]
                return
            k = self.head.key
            self.head.next.prev = self.tail
            self.tail.next = self.head.next
            self.head = self.head.next
            del self.cacheMap[k]
    
    def put_new(self, item):
        if self.size() == 0:
            self.head = item
            self.tail = item
        else:
            item.prev = self.tail
            item.next = self.head
            
            self.tail.next = item
            self.head.prev = item
            
            self.tail = item

            if self.head.next is None:
                self.head.next = item

        self.cacheMap[item.key] = item
        
    def size(self):
        return len(self.cacheMap)

