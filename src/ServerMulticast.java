
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime;

public class ServerMulticast implements Runnable{
	private String finalAddress;

	public ServerMulticast(String finalAddress){
		this.finalAddress = finalAddress;
	}	
	
	public void run()   {
		List<String> lstClientes = new ArrayList<>();
		String msg = " ";
		 try {
			MulticastSocket socket;
			InetAddress ia = InetAddress.getByName("230.0.0."  + finalAddress);
			socket = new MulticastSocket(4320);
			InetSocketAddress grupo = new InetSocketAddress(ia , 4320);
			NetworkInterface ni = NetworkInterface.getByInetAddress(ia);
			socket.joinGroup(grupo,ni);

			//SUPORTE AO CLIENTE
			if(finalAddress=="2"){
				System.out.println("\n[SERVIDOR] Esperando por mensagem do Cliente...");
			}

			do {
				byte[] buffer = new byte[1024];						
				DatagramPacket packet=new DatagramPacket(buffer,buffer.length);
				socket.receive(packet);
				msg = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
				if(msg.startsWith("[CLIENT-CONNECT]")){
					String nomeCliente = msg.split("\\.")[1];
					lstClientes.add(nomeCliente);
					System.out.println("\nCLIENTES NOS AVISOS GERAIS: " + lstClientes);
				}else{
					System.out.println("\n[SERVIDOR] Mensagem recebida do cliente: "+msg); 
				}
			}while(!msg.contains("Servidor Encerrado!"));
	
			System.out.println("[Servidor] Conexao Encerrada!");
			socket.leaveGroup(grupo, ni);
			socket.close();
			
		 } catch (IOException e) {
				e.printStackTrace();
		 }
	}
	
	
	
   public static void main(String[] args) throws IOException {
	   String data = " ";
	   String topico = " ";
	   String mensagem = " ";
	   byte[] envio = new byte[1024];
	   Scanner sc = new Scanner(System.in);
	   int escolha;
	   
	   MulticastSocket socket = new MulticastSocket();
	   
	   Thread a1 = new Thread(new ServerMulticast("2"));
	   a1.start();
	   Thread a2 = new Thread(new ServerMulticast("4"));
	   a2.start();
	   
	   while(!mensagem.equals("Servidor Encerrado!")){
		   
		   System.out.print("[Servidor] Selecione o tópico:\n"
		   		+ "1. Avisos gerais\n"
		   		+ "2. Suporte ao Cliente");
		   escolha = Integer.parseInt(sc.nextLine());
		   InetAddress ia = InetAddress.getByName("230.0.0." + escolha);
		   
		   if(escolha == 1) {		   
			   System.out.print("[Servidor] Digite a mensagem:");
			   mensagem = sc.nextLine();
			   if(mensagem.equals("encerrar"))
				   mensagem = "Servidor Encerrado!";
			   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); 
			   LocalDateTime now = LocalDateTime.now();  
			   data = dtf.format(now);
			   
			   envio = ("[" + data + "] " + topico + ": " + mensagem).getBytes();	   
			   
			   
			   DatagramPacket pacote = new DatagramPacket(envio, envio.length,ia, 4321);
			   
			   socket.send(pacote);
		   }else if(escolha == 2) {
			   System.out.print("[Servidor] Digite o nome do recipiente da mensagem:\n");   
			   String nome = sc.nextLine();   
			   
			   System.out.print("[Servidor] Digite a mensagem:");
			   mensagem = sc.nextLine();
			   if(mensagem.equals("encerrar"))
				   mensagem = "Servidor Encerrado!";
			   DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); 
			   LocalDateTime now = LocalDateTime.now();  
			   data = dtf.format(now);
			   
			   topico = "Suporte ao Cliente";
			   envio = ("[" + data + "] " + topico + " para " + nome + " : " + mensagem).getBytes();	   
			   
			   
			   DatagramPacket pacote = new DatagramPacket(envio, envio.length,ia, 4321);
			try {
				socket.send(pacote);

				socket.setSoTimeout(10000);
			} catch (SocketTimeoutException e) {
				System.out.println("Tempo de espera excedido. Desconectando...");
				socket.disconnect();
			}

		   }
		   
		      
		   
		   
		  
	   }

	   System.out.print("[Servidor] Multicast Encerrado");
	   socket.close(); 
   }
}