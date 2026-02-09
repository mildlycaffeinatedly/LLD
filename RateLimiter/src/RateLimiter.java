import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RateLimiter {
    private final ConcurrentMap<String, Limiter> endpointToLimiter = new ConcurrentHashMap<>();
    private final Limiter defaultLimiter;

    public RateLimiter(Map<String, Object> externalConfig, Map<String, Object> defaultConfig, RateLimiterFactory rateLimiterFactory) {
        if(defaultConfig == null) {
            throw new IllegalArgumentException("Default configuration can not be null.");
        }

        if(externalConfig != null) {
            Object endpointObj = externalConfig.get("endpoint");
            if(endpointObj != null) {
                String endpoint = endpointObj.toString().trim().toLowerCase();
                Limiter limiter = rateLimiterFactory.create(externalConfig);
                endpointToLimiter.put(endpoint, limiter);
            }
        }

        this.defaultLimiter = rateLimiterFactory.create(defaultConfig);
    }

    public RateLimitResponse allow(String clientId, String endpoint) {
        String normalized = endpoint == null ? "" : endpoint.trim().toLowerCase();
        Limiter limiter = endpointToLimiter.getOrDefault(normalized, defaultLimiter);
        return limiter.allow(clientId);
    }
}
