package object_oriented;

public class Bag {
    private Long amount; // cash
    private Ticket ticket;
    private Invitation invitation;

    // Constructor
    public Bag(Long amount) {
        this(null , amount);
    }
    public Bag(Invitation invitation, Long amount) {
        this.amount = amount;
        this.invitation = invitation;
    }

    // enter condition
    public boolean hasInvitation() {
        return invitation != null;
    }
    public boolean hasTicket() {
        return ticket != null;
    }

    // set ticket if right condition
    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    // cash amount update
    public void minusAmount(Long amount) {
        this.amount -= amount;
    }
    public void plusAmount(Long amount) {
        this.amount += amount;
    }

    public Long hold(Ticket ticket) {
        if (hasInvitation()) {
            setTicket(ticket);
            return 0L;
        } else {
            minusAmount(ticket.getFee());
            setTicket(ticket);
            return ticket.getFee();
        }
    }
}
