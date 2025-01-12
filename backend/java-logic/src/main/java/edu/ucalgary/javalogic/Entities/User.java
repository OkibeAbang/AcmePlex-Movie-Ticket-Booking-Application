package edu.ucalgary.javalogic.Entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "_email", unique = true)
    private String email;

    @Column(name = "_password")
    private String password;

    @Column(name = "fname")
    private String firstName;

    @Column(name = "lname")
    private String lastName;

    @Column(name = "credit")
    private Double credit;

    @Column(name = "premium")
    private Boolean premium;

    @Column(name = "premium_sign_up_date")
    private Date premiumSignupDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<PaymentMethod> payment_methods = new ArrayList<>();

    public User() {}

    public User(String email, String password, String firstName, String lastName, Double credit, Boolean premium, Date premiumSignupDate) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.credit = credit;
        this.premium = premium;
        this.premiumSignupDate = premiumSignupDate;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public Date getPremiumSignupDate() {
        return premiumSignupDate;
    }

    public void setPremiumSignupDate(Date premiumSignupDate) {
        this.premiumSignupDate = premiumSignupDate;
    }

    public List<PaymentMethod> getPayment_methods() {
        return payment_methods;
    }

    public void setPayment_methods(List<PaymentMethod> payment_methods) {
        this.payment_methods = payment_methods;
    }

    public void addPaymentMethod(PaymentMethod paymentMethod) {
        payment_methods.add(paymentMethod);
    }


}
