public class AlertConfig {
    private int threshold;
    private AlertListener listener;

    AlertConfig(int threshold, AlertListener listener) {
        this.threshold = threshold;
        this.listener = listener;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public AlertListener getListener() {
        return this.listener;
    }
}
