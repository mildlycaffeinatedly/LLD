import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BasicLocker implements Locker {
    private Compartment[] compartments;
    private ConcurrentHashMap<String, AccessToken> codeToTokenMapping = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Compartment> codeToCompartmentMapping = new ConcurrentHashMap<>();

    BasicLocker(Compartment[] compartments) {
        this.compartments = compartments;
    }

    @Override
    public String tryDeposit(Size size) {
        Compartment availableCompartment = getAvailableCompartment(size);
        if(availableCompartment == null) {
            throw new IllegalStateException("No compartments available of " + size.toString() + " size.");
        }
        
        String accessCode = depositPackage(availableCompartment.getCompartmentId());

        return accessCode;
    }

    
    @Override
    public boolean tryWithdraw(String accessCode) {
        if(accessCode == null) {
            throw new IllegalArgumentException("Access code can not be null.");
        }

        AccessToken accessToken = getTokenWithCode(accessCode);
        if(accessToken == null || accessToken.isExpired()) 
            return false;

        withdrawPackage(accessToken);

        return true;
    }

    @Override
    public void removeExpiredPackages() {
        for(Map.Entry<String, AccessToken> entry : codeToTokenMapping.entrySet()) {
            String accessCode = entry.getKey();
            AccessToken accessToken = entry.getValue();
            
            if(accessToken.isExpired()) {
                Compartment compartment = compartments[accessToken.getCompartmentId()];

                compartment.setCompartmentState(CompartmentState.AVAILABLE);

                codeToTokenMapping.remove(accessCode);
                codeToCompartmentMapping.remove(accessCode);
            }
        }
    }

    public String depositPackage(int compartmentId) {
        String accessCode = UUID.randomUUID().toString();
        AccessToken accessToken = createToken(accessCode, Instant.now().plus(Duration.ofDays(7)), compartmentId);

        compartments[compartmentId].setCompartmentState(CompartmentState.OCCUPIED);

        codeToTokenMapping.put(accessCode, accessToken);
        codeToCompartmentMapping.put(accessCode, compartments[compartmentId]);

        return accessCode;
    }

    public void withdrawPackage(AccessToken accessToken) {
        int compartmentWithPackageId = accessToken.getCompartmentId();
        Compartment compartmentWithPackage = compartments[compartmentWithPackageId];

        String accessCode = accessToken.getAccessCode();
        codeToCompartmentMapping.remove(accessCode);
        compartmentWithPackage.setCompartmentState(CompartmentState.AVAILABLE);
    }

    public AccessToken getTokenWithCode(String accessCode) {
        if(accessCode == null) {
            throw new IllegalArgumentException("Access code can not be null.");
        }

        if(codeToTokenMapping.get(accessCode) == null) {
            throw new IllegalArgumentException("Access code is invalid");
        }

        return codeToTokenMapping.get(accessCode);
    }

    public Compartment getAvailableCompartment(Size size) {
        Compartment compartment = null;

        for(int i = 0; i < compartments.length; i++) {
            Compartment curr = compartments[i];

            if(curr.getCompartmentState() == CompartmentState.AVAILABLE && curr.getCompartmentSize() == size) {
                return curr;
            }
        }

        return null;
    }

    public boolean checkAvailability(int comaprtmentId) {
        if(compartments[comaprtmentId].getCompartmentState() == CompartmentState.AVAILABLE) {
            return true;
        }
        return false;
    }

    public AccessToken createToken(String accessCode, Instant timestamp, int compartmentId) {
        AccessToken accessToken = new AccessToken(accessCode, timestamp, compartmentId);

        return accessToken;
    }
}
