package javaThread;


import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam03_ThreadSleep extends Application {

	TextArea textarea;
	Button btn;
	
	private void printMsg(String msg) {
		Platform.runLater(()->{
			textarea.appendText(msg + "\n");
		});
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// 화면구성해서 window 띄우는 코드
		// 화면기본 layout을 설정 => 화면을 동서남북중앙(5개의 영역)으로 분리
		BorderPane root = new BorderPane();
		// BorderPane의 크기를 설정 => 화면에 띄우는 window의 크기 설정
		root.setPrefSize(700, 500);
		
		// Component생성해서 BorderPane에 부착
		textarea = new TextArea();
		root.setCenter(textarea);
		
		btn = new Button("버튼 클릭!!");
		btn.setPrefSize(250, 50);
		btn.setOnAction(t->{
			// 버튼에서 Action이 발생(클릭)했을 떄 호출!
			// 1부터 5까지 5번 반복 => for문 이용
//			for(int i=0; i<5; i++) {
//				
//			}
			// 1부터 5까지 5번 반복 =>Stream
			IntStream intStream = IntStream.rangeClosed(1,5);
			intStream.forEach(value-> {
				// 1부터 5까지 5번 반복하면서 Thread를 생성
				Thread thread = new Thread(()->{
					for(int i=0; i<5; i++) {
						try{
							Thread.sleep(3000);
							printMsg(i + " : " + Thread.currentThread().getName());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				thread.setName("ThreadNumber-" + value);
				thread.start();
			});
		});
		FlowPane flowpane = new FlowPane(); // 긴 판넬이라 보면 된다.
		flowpane.setPrefSize(700, 50);
		// flowpane에 버튼을 올려요.
		flowpane.getChildren().add(btn);
		root.setBottom(flowpane); // 밑에 판넬을 붙인다.
		
		// Scene객체가 필요.
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Thread 예제입니다.");
		primaryStage.show();
		
	}

public static void main(String[] args) {
		
		launch();
	}
}
