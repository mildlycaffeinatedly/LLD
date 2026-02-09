import java.time.Instant;

public class AccessToken {
    private final String accessCode;
    private final Instant expiryTimestamp;
    private final int compartmentId;

    public AccessToken(String accessCode, Instant expiryTimestamp, int compartmentId) {
        if(accessCode == null || accessCode.isBlank()) {
            throw new IllegalArgumentException("Access Code can not be null or empty.");
        }

        if(expiryTimestamp == null || Instant.now().isAfter(expiryTimestamp)) {
            throw new IllegalArgumentException("Invalid expiry time.");
        }

        if(compartmentId < 0) {
            throw new IllegalArgumentException("Compartment Id can not be negative.");
        }

        this.accessCode = accessCode;
        this.expiryTimestamp = expiryTimestamp;
        this.compartmentId = compartmentId;
    }

    public String getAccessCode() {
        return this.accessCode;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryTimestamp);
    }

    public int getCompartmentId() {
        return this.compartmentId;
    }
}