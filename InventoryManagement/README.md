## Inventory Management System

## Overview

An inventory management system tracks product stock across multiple warehouse locations. When inventory arrives, the system records it. When orders ship, the system deducts stock. The system can also transfer inventory between locations and alert managers when stock runs low.

## Requirements

Clarifying Questions:
1. Should the system support multiple products across multiple warehouses?
2. What operations are needed: add stock, remove stock, transfer between warehouses?
3. How should low-stock alerts work? Are thresholds configurable per product per warehouse?
4. Should the system validate operations (e.g., prevent negative inventory)?
5. Do we need to check which warehouses can fulfill a given quantity for a product?
6. Is thread-safety required for concurrent operations?
7. Are products managed externally (we only track by ID)?

Requirements:
1. Track inventory quantities for products across multiple warehouses
2. Add stock to a specific warehouse (receiving shipments)
3. Remove stock from a specific warehouse (fulfilling orders)
   - Must reject removal if insufficient stock exists
4. Transfer stock between warehouses
   - Must reject transfer if source warehouse has insufficient stock
5. Check availability: given a product and quantity, return which warehouses can fulfill it
6. Low-stock alerts when inventory falls below threshold
   - Thresholds are configurable per product per warehouse
   - Alert triggered when stock drops below the configured threshold
7. Reject any operation that would result in negative inventory
   - Remove operations must validate sufficient stock exists
   - Transfer operations must validate sufficient stock at source
8. System must be thread-safe to handle concurrent operations

Out of Scope:
- Product catalog management (products exist externally, referenced by ID)
- Order processing, payment, or serviceability logic
- Persistence or database integration
- User authentication or authorization
- Warehouse management (creating/deleting warehouses)

## Entities

InventoryManager
Warehouse
AlertConfig
AlertListener

InventoryManager

Fields:
    - HashMap<String, Warehouse> warehouses

Method:
    + boolean recordShipment(int productId, int quantity, Warehouse warehouseId)
    - boolean fulfillOrder(int productId, int quantity, Warehouse warehouseId)
    + boolean transfer(Warehouse from, Warehouse to, int productId, int quantity)
    - int checkAvailability(int productId)

Warehouse

Fields:
    - HashMap<int, int> productToQuantity;

Methods:
    + boolean setQuantity(int productId, int quantity)
    + int getQuantity(int quantity)

AlertConfig

Fields:
    - int threshold
    - 