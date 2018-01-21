package protocol_stack;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class L3 {
	private byte version;
	private byte IHL;
	private byte DSCP;
	private byte ECN;
	private short Total_length;
	private short identification;
	private byte flags;
	private short fragment_offset;
	private byte ttl;
	private byte protocol;
	private short header_checksum;
	public ArrayList<Byte> options = new ArrayList<Byte>();
	public short calulated_checksum;
	private Inet4Address source;
	private Inet4Address destination;
	public String errorMessages="";
	
	public L3(byte[] header_data) throws UnknownHostException {
		version = (byte) ((header_data[0])>>4);
		IHL = (byte) (header_data[0]&15);
		DSCP = (byte) ((header_data[1]&252)>>2);
		ECN = (byte)(header_data[1]&3);
		Total_length = (short) ((header_data[2]<<8)|(header_data[3]&0xFF));
		identification = (short) ((header_data[4]<<8)|(header_data[5]&0xFF));
		flags = (byte)((header_data[6]&224)>>5);
		fragment_offset = (short) (((header_data[6]&31)<<8)|(header_data[7]&0xFF));
		ttl = (byte)((header_data[8]));
		protocol = (byte)((header_data[9]));
		header_checksum = (short) ((header_data[10]<<8)|(header_data[11]&0xFF));
		source = (Inet4Address) Inet4Address.getByAddress(new byte[]{header_data[12], header_data[13], 
				header_data[14], header_data[15]});
		destination = (Inet4Address) Inet4Address.getByAddress(new byte[]{header_data[16], header_data[17], 
				header_data[18], header_data[19]});
		for(int i=0;i<20;i++){
			if(!(i==10)&&!(i==11)){ //except for the checksum header field
				calulated_checksum += header_data[i];
			}
		}
		checkErrors();
	}	

	public byte get_version(){
		return version;
	}
	public byte get_IHL(){
		return IHL;
	}
	public byte get_DSCP(){
		return DSCP;
	}
	public byte get_ECN(){
		return ECN;
	}
	public int total_length(){
		return Short.toUnsignedInt(Total_length);
	}
	public int get_identification(){
		return Short.toUnsignedInt(identification);
	}
	public int get_flags(){
		return Byte.toUnsignedInt(flags);
	}
	public int get_fragment_offset(){
		return Short.toUnsignedInt(fragment_offset);
	}
	public int get_ttl(){
		return Byte.toUnsignedInt(ttl);
	}
	public int get_protocol(){
		return Byte.toUnsignedInt(protocol);
	}
	public int get_header_checksum(){
		return Short.toUnsignedInt(header_checksum);
	}
	public Inet4Address get_sourceip(){
		return source;
	}
	public Inet4Address get_destinationip(){
		return destination;
	}
	public void checkErrors(){
		//Check errors in ip version
		if(version!=4){
			errorMessages = errorMessages.concat("Invalid ipv4 Header\n");
		}
		//check errors in checksum
		if(calulated_checksum!=header_checksum){
			errorMessages = errorMessages.concat("Invalid Checksum\t"+Short.toUnsignedInt(calulated_checksum)+"\n");
		}
		//Check errors in header length
		if(this.IHL<5){
			errorMessages = errorMessages.concat("Invalid Header Length\n");
		}
		//ECN errors
		if(this.ECN==3){
			errorMessages = errorMessages.concat("ECN is 3, Congestion in the network, "
					+ "Lower the data transfer rate\n");				
		}
	}
	@Override
	public String toString() {
		return  "version:"+this.get_version()+"\t"+
                "IHL:"+this.get_IHL()+"\t"+
                "ECN:"+this.get_ECN()+"\t"+
                "DSCP:"+this.get_DSCP()+"\t"+
                "Total length:"+this.total_length()+"\n"+
                "Identification:"+this.get_identification()+"\t"+
                "Flags:"+this.get_flags()+"\t\t"+
                "Fragement Offset:"+this.get_fragment_offset()+"\n"+
                "TTL:"+this.get_ttl()+"\t"+
                "Protocol:"+this.get_protocol()+"\t"+
                "Header Checksum:"+this.get_header_checksum()+"\n"+
                "SourceIP:"+this.get_sourceip()+"\t	"+
                "DestinationIP:"+this.get_destinationip()+"\n"+
                "Options:"+this.options+"\n"+
                "Error Messages:"+this.errorMessages;
	}
}
