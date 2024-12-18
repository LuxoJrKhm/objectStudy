# chapter1 [객체, 설계]

```java
public class Theater {
    private final TicketSeller ticketSeller;

    public Theater(TicketSeller ticketSeller) {
        this.ticketSeller = ticketSeller;
    }

    public void enter(Audience audience) {
        if (audience.getBag().hasInvitation()) {
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();
            audience.getBag().setTicket(ticket);
        } else {
            Ticket ticket = ticketSeller.getTicketOffice().getTicket();
            audience.getBag().minusAmount(ticket.getFee());
            ticketSeller.getTicketOffice().plusAmount(ticket.getFee());
            audience.getBag().setTicket(ticket);
        }
    }
}
```

---

책에서 처음 예시로 든 `Theater` 객체이다. 전형적인 절차 지향형 코드라고 한다. 다른 클래스들을 보면 알겠지만, 사실상 모든 로직은 다 여기서 구현된다. 다른 클래스들에는 속성과, 그 속성을 간단히 보여주는 기본적인 메서드들만 있다.  하지만 여기서 몇 가지 문제가 있다.

## 문제점

<aside>
💡

`Theater` 이 가지고 있는 책임이 너무 크다.  

---

`Theater`는 

1. 관객이 초대장을 가지고 있는지 **관객의 가방을 직접 열어서 확인**하고,
2. 없는 경우 **`TicketSeller`의 의존객체인 `TicketOffice`에 직접 접근**해서 **요금 처리**를 하고 
3. 얻어온 티켓을 **직접 관객의 가방에 넣어준다**. 

이러면 우리의 입장에서도 한눈에 보고 파악되지 않는 문제가 발생한다.

</aside>

<aside>
💡

캡슐화가 제대로 안되어 있다. 

---

`Theater`가 다른 클래스들의 메서드들을 직접 너무 많이 사용하고 있다.

다른 객체들의 세부적인 구현들이 너무 밖으로 노출되어 있어서 외부에서 봤을 때 코드가 드러나고, 객체들의 내부 로직이 바뀌었을 때 `Theater`의 로직도 수정해야 한다. 

</aside>

<aside>
💡

객체들 간의 결합도가 너무 크다

---

`Theater`가 의존하는 클래스들이 너무 많다. 

현실세계에서는 영화관, 티켓 판매소, 관객이 따로 존재할 것인데, 다른 곳에서 뭐가 하나라도 바뀌면 다른 클래스에서 오류가 난다.

예를 들어서 `Audience`가 `Bag`을  `LouisVuittonBag`으로 바꿨다고 하자. 그럼 영화관에서는 자신이 건드리지도 않은 코드에서 갑자기 컴파일 에러가 발생하는 것이다. 

</aside>

## 해결책

> Theater에 모여 있던 책임을 다른 클래스로 분리하고, 최대한 다른 클래스들의 속성이나 메서드를 보지 못하게 코드를 수정한다.
> 

### `Theater`로부터 책임 분리

1. `Theater` → `Audience` 
2. `Theater` → `TicketSeller`
    
    ```java
    public class Audience {
        ...
        public Long buy(Ticket ticket) {
            if (bag.hasInvitation()) {
                bag.setTicket(ticket);
                return 0L;
            } else {
                bag.setTicket(ticket);
                bag.minusAmount(ticket.getFee());
                return ticket.getFee();
            }
        }
    }
    public class TicketSeller {
        ...
        public void sellTo(Audience audience) {
    		    Ticket ticket = ticketOffice.getTicket()
            ticketOffice.plusAmount(audience.buy(ticket));
        }
    }
    public class Theater {
    		...
    		 public void enter(Audience audience) {
            ticketSeller.sellTo(audience);
        }
    }
    ```
    
    가방에서 티켓이 있으면 꺼내고 티켓을 넣는 행위는 `Audience`가 하고, `TicketOffice`에서 `Ticket`을 꺼내고 돈을 관리하는 행위는 `TicketSeller`가 하도록 수정하였다. 
    

### `Audience`, `TicketSeller`의 책임도 분리

1. `Audience` → `Bag`
    
    ```java
    public class Bag {
        ...
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
    public class Audience {
    		...
    		public Long buy(Ticket ticket) {
            return bag.hold(ticket);
        }
    }
    ```
    
    `Bag`에 hold 메서드를 추가하고 `Audience`의 buy로직을 전가하였다. 이제 `Bag` 는 자신 안에서 필요한 로직을 수행하고 hold라는 메서드만을 `Audience`에게 제공한다. 
    
2. `TicketSeller` → `TicketOffice`
    
    ```java
    public class TicketOffice {
    	...
    	public void sellTicketTo(Audience audience) {
            Ticket ticket = getTicket();
            plusAmount(audience.buy(ticket));
    }
    
    public class TicketSeller {
    	...
    	public void sellTo(Audience audience) {
            ticketOffice.sellTicketTo(audience);
    }
    ```
    
    최종적으로 `TicketOffice` 클래스도 객체 지향화 시켜서 메서드를 분리시켜 보았다. 이제 `TicketSeller`가 `Ticket`을 직접 가져오는 것이 아니라 `TicketOffice`에서 받아오기만 한다.
    
    ---
    
    ### 이렇게 했을 때의 장점
    
    <aside>
    💡
    
    `Theater` 의 책임을 다른 클래스들이 나누어서 부담한다.
    
    ---
    
    `Theater`가 직접 수행하던 모든 일을 이제 다른 클래스에 맡기게 되면서 `Theater`의 책임이 크게 줄었다.
    
    `Ticket`을 관리하는 일은 이제 `TicketSeller`가 수행하고, `TicketSeller`도 직접 `Ticket`을 가져오지 않고 `TicketOffice`가 `Ticket`을 주고 돈을 관리한다.
    
    가방을 관리하는 일은 `Audience`가 하고, `Bag`가 자신의 티켓과 돈 관리를 직접 한다.
    
    </aside>
    
    <aside>
    💡
    
    코드가 캡슐화되었고, 의존성이 줄어들었다.
    
    ---
    
    이제 필요한 메서드를 제외한 나머지 메서드들은 private로 선언되어서 내부적으로만 사용될 뿐 다른 객체에서 사용할 수 없다. 이로써 밖에서 볼 때는 내부의 로직을 알 필요가 없고 필요한 메서드들만 사용할 수 있다.
    
    `Theater`은 `Bag`와 `TicketOffice`까지 의존하지 않는다. 이제 `Bag`가 `LouisVuittonBag`으로 바뀌더라도 `Theater`에서는 에러가 나지 않고 그것을 의존하는 객체인 `Audience`에서만 에러가 난다.
    
    </aside>
    
    ---
    
    하지만 이렇게 되면 문제가 있다. `TicketOffice`의 관점에서는 그 전까지는 하지 않는 일을 하면서 `Audience` 클래스를 의존할 수 밖에 없어졌다. `TicketSeller` 도 `Audience` 를 의존하고 있으니 결과적으로 의존성의 개수가 늘어난 것이다. 
    
    어느 정도는 **자율성**과 **의존성** 사이에 충돌이 있기 때문에 적절한 관계를 유지해야 한다. 책에서는 `TicketOffice` 를 자율화 시켜서 의존성이 늘어난 것보다는  `TicketOffice`가 수동적이 되더라도 의존성이 낮은 것이 더 좋은 코드라고 말하고 있고 나 또한 그렇게 생각한다. 
    
    - **근데 추가적으로……**
        
        근데 예시의 경우에서 의존성이 늘어난 이유를 예측해 보면, 원래 `TicketSeller` 가 하던 일을 `TicketOffice` 가 대신하게 되면서 `TicketSeller`는 `Theater`과 `TicketOffice`를 연결하는 매개체의 역할밖에 수행하지 않게 되었다. 
        
        ```java
        public class Theater {
        	private TicketOffice ticketOffice;
        	  public Theater(TicketOffice ticketOffice) {
        	        this.ticketOffice = ticketOffice
        	    }
        	
        	    public void enter(Audience audience) {
        	        ticketOffice.sellTo(audience);
        	    }
        }
        
        public class TicketOffice {
        	...
        	public void sellTo(Audience audience) {
                Ticket ticket = getTicket();
                plusAmount(audience.buy(ticket));
        }
        ```
        
        오히려 `TicketSeller`를 해고하고  `TicketOffice`에 바로 판매를 시키면 의존성도 추가되지 않고 깔끔해지지 않나? 현실세계에서는 `TicketOffice`가 자율성을 가지지는 않으니 책임에 대한 내용이 직관과는 조금 상충될 수밖에 없는 것 같다….