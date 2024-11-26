package object_oriented;

public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public Bag getBag() {
        return bag;
    }
    // modifying getBag() method
    public Long buy(Ticket ticket) {
        return bag.hold(ticket);
    }
}
