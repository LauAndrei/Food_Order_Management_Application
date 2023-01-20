package Repository;

import Domain.MenuItem;
import Exceptions.ItemAlreadyExistsException;
import Exceptions.ItemNotFoundException;

import java.util.List;
import java.util.function.Predicate;


public interface IProductRepository {
    void addProduct(MenuItem item) throws ItemAlreadyExistsException;
    void deleteProduct(Integer id) throws ItemNotFoundException;
    void updateProduct(MenuItem item) throws ItemNotFoundException;
    List<MenuItem> getItemsFromPage(Integer page);
    MenuItem getProductById(Integer id) throws ItemNotFoundException;
    List<MenuItem> filter(List<Predicate<MenuItem>> predicates, Integer pageCount);
    void serialize(String filename);
    void deserialize(String filename);
}
