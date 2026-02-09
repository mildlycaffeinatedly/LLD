import java.util.Map;

public class RateLimiterFactory {
    public Limiter create(Map<String, Object> externalConfig) {
        Object nameObj = externalConfig.get("name");
        LimiterType algoName;

        if(nameObj instanceof LimiterType) {
            algoName = (LimiterType)nameObj;
        } else if(nameObj instanceof String) {
            try {
                algoName = LimiterType.valueOf(((String)nameObj).trim().toUpperCase());
            } catch(IllegalArgumentException e) {
                throw new IllegalArgumentException("Unknown limiter type: " + nameObj, e);
            }
        } else {
            throw new IllegalArgumentException("Missing or invalid 'name' in config");
        }

        switch (algoName) {
            case TOKEN_BUCKET: {
                int capacity = toInt(externalConfig.get("capacity"), 0);
                double refillRate = toDouble(externalConfig.get("refillRate"), 0.0);
                return new TokenBucketLimiter((double)capacity, refillRate);
            }
            case SLIDING_WINDOW_LOG: {
                int capacity = toInt(externalConfig.get("capacity"), 0);
                long windowMs = toLong(externalConfig.get("windowMs"), 0L);
                return new SlidingWindowLogLimiter(capacity, windowMs);
            }
            default:
                throw new IllegalStateException("Unhandled limiter: " + algoName);
            }
    }

    private int toInt(Object obj, int def) {
        if(obj == null)
            return def;

        if(obj instanceof Number) {
            return ((Number) obj).intValue();
        }

        return Integer.parseInt(obj.toString());
    }

    private double toDouble(Object obj, double def) {
        if(obj == null) {
            return def;
        }

        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }

        return Double.parseDouble(obj.toString());
    }

    private Long toLong(Object obj, long def) {
        if(obj == null) {
            return def;
        }

        if (obj instanceof Number) {
            return ((Number)obj).longValue();
        }

        return Long.parseLong(obj.toString());
    }
}
