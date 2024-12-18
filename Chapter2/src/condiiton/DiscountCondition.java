package condiiton;

import movie.Screening;

public interface DiscountCondition {
    boolean isSatisfiedBy(Screening screening);
}
