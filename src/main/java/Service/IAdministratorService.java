package Service;

import Domain.MenuItem;
import Domain.User;
import Exceptions.ItemAlreadyExistsException;
import Exceptions.ItemNotFoundException;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface IAdministratorService {
    void importProducts(String filename) throws IOException;
    void addBaseProduct(String title, Double rating, Double calories, Double proteins, Double fats, Double sodium, Double price) throws ItemAlreadyExistsException;
    void deleteProduct(Integer id) throws ItemNotFoundException;
    void updateBaseProduct(Integer id, String title, Double rating, Double calories, Double proteins, Double fats, Double sodium, Double price) throws ItemNotFoundException;
    void addCompositeProduct(List<Integer> ids) throws ItemAlreadyExistsException, ItemNotFoundException;
    List<MenuItem> getReportForNumberOfOrders(Integer count);
    List<User> getReportForClients(Integer minimumOrderCount, Integer minimumOrderValue);
    Map<MenuItem, Integer> getReportForProducts(Date dateRequested);
    void readProductsFromFile(String filename);
    void writeProductsToFile(String filename);
    void readOrdersFromFile(String filename);
    void writeOrdersToFile(String filename);
}
