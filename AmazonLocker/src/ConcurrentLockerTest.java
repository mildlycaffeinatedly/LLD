import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentLockerTest {
    public static void main(String[] args) throws InterruptedException {
        int numCompartments = 10;
        Compartment[] compartments = new Compartment[numCompartments];
        for (int i = 0; i < numCompartments; i++) {
            compartments[i] = new Compartment(i, Size.SMALL, CompartmentState.AVAILABLE);
        }

        BasicLocker basic = new BasicLocker(compartments);
        Locker locker = new ConcurrentLockerDecorator(basic, compartments);

        Queue<String> codes = new ConcurrentLinkedQueue<>();
        // quick single-thread sanity checks
        System.out.println("Single-thread deposits:");
        for (int i = 0; i < 3; i++) {
            try {
                String code = locker.tryDeposit(Size.SMALL);
                System.out.println("  deposit returned: " + code);
                if (code != null && !"No compartments available.".equals(code)) codes.add(code);
            } catch (Exception e) {
                System.out.println("  deposit threw: " + e);
            }
        }
        ExecutorService depositPool = Executors.newFixedThreadPool(20);

        // Submit more tasks than compartments to test contention
        for (int i = 0; i < 50; i++) {
            depositPool.submit(() -> {
                try {
                    String code = locker.tryDeposit(Size.SMALL);
                    if (code != null && !"No compartments available.".equals(code)) {
                        codes.add(code);
                    }
                } catch (IllegalStateException | IllegalArgumentException e) {
                    // expected when no compartments remain or invalid args
                }
            });
        }

        depositPool.shutdown();
        depositPool.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Deposited codes: " + codes.size() + " (expected " + numCompartments + ")");

        // Withdraw concurrently
        AtomicInteger withdrawSuccess = new AtomicInteger(0);
        ExecutorService withdrawPool = Executors.newFixedThreadPool(20);
        List<String> codesList = new ArrayList<>(codes);
        for (String code : codesList) {
            withdrawPool.submit(() -> {
                boolean ok = locker.tryWithdraw(code);
                if (ok) withdrawSuccess.incrementAndGet();
            });
        }

        withdrawPool.shutdown();
        withdrawPool.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Successful withdrawals: " + withdrawSuccess.get() + " (expected " + codesList.size() + ")");

        if (codes.size() == numCompartments && withdrawSuccess.get() == codesList.size()) {
            System.out.println("CONCURRENT LOCKER TEST: PASSED");
        } else {
            System.out.println("CONCURRENT LOCKER TEST: FAILED");
        }
    }
}
