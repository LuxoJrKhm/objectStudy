package movie;

import java.time.LocalDateTime;

public class Screening {
    private Movie movie; // 상영 영화
    private int sequence; //
    private LocalDateTime whenScreened; // 상영 시간

    private Money calculationFee(int audienceCount) {
        return movie.calculateMovieFee(this).times(audienceCount);
    }

    public Screening(Movie movie, int sequence, LocalDateTime whenScreened) {
        this.movie = movie;
        this.sequence = sequence;
        this.whenScreened = whenScreened;
    }

    public LocalDateTime getStartTime() {
        return whenScreened;
    }

    public boolean isSequence(int sequence) {
        return this.sequence == sequence;
    }

    public Money getMovieFee() {
        return movie.getFee();
    }

    public Reservation reserve(Customer customer, int audienceCount) {
        return new Reservation(customer, this, calculationFee(audienceCount), audienceCount);
    }
}
