import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentLockerDecorator implements Locker {
    private final Locker delegate;
    private final ReentrantLock[] locks;
    private Compartment[] compartments;
    
    public ConcurrentLockerDecorator(Locker delegate, Compartment[] compartments) {
        this.delegate = delegate;
        locks = new ReentrantLock[compartments.length];
        for(int i = 0; i < locks.length; i++) {
            locks[i] = new ReentrantLock();
        }
        this.compartments = compartments;
    }

    @Override
    public String tryDeposit(Size size) {
        // Scan compartments and try to acquire a lock for a matching, available compartment.
        for (int i = 0; i < compartments.length; i++) {
            Compartment c = compartments[i];
            if (c == null) continue;
            if (c.getCompartmentSize() != size) continue;

            boolean locked = locks[i].tryLock();
            if (!locked) continue;

            try {
                if (c.getCompartmentState() == CompartmentState.AVAILABLE) {
                    return delegate.depositPackage(i);
                }
            } finally {
                locks[i].unlock();
            }
        }

        throw new IllegalStateException("No compartments available of " + size.toString() + " size.");
    }

    @Override
    public boolean tryWithdraw(String accessCode) {
        if(accessCode == null) {
            throw new IllegalArgumentException("Access code can not be null.");
        }
        try {
            AccessToken token = delegate.getTokenWithCode(accessCode);
            if (token == null || token.isExpired()) return false;

            int compartmentId = token.getCompartmentId();
            locks[compartmentId].lock();
            try {
                // Re-fetch to ensure the token/association didn't change before we locked
                AccessToken fresh = delegate.getTokenWithCode(accessCode);
                if (fresh == null || fresh.isExpired()) return false;

                delegate.withdrawPackage(fresh);
                return true;
            } finally {
                locks[compartmentId].unlock();
            }
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void removeExpiredPackages() {
        // Lock all compartments in a consistent order to avoid races while removing expired packages.
        for (int i = 0; i < locks.length; i++) {
            locks[i].lock();
        }
        try {
            delegate.removeExpiredPackages();
        } finally {
            for (int i = locks.length - 1; i >= 0; i--) {
                locks[i].unlock();
            }
        }
    }

    @Override
    public String depositPackage(int compartmentId) {
        return delegate.depositPackage(compartmentId);
    }

    @Override
    public void withdrawPackage(AccessToken accessToken) {
        delegate.withdrawPackage(accessToken);
    }

    @Override
    public AccessToken getTokenWithCode(String accessCode) {
        return delegate.getTokenWithCode(accessCode);
    }

    @Override
    public AccessToken createToken(String accessCode, Instant timestamp, int compartmentId) {
        return delegate.createToken(accessCode, timestamp, compartmentId);
    }

    @Override
    public Compartment getAvailableCompartment(Size size) {
        return delegate.getAvailableCompartment(size);
    }

    @Override
    public boolean checkAvailability(int compartmentId) {
        return delegate.checkAvailability(compartmentId);
    }
}