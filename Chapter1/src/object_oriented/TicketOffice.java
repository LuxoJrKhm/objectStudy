package object_oriented;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicketOffice {
    private Long amount;
    private List<Ticket> tickets = new ArrayList<Ticket>();

    public TicketOffice(Long amount, Ticket ... tickets) {
        this.amount = amount;
        this.tickets.addAll(Arrays.asList(tickets));
    }
    public Ticket getTicket() {
        return tickets.remove(0);
    }
    private void minusAmount(Long amount) {
        this.amount -= amount;
    }
    public void plusAmount(Long amount) {
        this.amount += amount;
    }

    // removing TicketSeller
    // public void sellTo(Audience audience) {
    public void sellTicketTo(Audience audience) {
        Ticket ticket = getTicket();
        plusAmount(audience.buy(ticket));
    }
}
