package javaNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

class EchoRunnable implements Runnable {
	// 가지고 있어야 하는 field
	Socket socket; // 클라이언트와 연결된 소켓
	BufferedReader br; // 입력을 위한 스트림
	PrintWriter out; // 출력을 위한 스트임
	
	public EchoRunnable(Socket socket) {
		super();
		this.socket = socket;
		try {
			
			this.br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		// 클라이언트와 echo처리 구현
		// 클라이언트가 문자열을 보내면 해당 문자열을 받아서 다시 클라이언트에게
		// 전달. 한번하고 종료하는게 아니라 클라이언트가 "/EXIT"라는 문자열을
		// 보낼때까지 지속.
		String line = "";
		try {
			while((line = br.readLine()) != null) {
				if(line.equals("/EXIT/")) {
					break; // 가장 근접한 loop를 탈출!
				} else {
					out.println(line);
					out.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

public class Exam03_EchoServerMulitClient extends Application {

	TextArea textarea; // 메세지창 용도로 사용
	Button startBtn, stopBtn; // 서버시작, 서버중지 버튼
	// threadPool을 생성
	ExecutorService executorservice = Executors.newCachedThreadPool();
	// 클라이언트의 접속을 받아들이는 서버소켓.
	ServerSocket server;
	
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
			// 서버프로그램을 시작
			// 클라이언트에 접속을 기다린다. -> 접속이 되면 Thread를 하나 생성
			// -> Thread를 시작해서 클라이언트와 Thread가 통신하도록 만든다.
			// 서버는 다시 다른 클라이언트의 접속을 기다린다.
			Runnable runnable = ()-> {
				try {
					server = new ServerSocket(7777);
					printMsg("Echo 서버 기동");
					while(true) {
						printMsg("클라이언트 접속 대기");
						Socket s = server.accept(); // bolcking
						printMsg("클라이언트 접속 성공");
						// 클라이언트가 접속했으니 Thread만들고 시작해야 한다.
						EchoRunnable r = new EchoRunnable(s);
						executorservice.execute(r); // thread 실행
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}	
			};
			executorservice.execute(runnable);
		});
		
		stopBtn = new Button("Echo 서버 종료");
		stopBtn.setPrefSize(250, 50);
		stopBtn.setOnAction(t->{
			// 버튼에서 Action이 발생(클릭)했을 떄 호출!
			
		});
		
		FlowPane flowpane = new FlowPane(); // 긴 판넬이라 보면 된다.
		flowpane.setPrefSize(700, 50);
		// flowpane에 버튼을 올려요.
		flowpane.getChildren().add(startBtn);
		flowpane.getChildren().add(stopBtn);
		root.setBottom(flowpane); // 밑에 판넬을 붙인다.
		
		// Scene객체가 필요.
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("다중 클라이언트 Echo Server");
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		
		launch();
	}
}