package javaStream;
/*
 * reduction
 * 
 * => 대량의 데이터를 가공해서 축소하는 개념
 * => sum, average, count, max, min
 * 
 * Collection을 사용할 때 Stream을 이용해서 이런 reduction작업을 쉽게 할 수 있다.
 * 
 * 만약 Collection안에 reduction하기가 쉽지 않은 형태로 데이터가 들어가 있으면
 * 중간처리과정을 거쳐서 reduction하기 좋은 형태로 변환.
 * 
 * Stream은 pipeline을 지원(stream을 연결해서 사용할 수  있다.)
 * 
 *간단한 예제를 통해서 이해해 보자
 *직원객체를 생성해서 ArrayList안에 여러명의 직원을 저장
 *이 직원중에 IT에 종사하고 남자인 직원을 추려서 해당 직원들의 연봉 평균을 출력
 */

import java.io.ObjectInputStream.GetField;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Exam03_StreamPipeline {

	private static List<Exam03_Employee> employees = 
			Arrays.asList(
					new Exam03_Employee("홍길동",20,"IT","남자",2000),
					new Exam03_Employee("김길동",50,"Sales","여자",3000),
					new Exam03_Employee("최길동",40,"IT","남자",1000),
					new Exam03_Employee("이순신",20,"Sales","남자",3500),
					new Exam03_Employee("유관순",35,"IT","여자",7000),
					new Exam03_Employee("신사임당",60,"IT","여자",4000),
					new Exam03_Employee("강감찬",30,"IT","남자",5000),
					new Exam03_Employee("이황",45,"Sales","남자",4000),
					new Exam03_Employee("홍길동",20,"IT","남자",2000));
	
	public static void main(String[] args) {
		// 부서가 IT인 사암들 중 남자에 대한 연봉 평균을 구해보시오.
		Stream<Exam03_Employee> stream = employees.stream();
		// stream의 중간처리와 최종처리를 이용해서 원하는 작업을 해 보자.
		// filter method는 결과값을 가지고 있는 stream을 리턴.
//		double avg = stream.filter(t->t.getDept().equals("IT"))
//		                   .filter(t->t.getGender().equals("남자"))
//		                   .mapToInt(t->t.getSalary())
//		                   .average().getAsDouble();
//		                   // lazy 처리
//		System.out.println("IT부서의 남자평균 연봉 : " + avg);
		
		// 그럼 Stream이 가지고 있는 method는 무엇이 있나여?
		// 나이가 35살 이상인 직원 중 남자 직원의 이름을 출력해 보자
//		stream.filter(t->(t.getAge() >= 35))
//		      .filter(t->t.getGender().equals("남자"))
//		      .forEach(t->System.out.println(t.getName()));
		
		// 중복제거를 위한 중간처리
//		int temp[] = {10,20,30,40,50,30,40};
//		IntStream s = Arrays.stream(temp);
//		s.distinct().forEach(t->System.out.println(t));
		
		// 객체에 대한 중복제거를 해보자
		// VO안에서 equals() method를 overriding해서 처리
//		employees.stream()
//		         .distinct()
//		         .forEach(t->System.out.println(t.getName()));
		
		// mapToInt() => mapXXX()
		
		// 정렬( 부서가 IT인 사람을 연봉순으로 출력 )
//		employees.stream()
//		         .filter(t->t.getDept().equals("IT"))
//		         .distinct()
//		         .sorted( Comparator.reverseOrder() ) // 기본적으로 오름차순 정렬, 인자가 있으면 내립차순
//                 .forEach(t->System.out.println(t.getName() + "," + t.getSalary()));
		// 반복
		// forEach()를 이용하면 스트림안의 요소를 반복할 수 있다.
		// forEach()는 최종 처리 함수
		// 중간처리 함수로 반복처리하는 함수가 하나더 제공(peek)
//		employees.stream()
//		         .peek(t->System.out.println(t.getName()))
//		         .mapToInt(t->t.getSalary())
//		         .forEach(t->System.out.println(t));
		// 확인용 최종 처리 함수 => predicate를 이용해서 boolean으로 return
		// 50살 이상인 사람만 추출해서 이름 출력
		boolean result = employees.stream()
		                          .filter(t->(t.getAge() >= 50))
		                          .anyMatch(t->(t.getAge() > 55)); // 하나만 만족해도 true
		                          //.allMatch(t->(t.getAge() > 55)); // 모두 만족해야 true
		System.out.println(result);
		
		// 최종 확인용 함수로 forEach를 많이 사용했는데
		// forEach말고 collect()를 이용해 보아요.
		// 나이가 50살 이상인 사람들의 연봉을 구해서
		// List<Integer>형태의 ArrayList에 저장해 보아요
//		List<Integer> tmp = employees.stream()
//		                             .filter(t->(t.getAge() >= 50))
//                                   .map(t->t.getSalary())
//                                   .collect(Collectors.toList());
//                                     
//		System.out.println(tmp);
		// 당영히 Set으로도 저장할 수 있고, Map으로도 저장할 수 있다.

		
		Set<Integer> tmp = employees.stream()
				                    .filter(t->(t.getAge() >= 50))
				                    .map(t->t.getSalary())
				                    .collect(Collectors.toCollection(HashSet :: new));
		System.out.println(tmp);
		// 당영히 Set으로도 저장할 수 있고, Map으로도 저장할 수 있다.
		}
}

class Exam03_Employee implements Comparable<Exam03_Employee>{
	private String name; // 이름
	private int age; // 나이
	private String dept; // 부서
	private String gender; // 성별
	private int salary; // 연봉
	
	public Exam03_Employee() {
		
	}
	
	public Exam03_Employee(String name, int age, String dept, String gender, int salary) {
		super();
		this.name = name;
		this.age = age;
		this.dept = dept;
		this.gender = gender;
		this.salary = salary;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getDept() {
		return dept;
	}
	
	public void setDept(String dept) {
		this.dept = dept;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public int getSalary() {
		return salary;
	}
	
	public void setSalary(int salary) {
		this.salary = salary;
	}

	@Override
	public boolean equals(Object obj) {
		// 만약 overriding을 하지 않으면 메모히 주소가지고 비교
		// 내가 원하는 방식으로 overriding을 해서 특정 조건을 만족하면 객체가 같아!!
		// 라는 식으로 작성해보자.
		boolean result = false;
		Exam03_Employee target = (Exam03_Employee)obj;
		if(this.getName().equals(target.getName())) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((dept == null) ? 0 : dept.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + salary;
		return result;
	}
	
	@Override
	public int compareTo(Exam03_Employee o) {
		// 정수값을 리턴.
		// 양수가 리턴되면 순서를 바꾼다.
		// 0이나 음수가 리턴되면 순서를 바꾸지 않는다.
		int result = 0;
		if(this.getSalary() > o.getSalary()) {
			result = 1;
		} else if(this.getSalary() == o.getSalary()) {
			result = 0;
		} else {
			result = -1;
		}
		return result;
	}
}