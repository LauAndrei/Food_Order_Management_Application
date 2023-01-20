package Service;

import Domain.*;
import Exceptions.ItemAlreadyExistsException;
import Exceptions.ItemNotFoundException;
import Repository.IOrderRepository;
import Repository.IProductRepository;
import Repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdministratorService implements IAdministratorService {
    private final IProductRepository productRepository;
    private final AtomicInteger currentId;
    private final IOrderRepository orderRepository;
    private final UserRepository userRepository;

    public AdministratorService(IProductRepository productRepository, IOrderRepository orderRepository, UserRepository userRepository, AtomicInteger currentId) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.currentId = currentId;
    }


    @Override
    public void importProducts(String filename) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            stream
                    .map(line -> line.split(","))
                    .skip(1)
                    .map(line -> new BaseProduct(currentId.getAndIncrement(), line[0], Double.parseDouble(line[1]),
                            Double.parseDouble(line[2]), Double.parseDouble(line[3]),
                            Double.parseDouble(line[4]), Double.parseDouble(line[5]),
                            Double.parseDouble(line[6])))
                    .forEach(
                            item -> {
                                try {
                                    productRepository.addProduct(item);
                                } catch (ItemAlreadyExistsException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
        }
    }

    @Override
    public void addBaseProduct(String title, Double rating, Double calories, Double proteins, Double fats, Double sodium, Double price) throws ItemAlreadyExistsException {
        MenuItem baseProduct = new BaseProduct(currentId.getAndIncrement(), title, rating, calories, proteins, fats, sodium, price);
        productRepository.addProduct(baseProduct);
    }

    @Override
    public void deleteProduct(Integer id) throws ItemNotFoundException {
        productRepository.deleteProduct(id);
    }

    @Override
    public void updateBaseProduct(Integer id, String title, Double rating, Double calories, Double proteins, Double fats, Double sodium, Double price) throws ItemNotFoundException {
        MenuItem baseProduct = new BaseProduct(id, title, rating, calories, proteins, fats, sodium, price);
        productRepository.updateProduct(baseProduct);
    }

    @Override
    public void addCompositeProduct(List<Integer> ids) throws ItemAlreadyExistsException, ItemNotFoundException {
        List<MenuItem> containedItems = ids.stream().map(id -> {
                    try {
                        return productRepository.getProductById(id);
                    } catch (ItemNotFoundException exception) {
                        System.out.println(exception.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (containedItems.size() <= 1)
            throw new ItemNotFoundException("List of items is empty or has only 1 item!");
        MenuItem compositeProduct = new CompositeProduct(currentId.getAndIncrement(), containedItems);
        productRepository.addProduct(compositeProduct);
    }

    public List<MenuItem> getReportForNumberOfOrders(Integer count) {
        List<MenuItem> itemsOrdered = orderRepository.getOrders()
                .values()
                .stream().toList()
                .stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        Map<MenuItem, Integer> itemFrequencies = new HashMap<>();
        itemsOrdered.forEach(item -> itemFrequencies.merge(item, 1, Integer::sum));
        return itemFrequencies.keySet().stream().filter(key -> itemFrequencies.get(key) >= count)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getReportForClients(Integer minimumOrderCount, Integer minimumOrderValue) {
        List<Integer> clientsOrdering = orderRepository.getOrders()
                .keySet().stream().map(Order::getClientId)
                .collect(Collectors.toList());
        Map<Integer, Integer> clientOrderFrequencies = new HashMap<>();
        clientsOrdering.forEach(client -> clientOrderFrequencies.merge(client, 1, Integer::sum));
        Set<Integer> firstFilteredClients = clientOrderFrequencies.keySet().stream()
                .filter(key -> clientOrderFrequencies.get(key) >= minimumOrderCount)
                .collect(Collectors.toSet());
        Map<Integer, Double> clientCosts = new HashMap<>();

        List<Order> relevantOrders = orderRepository.getOrders()
                .keySet().stream().filter(order -> firstFilteredClients.contains(order.getClientId()))
                .collect(Collectors.toList());
        relevantOrders.forEach(order -> clientCosts.merge(order.getClientId(), orderRepository.getCostForOrder(order), Double::sum));
        List<Integer> userIds = clientCosts.keySet().stream().filter(key -> clientCosts.get(key) >= minimumOrderValue)
                .collect(Collectors.toList());

        return userIds
                .stream()
                .map(userRepository::getUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public Map<MenuItem, Integer> getReportForProducts(Date dateRequested) {

        List<MenuItem> menuItems = orderRepository.getOrders()
                .keySet().stream().filter(order -> order.getOrderDate().equals(dateRequested))
                .map(orderRepository::getItemsForOrder)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        Map<MenuItem, Integer> itemFrequencies = new HashMap<>();
        menuItems.forEach(item -> itemFrequencies.merge(item, 1, Integer::sum));
        return itemFrequencies;
    }

    @Override
    public void readProductsFromFile(String filename) {
        productRepository.deserialize(filename);
    }

    @Override
    public void writeProductsToFile(String filename) {
        productRepository.serialize(filename);
    }

    @Override
    public void readOrdersFromFile(String filename) {
        orderRepository.deserialize(filename);
    }

    @Override
    public void writeOrdersToFile(String filename) {
        orderRepository.serialize(filename);
    }
}
