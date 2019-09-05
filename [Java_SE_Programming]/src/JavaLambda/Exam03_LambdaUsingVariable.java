package JavaLambda;

/*
 * 람다식을 정의해서 사용할 때 주의해야 할 점이 몇가지 있다.
 * 클래스의 멤버(필드 + 메소드)와 로컬변수(지역변수)의 사용에 약간의 제약이 있다.
 * 
 * 특히 this keyword를 사용할 때 주의해야 한다.
 * this : 현재 사용되는 객체의 reference
 * 람다식은 익명 객체를 만들어 내는 코드이다. 람다식의 실행코드내에서
 * this keyword를 쓰면 익명객체를 지칭하지 않는다. 상위객체를 지장하게 된다.
 * 람다식안에서는 지역변수를 readonly 형태로 사용해야 한다.
 */

@FunctionalInterface
interface Exam03_LambdaIF {
	public void myFunc();
}

class OuterClass {
	// field ( 기본적으로 class의 field는 private )
	public int outerField = 100;
	
	public OuterClass() {
		// default 생성자
		System.out.println(this.getClass().getName());
	}
	
	// class 안에 다른 class를 정의할꺼다.(inner class)
	class InnerClass {
		int innerfield = 200; // Field
		
		Exam03_LambdaIF fieldLambda = ()-> {
			System.out.println("outerField : " + outerField);
			System.out.println("OuterClass의 객체를 찾아요 : " + OuterClass.this.outerField);
			System.out.println("this.innerfield : " + this.innerfield);
			System.out.println(this.getClass().getName());


		}; // Field
		Exam03_LambdaIF localLambda;
		
		public InnerClass() {
			System.out.println(this.getClass().getName());
		}  // 생성자
		public void innerMethod() { // method
			int localVal = 100;     // 지역변수 (local variable)
			                        // 지역변수는 stack영역에 저장이되고
			                        // method가 호출되면 생기고
			                        // method가 끝나면 없어진다.
			localLambda = () -> {
				System.out.println(localVal);
				//localVal = 50;  // final로 설정하면 값을 못바꾼다.
				                // 값을 바꿀수가 없다 (readonly)
			};
			
			localLambda.myFunc();
			
		}
	}
}

// 프로그램의 시작을 위한 dummy class로 사용
public class Exam03_LambdaUsingVariable {


	public static void main(String[] args) {
		// 람다식을 사용하려면 InnerClass의 instance가 존재해야 한다.
		// 그런데 하필이면 InnerClass가 inner class네??
		// inner class의 instance를 생성하려면 outer class의 instance부터
		// 생성해야 한다.
		 OuterClass outer = new OuterClass(); // 외부 클래스의 객체 생성
		 OuterClass.InnerClass inner = outer.new InnerClass(); // 내부 클래스의 객체 생성
//		 inner .fieldLambda.myFunc();
		 inner.innerMethod();
	}

}
