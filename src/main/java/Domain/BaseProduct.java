package Domain;

import java.util.Objects;

public class BaseProduct implements MenuItem{
    private final Integer id;
    private final String title;
    private final Double rating;
    private final Double calories;
    private final Double proteins;
    private final Double fats;
    private final Double sodium;
    private final Double price;

    public BaseProduct(Integer id, String title, Double rating, Double calories, Double proteins, Double fats, Double sodium, Double price) {
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.sodium = sodium;
        this.price = price;
    }

    @Override
    public Double computePrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public Double getRating() {
        return rating;
    }

    public Double getCalories() {
        return calories;
    }

    public Double getProteins() {
        return proteins;
    }

    public Double getFats() {
        return fats;
    }

    public Double getSodium() {
        return sodium;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseProduct that = (BaseProduct) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", rating=" + rating +
                ", calories=" + calories +
                ", proteins=" + proteins +
                ", fats=" + fats +
                ", sodium=" + sodium +
                ", price=" + price + '\n';
    }
}
