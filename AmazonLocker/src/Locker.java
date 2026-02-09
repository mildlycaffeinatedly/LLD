import java.time.Instant;

public interface Locker {
    String tryDeposit(Size size);
    boolean tryWithdraw(String accessCode);
    void removeExpiredPackages();
    Compartment getAvailableCompartment(Size size);
    String depositPackage(int compartmentId);
    void withdrawPackage(AccessToken accessToken);
    boolean checkAvailability(int compartmentId);
    AccessToken createToken(String accessCode, Instant timestamp, int compartmentId);
    AccessToken getTokenWithCode(String accessCode);
}