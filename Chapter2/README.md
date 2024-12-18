# chapter2 [객체 지향 프로그래밍]

## 영화 예매 프로그램 구현

### Movie 클래스

```java
package movie;

import policy.DiscountPolicy;

import java.time.Duration;

public class Movie {
    private String title;
    private Duration runningTime;
    private Money fee;
    private DiscountPolicy discountPolicy;

    public Movie(String title, Duration runningTime, Money fee, DiscountPolicy discountPolicy) {
        this.title = title;
        this.runningTime = runningTime;
        this.fee = fee;
        this.discountPolicy = discountPolicy;
    }

    public Money getFee() {
        return fee;
    }

    public Money calculateMovieFee(Screening screening) {
        return fee.minus(discountPolicy.calculateDiscountAmount(screening));
    }

}
```

### Screening 클래스

```java
package movie;

import java.time.LocalDateTime;

public class Screening {
    private Movie movie; // 상영 영화
    private int sequence; // 순번
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

```

위의 `Movie` 클래스와 `Screening` 클래스는 여러 다른 객체들의 도움을 받아서 여러 로직을 구현하고 있다. 여기서 중요한 점은, 이 클래스들은 각자 특정 허용된 메서드들만 사용할 수 있게끔 만들어져 있고, 다른 세부 로직이나 속성들은 모두 private으로 선언되어 다른 클래스에서 접근할 수 없도록 만들어진 것이다. 이 개념은 저번 시간에 학습한 캡슐화와 은닉화를 그대로 적용한 것이다.

## 상속과 다형성

### DiscountPolicy 클래스

```java
package policy;

import condiiton.DiscountCondition;
import movie.Money;
import movie.Screening;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DiscountPolicy {
    private List<DiscountCondition> conditions = new ArrayList<>();

    public DiscountPolicy(DiscountCondition ... conditions) {
        this.conditions = Arrays.asList(conditions);
    }

    public Money calculateDiscountAmount(Screening screening) {
        for (DiscountCondition each : conditions) {
            if(each.isSatisfiedBy(screening)) {
                return getDiscountAmount(screening);
            }
        }
        return Money.ZERO;
    }

    abstract protected Money getDiscountAmount(Screening Screening);
}
```

책에 나온 대로 해석하면, 각 영화는 할인 정책을 가지고 있고, 각 할인 정책들은 다시 할인 조건들을 가지고 있다. 

여기서 **상속**의 개념이 등장한다. 위의 `Movie` 클래스에서는 정책이 무엇인지 모른다. 즉, 각 영화가 정해질 때에 사용되는 정책에 따라 구현이 달라지는 것이다. 그래서 `DiscountPolicy`를 추상화(abstract) 시켜놓고 나머지 세부 정책들은 그 자식에서 구현하는 것이다.

이런 is a 방식의 상속은 여러 모로 개발자의 입장에서는 유리하다. 

1. 첫 번째로 서로 비슷하거나 계층상 비슷한 클래스들을 작성할 때 **일일히 다시 작성해야 하는 번거로움**을 피할 수 있다. 처음에 만들어진 코드를 조금만 수정을 해서 다른 로직을 가지는 클래스를 만들어낼 수 있기 때문이다. 쉽게 말해, **틀만 정해놓고 세부 사항만을 구현**하는 것이다.
2. 두 번째로 이것을 사용하는 클래스들이 **직접 해당 객체를 사용하지 않고도 간접적으로 이 객체들을 사용할 것**이라는 것을 명시할 수 있다. 코드 상에서의 의존 정도와 실제 실행을 할 때의 의존 정도가 달라지는 것이다. 

## 상속과 인터페이스

같은 클래스에서 원래 있던 메서드도 **오버로딩**을 통해 조금 다르게 변형할 수 있고, 다른 클래스에서도 **오버라이딩**을 통해 로직을 수정할 수 있게 된다. 

```java
   	public Money calculateMovieFee(Screening screening) {
        return fee.minus(discountPolicy.calculateDiscountAmount(screening));
    }
```

다음과 같이 Movie 클래스에서는 `discountPolicy`의 메서드를 사용해서 실제 예매자가 내야 할 금액을 계산하고 있다. 이를 위해 위에서 `DiscountPolicy`가 `calculateDiscountAmount()`를 정의해 두면 이를 상속받은 객체들은 자동으로 이 기능을 사용할 수 있게 된다.

> java나 C++같은 언어들에서 처음에 변수의 타입을 지정하고, 객체를 지정하는 것도 이와 같은 이유에서이다. (`Movie movie`)컴파일러는 컴파일 시점에 실제 변수가 가지고 있는 객체가 무엇인지는 확인하지 않고 타입만 일치하는지를 확인한다. 객체가 무엇인지는 런타임 시에 알게 되는 것이다. 그래서 상속된 객체도 런타임 시에 불려와서 결정이 된다. 이를 **동적 바인딩**, 또는 **지연 바인딩**이라 한다.
> 

### DicountCondition 인터페이스

```java
public interface DiscountCondition {
    boolean isSatisfiedBy(Screening screening);
}
```

`DiscountCondition`은 클래스가 아니라 인터페이스이다. Is A 상속에서도 자식 클래스는 부모 클래스의 인터페이스를 사용하지만 이를 더 명시하기 위해 사용하는 것이다. 

인터페이스에서는 각각의 개체들에서 공통되어야 할 최소한의 속성, 로직들만 정의를 하고, 나머지 부분들은 모두 그것을 구현한 클래스들에서 담당한다.

## 추상화와 유연성

Movie 코드에서 오류가 날 만한 여지가 하나 있다. 할인 정책 속성이 존재하지만, 실제 영화에서는 할인이 존재하지 않는 영화도 있다. 

하지만 생성자에서 예를 들어

```java
Movie movie = new Movie(title, runningTime, fee, null);
```

이렇게 작성을 하면 앞서 말한 `calculateDiscountAmount()`가 실행되지 않는다. 그것을 담고 있는 객체가 없기 때문이다. 

이를 위해 단순하게 생각하면 다음과 같이 로직을 바꿀 수 있다.

```java
    public Money calculateMovieFee(Screening screening) {
				if (discountPolicy == null) {
				    return fee;
				}
        return fee.minus(discountPolicy.calculateDiscountAmount(screening));
    }
```

정책이 없는 경우만을 예외로 처리하는 것이다. 하지만 이를 `DiscountPolicy`에서 처리하지 않고 엉뚱한 `Movie`에서 처리하는 것은 분명 좋은 코드라 할 수 없을 것이다.

또 다른 방법은 정책이 없는 경우의 정책을 하나 더 만드는 것이다.

```java
public class NoneDiscountPolicy extends DiscountPolicy{
    @Override
    protected Money getDiscountAmount (Screening screening) {
        return Money.ZERO;
    }
}
```

즉, `Movie` 생성 시 다음과 같이 생성하겠다는 것이다.

```java
Movie movie = new Movie(title, runningTime, fee, new NoneDiscountPolicy());
```

이렇게 하면 `Movie` 클래스의 수정 없이 `DiscountPolicy`에서 상속받은 클래스들만으로 로직을 모두 구현할 수 있다. 

하지만, 이렇게 되면 작은 문제가 하나 발생한다. abstract인 `DiscountPolicy`랑 그 자식인 `NoneDiscountPolicy`랑 로직이 사실상 아예 겹치는 것이다. `DiscountPolicy` 자체의 코드를 보면, 조건이 없을 경우에는 `return Money.ZERO;` 를 포함하고 있기 때문에 개념적으로 두 클래스가 완전히 겹치는 것이다.

그래서 책에서는 **상속 정도를 하나 더 추가하는 방법**도 제시하고 있다. 

`DiscountPolicy` 를 사용하는 클래스로 정책이 없는 `NoneDiscountPolicy`와 정책이 1개 이상인 `DefaultDiscountPolicy`를 만들고 아까 만들었던 세부 정책들을 모두 `DefaultDiscountPolicy`로 옮기는 것이다.

```java
public interface DiscountPolicy {
    Money calculateDiscountAmount(Screening screening);
}
```

> 왜 interface로 바꾸냐? 
java에서는 다중 상속을 지원하지 않는다. 따라서 예시의 경우처럼 다중 상속일 경우 구현 객체만을 남기고 나머지는 인터페이스로 만들어야 한다
> 
- 추가로…..
    
    이 경우에 글을 읽으면서 생각이 든 게, 처음에 `DiscountPolicy`를 abstract로 만들지 않고 그 안에 빈 오버로딩 생성자를 하나 추가하면 되지 않을까 싶긴 했다.
    
    ```java
    public class DiscountPolicy {
        private List<DiscountCondition> conditions;
        
        public DiscountPolicy() {
    	      this.conditions = new ArrayList<>();
        }
        public DiscountPolicy(DiscountCondition ... conditions) {
            this.conditions = Arrays.asList(conditions);
        }
        ...
    }
    ```
    
    문제는 없다만, `DiscountPolicy`가 다른 자식 클래스들보다 추상화되어야 한다는 사실을 위반하는 것 같아 좋은 코드는 아닌 것 같다.
    

### 합성

사실 이렇게 `DiscountPolicy`를 하지 않고 상속만을 사용해서 코드를 재사용할 수도 있다. `Movie`를 직접 상속받은 **`**AmountDiscountMovie`와 `PercentDiscountMovie`를 직접 생성하는 것이다. 

하지만 이 방법은 의미가 없다는 것이 바로 느껴진다. 왜냐하면 이렇게 했을 때 얻는 것보다 잃는 것이 훨씬 많기 때문이다.

1. **캡슐화를 위반한다.** `Movie` 입장에서는 자신과 상관도 없는 정책의 문제 때문에 부모 클래스의 메서드를 모두 알고 있어야 한다. 사실 상속은 (책에서 나온 단어를 차용하면) **클라이언트 프로그래머**를 위한 개념이라기 보다 **클래스 작성자**를 위한 개념이라고 할 수 있다. 이렇게 캡슐화를 위반하면 1절에서 언급한 내용들이 의미가 없어지는 것이다.
2. **유연성이 감소한다.** 상속이라는 개념은 사실 두 클래스가 다른 존재라기 보다 한 존재로부터 조금 변형되어 나온 존재이다. 따라서 사실상 종류가 다른 것이 아니라 부모 클래스 안에 묶여 있는 것이다. 

이보다는 앞서 적은 예시의 경우처럼 합성으로 각 개체들이 재사용되는 것이 더 좋은 코드라고 할 수 있다.