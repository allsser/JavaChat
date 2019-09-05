package JavaLambda;
/*
 * 힘수적 프로그래밍 패턴을 위해 Java는 8버전부터 Lambda를 지원
 * 
 * 람다는 익명함수를 만들기 위한 expression(식)
 * ==> 객체지향언어보다는 함수지향적 언어에서 사용된다.
 * 
 * 기존 자바 개발자들은 이 Lambda라는 개념에 익숙해지기가 쉽지 않다.
 * 그럼에도 불구하고 Lambda를 도입한 이유는 크게 2가지 정도로 생각할 수 있다.
 * 1. 코드가 간결해 진다.
 * 2. Java Stream를 이용하기 위해서 람다를 이용.
 * 	  Java Stream은 collection(List, Map, Set, Array..)의 처리를
 *    굉장해 효율적으로 할 수 있다.( 병렬처리가 가능  )
 *    
 * 람다식의 기본 형태
 * (매개변수)-> {실행 코드}
 * 익명함수를 정의하는 형태로 되어 있지만 실제로는 익명클래스의 인스턴스를 생성.
 * 
 * 람다식이 어떤 객체를 생성하느냐는 람다식이 대입되는 interface 변수가
 * 어떤 interface인가에 달려있다.
 * 
 * 일반적인 interface를 정의해서 람다식으로 표현해 보기
 * 
 */

interface Exam01_LambdaIF {
	// 추상 method만 올 수 있다.
	// method의 정의가 없고 선언만 존재하는게 추상(abstract method) method
	// 란다식은 반드시 추상메소드가 하나만 있어야 한다. 그래야 컴파일러가 인식 ex) void test1(int k);
	void myFunc(int k);


}

public class Exam01_LambdaBasic {

	public static void main(String[] args) {
		
		// Thread를 생성하려고 한다.
		// 1. Thread의 subclass를 이용해서  Thread 생성
		//    그다지 좋은 방식이 아니다.
		// 2. Runnable interface를 구현한 class를 이용해서
		//    Thread를 생성. ( 더 좋은 방식 )
		// 3. Runnable interface를 구혆나 익명 class를 이용해서
		//    Thread를 생성. ( 안드로이드에서 일반적인 형태 )
		
//		Runnable runnable = new Runnable() {
//			
//			// 객체를 생성 못하는 이유는 추상 메소드가 존재하기 때문인데
//			// 이  method를 overriding하면 객체를 생성할 수 있다.
//			@Override
//			public void run() {
//				
//			}
//			
//		};
		
//		new Thread(()->{
//			System.out.println("쓰레드 실행!!");
//		}).start();
		
		Exam01_LambdaIF sample = 
				(int k) -> { System.out.println("출력되요"); };
		sample.myFunc(100);
	}

}