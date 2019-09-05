package javaNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Exam02_EchoClient extends Application {

	TextArea textarea;
	Button startBtn; // 서버접속 버튼
	Button stopBtn;
	TextField tf;
	Socket socket;
	BufferedReader br;
	PrintWriter out;
	
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
		
		startBtn = new Button("Echo 서버 접속");
		startBtn.setPrefSize(250, 50);
		startBtn.setOnAction(t->{
			// 버튼에서 Action이 발생(클릭)했을 떄 호출!
			try {
				// 클라이언트는 버튼을 누르면 서버쪽에 Socket접속을 시도.
				// 만약에 접속에 성공하면 socket객체를 하나 획득.
				socket = new Socket("127.0.0.1", 5557);
				// Stream을 생성
				InputStreamReader isr = 
						new InputStreamReader(socket.getInputStream());
				br = new BufferedReader(isr);
				out = new PrintWriter(socket.getOutputStream());
				printMsg("Echo 서버 접속 성공!!");
			} catch (Exception e) {
				System.out.println(e);
			}

		});
		
		tf = new TextField();
		tf.setPrefSize(200, 40);
		tf.setOnAction(t->{
			// 입력상자(TextField)에서 enter key가 입력되면 호출
			String msg = tf.getText();
			out.println(msg); // 서버로 문자열 전송!!
			out.flush();
			try {
				String result = br.readLine();
				printMsg(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		stopBtn = new Button("Echo 서버 종료");
		stopBtn.setPrefSize(250, 50);
		stopBtn.setOnAction(t->{
			out.println("/@EXIT"); // 서버로 문자열 전송!!
			out.flush();
		});
		
		FlowPane flowpane = new FlowPane(); // 긴 판넬이라 보면 된다.
		flowpane.setPrefSize(700, 50);
		// flowpane에 버튼을 올려요.
		flowpane.getChildren().add(startBtn);
		flowpane.getChildren().add(tf);
		flowpane.getChildren().add(stopBtn);
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