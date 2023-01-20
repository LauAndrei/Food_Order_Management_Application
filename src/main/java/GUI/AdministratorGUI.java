package GUI;

import Domain.MenuItem;
import Exceptions.ItemAlreadyExistsException;
import Exceptions.ItemNotFoundException;
import Repository.IOrderRepository;
import Repository.IProductRepository;
import Service.IAdministratorService;

import javax.swing.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class AdministratorGUI extends JFrame {
    private JPanel AdministratorGUIPanel;
    private JTextArea textArea1;
    private JButton importProductsButton;
    private JTextField idTextField;
    private JTextField proteinsTextField;
    private JTextField fatsTextField;
    private JTextField sodiumTextField;
    private JTextField priceTextField;
    private JTextField titleTextField;
    private JTextField ratingTextField;
    private JTextField caloriesTextField;
    private JButton addBaseProductButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton getButton;
    private JButton viewProductsButton;
    private JTextField pageNumberTextField;
    private JButton createMenuButton;
    private JTextField idsTextField;
    private JTextField filenameTextField;
    private JButton serializeProductsButton;
    private JButton deserializeProductsButton;
    private JButton serializeOrdersButton;
    private JButton deserializeOrdersButton;
    private JButton generateAReportButton;

    IAdministratorService administratorService;
    IProductRepository productRepository;
    IOrderRepository orderRepository;

    public AdministratorGUI(IAdministratorService administratorService, IProductRepository productRepository, IOrderRepository orderRepository) {
        this.administratorService = administratorService;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;

        setContentPane(AdministratorGUIPanel);
        setTitle("Administrator Window");
        setBounds(400, 50, 900, 700);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        importProductsButton.addActionListener(e -> {
            String filename;
            if (filenameTextField.getText().isEmpty()) {
                filename = "products.csv";
            } else {
                filename = filenameTextField.getText();
            }
            try {
                administratorService.importProducts(filename);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error importing!\nFile might not exist!");
            }
        });

        viewProductsButton.addActionListener(e -> {
            Integer pageNumber;
            if (pageNumberTextField.getText().isEmpty()) {
                pageNumber = 1;
            } else {
                pageNumber = Integer.parseInt(pageNumberTextField.getText());
            }
            String itemString = "";
            var result = productRepository.getItemsFromPage(pageNumber);
            for (MenuItem item : result) {
                itemString += item.toString() + '\n';
            }
            textArea1.setText(itemString);
        });

        addBaseProductButton.addActionListener(e -> {
            String title = titleTextField.getText();
            Double rating = Double.parseDouble(ratingTextField.getText());
            Double calories = Double.parseDouble(caloriesTextField.getText());
            Double proteins = Double.parseDouble(proteinsTextField.getText());
            Double fats = Double.parseDouble(fatsTextField.getText());
            Double sodium = Double.parseDouble(sodiumTextField.getText());
            Double price = Double.parseDouble(priceTextField.getText());
            try {
                administratorService.addBaseProduct(title, rating, calories, proteins, fats, sodium, price);
                JOptionPane.showMessageDialog(null, "Base Product successfully added!");
                idTextField.setText("");
                titleTextField.setText("");
                ratingTextField.setText("");
                caloriesTextField.setText("");
                proteinsTextField.setText("");
                fatsTextField.setText("");
                sodiumTextField.setText("");
                priceTextField.setText("");
                viewProductsButton.doClick();
            } catch (ItemAlreadyExistsException exception) {
                JOptionPane.showMessageDialog(null, "Error adding base product!\nIt might already exist!");
            }
        });

        deleteButton.addActionListener(e -> {
            Integer id = Integer.parseInt(idTextField.getText());
            try {
                administratorService.deleteProduct(id);
                JOptionPane.showMessageDialog(null, "Item successfully deleted!");
                idTextField.setText("");
                titleTextField.setText("");
                ratingTextField.setText("");
                caloriesTextField.setText("");
                proteinsTextField.setText("");
                fatsTextField.setText("");
                sodiumTextField.setText("");
                priceTextField.setText("");
                viewProductsButton.doClick();
            } catch (ItemNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Item with id " + id + " does not exist!");
            }
        });

        updateButton.addActionListener(e -> {
            Integer id = Integer.parseInt(idTextField.getText());
            String title = titleTextField.getText();
            Double rating = Double.parseDouble(ratingTextField.getText());
            Double calories = Double.parseDouble(caloriesTextField.getText());
            Double proteins = Double.parseDouble(proteinsTextField.getText());
            Double fats = Double.parseDouble(fatsTextField.getText());
            Double sodium = Double.parseDouble(sodiumTextField.getText());
            Double price = Double.parseDouble(priceTextField.getText());
            try {
                administratorService.updateBaseProduct(id, title, rating, calories, proteins, fats, sodium, price);
                JOptionPane.showMessageDialog(null, "Product successfully updated!");
                idTextField.setText("");
                titleTextField.setText("");
                ratingTextField.setText("");
                caloriesTextField.setText("");
                proteinsTextField.setText("");
                fatsTextField.setText("");
                sodiumTextField.setText("");
                priceTextField.setText("");
                viewProductsButton.doClick();
            } catch (ItemNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Product with id " + id + " does not exist!");
            }
        });

        getButton.addActionListener(e -> {
            Integer id = Integer.parseInt(idTextField.getText());
            try {
                MenuItem item = productRepository.getProductById(id);
                idTextField.setText(item.getId().toString());
                titleTextField.setText(item.getTitle());
                ratingTextField.setText(item.getRating().toString());
                caloriesTextField.setText(item.getCalories().toString());
                proteinsTextField.setText(item.getProteins().toString());
                fatsTextField.setText(item.getFats().toString());
                sodiumTextField.setText(item.getSodium().toString());
                priceTextField.setText(item.computePrice().toString());
            } catch (ItemNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Product with id " + id + " does not exist!");
            }
        });

        createMenuButton.addActionListener(e -> {
            String[] idsString = idsTextField.getText().split(",");
            List<Integer> ids = new LinkedList<>();
            for (int i = 0; i < idsString.length; i++) {
                ids.add(Integer.parseInt(idsString[i]));
            }
            try {
                administratorService.addCompositeProduct(ids);
                JOptionPane.showMessageDialog(null, "Menu was successfully created!");
            } catch (ItemAlreadyExistsException ex) {
                JOptionPane.showMessageDialog(null, "Item already exists!");
            } catch (ItemNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "At least one item was not found!");
            }
        });

        serializeProductsButton.addActionListener(e -> {
            String filename;
            if (filenameTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "File name not specified!");
            } else {
                filename = filenameTextField.getText();
                productRepository.serialize(filename);
            }
        });

        deserializeProductsButton.addActionListener(e -> {
            String filename;
            if (filenameTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "File name not specified!");
            } else {
                filename = filenameTextField.getText();
                productRepository.deserialize(filename);
            }
        });

        serializeOrdersButton.addActionListener(e -> {
            String filename;
            if (filenameTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "File name not specified!");
            } else {
                filename = filenameTextField.getText();
                orderRepository.serialize(filename);
            }
        });

        deserializeOrdersButton.addActionListener(e -> {
            String filename;
            if (filenameTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "File name not specified!");
            } else {
                filename = filenameTextField.getText();
                orderRepository.deserialize(filename);
            }
        });

        generateAReportButton.addActionListener(e -> {
            new ReportGUI(administratorService);
        });
    }
}
