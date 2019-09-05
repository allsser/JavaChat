package javaThread;
/*
 * 1부터 100까지 숫자의 합을 구하려고 한다.
 * 1~10까지 1개의 Thread가 합을 계산해서 결과를 return
 * 11~20까지 1개의 Thread가 합을 계산해서 결과물 return
 * ...
 * 91~100까지 1개의 Thread가 합을 계산해서 결과물 return
 * ==> Thread Pool을 이용해야 하고 Callable을 이용해서 리턴값을 받아야한다.
 * ==> 10개의 Thread로부터 각각 데이터를 받아들이는 Thread를 하나 만들어야 한다.
 *  
 */
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam10_ThreadCompleteService extends Application {

	TextArea textarea;
	Button initBtn, startBtn, stopBtn;
	// initBtn : Thread Pool을 생성하는 버튼
	// startBtn : Thread Pool을 이용해서 Thread를 실행시키는 버튼
	// stopBtn : Thread Pool을 종료하는 버튼
	ExecutorService executorService;
	// ExecutorService : Thread Pool
	ExecutorCompletionService<Integer> executorCompletionService;
	private int total = 0;
	
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
		root.setPrefSize(750, 500);
		
		// Component생성해서 BorderPane에 부착
		textarea = new TextArea();
		root.setCenter(textarea);
		
		initBtn = new Button("Thread Pool 생성");
		initBtn.setPrefSize(250, 50);
		initBtn.setOnAction(t->{
			// 버튼에서 Action이 발생(클릭)했을 떄 호출!
			executorService = Executors.newCachedThreadPool();
			executorCompletionService = 
					new ExecutorCompletionService<Integer>(executorService);
			
		});
		
		stopBtn = new Button("Thread Pool 종료");
		stopBtn.setPrefSize(250, 50);
		stopBtn.setOnAction(t->{
			executorService.shutdown();
		});
		
		startBtn = new Button("Thread를 실행");
		startBtn.setPrefSize(250, 50);
		startBtn.setOnAction(t->{
			for(int i=1; i<101; i+=10) {
				final int k = i; // 람다에서 로컬변수라 직접 사용은 못하기 때문에 돌아간다.
				
				Callable<Integer> callable = new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						IntStream intStream =
								IntStream.rangeClosed(k, k+9);
						int sum =intStream.sum();
						return sum;
					}
				};
				executorCompletionService.submit(callable);
			}
			
			Runnable runnable = new Runnable() {	
				@Override
				public void run() {
					for(int i=0; i<10; i++) {
						try {
							Future<Integer> futuer = 
									executorCompletionService.take();
							total += futuer.get();
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					}
					printMsg("최종 결과값은 : " + total);
				}
			};
			executorService.execute(runnable);
		}); 
		FlowPane flowpane = new FlowPane(); // 긴 판넬이라 보면 된다.
		flowpane.setPrefSize(700, 50);
		// flowpane에 버튼을 올려요.
		flowpane.getChildren().add(initBtn);
		flowpane.getChildren().add(stopBtn);
		flowpane.getChildren().add(startBtn);
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
