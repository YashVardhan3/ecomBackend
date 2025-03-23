package xa.sh.ecom.ecom.seller.dto;

import java.util.Map;

public class SalesReport {
    private int totalOrders;
    private double totalRevenue;
    private Map<Long, Integer> productSales; // Product ID -> Quantity Sold

    public SalesReport(int totalOrders, double totalRevenue, Map<Long, Integer> productSales) {
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.productSales = productSales;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public Map<Long, Integer> getProductSales() {
        return productSales;
    }
}
