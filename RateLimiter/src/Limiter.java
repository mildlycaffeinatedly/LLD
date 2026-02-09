public interface Limiter {
    RateLimitResponse allow(String key);
}
