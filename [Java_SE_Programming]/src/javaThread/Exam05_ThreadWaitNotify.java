package javaThread;

import javax.management.Notification;

// 공용객체를 생성하기 위한 class를 정의
class MyShared {
	
	// method호출할 때 Thread가 번갈아 가면서 호출하도록 만들고 싶다!
	public synchronized void printNum() {
		for(int i=0; i<10; i++) {
			try {
				Thread.sleep(1000);
				System.out.println(Thread.currentThread().getName() + " : " + i);
				notify(); // 현재 wait()상태에 있는 Thread를 깨워서
				          // runnable상태로 전환
				wait();   // 자기가 가지고 있는 monitor객체를 놓고
				          // 스스로 wait block에 들어간다.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class Exam05_Runnable implements Runnable {
	MyShared obj;
	
	public Exam05_Runnable(MyShared obj) {
		this.obj = obj;
	}
	@Override
	public void run() {
		obj.printNum();
	}
}
public class Exam05_ThreadWaitNotify {

	public static void main(String[] args) {
		// 공용객체 만들고
		MyShared shared = new MyShared();
		
		// Thread를 생성하면서 공용객체를 넣어준다.
		Thread t1 = new Thread(new Exam05_Runnable(shared));
		Thread t2 = new Thread(new Exam05_Runnable(shared));
		
		t1.start();
		t2.start();

	}

}
