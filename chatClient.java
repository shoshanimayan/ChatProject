import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.Scanner;
public class chatClient extends JFrame implements Runnable{

	Socket socket;
	JTextArea ta;
	JButton send, logout;
	JTextField tf;
	Thread thread;
	DataInputStream din;
	DataOutputStream dout;
	String LoginName;
	chatClient(String login) throws UnknownHostException, IOException{
		super(login);
		LoginName=login;

		ta = new JTextArea(18,50);

		tf = new JTextField(50);
		send = new JButton("send");
		logout= new JButton("logout");


		send.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try{
				dout.writeUTF(LoginName+ " "+"DATA "+ tf.getText().toString())	;
				System.out.println( tf.getText().toString());
				tf.setText("");}
				catch(IOException ex){
					ex.printStackTrace();
				}
			}
		});

		logout.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try{
				dout.writeUTF(LoginName+ " "+"Logout");
				System.exit(1);
				}
				catch(IOException ex){
					ex.printStackTrace();
				}
			}
		});



		socket= new Socket("localhost",1007);
		din = new DataInputStream(socket.getInputStream());
		dout=new  DataOutputStream(socket.getOutputStream());

		dout.writeUTF(LoginName);
		dout.writeUTF(LoginName+" " +"Login");

		thread = new Thread(this);
		thread.start();
		setup();

	}

	private void setup(){
		setSize(600,600);
		JPanel panel= new JPanel();
		panel.add(new JScrollPane(ta));
		panel.add(tf);
		panel.add(send);
		panel.add(logout);
		add(panel);
		setVisible(true);
	}

	@Override
	public void run(){
		while(true){
			try{
				ta.append("\n" + din.readUTF());
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws UnknownHostException,IOException{
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter a name: ");
		String name = scan.next();
		chatClient client = new chatClient(name);
	}
}
