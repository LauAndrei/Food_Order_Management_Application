package Domain;

import java.io.Serializable;

public interface MenuItem extends Serializable {

    Double computePrice();

    Double getCalories();

    Double getProteins();

    Double getFats();

    Double getSodium();

    Integer getId();

    String getTitle();

    Double getRating();
}
