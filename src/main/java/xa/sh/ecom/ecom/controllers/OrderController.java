package xa.sh.ecom.ecom.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
// Import relevant exceptions
import xa.sh.ecom.ecom.exception.EmptyCartException;
import xa.sh.ecom.ecom.exception.InsufficientStockException;
import xa.sh.ecom.ecom.exception.InvalidCouponException;
import xa.sh.ecom.ecom.exception.InvalidGiftCardException;
import xa.sh.ecom.ecom.exception.ResourceNotFoundException;
import xa.sh.ecom.ecom.order.models.Order;
import xa.sh.ecom.ecom.order.service.impl.OrderRequestDto;
import xa.sh.ecom.ecom.order.service.impl.OrderServiceImpl; // TODO: Use OrderService interface
import xa.sh.ecom.ecom.security.SecurityUtils; // Your utility

@RestController
@RequestMapping("/api/orders")
@PreAuthorize("isAuthenticated()") // User must be logged in
public class OrderController {

    private final OrderServiceImpl orderService; // TODO: Inject OrderService interface

    // Constructor Injection
    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    // Create a new order from the user's cart
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequestDto orderRequest)
            throws InvalidCouponException, ResourceNotFoundException, InvalidGiftCardException,
                   EmptyCartException, InsufficientStockException, Exception { // Exception needs refinement in service

        Long userId = SecurityUtils.getAuthenticatedUserId();

        // !!! IMPORTANT: Assumes OrderServiceImpl is FIXED !!!
        Order createdOrder = orderService.createOrder(userId, orderRequest);

        // Return 201 Created status
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    // --- Add methods to get orders ---
    // Example: Get a specific order by ID (ensure user owns the order)
    // @GetMapping("/{orderId}")
    // public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) throws ResourceNotFoundException, AccessDeniedException {
    //     Long userId = SecurityUtils.getAuthenticatedUserId();
    //     Order order = orderService.getOrderByIdForUser(orderId, userId); // Requires new service method
    //     return ResponseEntity.ok(order);
    // }

    // Example: Get all orders for the current user (with pagination)
    // @GetMapping
    // public ResponseEntity<Page<Order>> getUserOrders(
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "10") int size) throws ResourceNotFoundException {
    //     Long userId = SecurityUtils.getAuthenticatedUserId();
    //     Page<Order> orders = orderService.findOrdersByUserId(userId, PageRequest.of(page, size)); // Requires new service method
    //     return ResponseEntity.ok(orders);
    // }

}