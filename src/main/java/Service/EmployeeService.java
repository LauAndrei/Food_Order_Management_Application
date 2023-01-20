package Service;

import ObserverLayer.Observer;
import Domain.Order;
import Repository.IOrderRepository;

import java.util.List;

public class EmployeeService implements IEmployeeService{
    private final IOrderRepository orderRepository;

    public EmployeeService(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public List<Order> getOrders() {
        return orderRepository.getOrders().keySet().stream().toList();
    }
}
