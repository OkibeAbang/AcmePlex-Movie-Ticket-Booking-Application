package DomainLayer;

import java.util.ArrayList;

public abstract class User {
    private String email;
    private ArrayList<Ticket> tickets;
    public User(String email) {
        this.email = email;
    }

    public void addTicket(Ticket newTicket) {
        tickets.add(newTicket);
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }
}
