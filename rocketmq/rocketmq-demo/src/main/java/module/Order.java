package module;

public class Order {

    private long orderId;
    private String desc;

    public Order() {
    }

    public Order(long orderId, String desc) {
        this.orderId = orderId;
        this.desc = desc;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", desc='" + desc + '\'' +
                '}';
    }
}
