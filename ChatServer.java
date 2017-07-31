import java.util.*;
import java.net.*;
import java.io.*;
public class ChatServer{
	static Vector clientSockets;
	static Vector loginNames;

	ChatServer() throws IOException{//setup
		ServerSocket server = new ServerSocket(1007);
		clientSockets=new Vector();//holds connections
		loginNames= new Vector();//holds usernames
		while(true){
			Socket client = server.accept();
			AcceptClient acceptClient= new AcceptClient(client);
		}
	}


	public static void main(String[] args) throws IOException{
		ChatServer server = new ChatServer();
	}

	class AcceptClient extends Thread{
		//set up inputs and output streams
		Socket clientSocket;
		DataInputStream din;
		DataOutputStream dout;
		AcceptClient(Socket client) throws IOException{
			clientSocket=client;
			din= new DataInputStream(clientSocket.getInputStream());
			dout = new DataOutputStream(clientSocket.getOutputStream());
			String lg= din.readUTF();
			loginNames.add(lg);
			clientSockets.add(clientSocket);
			start();
		}
		public void run(){//the run function is while loop that delivers messages and tracks users joining a nd logging out
			while(true){
				try{
				String msgFromClient=din.readUTF();
				StringTokenizer st= new StringTokenizer(msgFromClient);
				String LoginName = st.nextToken();
				String MsgType= st.nextToken();
				int mSize=-1;
				String msg="";
				while(st.hasMoreTokens()){msg=msg+" "+st.nextToken();}
				if(MsgType.equals("Login")){//joining chat, should tell you that NAME has logged on
				for(int i=0;i<loginNames.size();i++){
					Socket pSocket=(Socket) clientSockets.elementAt(i);
					DataOutputStream pOut= new DataOutputStream(pSocket.getOutputStream());
					pOut.writeUTF(LoginName+" has logged on");
				}
				}

				else if(MsgType.equals("Logout")){///leaving chat, should tell you that user logged out, and close client for that user
				for(int i=0;i<loginNames.size();i++){
					if(LoginName==loginNames.elementAt(i)){mSize=i;}
					Socket pSocket=(Socket) clientSockets.elementAt(i);
					DataOutputStream pOut= new DataOutputStream(pSocket.getOutputStream());
					pOut.writeUTF(LoginName+"  logged out");
				}
				if(mSize>=0){
					loginNames.removeElementAt(mSize);
					clientSockets.removeElementAt(mSize);
				}
				}
				else{
					for(int i=0;i<loginNames.size();i++){
					Socket pSocket=(Socket) clientSockets.elementAt(i);
					DataOutputStream pOut= new DataOutputStream(pSocket.getOutputStream());
					pOut.writeUTF(LoginName+": "+msg);
				}

				}

				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}
