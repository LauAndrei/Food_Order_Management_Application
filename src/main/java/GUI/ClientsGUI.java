package GUI;

import Domain.MenuItem;
import Repository.*;
import Service.IAdministratorService;
import Service.IClientService;

import javax.swing.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientsGUI extends JFrame {
    private JPanel ClientsGUIPanel;
    private JTextArea textArea1;
    private JButton viewAllProductsButton;
    private JTextField pageNumberField;
    private JTextField keywordField;
    private JTextField ratingTextField;
    private JTextField caloriesTextField;
    private JTextField proteinsTextField;
    private JTextField fatsTextField;
    private JTextField sodiumTextField;
    private JTextField priceTextField;
    private JButton searchButton;
    private JButton orderButton;
    private JTextField productsIDsTextField;

    Integer clientId;
    IClientService clientService;
    IAdministratorService administratorService;
    IProductRepository productRepository;
    AtomicInteger copyOrderId;
    Map<Integer, MenuItem> items;

    public ClientsGUI(Map<Integer, MenuItem> items, IClientService clientService, IAdministratorService administratorService, IProductRepository productRepository, Integer clientId, AtomicInteger copyOrderId) throws IOException {
        this.items = items;
        this.clientService = clientService;
        this.administratorService = administratorService;
        this.productRepository = productRepository;
        this.clientId = clientId;
        this.copyOrderId = copyOrderId;

        setContentPane(ClientsGUIPanel);
        setTitle("Clients Window");
        setBounds(400, 50, 900, 700);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        //administratorService.importProducts("products.csv");

        viewAllProductsButton.addActionListener(e -> {
            Integer pageNumber;
            if (pageNumberField.getText().isEmpty()) {
                pageNumber = 1;
            } else {
                pageNumber = Integer.parseInt(pageNumberField.getText());
            }
            String itemString = "";
            var result = productRepository.getItemsFromPage(pageNumber);
            for (MenuItem item : result) {
                itemString += item.toString() + '\n';
            }
            textArea1.setText(itemString);
        });

        searchButton.addActionListener(e -> {
            Integer pageNumber = 1;
            if (!pageNumberField.getText().isEmpty()) {
                pageNumber = Integer.parseInt(pageNumberField.getText());
            }
            String itemString = "";
            String keyword = keywordField.getText();
            String rating = ratingTextField.getText();
            String calories = caloriesTextField.getText();
            String proteins = proteinsTextField.getText();
            String fats = fatsTextField.getText();
            String sodium = sodiumTextField.getText();
            String price = priceTextField.getText();
            List<String> filters = Arrays.asList(keyword, rating, calories, proteins, fats, sodium, price);
            var result = clientService.getProductsFromPage(filters, pageNumber);
            for (MenuItem item : result) {
                itemString += item.toString() + '\n';
            }
            textArea1.setText(itemString);
        });

        orderButton.addActionListener(e -> {
            String[] idsString = productsIDsTextField.getText().split(",");
            List<Integer> ids = new LinkedList<>();
            for (int i = 0; i < idsString.length; i++) {
                ids.add(Integer.parseInt(idsString[i]));
            }
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(date);
            java.sql.Date date2 = java.sql.Date.valueOf(dateString);
            try {
                clientService.addOrder(clientId, date2, ids);
                JOptionPane.showMessageDialog(null, "Order with products " + ids + " placed! \nCheck the bill with number " + copyOrderId.getAndIncrement());
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }
        });
    }
}
