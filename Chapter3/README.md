# chapter3 [역할, 책임, 협력]

## 서론

### 먼저…

> 글을 읽으면서 머릿속에서 정리가 안된 부분들이 있어서 몇가지 짚고 넘어가겠다.
> 

2장에서는 상속(Is A 상속) 보다는 합성(Composition 방식의 Has A 상속)을 많이 사용하라고 배웠다. 3장에서는 이 가정 하에서, **합성을 사용하는 것에 대한** 세 가지 핵심적인 개념(단어)들에 대해 설명한다. 현실에 있는 단어들이지만, 이 챕터에서는 다음과 같이 명시를 하고 있다.

- **협력(collaboration)** : 객체들이 프로그램의 기능을 구현하기 위해 수행하는 상호작용
- **책임(responsibility)** : 객체가 협력에 참여하기 위해 수행하는 로직
- **역할(role)** : 객체가 특정 협력 안에서 수행하는 책임의 집합

---

 여기서 책임과 역할은 개발자가 설계를 할 때 **어떤 객체에게, 어떤 일을** 하게 할 것인지를 정하는 데에 있어서 고려할 요소들이고, 협력은 그것보다 포괄적인, 기본적으로 생각해야 할 **원칙** 같은 것으로 이해를 하였다. 다시 말해, 책임과 역할을 부여해서 각각의 객체들이 돌아가는 것 자체가 협력인 것이다.

사실, 개념들이 모두 추상적이고 각각이 독립된 개념들이 아니라서 이 장이 조금 뜬구름잡는 소리같이 들릴 수 있다. 그래서 협력은 시스템 설계를 할 때 객체들이 서로 상호작용하는 상황을 말하는 것이고, 책임은 객체가 가지고 있는 기능들, 역할은 객체의 메서드들의 집합을 말하는 것으로 생각하는 것이 편하다. 

### 예시1 : Macbook 클래스

> 이번 챕터에서는 예시 구현 자료가 딱히 없기 때문에 내가 구현 예시들을 하나 만들어보았다. 
설명을 할 때 책에 나온 예시와 함께 내가 만든 예시도 같이 포함하도록 하겠다.
> 

```java
public abstract class Macbook {
    private Chip chip;
    private Memory memory;
    private Storage storage;
    private Display display;

    public Macbook(Chip chip, Memory memory, Storage storage, Display display) { ... }

    public Chip getChip() { return chip; }

    public Memory getMemory() { return memory; }

    public Storage getStorage() { return storage; }

    public Display getDisplay() { return display; }

    public abstract void performTask(String task, double a, double b);

    public double calculatePrice() { ... }

    @Override
    public String toString() { ... } // 사양 조회
}
```

## 협력

### 예시2 : Chip 클래스

```java
public abstract class Chip {
		...		
		public void recordOperation(Storage storage, String operation) {
		        int capacity = Integer.parseInt(storage.getCapacity().replaceAll("[^0-9]", ""));
		        if (storage.getOperationHistory().size() < capacity) {
		            storage.addOperationRecord(operation);
		        } else {
		            System.out.println("Storage capacity reached. Cannot record more operations.");
		        }
		    }

    public abstract void performOperation(String operation, double a, double b, Storage storage);
    ...
}
```

### 예시3: Storage 클래스

```java
public class Storage {
...
public String getCapacity() {
        return capacity;
    }

    public void addOperationRecord(String record) {
        operationHistory.add(record);
    }

    public ArrayList<String> getOperationHistory() {
        return new ArrayList<>(operationHistory);
    }
...
}
```

객체는 혼자 일하지 않는다. 위의 예시에서 `Chip`은 계산을 수행하고 자신이 직접 저장을 하지 않고 그것을 저장하기 위한 기능으로 `Storage`라는 전문가와 협력해서 처리를 하고 있다. 

더 자세히 말하면, `chip` 객체는 계산을 하고 나서 저장 공간이 남았으면 저장하라는 메세지를 `storage` 객체에 전송하고 `storage`는 자신의 메서드를 실행에 요청을 수행하는 것이다. 저장하는 로직을 가장 잘 수행할 수 있는 객체가 바로 `storage`이기 때문이다. 

또, 예시에는 적지 않았지만 `Chip`은 8번 연산을 수행할 때마다 2번씩 출력이 안되는 현상이 발생한다. (나름 스로틀링 재현,,,) 이 때 `chip` 객체는 이를 바로 해결하기 위해 `CoolingFan`이라는 전문가와 협력하고 있다. (자세한 것은 코드에서 확인하도록,,,)

---

### 예시3: Movie 클래스

```java
public class Movie {
		private Money fee;
		private DiscountPolicy discountPolicy;
		
		public Money calculateMovieFee(Screening screening) {
				return fee.minus(discountPolicy.calculateDiscountAmount(screening));
		}
}
```

책에 나온 예시에서 `Movie` 클래스라 하면 상영을 위한 기능도 당연히 포함될 것 같지만, 사실은 그렇지 않다. 자신은 요금을 계산하는 기능들을 주로 담당하고, 상영을 위한 기능들은 자신보다 그것을 더 잘 알고 있는 객체인 `screening`객체가 담당한다.  

---

이렇게 객체들은 다른  객체들과 서로 협력을 하면서 자신보다 더 일을 잘 할 수 있고, 정보를 더 많이 가지고 있는 객체에게 특정 행동을 부탁한다. 따라서 협력이라는 과정을 통해서 객체를 설계하는 문맥을 정하는 것이라 할 수 있다. 

## 책임

### 책임의 종류

책임은 쉽게 말해서 어떤 기능을 구현하기 위해 객체가 하는 행동이다. 이는 다시 두 가지로 나뉜다.

- **하는 것 (doing)**
- **아는 것 (knowing)**

다시 말해 **특정 기능을 수행하는 책임**과 **정보를 제공하는 책임**이 있다. 

### 책임 주도 설계

이 **책임**이라는 개념은 매우 중요하다. 객체는 자신이 할 수 있는 일과 그 일을 하는 데 필요한 정보들을 알고 있을 책임이 있고, 자신이 할 수 없는 일을 대신 해줄 객체를 알고 있을 책임이 있다. 또, 개발자는 이 책임들을 여러 객체들에 적절히 할당하여 협력을 만들어야 한다. 이를 **책임 주도 설계**라 한다.

책임을 할당하는 방법은 단순히 생각해서 관련 정보를 가장 많이 알고 있는 객체에게 책임을 부과하는 것이다. 

위의 맥북 예시에서 저장을 해야 한다고 해서 `storage`에서 직접 공간이 남는지 확인하고, 저장 공간을 꺼내어서 넣는 과정을 한다면 책임이 적절히 할당되었다고 보기 어려울 것이다. `storage`에게 적절한 책임을 할당하는 과정을 통해 단순히 공간이 있는지 확인하는 메세지를 보내고, `storage`에게 저장을 요청하는 것으로 마무리한다. 앞의 예시는 단순한 로직이라 그렇게 차이가 나지 않지만 만약 이것보다 훨씬 복잡한 프로그램의 경우에는 이 책임을 적절히 전가하는지 여부가 코드의 길이와 복잡도에 아주 큰 영향을 끼칠 것이다.

마찬가지로 `Macbook`은 자신이 직접 계산을 하기 보다 그 로직을 더 잘 수행할 수 있는 `chip`을 인스턴스로 두고 그 객체에게 시키는 것이 훨씬 책임 할당이 잘 되었다고 볼 수 있다. 

이렇게 책임을 할당하는 과정은 먼저 책임을 파악하고 그것을 더 작은 책임으로 분할해서 각각의 책임들을 잘 수행할 수 있는 객체에 전가하는 과정의 반복이라고 할 수 있다.

## 역할

### 객체의 존재 이유

역할은 사실 객체의 존재 이유라고 봐도 무방하다. 객체를 만드는 과정에서 개발자는 당연히 그 객체가 어디에 사용되는지, 그리고 그 객체가 가지고 있는 정보들이  무엇인지를 파악하고, 객체가 쓰일 곳에 맞추어서 책임들을 정의한다. 

이런 이유로 역할은 한 객체에만 국한되지 않을 수도 있다. 

### 여러 객체의 협력

```java
public abstract class Macbook {
		...
    public abstract void performTask(String task, double a, double b);

    public double calculatePrice() { ... }
    ...
}    
```

앞서 말한 `Macbook` 클래스에서 이 두 메서드를 보면, 각각은  `Chip`과 `Storage`를 활용해서 계산을 수행하는 로직과, 컴퓨터의 부품들을 조합한 가격을 보여주는 로직을 수행하고 있다. `Macbook`이라는 컴퓨터가 여러 부품들을 조합해서 사용자로부터 입력을 받고 결과를 보여주는 관리자의 역할을 담당하고 있는 것이다. 이 과정들의 최종 책임은 물론 `Macbook` 에게 있지만 이 과정은 다시 **여러 역할을 지닌 객체들에게로 나뉘어져서 서로 협력하는 현상**을 보여주고 있다. 

쉽게 생각하면 어떤 기능을 수행하기 위해 다른 객체를 이용해서 최종 결과물을 전달하는 것 자체가 여러 객체들이 협력해서 하나의 역할을 수행하는 것으로 볼 수 있는 것이다.

### 추상화

추상화도 어떻게 보면 하나의 기능을 구현하기 위해 여러 객체들이 모여 역할을 수행하는 것이라 할 수 있다. 

상황이 다르거나 바뀌는 경우에 각각의 부품들을 새로 다시 만들어 사용하는 것보다 **정해진 틀에게 역할을 부여해 놓고** 그 안에서 여러 경우들을 나누어서 **각각의 경우에 책임을 부여**하는 것이 훨씬 효율적이다.