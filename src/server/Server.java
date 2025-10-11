/*
Multithreaded server to run 24/7 on a computer. When a client connects, the server executes
a ClientProcessor object on an available thread, passing the socket connection to it to 
continue listening to other connection attempts.

CONFIGURATION:
Change PORT_NUMBER to desired port that server will listen on. Forward that port on your
home router.

TODO:
Utilize numClients to better see how many client connections to server at any given time.

Look into requiring client certificates.
*/

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import java.io.IOException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server
{
	private final int PORT_NUMBER = 12345;			// port number server listens to
	
	private SSLServerSocketFactory factory = null;	// creates SSLServerSocket object
	private SSLServerSocket serverSocket = null;	// holds socket connection to client
	private int numClients = 0;						// UTILIZE THIS BETTER
	
	private int coreCount = 0;						// number of cores on computer
	private ExecutorService executorService = null;	// to execute ClientProcessors on other threads
	
	// starts server on port global variable, returns true/false whether server started without issue
	private boolean startServer()
	{
		boolean serverStarted = false;
		
		try
		{
			// create SSLServerSocket, start server
			factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			serverSocket = (SSLServerSocket) factory.createServerSocket(PORT_NUMBER);
			System.out.println("Server started.");
			
			serverSocket.setNeedClientAuth(false);
			serverStarted = true;
		}
		catch(IOException ioe)
		{
			System.out.println("Server can't be created on specified port. Quitting.");
		}
		return serverStarted;
	}
	
	/* waits for a client to connect
	once a client connects, returns the socket connection between the client */
	private SSLSocket awaitClient()
	{
		SSLSocket clientSocket = null;
		
		try
		{
			System.out.println("Waiting for a client...\n");
			clientSocket = (SSLSocket) serverSocket.accept();
			
			System.out.println("Client accepted.");
			numClients++;
		}
		catch(IOException ioe)
		{
			System.out.println("Cannot accept client request.");
			ioe.printStackTrace();
		}
		return clientSocket;
	}
	
	// passes off socket connection to ClientProcessor object to execute on other thread
	private void processClient(SSLSocket clientSocket)
	{
		System.out.println("Processing client...");
		executorService.execute(new ClientProcessor(clientSocket));
	}
	
	// listens forever on port number global variable
	private void serverMainProcess()
	{
		//System.setProperty("javax.net.ssl.keyStore", "serverkeystore.jks");
		//System.setProperty("javax.net.ssl.keyStorePassword", "password");
		
		// get number of cores for maximum number of simultaneously connected clients
		coreCount = Runtime.getRuntime().availableProcessors();
		executorService = Executors.newFixedThreadPool(coreCount);
		
		boolean serverStarted = startServer();
		if(serverStarted)
		{
			while(true)
			{
				SSLSocket clientSocket = awaitClient();
				if(clientSocket != null)
				{
					processClient(clientSocket);
				}
			}
		}
	}
	
	// driver
	public static void main(String[] args)
	{
		// create object and use it to call main process
		Server serverObj = new Server();
		serverObj.serverMainProcess();
	}
}