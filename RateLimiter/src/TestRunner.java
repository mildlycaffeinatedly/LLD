import java.util.HashMap;
import java.util.Map;

public class TestRunner {
    private static int failures = 0;

    private static void assertTrue(boolean cond, String msg) {
        if (!cond) {
            System.out.println("FAIL: " + msg);
            failures++;
        } else {
            System.out.println("PASS: " + msg);
        }
    }

    public static void main(String[] args) throws Exception {
        RateLimiterFactory factory = new RateLimiterFactory();

        testTokenBucket(factory);
        testSlidingWindow(factory);
        testFactoryBadName(factory);
        testRateLimiterFacade(factory);

        System.out.println("\nTest run complete. Failures: " + failures);
        if (failures > 0) System.exit(1);
    }

    private static void testTokenBucket(RateLimiterFactory factory) throws InterruptedException {
        System.out.println("\n=== TokenBucket Test ===");
        Map<String, Object> cfg = new HashMap<>();
        cfg.put("name", "TOKEN_BUCKET");
        cfg.put("capacity", 2);
        cfg.put("refillRate", 1.0);

        Limiter limiter = factory.create(cfg);
        RateLimitResponse r1 = limiter.allow("client1");
        assertTrue(r1.isAllowed(), "first token should be allowed");
        assertTrue(r1.getRemaining() != null && r1.getRemaining() >= 0, "remaining present after first");

        RateLimitResponse r2 = limiter.allow("client1");
        assertTrue(r2.isAllowed(), "second token should be allowed");

        RateLimitResponse r3 = limiter.allow("client1");
        assertTrue(!r3.isAllowed() && r3.getRetryAfterMs() != null && r3.getRetryAfterMs() > 0, "third request denied with retryAfterMs");

        Thread.sleep(1100);
        RateLimitResponse r4 = limiter.allow("client1");
        assertTrue(r4.isAllowed(), "after refill one token should be allowed");
    }

    private static void testSlidingWindow(RateLimiterFactory factory) throws InterruptedException {
        System.out.println("\n=== SlidingWindow Test ===");
        Map<String, Object> cfg = new HashMap<>();
        cfg.put("name", "SLIDING_WINDOW_LOG");
        cfg.put("capacity", 2);
        cfg.put("windowMs", 1000L);

        Limiter limiter = factory.create(cfg);
        RateLimitResponse a1 = limiter.allow("userA");
        assertTrue(a1.isAllowed(), "sliding: first allowed");
        RateLimitResponse a2 = limiter.allow("userA");
        assertTrue(a2.isAllowed(), "sliding: second allowed");
        RateLimitResponse a3 = limiter.allow("userA");
        assertTrue(!a3.isAllowed() && a3.getRetryAfterMs() != null && a3.getRetryAfterMs() > 0, "sliding: third denied with wait");

        Thread.sleep(1100);
        RateLimitResponse a4 = limiter.allow("userA");
        assertTrue(a4.isAllowed(), "sliding: after window expired allowed");
    }

    private static void testFactoryBadName(RateLimiterFactory factory) {
        System.out.println("\n=== Factory Invalid Type Test ===");
        Map<String, Object> cfg = new HashMap<>();
        cfg.put("name", "UNKNOWN_TYPE");
        try {
            factory.create(cfg);
            assertTrue(false, "factory should throw for unknown type");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("PASS: factory threw for unknown type: " + e.getMessage());
        }
    }

    private static void testRateLimiterFacade(RateLimiterFactory factory) {
        System.out.println("\n=== RateLimiter Facade Test ===");
        Map<String, Object> defaultCfg = new HashMap<>();
        defaultCfg.put("name", "TOKEN_BUCKET");
        defaultCfg.put("capacity", 1);
        defaultCfg.put("refillRate", 1.0);

        Map<String, Object> externalCfg = new HashMap<>();
        externalCfg.put("name", "SLIDING_WINDOW_LOG");
        externalCfg.put("capacity", 1);
        externalCfg.put("windowMs", 1000L);
        externalCfg.put("endpoint", "/api/test");

        RateLimiter rl = new RateLimiter(externalCfg, defaultCfg, factory);

        RateLimitResponse r1 = rl.allow("clientX", "/api/test");
        assertTrue(r1.isAllowed(), "facade: endpoint-specific limiter allowed");

        RateLimitResponse r2 = rl.allow("clientX", "/other");
        assertTrue(r2.isAllowed(), "facade: default limiter allowed");
    }
}
