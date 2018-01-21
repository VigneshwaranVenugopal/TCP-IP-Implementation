package protocol_stack;

//A Java program for a Client
import java.net.*;
import java.io.*;

public class Client
{
	// initialize socket and input output streams
	private Socket socket = null;
	private DataInputStream  input = null;
	private DataOutputStream out = null;

	// constructor to put ip address and port
	@SuppressWarnings("deprecation")
	public Client(String address, int port)
	{
		// establish a connection
		try
		{
			socket = new Socket(address, port);
			System.out.println("Connected");

			// takes input from terminal
			//input = new DataInputStream(System.in);
			input = new DataInputStream(new FileInputStream("tcp.txt"));

			// sends output to the socket
			out = new DataOutputStream(socket.getOutputStream());
		}
		catch(UnknownHostException u)
		{
			System.out.println(u);
			System.exit(0);
		}
		catch(IOException i)
		{
			System.out.println(i);

			System.exit(0);
		}

		// string to read message from input
		String line = "";
		// keep reading until "Over" is input
		while (true)
		{
			try
			{
				line  = input.readLine();
				if(line==null) break;
				out.writeByte(Integer.parseInt(line));
			}
			catch(Exception i)
			{
				System.out.println("End of Data");
			}
		}

		// close the connection
		try
		{
			input.close();
			out.close();
			socket.close();
		}
		catch(IOException i)
		{
			System.out.println(i);

			System.exit(0);
		}
	}

	@SuppressWarnings("unused")
	public static void main(String args[])
	{
		Client client = new Client("127.0.0.1", 5000);
	}
}