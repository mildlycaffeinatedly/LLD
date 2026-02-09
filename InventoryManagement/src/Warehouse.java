import java.util.HashMap;

public class Warehouse {
    int warehouseId;
    HashMap<Integer, Integer> productToQuantity;
    HashMap<Integer, AlertConfig> productToAlertConfig;

    public Warehouse() {
        productToQuantity = new HashMap<>();
        productToAlertConfig = new HashMap<>();
    }

    public boolean setQuantity(int productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity can not be negative.");
        }

        productToQuantity.put(productId, quantity);

        AlertConfig config = productToAlertConfig.get(productId);


        if (quantity < config.getThreshold()) {
            config.getListener().onLowStock(productId, quantity, warehouseId);
            return false;
        } 

        return true;
    }

    public int getQuantity(int productId) {
        if (!productToQuantity.containsKey(productId)) {
            throw new IllegalArgumentException("This product is not available in this warehouse");
        }

        return productToQuantity.get(productId);
    }
}
