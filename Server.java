package protocol_stack;
import java.net.*;
import java.util.ArrayList;
import java.io.*;
 
public class Server
{
    //initialize socket and input stream
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
 
    // constructor with port
    public Server(int port)
    {
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");
 
            System.out.println("Waiting for a client ...");
 
            socket = server.accept();
            System.out.println("Client accepted");
 
            // takes input from the client socket
            in = new DataInputStream(
                new BufferedInputStream(socket.getInputStream()));
 

            ArrayList<Byte> lines = new ArrayList<Byte>();
            int j =0;
            int layer4protocol=0;
            int layer3Size =0;
            
            // reads message from client until last byte is sent
            while (lines.add(in.readByte()))
            {
                try
                {
                	L3 ipheader ;
                	              
                    if(j==19){
                    	byte[] headerdata = new byte[20];
                    	for(int k=0;k<20;k++){
                    		headerdata[k]=lines.get(k);
                    	}
                    	ipheader = new L3(headerdata);
                    	layer4protocol = ipheader.get_protocol();
                    	layer3Size = ipheader.get_IHL();
                    	if(layer3Size>5){
                    	for(int i =0;i<layer3Size-5;i++){
                    		for(int k=20;k<layer3Size*4;k++){
                    		ipheader.options.add(lines.get(k));
                    		System.out.println(lines.get(k));}
                    	}
                    }
                    	System.out.println(ipheader.toString());
                    }
                    
                    if((layer4protocol==6)&&j==(layer3Size*4+19)){
                    	byte[] tcpheaderdata = new byte[20];
                    	for(int l=0,k=layer3Size*4;k<layer3Size*4+20;k++,l++){
                    		tcpheaderdata[l]=lines.get(k);
                    	}
               		 L4 tcp1 = new L4(tcpheaderdata, layer4protocol);
               		 System.out.println(tcp1.toString());
                    }
                    if((layer4protocol==17)&&j==layer3Size*4+7){
                    	byte[] udpheaderdata = new byte[8];
                    	for(int l=0,k=layer3Size*4;k<layer3Size*4+8;k++,l++){
                    		udpheaderdata[l]=lines.get(k);
                    	}
                  		 L4.UDP udp = new L4.UDP(udpheaderdata); 
                  		 System.out.println(udp.toString());
                       }
                   
                }
                catch(IOException i)
                {
                	
                    System.out.println("End of Data\n");
                }
                j++;
            }
            System.out.println("Closing connection");
            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println("Sent Data");
        }
    }
 
    @SuppressWarnings("unused")
	public static void main(String args[])
    {
        Server server = new Server(5000);
    }
}