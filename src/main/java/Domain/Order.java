package Domain;

import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

public class Order implements Serializable {
    private final Integer orderId;
    private final Integer clientId;
    private final Date orderDate;

    public Order(Integer orderId, Integer clientId, Date orderDate) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.orderDate = orderDate;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public Date getOrderDate() {
        return orderDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId.equals(order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return "orderId = " + orderId + ", clientId = " + clientId + ", orderDate = " + orderDate + "\n";
    }
}
