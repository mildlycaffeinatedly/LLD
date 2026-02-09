public class TokenBucket {
    private double tokenCount;
    private long lastRefillTimeMs;

    TokenBucket(double tokenCount, long lastRefillTime) {
        this.tokenCount = tokenCount;
        this.lastRefillTimeMs = lastRefillTime;
    }

    public double getTokensCount() {
        return tokenCount;
    }

    public void setTokensCount(double tokenCount) {
        this.tokenCount = tokenCount;
    }

    public long getLastRefillTime() {
        return this.lastRefillTimeMs;
    }

    public void setLastRefillTime(long time) {
        this.lastRefillTimeMs = time;
    }
}
