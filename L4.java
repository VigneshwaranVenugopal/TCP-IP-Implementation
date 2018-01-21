package protocol_stack;

public class L4 {
	byte[] header;
	int protocol;
	TCP tcp;
	UDP udp;
	public static class UDP{
		private short source_port;
		private short destination_port;
		private short checksum;
		private short data_length;
		private short calulated_checksum=0;
		String errorMessages="";
		UDP(byte[] udpdata){
			source_port = (short)((udpdata[0]<<8)|(udpdata[1]&0xFF));
			destination_port = (short)((udpdata[2]<<8)|(udpdata[3]&0xFF));
			checksum = (short)((udpdata[4]<<8)|(udpdata[5]&0xFF));
			data_length = (short)((udpdata[6]<<8)|(udpdata[7]&0xFF));
			for(int i=0;i<udpdata.length;i++){
				if(!(i==4)&&!(i==5)){ //except for the checksum header field
					calulated_checksum += udpdata[i];
				}
			}
			checkUDPErrors();
		}
		public String toString() {
			return "UDP Header:\n\nSource Port:"+this.get_sorce_port()+"\t"+"Destination Port:"+this.get_destination_port()+"\n"+
		"Checksum:"+this.get_checksum()+"\t"+"Data Legth:"+this.get_data_length()+"\n"+
			    "Error Messages:"+"\n"+errorMessages;
		}
		public int get_sorce_port() {
			return Short.toUnsignedInt(source_port);
		}
		public int get_destination_port() {
			return Short.toUnsignedInt(destination_port);
		}
		public int get_checksum() {
			return Short.toUnsignedInt(checksum);
		}
		public int get_data_length() {
			return Short.toUnsignedInt(data_length);
		}
		public void checkUDPErrors(){
			if(this.checksum!=calulated_checksum){
				errorMessages = errorMessages.concat("Invalid checksum, Correct chksum is:"
			+Short.toUnsignedInt(calulated_checksum)+"\n");
			}
		}
	}
	public static class TCP{
		private short source_port;
		private short destination_port;
		private int sequence_number;
		private int acknowledgement_number;
		private byte data_offset;
		private byte reserved;
		private short control_flags;
		private short window_size;
		private short checksum;
		private short urgent_pointer;
		private short calulated_checksum=0;
		String errorMessages="";
		//Constructor that calculates the TCP header by Bit Shifting
		TCP(byte[] tcpdata){
			source_port = (short)((tcpdata[0]<<8)|(tcpdata[1]&0xFF));
			destination_port = (short)((tcpdata[2]<<8)|(tcpdata[3]&0xFF));
			sequence_number = (int)((tcpdata[4]<<24)|(tcpdata[5]<<16)|(tcpdata[6]<<8)|(tcpdata[7]&0xFF));
			acknowledgement_number = (int)((tcpdata[8]<<24)|(tcpdata[9]<<16)|(tcpdata[10]<<8)|(tcpdata[11]&0xFF));
			data_offset = (byte)((tcpdata[12]&240)>>4);
			reserved = (byte)((tcpdata[12]&14)>>1);
			control_flags = (short)(((tcpdata[12]<<8)|(tcpdata[13]&0xFF))&511);
			window_size = (short)((tcpdata[14]<<8)|(tcpdata[15]&0xFF));
			checksum  = (short)((tcpdata[16]<<8)|(tcpdata[17]&0xFF));
			urgent_pointer = (short)((tcpdata[18]<<8)|(tcpdata[19]&0xFF));
			
			for(int i=0;i<tcpdata.length;i++){
				if(!(i==16)&&!(i==17)){ //except for the checksum header field
					calulated_checksum += tcpdata[i];
				}
			}
			checkTCPErrors();
		}	
		//tostring method to display the tcp header
		public String toString() {
			return "TCP Header:\n\nSourceport:"+ this.get_source_port()+"\t"+
		"Destination port:"+ this.get_destination_port()+"\n"+
					"Sequence number:"+Integer.toUnsignedString(sequence_number)+"\t"+
		"Acknowledgment number:"+Integer.toUnsignedString(acknowledgement_number)+"\n"+
					"Data offset:"+this.get_dataoffset()+"\t"+
		"Reserved:"+this.get_reserved()+"\t"+
					"Control_flags:"+this.get_control_flags()+"\n"+
		"Window Size:"+this.get_window_size()+"\t"+
				    "Checksum:"+this.get_checksum()+"\t"+
		"Urgent Pointer:"+this.get_urgent_pointer()+"\n"+
				    "Error Messages:"+"\n"+errorMessages;
			
		}
		public void checkTCPErrors(){
			if(this.checksum!=calulated_checksum){
				errorMessages = errorMessages.concat("Invalid checksum, Correct chksum is:"+
			Short.toUnsignedInt(calulated_checksum)+"\n");
			}
		}
		public int get_source_port(){
			return Short.toUnsignedInt(source_port);
		}
		public int get_destination_port(){
			return Short.toUnsignedInt(destination_port);
		}
		public int get_dataoffset(){
			return Byte.toUnsignedInt(data_offset);
		}
		public int get_reserved(){
			return Byte.toUnsignedInt(reserved);
		}
		public int get_control_flags(){
			return Short.toUnsignedInt(control_flags);
		}
		public int get_window_size(){
			return Short.toUnsignedInt(window_size);
		}
		public int get_checksum(){
			return Short.toUnsignedInt(checksum);
		}
		public int get_urgent_pointer(){
			return Short.toUnsignedInt(urgent_pointer);
		}
		
	}
	public L4(byte[] header_data,int protocol) {
		this.protocol = protocol;
		if(protocol == 6){
			tcp =  new TCP(header_data);
		}else if(protocol == 17)
		{
			udp = new UDP(header_data);
		}

	}
	
	
	@Override
	public String toString() {
		if(protocol == 6){
			return tcp.toString();
		}else if(protocol == 17)
		{
			return udp.toString();
		}else{
			return "IP Other";
		}
	}

	
}
