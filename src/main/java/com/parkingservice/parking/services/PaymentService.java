package com.parkingservice.parking.services;

import com.parkingservice.parking.controllers.dto.PaymentDTO;
import com.parkingservice.parking.models.Payment;
import com.parkingservice.parking.models.Reservation;
import com.parkingservice.parking.repositories.PaymentRepository;
import com.parkingservice.parking.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    public PaymentService(PaymentRepository paymentRepository, ReservationRepository reservationRepository) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Payment createPayment(PaymentDTO paymentDTO) {
        // Need to check payment validity here
        Payment payment = new Payment();

        Reservation reservation = reservationRepository.findById(paymentDTO.getReservationID())
                        .orElseThrow(() -> new IllegalArgumentException("Reservation not found with ID " +
                                paymentDTO.getReservationID()));

        payment.setReservation(reservation);
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setTransactionId(paymentDTO.getTransactionID());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        return paymentRepository.save(payment);
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + id));
    }

    @Transactional
    public Payment updatePayment(Long id, PaymentDTO paymentDTO) {
        Payment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + id));

        existingPayment.setPaymentDate(paymentDTO.getPaymentDate());
        existingPayment.setPaymentMethod(paymentDTO.getPaymentMethod());
        existingPayment.setAmount(paymentDTO.getAmount());

        return paymentRepository.save(existingPayment);
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + id));

        paymentRepository.delete(payment);
    }
}