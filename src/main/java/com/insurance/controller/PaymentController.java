package com.insurance.controller;

import com.insurance.model.ApiResponse;
import com.insurance.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.insurance.dto.Payment.PaymentDTO;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    

    public PaymentController(PaymentService paymentService){
        this.paymentService=paymentService;
    }

    

    @PostMapping("/pay")
    public ResponseEntity<ApiResponse<PaymentDTO>> makePayment(
            @RequestParam UUID userId,
            @RequestParam UUID policyId,
            @RequestParam Double amount
    ) {
        PaymentDTO dto = paymentService.makePayment(userId, policyId, amount);
        ApiResponse<PaymentDTO> response = new ApiResponse<>(
                "Payment processed (mock)",
                dto,
                true
        );
        return ResponseEntity.ok(response);
    }



    @GetMapping("/{transactionId}")
    public ResponseEntity<ApiResponse<PaymentDTO>> getPaymentByTransactionId(@PathVariable String transactionId) {
        PaymentDTO dto = paymentService.getPaymentByTransactionId(transactionId);
        ApiResponse<PaymentDTO> response = new ApiResponse<>(
                "Payment retrieved successfully",
                dto,
                true
        );
        return ResponseEntity.ok(response);
    }
}
