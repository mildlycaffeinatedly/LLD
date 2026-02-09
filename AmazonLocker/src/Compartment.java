public class Compartment {
    private final int compartmentId;
    private final Size compartmentSize;
    private CompartmentState compartmentState;

    Compartment(int compartmentId, Size compartmentSize, CompartmentState compartmentState) {
        if(compartmentId < 0) {
            throw new IllegalArgumentException("Compartment Id can not be negative.");
        }

        if(compartmentSize == null) {
            throw new IllegalArgumentException("Compartment needs to have a defined size.");
        }

        if(compartmentState != CompartmentState.AVAILABLE) {
            throw new IllegalArgumentException("Compartment needs to be available on creation.");
        }
        
        this.compartmentId = compartmentId;
        this.compartmentSize = compartmentSize;
        this.compartmentState = compartmentState;
    }

    public int getCompartmentId() {
        return compartmentId;
    }

    public Size getCompartmentSize() {
        return compartmentSize;
    }

    public CompartmentState getCompartmentState() {
        return compartmentState;
    }
    
    public void setCompartmentState(CompartmentState compartmentState) {
        if(compartmentState == null) {
            throw new IllegalArgumentException("Compartment state can not be null.");
        }
        
        this.compartmentState = compartmentState;
    }
}
