package Repository;

import Domain.MenuItem;
import Exceptions.ItemAlreadyExistsException;
import Exceptions.ItemNotFoundException;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProductRepository implements IProductRepository{
    private Map<Integer, MenuItem> items;
    private static final Integer itemsPerPage = 10;

    public ProductRepository(Map<Integer, MenuItem> items) {
        this.items = items;
    }

    @Override
    public void addProduct(MenuItem item) throws ItemAlreadyExistsException {
        Integer id = item.getId();
        if (items.containsKey(id))
            throw new ItemAlreadyExistsException("Item with ID " + id + " already exists!");
        items.put(id, item);
    }

    @Override
    public void deleteProduct(Integer id) throws ItemNotFoundException {
        if (!items.containsKey(id))
            throw new ItemNotFoundException("Item with ID " + id + " does not exist!");
        items.remove(id);
    }

    @Override
    public void updateProduct(MenuItem item) throws ItemNotFoundException {
        Integer id = item.getId();
        if (!items.containsKey(id))
            throw new ItemNotFoundException("Item with ID " + id + " does not exist!");
        items.replace(id, item);
    }

    @Override
    public List<MenuItem> getItemsFromPage(Integer page) {
        return items.values().stream()
                .skip((long) (page - 1) * itemsPerPage)
                .limit(itemsPerPage)
                .collect(Collectors.toList());
    }

    @Override
    public MenuItem getProductById(Integer id) throws ItemNotFoundException {
        if (!items.containsKey(id))
            throw new ItemNotFoundException("Item with ID " + id + " does not exist!");
        return items.get(id);
    }

    @Override
    public List<MenuItem> filter(List<Predicate<MenuItem>> predicates, Integer pageNumber) {
        Predicate<MenuItem> predicate = predicates.stream().reduce(Predicate::and).orElse(x->true);
        return items.values().stream().filter(predicate)
                .skip((long) (pageNumber - 1) * itemsPerPage)
                .limit(itemsPerPage)
                .collect(Collectors.toList());
    }

    @Override
    public void serialize(String filename) {
        try (FileOutputStream fileOutputStream
                     = new FileOutputStream(filename);
             ObjectOutputStream objectOutputStream
                     = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(items);
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
            items = (Map<Integer, MenuItem>)objectInputStream.readObject();
        }
        catch (IOException | ClassNotFoundException exception){
            items = new HashMap<>();
        }
    }
}
