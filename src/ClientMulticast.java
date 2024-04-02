
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ClientMulticast implements Runnable{
	private int socketnum;
	
	public ClientMulticast(int socket) {
		socketnum = socket;

	}

	public void run()   {
		String msg = " ";
		 try {
			 MulticastSocket socket;	      
			 socket = new MulticastSocket(socketnum);
			 InetAddress ia =InetAddress.getByName("230.0.0.0");
			 
			 InetSocketAddress grupo = new InetSocketAddress(ia , socketnum);
			 NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
			 socket.joinGroup(grupo,ni);
			 while(!msg.contains("Servidor Encerrado!")){
				   System.out.println("\n[Cliente] Esperando por mensagem Multicast...");
				         
			   do {
				   byte[] buffer = new byte[1024];
				         
				   DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
				   socket.receive(packet);
				   msg =new String(packet.getData());
				   System.out.println("\n[Cliente] Mensagem recebida do Servidor: "+msg);    
				         }
				         while(!msg.contains("Servidor Encerrado!"));
			 }
			   
					 System.out.println("[Cliente] Conexao Encerrada!");
				     socket.leaveGroup(grupo, ni);
				     socket.close();
				     
		 
		 } catch (IOException e) {
				e.printStackTrace();
		 }
	}
	
	
   public static void main(String[] args) throws IOException {	   
	   String msg = " ";	 
	   String nomeUser = " ";
	   Scanner sc = new Scanner(System.in);
	   byte[] envio = new byte[1024];
	   int escolha;
	   InetAddress ia =InetAddress.getByName("230.0.0.0");
	   MulticastSocket socket;
	   socket = new MulticastSocket(4322);
	   InetSocketAddress grupo = new InetSocketAddress(ia , 4322);
		   
		   String data = " ";
		   String topico = " ";
		   String mensagem = " ";
		   
		   while(!mensagem.equals("Servidor Encerrado!")) {
			   
			   System.out.println("[Cliente] Selecione um tópico para inscrição:\n"
					   + "1. Avisos Gerais\n"
					   + "2. Suporte Técnico e Avisos Gerais\n"
					   + "3. Fórum e Avisos Gerais");
			   
			   escolha = Integer.parseInt(sc.nextLine().trim());
			   
			   
			   if(nomeUser == " ") {	   
				   System.out.println("[Cliente] Bem vindo, digite seu nome de usuário:\n");
				   nomeUser = sc.nextLine();
				   
			   }
			   
			   if(escolha == 1) {
				   Thread a1 = new Thread(new ClientMulticast(4321));
				   a1.start();
				   
				   
				   System.out.println("[Cliente] Digite 0 para sair do tópico Avisos Gerais");
				   escolha = Integer.parseInt(sc.nextLine().trim());
				   if(escolha == 0) {
					   a1.stop();
				   }else {
					   System.out.println("[Cliente] Comando inválido");
				   }
				   
			   }else if(escolha == 2) {
				   Thread a1 = new Thread(new ClientMulticast(4321));
				   a1.start();
				   
				   topico = "Suporte ao Cliente";
				   
				   while(!mensagem.equals("Servidor Encerrado!")){
					   System.out.print("[Cliente] Digite 0 para sair dos tópicos ou 1 para enviar uma mensagem");
					   escolha = Integer.parseInt(sc.nextLine().trim());
					   
					   if(escolha == 0) {
						   a1.stop();
						   break;
					   }else if (escolha == 1){
						   System.out.print("[Cliente] Digite a mensagem:");
						   mensagem = sc.nextLine();
						   
						   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); 
						   LocalDateTime now = LocalDateTime.now();  			   
						   data = dtf.format(now);
						   
						   envio = ("[" + data + "] " + topico + " de "+ nomeUser + " : " + mensagem).getBytes();	
						   DatagramPacket pacote = new DatagramPacket(envio, envio.length,ia, 4320);
						   
						   socket.send(pacote);
						   
						   do {
							   byte[] buffer = new byte[1024];
							   
							   DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
							   socket.receive(packet);
							   msg =new String(packet.getData());
							   if(msg.contains(nomeUser)) {
								   System.out.println("[Cliente] Mensagem recebida do Servidor: "+msg);								   
							   }
						   }while(!msg.contains(nomeUser));
						   
					   }
					   
					   
				   }
			   }else if(escolha == 3) {
				   Thread a1 = new Thread(new ClientMulticast(4321));
				   a1.start();
				   
				   Thread a2 = new Thread(new ClientMulticast(4323));
				   a2.start();
				   
				   topico = "Fórum";
				   
				   while(!mensagem.equals("Servidor Encerrado!")){
					   System.out.print("[Cliente] Digite 0 para sair dos tópicos ou 1 para enviar uma mensagem");
					   escolha = Integer.parseInt(sc.nextLine().trim());
					   
					   if(escolha == 0) {
						   a1.stop();
						   break;
					   }else if (escolha == 1){
						   System.out.print("[Cliente] Digite a mensagem:");
						   mensagem = sc.nextLine();
						   
						   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); 
						   LocalDateTime now = LocalDateTime.now();  			   
						   data = dtf.format(now);
						   
						   envio = ("[" + data + "] " + topico + " de "+ nomeUser + " : " + mensagem).getBytes();	
						   DatagramPacket pacote = new DatagramPacket(envio, envio.length,ia, 4323);
						   
						   socket.send(pacote);
				
						   
					   }
					   
					   
				   }
			   }
			   
		   }

	   	      
	      
	   
   }

  
}