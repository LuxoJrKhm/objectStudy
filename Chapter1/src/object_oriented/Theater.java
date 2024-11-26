package object_oriented;

public class Theater {
    private TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }
    public void enter(Audience audience) {
        ticketSeller.sellTo(audience);
    }
    /*
    // removing TicketSeller
    private TicketOffice ticketOffice;

    public Theater(TicketOffice ticketOffice) {
        this. ticketOffice = ticketOffice;
    }

    public void enter(Audience audience) {
        ticketOffice.sellTo(audience);
    }
     */

}
