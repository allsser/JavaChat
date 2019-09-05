package JavaLambda;

/*
 * 란다식을 표현하는 방식은
 * (인자1, 인자2, 인자3, ....) -> { 실행코드 }
 * 
 * 1. 매개변수의 이름은 개발자가 지정할 수 있다.
 * 2. 매개변수의 타입은 컴파일러의 타입유추에 의해서 알 수 있기 때문에
 *    람다식에서는 일반적으로 매개변수의 타입을 지정하지 않는다.
 * 3. 만약 매개변수가 1개인 경우 ()도 생략할 수 있다.
 * 4. 만약 실행문이 1개인 경우 {}도 생략할 수 있다.
 * 5. 매개변수가 없다면 ()는 생략할 수 없다.
 * 6. 실행문에 당연히 return구문이 들어올 수 있다.
 * 7. 만약 실행문에 return 구문 1개만 존재하면
 * 
 * 람다식은 interface type의 instance를 생성하는 expression
 * 람다식은 결국 익명 객체를 생성하는 코드
 * 람다식이 생성하는 객체는 결국 어떤 interface type의 변수에 assign이 되는가에 달려있다.
 * 이렇게 람다식으로 만들어지는 객체의 interface type을 람다의 target type이라고 한다.
 * 
 * target type은 아무 interface나 사용될 수 없다.
 * 람다의 target type이 될려면 헤당 interface는 반드시 추상 메소드가 1개만 존재해야 한다.
 * 그래서 interface를 사용할 때 어노테이션을 이용해서 check를 할 수 있다.
 * @FunctionalInterface을 이용해서 해당 interface가 람다의 target type이
 * 될 수 있는지 compiler에 의한 check를 할 수 있다.( 함수적 인터페이스 )
 * 
 * - thread를 생성할 때 Runnable interface를 사용하는데 
 *   이 Runnable interface는 public void run()이라는 추상 method 1개만
 *   달랑 가지고 있다. 따라서 이 interface는 람다의 target type이 될 수 있고
 *   Runnable interface는 함수적 인터페이스라고 할 수 있다.
 *   
 * - 이벤트를 처리하는 interface는 대체로 함수적 interface.
 */

@FunctionalInterface
interface Exam02_LambdaIF {
	int myFunc(int a, int b);
	
}

public class Exam02_LambdaExpression {

	public static void main(String[] args) {
		
		Exam02_LambdaIF obj = (a, b) -> a+b;
		System.out.println(obj.myFunc(10, 20));
	}

}
