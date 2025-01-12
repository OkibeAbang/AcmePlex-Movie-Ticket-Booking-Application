package DomainLayer;

public class RegisteredUser extends User{
    private String name;
    private String address;
    private CreditCard creditCard;

    public RegisteredUser(String name, String address, CreditCard creditCard, String email) {
        super(email);

        this.name = name;
        this.address = address;
        this.creditCard = creditCard;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }
}
