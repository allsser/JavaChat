package JavaChatGood2;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JList;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import javax.swing.JTextArea;

import javax.swing.JTextField;



public class ChatClient extends JFrame{
   //채팅방
   JTextField sendTF;
   JLabel la_msg;
   JTextArea ta;
   JScrollPane sp_ta,sp_list;   	
   JList<String> li_inwon;
   JButton bt_change,bt_exit; 
   JRadioButton radio1 = new JRadioButton();
   JRadioButton radio2 = new JRadioButton();
   JPanel p;	
   public ChatClient() {
	  setTitle("채팅방");
	  sendTF = new JTextField(15);	  
	  la_msg = new JLabel("Message");  
	  ta = new JTextArea();
	  ta.setLineWrap(true);//TextArea 가로길이를 벗어나는 text발생시 자동 줄바꿈
	  li_inwon = new JList<String>();

	  sp_ta = new JScrollPane(ta);
	  sp_list = new JScrollPane(li_inwon);	  

	  bt_change = new JButton("대화명변경");
	  bt_exit = new JButton("나가기");
	  p = new JPanel();

	  sp_ta.setBounds(10,10,380,390); 
	  la_msg.setBounds(10,410,60,30); 
	  sendTF.setBounds(70,410,320,30); 

	  sp_list.setBounds(400,10,120,260); 
	  bt_change.setBounds(400,370,120,30); 
	  bt_exit.setBounds(400,410,120,30); 
	  
	  radio1.setText("귓속말");
      radio1.setBounds(new Rectangle(400, 290, 120, 30));
      radio2.setText("귓속말해제");
      radio2.setBounds(new Rectangle(400, 330, 120, 30));

	  p.setLayout(null);
	  p.setBackground(Color.GRAY);
	  p.add(sp_ta);
	  p.add(la_msg);
	  p.add(sendTF);
	  p.add(sp_list);
	  p.add(bt_change);
	  p.add(bt_exit);
	  p.add(radio1);
	  p.add(radio2);

	  add(p);
	  setBounds(300,200,550,500);
	  //setVisible(true);
	  sendTF.requestFocus();	  
   }//생성자   
}