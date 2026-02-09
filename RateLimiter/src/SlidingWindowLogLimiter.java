import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Instant;

public class SlidingWindowLogLimiter implements Limiter{
    
    int capacity;
    long windowMs;
    ConcurrentHashMap<String, Deque<Long>> keyToTimestampLog;
    
    SlidingWindowLogLimiter(int capacity, long windowMs) {
        if(capacity <= 0) {
            throw new IllegalArgumentException("Capacity has to be greater than zero.");
        }
         
        if(windowMs <= 0) {
            throw new IllegalArgumentException("Window size has to be greater than zero.");
        }

        this.capacity = capacity;
        this.windowMs = windowMs;
        this.keyToTimestampLog = new ConcurrentHashMap<>();
    }

    public RateLimitResponse allow(String key) {
        Deque<Long> queue = keyToTimestampLog.computeIfAbsent(key, k -> new ArrayDeque<Long>());

        synchronized(queue) {
            long nowMs = Instant.now().toEpochMilli();

            while(!queue.isEmpty() && queue.peek() < nowMs - windowMs)
                queue.poll();

            if(queue.size() < capacity) {
                queue.add(nowMs);

                return new RateLimitResponse(true, capacity - queue.size(), null);
            } else {
                long oldest = queue.peek();
                long waitMs = oldest - (nowMs - windowMs);
                return new RateLimitResponse(false, 0, waitMs);
            }
        }
    }
}
