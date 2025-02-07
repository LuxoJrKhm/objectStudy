# chapter4 [설계 품질과 트레이드오프]

## 요약 전 정리

### 좋은 설계의 특징들

이 장에서는 **데이터 중심 설계**와 **객체 중심 설계**의 차이와 왜 **객체 중심 설계**가 좋은지에 대해 서술하고 있다.  더불어 좋은 설계의 기준점들도 몇 가지를 제시하고 있는데, 먼저 이 부분을 정리하고 가는 게 좋을 것 같다.

- **캡슐화**
- **응집도**
- **결합도**

---

### 캡슐화

변경될 가능성이 높은 부분을 **구현**이라고 부르고, 상대적으로 안정적인 부분을 **인터페이스**라고 부르기로 하자. (여기서 **구현**, **인터페이스**는 클래스 단위의 **구현**, **인터페이스** 보다는 조금 세부적인 수준이다)

구현이 되는 부분은 되도록 내부에서만 돌아가게 하고, 외부에서는 안정적인 인터페이스만 사용할 수 있게 만드는 것이 캡슐화의 핵심이다. 이렇게 하면 내부 구현이 변경이 되었을 때 외부에서 생기는 에러를 어느 정도 줄일 수 있다.

### 예시1 : `Movie` 클래스

```java
public class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private DiscountPolicy discountPolicy;
    ...

    public Money getFee() {
        return fee;
    }
    public Money calculateMovieFee(Screening screening) {
        return fee.minus(discountPolicy.calculateDiscountAmount(screening));
    }

}
```

---

### 응집도

모듈에 포함된 내부 요소들이 연관되어 있는 정도이다.  어렵게 들릴 수 있으나, 쉽게 말하면 한 책임에 관련된 요소들이 얼마나 하나의 집단에 모여있는지의 정도이다.

**단일 책임 원칙(SRP)** 과 관련이 있는데, 한 모듈이 담당하는 책임, 즉 변경 요인이 하나일 때 응집도가 높다고 할 수 있고, 한 모듈의 책임이 여러 개면 응집도가 낮다고 할 수 있다.

### 예시2 : `OrderService` 클래스 (책에 없음)

```java
public class OrderService {
    public void createOrder(Order order) {
        // 주문 생성 로직
    }

    public void cancelOrder(int orderId) {
        // 주문 취소 로직
    }

    public Order getOrderDetails(int orderId) {
        // 주문 상세 조회 로직
        return new Order();
    }
}
```

---

### 결합도

의존성의 정도를 나타낸다. 즉, 한 클래스가 자신의 로직을 구현하기 위해 다른 클래스의 사항들을 얼마나 알고 있어야 되는지를 나타낸다.

의존성의 정도는 얼마나 **강하게**, 또는 **느슨하게** 다른 클래스와 결합되어 있는지로 바꾸어 말할 수 있다.

**인터페이스**를 사용하는 것이 결합도를 낮추는 방법이라는 것이 잘 알려져 있다. 인터페이스를 사용하므로써 한 클래스가 다른 클래스의 속성들 중에서 꼭 필요한 내용만 알고 사용할 수 있게끔 한 단계 감추어두는 역할을 하기 때문이다.

## 데이터 중심의 영화 예매 시스템

> **데이터 중심 설계**와 **책임 중심 설계**의 차이를 이해하기 위해 2장과 다르게 데이터 중심으로 설계한 영화 예매 시스템을 책에 있는 대로 적어 보았다.
>

### 예시3 : 영화 예매 구성 클래스들

```java
class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountCondition> discountConditions;

    private MovieType movieType;
    private Money discountAmount;
    private double discountPercent;
}

enum DiscountConditionType {
    SEQUENCE, // 순번 조건
    PERIOD // 기간 조건
}

class DiscountCondition {
    private DiscountConditionType type;

    private int sequence;

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}

public enum MovieType {
    AMOUNT_DISCOUNT, // 금액 할인 정책
    PERCENT_DISCOUNT, // 비율 할인 정책
    NONE_DISCOUNT // 미적용
}

public class Reservation {
    private Customer customer;
    private Screening screening;
    private Money fee;
    private int audienceCount;
}

public class Screening {
    private Movie movie;
    private int sequence;
    private LocalDateTime whenScreened;
}

public class Customer {
    private String name;
    private String id;
}    
```

### 예시4: `ReservationAgency` 클래스

```java
class ReservationAgency {
    public Reservation reserve(Screening screening, Customer customer, int audienceCount) {
        Movie movie = screening.getMovie();

        boolean discountable = false;
        for (DiscountCondition condition : movie.getDiscountConditions()) {
            if (condition.getType() == DiscountConditionType.PERIOD) {
                discountable = screening.getWhenScreened().getDayOfWeek().equals(condition.getDayOfWeek()) && condition.getStartTime().compareTo(screening.getWhenScreened().toLocalTime()) <= 0 && condition.getEndTime().compareTo(screening.getWhenScreened().toLocalTime()) >= 0;
            } else {
                discountable = condition.getSequence() == screening.getSequence();
            }

            if (discountable) {
                break;
            }
        }
        // 상영할 영화를 가져와서 그 영화 안에 정의된 가능한 할인 조건들을 하나씩 검사한다.  
        // 각 조건들은 기간과 순번으로 나뉘는데 그 조건들 중 하나에 해당되는 경우에 discountable을 true로 바꾼다.    

        Money fee;
        if (discountable) {
            Money discountAmount = Money.ZERO;
            switch (movie.getMovieType()) {
                case AMOUNT_DISCOUNT:
                    discountAmount = movie.getDiscountAmount();
                    break;
                case PERCENT_DISCOUNT:
                    discountAmount = movie.getFee().times(movie.getDiscountPercent());
                    break;
                case NONE_DISCOUNT:
                    discountAmount = Money.ZERO;
                    break;
            }
            fee = movie.getFee().minus(discountAmount);
        } else {
            fee = movie.getFee();
        }
        return new Reservation(customer, screening, fee, audienceCount);
        // 할인이 되는 경우 할인되는 양을 계산해서 예매표를 만든다.
    }
}
```

위 클래스들은 영화 예매를 위한 설계에서 어떤 책임이 필요한지 생각하는 대신 어떤 구성 요소가 필요한지를 생각하여 작성된 클래스들이다.

**`ReservationAgency`  클래스**와 **그 외 클래스**로 구분을 하였다.

---

### 데이터 중심 설계 특징1

예를 들어서 데이터 중심의 `Movie` 클래스만 살펴보면

```java
    private String title;
    private Duration runningTime;
    private Money fee;
    private List<DiscountCondition> discountConditions;
```

이 부분은 동일하지만,

```java
 		private MovieType movieType; // 할인 정책
    private Money discountAmount; // 할인 
    private double discountPercent; // 할인 비율
```

이 부분이 다른 것을 알 수 있다.

즉, **영화**라는 객체에 어떤 **책임**이 필요한지 대신 영화가 가져야 할 구성 요소들을 정의한 것이다. 따라서 **할인 정책**과 **할인 조건**들이 다른 객체들로 존재하는 대신, 이렇게 영화의 구성 요소로 존재하게 되었다.

보면 알겠지만, `Movie`를 포함한 **예시3**의 모든 클래스들은 어떤 특정한 로직을 가지고 있지 않다. 기본적인 캡슐화를 위해 **접근자(getter)**와 **수정자(setter)**를 넣긴 했지만 (예시에는 안 넣었음) 이것들은 사실상 어떤 기능을 수행한다기보다 각 클래스들이 가지고 있는 기본적인 속성들을 보여주고 수정하는 용도이다.

---

### 데이터 중심 설계 특징2

**예시4**의 `ReservationAgency` 클래스에서 다른 모든 로직들이 구현되고 있다. 상영될 영화의 모든 할인 조건들을 찾아서 가능한 경우 할인을 해 준 다음 예매까지 하고 있다.

객체를 의인화에 비유하여 2장에서 설명한 적이 있는데, 따지자면 이 코드에서 실제로 의인화된 객체는 `ReservationAgency` 뿐이고 다른 객체는 그냥 서랍장의 용도로 쓰이고 있다.

## 데이터 중심의 영화 예매 시스템의 문제점

데이터 중심의 설계는 다음의 문제점들을 가진다.

- **캡슐화 위반**
- **높은 결합도**
- **낮은 응집도**

---

### 캡슐화 위반

위에서 **getter**, **setter**를 사용해서 기본적인 캡슐화를 시켰다고 했다.

하지만 따지고 보면 이 속성들을 사용하는 방법이 `movie.title` 에서 `movie.getTitle()` 로 바뀐 것에 지나지 않는다. 결과적으로 외부에서 마음만 먹으면 얼마든지 내부의 속성을 사용할 수 있다는 뜻이다. (실제로 예시에서 `ReservationAgency`는 이런 식으로 다른 클래스들의 속성들을 마음대로 사용하고 있다)

이렇게 내부의 상태들이 그대로 외부로 드러나게 되면 내부 상태가 어떤 방식으로든 변경되었을 경우에 외부에서 바뀐 내부 상태가 그대로 보이기 때문에 외부에서 일어나는 일들을 장담할 수 없게 된다.

그렇다고 해서 이 **접근자**와 **수정자**들을 없앨 수도 없다. 어쨌든 내부에 어떤 구현이 되어 있지 않기 때문에 외부에서 접근이 가능해야 하고, 변경이 있을 시를 대비해서 가능한 모든 속성들을 다 외부로 노출시켜야 한다.

---

### 낮은 응집도와 높은 결합도

`ReservationAgency`가 모든 책임을 담당하고 있기 때문에 생기는 문제점들도 있다.

먼저 할인 정책이나 다른 조건들이 변경될 경우 (예를 들어서 정책이 하나 더 추가되는 경우) 어떤 경우에서든 다른 클래스들 뿐만 아니라 `ReservationAgency` 를 무조건 수정해야 하고, 이를 사용하는 다른 클래스들도 똑같이 수정을 하지 않으면 오류가 난다. 한 변경에 여러 클래스를 수정하니 결합도가 아주 높은 것이다.

또, 이 경우에 `ReservationAgency` 안에 있는 상관없는 코드들까지 영향을 줄 수 있다. 한 클래스가 오류가 나는 원인(변경되어야 하는 이유) 가 다양하기 때문에 의존성도 낮아진다.

책에서 언급한 바로는, `ReservationAgency`가 변경되어야 하는 이유는 다음과 같다.

- **할인 정책 추가**
- **할인 정책별로 할인 요금을 계산하는 방법 변경**
- **할인 조건 추가**
- **할인 조건별로 할인 여부를 판단하는 방법 변경**
- **예매 요금 계산 방법 변경**

**단일 책임 원칙**에 크게 위배되는 것이다.

## 자율적인 객체를 향해

### 스스로 자신의 데이터를 책임지는 객체

문제점들을 가능한 해결해 보자. 먼저 **접근자**와 **수정자**를 사용하더라도 캡슐화가 해결되지 않는다는 문제점이 있었다.

위에 있는 클래스들은 각각의 **속성들**과 **접근자**, **수정자**로 이루어져 있다. 여기서 **데이터 중심 설계**를 두 갈래로 나뉘어서 생각하면 다음과 같다.

- **객체가 가져야 할 데이터(속성)**
- **객체가 가져야 할 오퍼레이터(메서드)**

결론적으로 **getter**와 **setter**를 가능한 없애면서도 외부에서 활용이 가능한 **오퍼레이터**를 만들어서 외부에 제공해야 한다.

예를 들어 `DiscountCondition` 에서 **getter**와 **setter**를 최대한 없애는 대신 활용 가능한 **오퍼레이터**가 무엇인지 생각하면 자신이 가지고 있는 속성들로 **할인이 가능한지 판단하는 오퍼레이터**를 작성할 수 있다.

### 예시5 : 책임이 부과된 `DiscountCondition` 클래스

```java
class DiscountCondition {
    ...
    public DiscountConditionType getType() {
	      return type;
    }
    
    public boolean disDiscountable(DayOfWeek dayOfWeek, LocalTime time) {
        if (type != DiscountConditionType.PERIOD) {
            throw new IllegalArgumentException();
        }
        return this.dayOfWeek.equals(dayOfWeek) && this.startTime.compareTo(time) <= 0 && this.endTime.compareTo(time) >= 0;
    }
    
    public boolean isDiscountable(int sequence) {
    if (type != DiscountConditionType.SEQUENCE) {
        throw new IllegalArgumentException();
    }
    return this.sequence == sequence; 
}
```

이렇게 해서 할인이 되는지 판단하는 책임이 `ReservationAgency`에서 `DiscountCondition`으로 넘어갔다.

이제 `ReservationAgency` 는 마음대로 `DiscountCondition`의 타입을 확인할 수 없고 이 객체를 통해 그저 할인이 가능한지 불가능한지를 물어보는 것만 가능하다. 다른 객체들에게도 적절한 **오퍼레이터**를 추가하면 결과적으로 `ReservationAgency`의 책임은 많이 줄어들게 된다.

### 예시6 : 책임이 줄어든 `ReservationAgency` 클래스

```java
public class ReservationAgency {
    public Reservation reserve(Screening screening, Customer customer, int audienceCount) {
        Money fee = screening.calculateFee(audienceCount);
        return new Reservation(customer, screening, fee, audienceCount);
    }
}
// 계산된 돈을 받아와서 예약을 하는 일만 담당하고 있다
```

## 하지만 여전히 부족하다

`DiscountCondition`을 위와 같이 바꾸었을 때 어느 정도 캡슐화는 되었다. 하지만 여전히 캡슐화가 위반이 되고 있는 부분이 있다고 한다. 내부의 속성에 무엇이 있는지 그대로 드러내고 있는 것이다.

외부에서 활용이 가능한 **인터페이스**들은 가능한 내부 구현을 숨기고 필요한 정보들만 알게 해야 하는데 여전히 파라미터 안의 변수들이 내부 속성으로 포함되어 있다는 사실을 드러낸다. 여기서 만약 어떤 속성이 변경되면 똑같이 원래 이 **오퍼레이터**를 사용하던 객체들에서는 오류가 발생할 것이다. 사용하는 속성이 다르기 때문이다. 결합도도 낮아지지 않은 것이다.

이는 `DiscountCondition` 뿐만 아니라 다른 클래스에게도 해당된다.

결국에는 처음부터 설계를 시작할 때에 **어떤 데이터가 필요한지**가 아닌, 각 객체가 **어떤 역할과 책임을 가져야 하는지**를 먼저 생각해야 한다. 데이터는 그 나중 문제인 것이다.

> 이 부족한 부분들을 해결하려면 결국에는 **인터페이스**의 정의와 추상화가 필수적이다. **인터페이스**를 정의해야 모든 세부 구현들을 **인터페이스** 안으로 숨길 수 있고, 추상화를 통해 속성들을 범위 내에서 수정해도 외부 객체에서는 비교적 오류 없이 수정할 수 있다.
>