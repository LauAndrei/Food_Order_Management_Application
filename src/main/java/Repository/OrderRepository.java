package Repository;

import Domain.MenuItem;
import Domain.Order;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository implements IOrderRepository{
    private Map<Order, List<MenuItem>> orders;

    public OrderRepository(Map<Order, List<MenuItem>> orders) {
        this.orders = orders;
    }

    @Override
    public void addOrder(Order order, List<MenuItem> items) {
        orders.put(order, items);
    }

    @Override
    public Map<Order, List<MenuItem>> getOrders() {
        return orders;
    }

    @Override
    public Double getCostForOrder(Order order) {
        List<MenuItem> items = orders.get(order);
        return items.stream().map(MenuItem::computePrice)
                .reduce(0.0, Double::sum);
    }

    @Override
    public List<MenuItem> getItemsForOrder(Order order) {
        return orders.get(order);
    }

    @Override
    public void serialize(String filename) {
        try (FileOutputStream fileOutputStream
                    = new FileOutputStream(filename);
            ObjectOutputStream objectOutputStream
                    = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(orders);
        }
        catch (IOException exception){
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void deserialize(String filename) {
        try (FileInputStream fileInputStream
                     = new FileInputStream(filename);
             ObjectInputStream objectInputStream
                     = new ObjectInputStream(fileInputStream)) {
            orders = (Map<Order, List<MenuItem>>)objectInputStream.readObject();
        }
        catch (IOException | ClassNotFoundException exception){
            orders = new HashMap<>();
        }
    }
}
