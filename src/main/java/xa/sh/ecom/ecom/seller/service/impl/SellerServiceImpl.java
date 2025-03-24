package xa.sh.ecom.ecom.seller.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.product.repo.ProductRepo;
import xa.sh.ecom.ecom.seller.models.Seller;
import xa.sh.ecom.ecom.seller.repo.SellerRepo;

@Service
public class SellerServiceImpl {

    @Autowired
    private SellerRepo sellerRepository;

    @Autowired
    private ProductRepo productRepository;

    // @Autowired
    // private OrderRepo orderRepository;

    public Seller approveSeller(Long sellerId) throws ResourceNotFoundException {
        Seller seller = sellerRepository.findById(sellerId)
            .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        
        seller.setIsApproved(true);
        return sellerRepository.save(seller);
    }

    // public Page<Product> getSellerProducts(Long sellerId, Pageable pageable) {
    //     return productRepository.findBySellerId(sellerId, pageable);
    // }

    // public SalesReport generateSalesReport(Long sellerId, DateRange dateRange) {
    //     List<OrderItem> items = orderRepository.findSellerOrderItems(
    //         sellerId, 
    //         dateRange.getStartDate(), 
    //         dateRange.getEndDate()
    //     );

    //     double totalSales = items.stream()
    //         .mapToDouble(item -> item.getPriceAtPurchase() * item.getQuantity())
    //         .sum();

    //     Map<Long, Integer> productSales = items.stream()
    //             .collect(Collectors.groupingBy(
    //                 item -> item.getProduct().getId(),
    //                 Collectors.summingInt(OrderItem::getQuantity)
    //             ));

    //     return new SalesReport(items.size(), totalSales, productSales);
    // }
}
