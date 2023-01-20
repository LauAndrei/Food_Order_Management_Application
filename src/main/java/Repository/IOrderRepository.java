package Repository;

import Domain.MenuItem;
import Domain.Order;

import java.util.List;
import java.util.Map;

public interface IOrderRepository {
    void addOrder(Order order, List<MenuItem> items);
    Map<Order, List<MenuItem>> getOrders();
    Double getCostForOrder(Order order);
    List<MenuItem> getItemsForOrder(Order order);
    void serialize(String filename);
    void deserialize(String filename);
}
