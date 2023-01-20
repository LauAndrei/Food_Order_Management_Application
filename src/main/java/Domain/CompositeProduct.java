package Domain;

import java.util.List;
import java.util.Objects;

public class CompositeProduct implements MenuItem{
    private final Integer id;
    private final List<MenuItem> items;

    public CompositeProduct(Integer id, List<MenuItem> items) {
        this.id = id;
        this.items = items;
    }

    @Override
    public Double computePrice() {
        return items.stream()
                .map(MenuItem::computePrice)
                .reduce((double) 0, Double::sum);
    }

    @Override
    public Double getCalories() {
        return items.stream()
                .map(MenuItem::getCalories)
                .reduce((double) 0, Double::sum);
    }

    @Override
    public Double getProteins() {
        return items.stream()
                .map(MenuItem::getProteins)
                .reduce((double) 0, Double::sum);
    }

    @Override
    public Double getFats() {
        return items.stream()
                .map(MenuItem::getFats)
                .reduce((double) 0, Double::sum);
    }

    @Override
    public Double getSodium() {
        return items.stream()
                .map(MenuItem::getSodium)
                .reduce((double) 0, Double::sum);
    }

    public List<MenuItem> getItems() {
        return items;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public Double getRating() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeProduct that = (CompositeProduct) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", items=" + items;
    }
}
