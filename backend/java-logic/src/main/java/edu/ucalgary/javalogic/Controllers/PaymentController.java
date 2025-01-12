package edu.ucalgary.javalogic.Controllers;

import edu.ucalgary.javalogic.Email.EmailService;
import edu.ucalgary.javalogic.Entities.*;
import edu.ucalgary.javalogic.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.TimeUnit;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow React app to connect
public class PaymentController {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TheatreRepository theatreRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ShowtimeRepository showtimeRepository;
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private EmailService emailService;

    public static boolean isWithin72Hours(Date dateToCheck) {
        if (dateToCheck == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        Date currentDate = new Date(); // Get the current date and time
        long timeDifference = dateToCheck.getTime() - currentDate.getTime(); // Time difference in milliseconds

        long hoursDifference = TimeUnit.MILLISECONDS.toHours(timeDifference); // Convert to hours
        return hoursDifference <= 72; // Check if within 72 hours
    }

    @GetMapping("/payment_methods/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethodById(@PathVariable("id") Long id) {
        if (!paymentMethodRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paymentMethodRepository.findById(id).get(), HttpStatus.OK);
    }

    @GetMapping("/payment_methods")
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethods() {
        try {
            List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();
            return new ResponseEntity<>(paymentMethods, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/payment_methods")
    public ResponseEntity<PaymentMethod> createPaymentMethod(@RequestBody PaymentMethod paymentMethod) {
        if (!userRepository.existsById(paymentMethod.getUserId())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findById(paymentMethod.getUserId()).get();
        user.addPaymentMethod(paymentMethod);
//        userRepository.save(user);
        return new ResponseEntity<>(paymentMethodRepository.save(paymentMethod), HttpStatus.CREATED);
    }

    @DeleteMapping("/payment_methods/{id}")
    public ResponseEntity<PaymentMethod> deletePaymentMethod(@PathVariable("id") Long id) {
        if (paymentMethodRepository.existsById(id)) {
            paymentMethodRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable("id") Long id) {
        if (!paymentRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(paymentRepository.findById(id).get(), HttpStatus.OK);
    }

    // Must set the user and paymentMethod fields to null before sending
    @PostMapping("/make_ticket_payment/{user_id}/{payment_method_id}")
    public ResponseEntity<Payment> makePayment(@RequestBody Payment payment, @PathVariable("user_id") Long user_id, @PathVariable("payment_method_id") Long payment_method_id) {
        if (!userRepository.existsById(user_id) || !paymentRepository.existsById(payment_method_id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userRepository.findById(user_id).get();
        PaymentMethod paymentMethod = paymentMethodRepository.findById(payment_method_id).get();

        Double creditUsed = 0.00;
        if (user.getCredit() < 10.00) {
            creditUsed = user.getCredit();
            user.setCredit(0.00);
        }
        else {
            creditUsed = user.getCredit() - 10.00;
            user.setCredit(user.getCredit() - 10.00);
        }

        payment.setUser(user);
        payment.setPaymentMethod(paymentMethod);

        userRepository.save(payment.getUser());

        payment.setCreditUsed(creditUsed);
        payment.setTotal(10.00 - creditUsed);

        payment = paymentRepository.save(payment);

        try {
            emailService.sendReceipt(user, payment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(payment, HttpStatus.CREATED);

    }

    @PostMapping("/make_premium_payment/{user_id}/{payment_method_id}")
    public ResponseEntity<Payment> makePremiumPayment(@RequestBody Payment payment, @PathVariable("user_id") Long user_id, @PathVariable("payment_method_id") Long payment_method_id) {

        if (!userRepository.existsById(user_id) || !paymentMethodRepository.existsById(payment_method_id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userRepository.findById(user_id).get();
        PaymentMethod paymentMethod = paymentMethodRepository.findById(payment_method_id).get();

        Double creditUsed = 0.00;
        if (user.getCredit() < 20.00) {
            creditUsed = user.getCredit();
            user.setCredit(0.00);
        }
        else {
            creditUsed = user.getCredit() - 20.00;
            user.setCredit(user.getCredit() - 20.00);
        }

        payment.setUser(user);
        payment.setPaymentMethod(paymentMethod);

        if (!user.getPremium()) {
            user.setPremium(true);
            user.setPremiumSignupDate(new Date());
        }

        userRepository.save(payment.getUser());

        payment.setCreditUsed(creditUsed);
        payment.setTotal(20.00 - creditUsed);

        payment = paymentRepository.save(payment);

        try {
            emailService.sendReceipt(user, payment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(payment, HttpStatus.CREATED);

    }

    @PostMapping("/tickets/cancel/{user_id}/{ticket_id}")
    public ResponseEntity<Ticket> cancelTicket(@PathVariable("user_id") Long user_id, @PathVariable("ticket_id") Long ticket_id) {
        if (!ticketRepository.existsById(ticket_id) || !userRepository.existsById(user_id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Ticket ticket = ticketRepository.findById(ticket_id).get();
        User user = userRepository.findById(user_id).get();

        if (!user.getTickets().contains(ticket) || ticket.getState().equals("Cancelled")) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (isWithin72Hours(ticket.getShowtime().get_Date())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPaymentType("Cancellation");
        payment.setSubtotal(0.00);
        payment.setTotal(0.00);

        if (user.getPremium()) {
            user.setCredit(user.getCredit() + 10.00);
            payment.setCreditUsed(-10.00);
        }
        else {
            user.setCredit(user.getCredit() + 8.50);
            payment.setCreditUsed(-8.50);
        }

        ticket.setState("Cancelled");
        userRepository.save(user);
        paymentRepository.save(payment);

        ticket = ticketRepository.save(ticket);

        try {
            emailService.sendCancellation(user, payment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(ticketRepository.save(ticket), HttpStatus.OK);
    }

    @PostMapping("/users/{user_id}/tickets/{theatre_id}/{movie_id}/{showtime_id}/{seat_string}")
    public ResponseEntity<Ticket> createTicket(@PathVariable("user_id") Long user_id, @PathVariable("theatre_id") Long theatre_id,
                                               @PathVariable("movie_id") Long movie_id, @PathVariable("showtime_id") Long showtime_id,
                                               @PathVariable("seat_string") String seat_string, @RequestBody Ticket ticket) {
        if (!theatreRepository.existsById(theatre_id)
                || !movieRepository.existsById(movie_id)
                || !showtimeRepository.existsById(showtime_id)
                || !userRepository.existsById(user_id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userRepository.findById(user_id).get();

        Showtime showtime = showtimeRepository.findById(showtime_id).get();
        Seat seat = new Seat();
        seat.setSeat(seat_string);
        seat.setIsReserved(true);
        seat.setShowtime_id(showtime_id);
        showtime.addSeat(seat);

        ticket.setTheatre(theatreRepository.findById(theatre_id).get());
        ticket.setMovie(movieRepository.findById(movie_id).get());
        ticket.setShowtime(showtimeRepository.findById(showtime_id).get());
        ticket.setSeat(seat);
        ticket.setUserId(user_id);

        user.addTicket(ticket);

        seatRepository.save(seat);

        ticket = ticketRepository.save(ticket);

        try {
            emailService.sendTicket(user, ticket);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(ticket, HttpStatus.CREATED);

    }

    @PutMapping("/users/{user_id}/tickets/check_expiry")
    public ResponseEntity<Void> checkExpiryOnTickets(@PathVariable("user_id") Long user_id) {
        if (!userRepository.existsById(user_id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = userRepository.findById(user_id).get();
        List<Ticket> tickets = user.getTickets();
        Date currentDate = new Date();
        for (Ticket ticket : tickets) {
            if (ticket.getShowtime().get_Date().before(currentDate)) {
//                System.out.println(ticket.getShowtime().get_Date());
                ticket.setState("Expired");
            }
        }
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
