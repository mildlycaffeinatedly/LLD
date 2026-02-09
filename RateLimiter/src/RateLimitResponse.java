public class RateLimitResponse {
    private final boolean allowed;
    private final Integer remaining;
    private final Long retryAfterMs;

    public RateLimitResponse(boolean allowed, Integer remaining, Long retryAfterMs) {
        this.allowed = allowed;
        this.remaining = remaining;
        this.retryAfterMs = retryAfterMs;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public Long getRetryAfterMs() {
        return retryAfterMs;
    }
}
