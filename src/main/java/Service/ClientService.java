package Service;

import ObserverLayer.Observable;
import ObserverLayer.Observer;
import Domain.BaseProduct;
import Domain.MenuItem;
import Domain.Order;
import Exceptions.ItemNotFoundException;
import Repository.IOrderRepository;
import Repository.IProductRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClientService implements IClientService, Observable {
    private final List<Observer> observers;
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final AtomicInteger currentId;

    public ClientService(List<Observer> observers, IOrderRepository orderRepository, IProductRepository productRepository, AtomicInteger currentId) {
        this.observers = observers;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.currentId = currentId;
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::notifyObserver);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void addOrder(Integer clientId, Date orderDate, List<Integer> itemIds) throws IOException {
        List<MenuItem> items = itemIds.stream().map(id -> {
                    try {
                        return productRepository.getProductById(id);
                    } catch (ItemNotFoundException exception) {
                        System.out.println(exception.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Order order = new Order(currentId.getAndIncrement(), clientId, orderDate);
        orderRepository.addOrder(order, items);
        Integer currentId = order.getOrderId();
        String fileName = "bills\\bill" + currentId + ".txt";
        String billText = "Order: " + currentId.toString() + "\n" + "Ordered at:" + orderDate + '\n' + " Ordered by: " + clientId + "\n";
        for (MenuItem item : items) {
            billText += item.toString() + "\n";
        }
        billText += orderRepository.getCostForOrder(order);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write(billText);
        }
        notifyObservers();
    }

    @Override
    public List<MenuItem> filterProducts(List<String> filters, Integer pageNumber) {
        List<Predicate<MenuItem>> predicates = new LinkedList<>();
        String keyword = filters.get(0);
        if (!keyword.isEmpty()) {
            predicates.add(menuItem -> {
                if (!(menuItem instanceof BaseProduct baseProduct)) {
                    return false;
                }
                return baseProduct.getTitle().contains(keyword);
            });
        }
        if (!filters.get(1).isEmpty()) {
            Double rating = Double.parseDouble(filters.get(1));
            predicates.add(menuItem -> {
                if (!(menuItem instanceof BaseProduct baseProduct)) {
                    return false;
                }
                return baseProduct.getRating() >= rating;
            });
        }

        if (!filters.get(2).isEmpty()) {
            Double calories = Double.parseDouble(filters.get(2));
            predicates.add(menuItem -> menuItem.getCalories() <= calories);
        }

        if (!filters.get(3).isEmpty()) {
            Double proteins = Double.parseDouble(filters.get(3));
            predicates.add(menuItem -> menuItem.getProteins() >= proteins);
        }

        if (!filters.get(4).isEmpty()) {
            Double fats = Double.parseDouble(filters.get(4));
            predicates.add(menuItem -> menuItem.getFats() <= fats);
        }

        if (!filters.get(5).isEmpty()) {
            Double sodium = Double.parseDouble(filters.get(5));
            predicates.add(menuItem -> menuItem.getSodium() >= sodium);
        }

        if (!filters.get(6).isEmpty()) {
            Double price = Double.parseDouble(filters.get(6));
            predicates.add(menuItem -> menuItem.computePrice() <= price);
        }

        return productRepository.filter(predicates, pageNumber);
    }

    @Override
    public List<MenuItem> getProductsFromPage(List<String> filters, Integer pageNumber) {
        boolean noFilters = filters.stream().map(String::isEmpty)
                .reduce(Boolean.TRUE, (bool1, bool2) -> bool1 && bool2);
        if (noFilters)
            return productRepository.getItemsFromPage(pageNumber);
        else
            return filterProducts(filters, pageNumber);
    }
}
