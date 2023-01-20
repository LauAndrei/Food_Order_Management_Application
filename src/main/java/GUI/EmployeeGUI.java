package GUI;

import ObserverLayer.Observer;
import Domain.MenuItem;
import Domain.Order;
import Repository.IOrderRepository;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EmployeeGUI extends JFrame implements Observer {
    private JPanel EmployeeGUIPanel;
    private JButton seeAllOrdersButton;
    private JTextArea textArea1;
    private JLabel ordersLabel;

    IOrderRepository orderRepository;
    AtomicInteger numberOfNewOrders;

    @Override
    public void notifyObserver() {
        if (numberOfNewOrders.get() == 0) {
            ordersLabel.setText("You don't have new orders!");
        } else {
            ordersLabel.setText("You have " + numberOfNewOrders.get() + " new Orders!");
        }
        numberOfNewOrders.set(0);
    }

    EmployeeGUI(IOrderRepository orderRepository, AtomicInteger numberOfNewOrders) {
        this.orderRepository = orderRepository;
        this.numberOfNewOrders = numberOfNewOrders;

        setContentPane(EmployeeGUIPanel);
        setTitle("Employee Window");
        setBounds(500, 150, 800, 500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        notifyObserver();

        seeAllOrdersButton.addActionListener(e -> {
            var orders = orderRepository.getOrders();
            String ordersString = "";
            for (Map.Entry<Order, List<MenuItem>> order : orders.entrySet()) {
                ordersString += order.getKey().toString() + order.getValue().toString() + '\n' + '\n';
            }
            textArea1.setText(ordersString);
        });
    }
}
