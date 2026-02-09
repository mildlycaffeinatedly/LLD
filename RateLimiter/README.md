# Rate limiter

## Overview

A rate limiter controls how many requests a client can make to an API within a specific time window. When a request comes in, the rate limiter checks if the client has exceeded their quota. If they're under the limit, the request proceeds. If they've hit the cap, the request gets rejected. 

## Requirements

Requirements:
1. Configuration is provided at startup (loaded once)
2. System receives requests with (clientId: string, endpoint: string)
3. Each endpoint has a configuration specifying:
   - Algorithm to use (e.g., "TokenBucket", "SlidingWindowLog", etc.)
   - Algorithm-specific parameters (e.g., capacity, refillRatePerSecond for Token Bucket)
4. System enforces rate limits by checking clientId against the endpoint's configuration
5. Return structured result: (allowed: boolean, remaining: int, retryAfterMs: long | null)
6. If endpoint has no configuration, use a default limit

Out of scope:
- Distributed rate limiting (Redis, coordination)
- Dynamic configuration updates
- Metrics and monitoring
- Config validation beyond basic checks


## Entities

RateLimiter:
    Fields:
        HashMap<String, >
    Methods:

RateLimiterFactory:
    Fields:

    Methods:
        RateLimiterFactory(Map<String, Object> config)
        create(Map<String, Object> config) : Limiter
        
Limiter(Interface):  
    Methods: 
        + allow(String key) : RateLimitResult
        + deny(String key) : RateLimitResult

RateLimitResult:
    Fields:
        - allowed : boolean
        - remaining : int
        - retryAfterMs: long | null

