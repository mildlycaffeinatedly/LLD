public interface AlertListener {
    void onLowStock(int productId, int quantity, int warehouseId);
}