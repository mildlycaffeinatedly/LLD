import java.util.HashMap;
import java.util.Map;

public class InventoryManager {
    private HashMap<String, Warehouse> warehouses;

    InventoryManager(HashMap<String, Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    public boolean recordShipment(int productId, int quantity, String warehouseId) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity can not be negative.");
        }

        if (warehouseId == null || warehouseId.isBlank()) {
            throw new IllegalArgumentException("Warehouse Id can not be null or blank.");
        }

        if (!warehouses.containsKey(warehouseId)) {
            throw new IllegalArgumentException("Invalid warehouse Id.");
        }

        Warehouse warehouse = warehouses.get(warehouseId);

        int currentQuantity = warehouse.getQuantity(productId);

        int newQuantity = currentQuantity + quantity;

        warehouse.setQuantity(productId, newQuantity);

        return true;
    }

    public boolean fulfillOrder(int productId, int quantity, String warehouseId) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity can not be negative.");
        }

        if (warehouseId == null || warehouseId.isBlank()) {
            throw new IllegalArgumentException("Warehouse Id can not be null or blank.");
        }

        if (!warehouses.containsKey(warehouseId)) {
            throw new IllegalArgumentException("Invalid warehouse Id.");
        }

        Warehouse warehouse = warehouses.get(warehouseId);

        int currentQuantity = warehouse.getQuantity(productId);

        if (currentQuantity < quantity) {
            throw new IllegalArgumentException("This warehouse doesn't have enough quantity to fulfill this order.");
        }

        int newQuantity = currentQuantity - quantity;

        warehouse.setQuantity(productId, newQuantity);

        return true;
    }

    public boolean transfer(String source, String destination, int productId, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity can not be negative.");
        }

        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("Source Warehouse Id can not be null or blank.");
        }

        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination Warehouse Id can not be null or blank.");
        }

        if (!warehouses.containsKey(source)) {
            throw new IllegalArgumentException("Invalid source warehouse Id.");
        }

        if (!warehouses.containsKey(destination)) {
            throw new IllegalArgumentException("Invalid destination warehouse Id.");
        }

        Warehouse sourceWarehouse = warehouses.get(source);
        Warehouse destinationWarehouse = warehouses.get(destination);

        int quantityAtSource = sourceWarehouse.getQuantity(productId);

        if (quantityAtSource < quantity) {
            throw new IllegalArgumentException("Insufficient quantity at the source warehouse for the transer");
        }

        sourceWarehouse.setQuantity(productId, quantityAtSource - quantity);

        int quantityAtDestination = destinationWarehouse.getQuantity(productId);
        destinationWarehouse.setQuantity(productId, quantityAtDestination + quantity);

        return true;
    }

    public String checkAvailability(int productId, int quantity) {
        for (Map.Entry<String, Warehouse> entry : warehouses.entrySet()) {
            String warehouseId = entry.getKey();
            Warehouse warehouse = entry.getValue();
        
            if (warehouse.getQuantity(productId) >= quantity) {
                return warehouseId;
            }
        }

        throw new IllegalArgumentException("No warehouse has the required quantity available for this product.");
    }
}
