package edu.ucalgary.javalogic.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_methods")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_holder_name")
    private String card_holder_name;

    @Column(name = "card_num")
    private String cardNum;

    @Column(name = "exp_date")
    private String expDate;

    @Column(name = "sec_num")
    private Integer secNum;

    @Column(name = "user_id")
    private Long userId;

    public PaymentMethod() {}

    public PaymentMethod(String cardHolderName, String cardNum, String expDate, Integer secNum, Long userId) {
        this.card_holder_name = cardHolderName;
        this.cardNum = cardNum;
        this.expDate = expDate;
        this.secNum = secNum;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCard_holder_name() {
        return card_holder_name;
    }

    public void setCard_holder_name(String cardHolderName) {
        this.card_holder_name = cardHolderName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public Integer getSecNum() {
        return secNum;
    }

    public void setSecNum(Integer secNum) {
        this.secNum = secNum;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
