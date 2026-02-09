import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketLimiter implements Limiter{
    private final double capacity;
    private final double refillRatePerSecond;
    private ConcurrentHashMap<String, TokenBucket> keyToBucketMap;

    public TokenBucketLimiter(double capacity, double refillRatePerSecond) {
        if(capacity <= 0.0) {
            throw new IllegalArgumentException("Capacity has to be greater than zero.");
        }
        if(refillRatePerSecond <= 0.0) {
            throw new IllegalArgumentException("Refill rate has to be greater than zero.");
        }
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
        this.keyToBucketMap = new ConcurrentHashMap<>();
    }

    public RateLimitResponse allow(String key) {
        if(key == null || key.isBlank()) {
            throw new IllegalArgumentException("Key can not be null or empty.");
        }

        long nowMs = Instant.now().toEpochMilli();

        TokenBucket bucket = keyToBucketMap.computeIfAbsent(key, k -> new TokenBucket(capacity, nowMs));

        synchronized (bucket) {
            long elapsedMs = nowMs - bucket.getLastRefillTime();
            double tokensToAdd = (elapsedMs/1000.0) * refillRatePerSecond;
            
            bucket.setTokensCount(Math.min(capacity, bucket.getTokensCount() + tokensToAdd));
            bucket.setLastRefillTime(nowMs);
        

            if(bucket.getTokensCount() >= 1.0) {
                bucket.setTokensCount(bucket.getTokensCount() - 1.0);
                return new RateLimitResponse(true, (int)Math.floor(bucket.getTokensCount()), null);
            } 
            
            long waitMs = (long)Math.ceil((1.0 - bucket.getTokensCount()) * 1000.0/refillRatePerSecond);
            return new RateLimitResponse(false, 0, waitMs);
        }
    }
}
