package GUI;

import Domain.MenuItem;
import Domain.User;
import Service.IAdministratorService;

import javax.swing.*;
import java.sql.Date;
import java.util.Map;

public class ReportGUI extends JFrame {
    private JPanel reportGUIPanel;
    private JTextField productCountTextField;
    private JButton generateProductCountButton;
    private JTextField productDayTextField;
    private JButton generateProductDayButton;
    private JTextField clientOrderCountTextField;
    private JButton generateClientRepButton;
    private JTextArea textArea1;
    private JTextField clientOrderValueTextField;

    IAdministratorService administratorService;

    public ReportGUI(IAdministratorService administratorService) {
        this.administratorService = administratorService;
        setContentPane(reportGUIPanel);
        setTitle("Reports Window");
        setBounds(400, 50, 600, 600);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        generateProductCountButton.addActionListener(e -> {
            Integer numberOfOrders;
            if (productCountTextField.getText().isEmpty()) {
                numberOfOrders = 1;
            } else {
                numberOfOrders = Integer.parseInt(productCountTextField.getText());
            }
            var report = administratorService.getReportForNumberOfOrders(numberOfOrders);
            String itemString = "";
            for (MenuItem item : report) {
                itemString += item.toString() + '\n' + '\n';
            }
            textArea1.setText(itemString);
        });

        generateProductDayButton.addActionListener(e -> {
            String date = "";
            if (productDayTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Insert a Date in Format: YYYY-MM-DD");
            } else {
                date = productDayTextField.getText();
                var report = administratorService.getReportForProducts(Date.valueOf(date));
                String itemString = "";
                for (Map.Entry<MenuItem, Integer> entry : report.entrySet()) {
                    itemString += entry.getKey().toString() + " number of times: " + entry.getValue() + '\n' + "\n";
                }
                textArea1.setText(itemString);
            }
        });

        generateClientRepButton.addActionListener(e -> {
            Integer minimumNumberOfTimes;
            Integer minimumValue;
            if (clientOrderValueTextField.getText().isEmpty() || clientOrderCountTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please insert the minimum order count and value!");
            } else {
                minimumNumberOfTimes = Integer.parseInt(clientOrderCountTextField.getText());
                minimumValue = Integer.parseInt(clientOrderValueTextField.getText());
                var report = administratorService.getReportForClients(minimumNumberOfTimes, minimumValue);
                String itemString = "";
                for (User user : report) {
                    itemString += user.toString() + "\n" + "\n";
                }
                textArea1.setText(itemString);
            }
        });
    }
}
