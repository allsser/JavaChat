package javaThread;
/*
 * Java Application은 main thread가 main() method를 호출해서 실행
 * 
 * 프로그램은 main method()가 종료될 때 종료되는게 아니라 프로그램내에서
 * 파생된 모든 Thread가 종료될 때 종료된다.
 * 
 * 1. Thread의 생성
 *    => Thread class를 상속받아서 class를 정의하고 객체 생성해서 사용
 *    => Runnable interface를 구현한 class를 정의하고 객체를 생성해서
 *       Thread 생성자의 인자로 넣어서 Thread 생성.
 *    => 현재 사용되는 Thread의 이름을 출력!!
 * 2. 실제 Thread의 생성(new) -> start() (thread를 실행시키는게 아니라
 *    runnable상태로 전환) -> JVM안에 있는 Thread schedule에 의해
 *    하나의 Thread가 선택되서 thread가 running상태로 전환 -> 어느 시점이
 *    되면 Thread scheduler에 의해서 runnable 상태로 전환 -> 다른 thread
 *    를 선택해서 running상태로 전환
 */

import com.sun.media.jfxmediaimpl.platform.Platform;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam06_ThreadInterrupt extends Application {
	TextArea textarea;
	Button startbtn, stopbtn;
	Thread counterThread;
		
	private void printMsg(String msg) {
		// textarea에 문자열 출력하는 method
		javafx.application.Platform.runLater(()->{
			textarea.appendText(msg + "\n");
		});
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		System.out.println(Thread.currentThread().getName());
		// JavaFX는 내부적으로 화면을 제어하는 Thread를 생성해서 사용한다.
		
		// 화면구성해서 window 띄우는 코드
		// 화면기본 layout을 설정 => 화면을 동서남북중앙(5개의 영역)으로 분리
		BorderPane root = new BorderPane();
		// BorderPane의 크기를 설정 => 화면에 띄우는 window의 크기 설정
		root.setPrefSize(700, 500);
			
		// Component생성해서 BorderPane에 부착
		textarea = new TextArea();
		root.setCenter(textarea);
			
		startbtn = new Button("Thread실행!!");
		startbtn.setPrefSize(250, 50);
		startbtn.setOnAction(t->{
			counterThread = new Thread(()->{
				try {
					for(int i=0; i<10; i++) {
						Thread.sleep(1000);
						printMsg(i + "-" + Thread.currentThread().getName());
					}
				} catch (Exception e) {
					// 만약 interrupt()가 걸려있는 상태에서 block상태로 진입하면
					// Exception을 내면서 catch문으로 이동
					
					printMsg("Thread가 종료되었다!");
				}
			});
			
			counterThread.start();		
		});

		stopbtn = new Button("Thread중지!!");
		stopbtn.setPrefSize(250, 50);
		stopbtn.setOnAction(t->{
			counterThread.interrupt(); // method가 실행된다고 바로
									 // Thread가 종료되지 않는다.
			// interrupt() method가 호출된 Thread는 sleep과 같이
			// block상태에 들어가야지 interrupt를 시킬 수 있다.
		});
		FlowPane flowpane = new FlowPane(); // 긴 판넬이라 보면 된다.
		flowpane.setPrefSize(700, 50);
		// flowpane에 버튼을 올려요.
		flowpane.getChildren().add(startbtn);
		flowpane.getChildren().add(stopbtn);
		root.setBottom(flowpane); // 밑에 판넬을 붙인다.
			
		// Scene객체가 필요.
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Thread 예제입니다.");
		primaryStage.show();
			
	}
		
	public static void main(String[] args) {
		
		// 현재 main method를 호출한 Thread의 이름을 호출!
		//currentThread() 현재 이 코드를 실행하고 있는 Thread를 알 수 있다.
		System.out.println(Thread.currentThread().getName());
		launch();
	}
}

