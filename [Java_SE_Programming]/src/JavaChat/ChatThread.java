package JavaChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.xml.sax.InputSource;

public class ChatThread extends Thread {

	private String id;
	private BufferedReader br;
	private PrintWriter pw;
	private Socket socket;
	private HashMap<String, PrintWriter> hm;
	private static ArrayList<String> v = new ArrayList<String>();
	private String str = "";

	public ChatThread(Socket socket, HashMap<String, PrintWriter> hm) {
		this.socket = socket;
		this.hm = hm;
		
		try {
			// 입력스트림 소켓에서 읽어오기
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// 소켓에 쓰기
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			// 한줄을 읽어ID에 넣지만 생성자 안에 있기 때문에 처음 들어온 문자를 아이디로 인식
			// 다음 부터는 챗팅글로 인식
			
			id = br.readLine(); // --> 클라이언트로 부터 아이디를 받는다.
			System.out.println(id+"님이 대화방에 들어왔습니다.");
			
			// broadcast는 매소드 인자값 String 값 받음... 둘 다 스트링 값
			broadcast(id+"님이 대화방에 들어왔습니다.");
			
			System.out.println("접속자 정보"+socket+"-id:"+id);
			
			synchronized (hm) {
				hm.put(id, pw); // 해쉬맵에 저장해 보자
			}
			
			v.add(id); // 아이디들을 벡터에 저장
			for(int i=0; i<v.size(); i++) { // 벡터에 저장되어 있는 아이디들을 for문으로 돌린다.
				str += v.get(i)+"/"; // 뒤에 /를 붙여 아이디와 아이디간에 구별
			}
			broadcast("/list/"+str);
			broadcast("/count/"+v.size());
			System.out.println("클라이언트로부터 들어온 id들 대기자 명단 ==>"+str);
			System.out.println("삭제된 후 아이디를 카운트 한 것 ==>"+v.size());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("서버쪽과 연결이 잘못되었습니다.");
		}
	}
	
	public void run() {
		String line = null;
		try {
			while ((line=br.readLine()) != null) {
				System.out.println("클라이언트로부터 받은 말 -->"+line);
				if(line == null) {
					break;
				}
				if(line.equals("/quit")) { //종료하기
					if(hm.containsKey(id)) {
						PrintWriter num = (PrintWriter)hm.get(id);
						num.println("/quit/ 대화방을 나오셨습니다.");
						num.close();
						hm.remove(id);
						v.clear();//백터를 초기화
						Set<String> set = hm.keySet();
						Iterator<String> iter = set.iterator();
						while(iter.hasNext()) {
							String strIdList = (String)iter.next();
							v.add(strIdList);
						}
					}
					str = "";
					for(int i=0; i<v.size(); i++) {
						str += v.get(i)+"/";
					}
					broadcast(id+"님이 대화방을 나가셨습니다.");
					broadcast("/relist/"+str);
					broadcast("/count/"+v.size());
					System.out.println("나간 아이디 삭제후 명단들 ==>"+str);
					System.out.println("아이디가 나간후 아이디를 카운트 한것==>"+v.size());
					System.out.println(id+"님이 대화방을 나가셨습니다.");
					
					break;
				}
				else if(line.startsWith("/to ")) { // to로 시작하면
					//위에 조건에 합당하면 밑에 메소드 실행
					sendmsg(line);
				}
				else if(line.startsWith("/click/")) {
					int nStart = line.indexOf(" ");
					String strList = line.substring(nStart);
					System.out.println("강퇴당할 아이디 ==>"+strList);
					if(hm.containsKey(strList.trim())) { //키가 있으면
						System.out.println(strList+"강퇴");
						PrintWriter num = (PrintWriter)hm.get(strList.trim()); // num이 벨류
						num.println("/quit/ 강퇴 되셨습니다.");
						System.out.println("강퇴 안돼?");
						num.close();
						hm.remove(strList.trim());
						v.clear();
						Set<String> set = hm.keySet();
						Iterator<String> iter = set.iterator();
						while(iter.hasNext()) {
							String strIdList = (String)iter.next();
							v.add(strIdList);
						}
					}
					str = "";
					for(int i=0; i<v.size(); i++) {
						str += v.get(i)+"/";
					}
					broadcast(strList+"님이 강퇴되셨습니다.");
					broadcast("/relist/"+str);
					broadcast("/count/"+v.size());
					System.out.println("지정한 아이디 삭제 후 명단들 ==>"+str);
					System.out.println("삭제된 후 아이디를 카운트 한 것 ==>"+v.size());
				}
				else {
					broadcast(id+"님의 말:" +line);
				}
				Iterator<String> ir = hm.keySet().iterator();
				while(ir.hasNext())
					System.out.println("해쉬에 저장되어 있는 Key:"+ir.next());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally { // 사용자가 퇴장하는 경우 hashMap 테이블에서 데이터를 삭제 시킨다.
			synchronized (hm) {
				hm.remove(id);
			}
			
			try {
				if(hm.containsKey(id)) {
					PrintWriter num = (PrintWriter)hm.get(id);
					num.println("/quit/ 대화방을 나오셨습니다.");
					num.close();
					hm.remove(id);
					v.clear(); //백터를 초기화
					Set<String> set = hm.keySet();
					Iterator<String> iter = set.iterator();
					while(iter.hasNext()) {
						String strIdList = (String)iter.next();
						v.add(strIdList);
					}
				}
				str = "";
				for(int i=0; i<v.size();i++) {
					str += v.get(i)+"/";
				}
				broadcast(id+"님이 대화방을 나가셨습니다.");
				broadcast("/relist/"+str);
				broadcast("/count/"+v.size());
				System.out.println("나간 아이디 삭제 후 명단들 ==>"+str);
				System.out.println("아이디가 나간 후 아이디를 카운트 한 것 ==>"+v.size());
				System.out.println(id+"님이 대화방을 나가셨습니다");
				
				if(socket != null) 
					socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void broadcast(String msg) {
		synchronized (hm) { // 싱크로나이지드는 동시 접속시 발생하는 에러 예방
			Collection<PrintWriter> collection = hm.values(); // hashmap에 키값과 데이터가 저장되는데 데이터를 가져온다.
			Iterator<PrintWriter> iter = collection.iterator(); // 비교해서 중복값을 뺀 나머지를
			while(iter.hasNext()) { // 더 가져올 것이 있는지
				PrintWriter pw = (PrintWriter)iter.next(); // 해쉬맵에 저장 될 때 오브젝트 타입으로 저장되기 때문에
				pw.println(msg);
				pw.flush();
			}
		}
	}
	
	private void sendmsg(String line) {
		int start = line.indexOf(" ")+1; //빈 칸 이후 그 다음부터
		int end = line.indexOf(" ", start);
		if(end != -1) { // 사용자가 /to id content의 경우  id를 구분해서 빼낸다.
			String to = line.substring(start,end);
			String msg = line.substring(end + 1);
			System.out.println("귓속말 메세지만 뽑기 ==>"+msg);
			PrintWriter pw = (PrintWriter)hm.get(to);
			
			System.out.println("이건 뭐지?==>"+to);
			if(pw != null) {
				pw.println(id+"님의 귓속말:"+msg);
				pw.flush();
				System.out.println("귓속말 보낼 아이디 인식하니?"+to);
				System.out.println("클라이언트로 보내자 귓속말:==>"+to+"님의 귓속말:"+msg);
			}
			
		}
	}
}
