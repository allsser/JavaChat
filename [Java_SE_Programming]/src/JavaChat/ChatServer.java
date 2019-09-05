package JavaChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ChatServer {

	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			server = new ServerSocket(5000); //포트 5000
			System.out.println("접속을 기다립니다.");
			HashMap hm = new HashMap(); // HashMap은 한나만 생성 해야 하기 때문에 while문 
		                                // 밖에서 생성한다.
			while(true) {
				Socket socket = server.accept();
				ChatThread chatthead = new ChatThread(socket, hm); // HashMap에서 보관해야 하기 때문에 인자값을 HashMap도 줌
				chatthead.start(); // 쓰레드 스타트
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
