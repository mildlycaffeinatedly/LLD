public class TestRunner {
    public static void main(String[] args) {
        Compartment[] compartments = new Compartment[] {
            new Compartment(0, Size.SMALL, CompartmentState.AVAILABLE),
            new Compartment(1, Size.MEDIUM, CompartmentState.AVAILABLE),
            new Compartment(2, Size.LARGE, CompartmentState.AVAILABLE)
        };

        BasicLocker locker = new BasicLocker(compartments);

        System.out.println("Depositing a medium package...");
        String token = locker.depositPackage(Size.MEDIUM);
        System.out.println("Received token: " + token);

        System.out.println("Attempting withdrawal with token...");
        boolean success = locker.withdrawPackage(token);
        System.out.println("Withdrawal success: " + success);

        System.out.println("Attempting withdrawal again (should fail)...");
        boolean second = locker.withdrawPackage(token);
        System.out.println("Second withdrawal success: " + second);
    }
}
