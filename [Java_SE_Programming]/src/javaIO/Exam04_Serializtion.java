package javaIO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

class MyClass implements Serializable {
	// 직렬화와 역직렬화를 할 때 같은 타입인지를 비교하기위해서 내부적으로 사용.
	private static final long serialVersionUID = 1L;
	
	String name; // 직렬화가 가능한 형태이어야 한다.
	int kor; // 직렬화가 가능하다.
	
	// transient는 직렬화 대상에서 제외시키는 키워드
	transient Socket socket; // 직렬화가 안되는 놈이다.
	
	public MyClass(String name, int kor) {
		super();
		this.name = name; 
		this.kor = kor; 
		
	}

}
public class Exam04_Serializtion {

	public static void main(String[] args) {
		// ObjectOutStream을 이용해서 File에 내가 만든 calss의
		// instance를 저장.
		// 1. 저장할 객체를 생성
		MyClass obj = new MyClass("홍길동", 70);
		// 2. 객체를 저장할 파일 객체를 생성
		File file = new File("asset/student.txt");
		// 3. 파일 객체를 이용해서 ObjectOutputStream을 생성
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			// 4. ObjectOutputStream을 이용해서 객체를 파일에 저장
			//    저장될 객체를 반드시 직렬화가 가능한 객체이여야 한다.
			//    우리가 생성한 객체는 직렬화가 가능한 객체가 아니다.
			//    객체 직렬화가 가능하려면 어떻게 해야하나?
			//    Serializable interface 를 구현해야 한다.
			//    class의 필드가 모두 직렬화가 가능한 형태이어야 한다.
			oos.writeObject(obj);
			// 5. 저장이 끝나면 Stream을 close해 줘야 한다.
			oos.close();
			fos.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

}
