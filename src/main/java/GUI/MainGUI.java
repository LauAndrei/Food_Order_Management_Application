package GUI;

import ObserverLayer.Observer;
import Domain.MenuItem;
import Domain.Order;
import Domain.User;
import Repository.*;
import Service.*;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainGUI extends JFrame {
    private JPanel MainPanel;
    private JLabel usernameLabel;
    private JButton logInButton;
    private JButton signUpButton;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public MainGUI() {
        setContentPane(MainPanel);
        setTitle("Main Window");
        setBounds(600, 200, 330, 190);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UserRepository userRepository = new UserRepository();
        ILoginService loginService = new LoginService(userRepository);

        Map<Integer, MenuItem> items = new HashMap<>();
        Map<Order, List<MenuItem>> orders = new HashMap<>();
        IOrderRepository orderRepository = new OrderRepository(orders);
        List<Observer> observers = new LinkedList<>();
        IProductRepository productRepository = new ProductRepository(items);
        AtomicInteger currentOrderId = new AtomicInteger(1);
        AtomicInteger copyOrderId = new AtomicInteger(1);
        AtomicInteger currentId = new AtomicInteger(1);
        IAdministratorService administratorService = new AdministratorService(productRepository, orderRepository, userRepository, currentId);
        IClientService clientService = new ClientService(observers, orderRepository, productRepository, currentOrderId);

        AtomicInteger numberOfNewOrders = new AtomicInteger(0);

        Observer observer = new Observer() {
            @Override
            public void notifyObserver() {
                numberOfNewOrders.incrementAndGet();
            }
        };
        observers.add(observer);

        logInButton.addActionListener(e -> {
            try {
                User user = loginService.logIn(usernameField.getText(), passwordField.getText());
                usernameField.setText("");
                passwordField.setText("");
                //open client/admin/employee GUI
                if (user.getUserType().ordinal() == 0) {
                    try {
                        new ClientsGUI(items, clientService, administratorService, productRepository, user.getId(), copyOrderId);
                    } catch (IOException exception) {
                        System.out.println(exception.getMessage());
                    }
                } else if (user.getUserType().ordinal() == 1) {
                    new EmployeeGUI(orderRepository, numberOfNewOrders);
                } else if (user.getUserType().ordinal() == 2) {
                    new AdministratorGUI(administratorService, productRepository, orderRepository);
                }
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(null, "User does not exist!");
            }
        });

        signUpButton.addActionListener(e -> {
            try {
                loginService.register(usernameField.getText(), passwordField.getText());
                JOptionPane.showMessageDialog(null, "Registered Successfully!");
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(null, "User already exists!");
            }
        });
    }

    public static void main(String[] args) {
        new MainGUI();
    }
}