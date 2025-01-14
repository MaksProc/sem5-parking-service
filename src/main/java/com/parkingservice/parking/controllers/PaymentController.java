package com.parkingservice.parking.controllers;

import com.parkingservice.parking.controllers.dto.PaymentDTO;
import com.parkingservice.parking.models.Payment;
import com.parkingservice.parking.services.PaymentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {


    private final PaymentService paymentService;
    private static final Logger logger = LogManager.getLogger(PaymentController.class);

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping

    //

    public ResponseEntity<Payment> createPayment(@RequestBody PaymentDTO paymentDTO) {
        logger.info("Processing payment at POST request for /api/payments. Transaction ID: "
                + paymentDTO.getTransactionID());
        try {
            Payment createdPayment = paymentService.createPayment(paymentDTO);
            return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.warn("Payment failed for reservation " + paymentDTO.getTransactionID());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        logger.info("Handling GET request for /api/payments/" + id);
        try {
            Payment payment = paymentService.getPaymentById(id);
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("Payment not found with ID " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")

    //

    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        logger.info("Updating payment at PUT request for /api/payments/" + id);
        try {
            Payment updatedPayment = paymentService.updatePayment(id, paymentDTO);
            return new ResponseEntity<>(updatedPayment, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to update payment. Provided ID: " + id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        logger.info("Handling DEL request at /api/payments/" + id);
        try {
            paymentService.deletePayment(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            logger.warn("Deletion failed. Payment not found with ID: " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}