package pack;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class ServerMulticast {
   public static void main(String[] args) throws IOException {
	   String data = " ";
	   String topico = " ";
	   String mensagem = " ";
	   byte[] envio = new byte[1024];
	   Scanner sc = new Scanner(System.in);
	   
	   MulticastSocket socket = new MulticastSocket();
	   InetAddress ia =InetAddress.getByName("230.0.0.0");
	   InetAddress grupo = InetAddress.getByName("230.0.0.0");
	   NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
	   
	   while(!mensagem.equals("Servidor Encerrado!")){
		   System.out.print("[Servidor] Escolha o tópico da mensagem:\n"
		   		+ "1. Avisos gerais\r\n"
		      		+ "2. Suporte técniquicocock\r\n");   
		      int escolha = Integer.parseInt(sc.nextLine());   
		      
		      if(escolha == 1) {
		    	  topico = "Avisos Gerais";
		      }else if(escolha == 2) {
		    	  topico = "Atividades Extracurriculares";
		      }
		   
		   System.out.print("[Servidor] Digite a mensagem:");
		   mensagem = sc.nextLine();
		   if(mensagem.equals("encerrar"))
			   mensagem = "Servidor Encerrado!";
		   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); 
		   LocalDateTime now = LocalDateTime.now();  
		   data = dtf.format(now);
		   
		   envio = ("[" + data + "] " + topico + " : " + mensagem).getBytes();	   
	   
		   
		   DatagramPacket pacote = new DatagramPacket(envio, envio.length,grupo, 4321);
		   
		   socket.send(pacote);
		   
		  
	   }

	   System.out.print("[Servidor] Multicast Encerrado");
	   socket.close(); 
   }
}