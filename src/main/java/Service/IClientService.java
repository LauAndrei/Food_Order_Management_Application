package Service;

import Domain.MenuItem;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public interface IClientService {
    void addOrder(Integer clientId, Date orderDate, List<Integer> itemIds) throws IOException;

    List<MenuItem> filterProducts(List<String> filters, Integer pageNumber);

    List<MenuItem> getProductsFromPage(List<String> filters, Integer pageNumber);
}
