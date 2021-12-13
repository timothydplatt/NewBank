package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class NewBankClient extends Thread{

	/**
	 * NewBankClient Class: Client service connecting to server running on localhost: 14004
	 */

	private Socket server;
	private PrintWriter bankServerOut;	
	private BufferedReader userInput;
	private Thread bankServerResponseThread;
	
	public NewBankClient(String ip, int port) throws UnknownHostException, IOException {
		server = new Socket(ip,port);
		userInput = new BufferedReader(new InputStreamReader(System.in)); 
		bankServerOut = new PrintWriter(server.getOutputStream(), true);
		
		bankServerResponseThread = new Thread() {
			private BufferedReader bankServerIn = new BufferedReader(new InputStreamReader(server.getInputStream())); 
			public void run() {
				try {
					while(true) {
						String response = bankServerIn.readLine();
						System.out.println(response);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		};
		bankServerResponseThread.start();
	}
	
	
	public void run() {
		while(true) {
			try {
				while(true) {
					String command = userInput.readLine();
					bankServerOut.println(command);
				}				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		new client.NewBankClient("localhost",14004).start();
	}
}
