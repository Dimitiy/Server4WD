package com.java.shiz.connection.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import com.android.shiz.connection.arduino.DeviceListener;
import com.android.shiz.connection.arduino.DeviceObserverThreadServer;
import com.android.shiz.connection.arduino.DeviceThread;
import com.android.util.AppConstants;

public class ClientObserverThreadServer implements Runnable, ClientListener,
		DeviceListener {

	// The server socket.
	private static ServerSocket serverSocket = null;
	// The client socket.
	private static Socket clientSocket = null;
	protected static boolean isStopped = false;
	// ������ ������������� �������
	private final Object lock = new Object();
	private DeviceObserverThreadServer multiDeviceServer;
	// ������ ������ ������������� ����
	public static LinkedList<ClientThread> clientlist = new LinkedList<ClientThread>();

	// ������ ���������� �������
	private static LinkedList<ClientListener> listenerList = new LinkedList<ClientListener>();
	// This chat server can accept up to maxClientsCount clients' connections.

	private static final int maxClientsCount = 10;
	private static final ClientThread[] threads = new ClientThread[maxClientsCount];

	public void connect() {
		/*
		 * Open a server socket on the portNumber (default 2222). Note that we
		 * can not choose a port less than 1023 if we are not privileged users
		 * (root).
		 */
		try {
			callDeviceObserverServer();

			serverSocket = new ServerSocket(AppConstants.TYPE_CLIENT_PORT);
			System.out.println("Waiting for a client...");
			String ip = serverSocket.getInetAddress().getLocalHost()
					.getHostAddress();
			// ����������� �������: ������ �������
			serverForClientStarted(ip, AppConstants.TYPE_CLIENT_PORT);

		} catch (IOException e) {
			System.out.println(e);
			serverForClientStopped();

		}

		/*
		 * Create a client socket for each connection and pass it to a new
		 * client thread.
		 */
		while (!isStopped) {
			try {
				clientSocket = serverSocket.accept();
				System.out.println("Got a client :)");

				int i = 0;
				for (i = 0; i < maxClientsCount; i++) {
					if (threads[i] == null) {
						threads[i] = new ClientThread(clientSocket, threads);
						break;
					}
				}
				if (i == maxClientsCount) {
					PrintStream os = new PrintStream(
							clientSocket.getOutputStream());
					os.println("Server too busy. Try later.");
					os.close();
					clientSocket.close();
				}
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		connect();
	}

	public synchronized static boolean isStopped() {
		return isStopped;
	}

	public synchronized void setFalseStopped() {

		isStopped = false;
	}

	public synchronized void stop() {
		isStopped = true;
		sendClientMessage(null, "END");
		serverForClientStopped();
		try {
			serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	// ***************** �������� ��������� ���� ������������� ****************/
	// sender - �����������

	public void sendClientMessage(ClientThread sender, String message) {
		synchronized (lock) {
			for (ClientThread user : clientlist) {
				try {
					user.out.writeUTF(message);
					user.out.flush();
				} catch (Exception ex) {
				}
			}
		}
	}

	/******************** ����������/�������� ���������� ********************/

	// ��������� ��������� ������� �������
	public void addListener(ClientListener listener) {
		synchronized (lock) {

			listenerList.add(listener);
		}
	}

	// ������� ���������
	public void removeListener(ClientListener listener) {
		synchronized (lock) {
			listenerList.remove(listener);
		}
	}

	/******************** ������ ���������� ServerListener *******************/

	public void serverForClientStarted(String ip, int port) {
		synchronized (lock) {
			for (ClientListener listener : listenerList) {
				listener.serverForClientStarted(ip, port);
			}
		}
	}

	public void serverForClientStopped() {
		synchronized (lock) {
			for (ClientListener listener : listenerList) {
				listener.serverForClientStopped();
			}
		}
	}

	public void onClientConnected(ClientThread user) {
		synchronized (lock) {
			for (ClientListener listener : listenerList) {
				listener.onClientConnected(user);
			}
		}
	}

	public void onClientDisconnected(ClientThread user) {
		synchronized (lock) {
			for (ClientListener listener : listenerList) {
				listener.onClientDisconnected(user);
			}
		}
	}

	public void onClientMessageReceived(ClientThread user, String message) {
		synchronized (lock) {
			for (ClientListener listener : listenerList) {
				listener.onClientMessageReceived(user, message);
			}
		}
	}

	public void callDeviceObserverServer() {
		multiDeviceServer = new DeviceObserverThreadServer();
		multiDeviceServer.addListener(this);

	}

	@Override
	public void serverForDeviceStarted(String ip, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public void serverForDeviceStopped() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceConnected(DeviceThread device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceDisconnected(DeviceThread device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceMessageReceived(DeviceThread device, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceMessageReceivedForClient(DeviceThread device,
			String message) {
		// TODO Auto-generated method stub
		System.out.println("onDeviceMessageReceivedForClient in clientobserver " + message);
		sendClientMessage(null, message);

	}

	@Override
	public void onClientMessageReceivedForDevice(ClientThread user,
			String message) {
		// TODO Auto-generated method stub
		for (ClientListener listener : listenerList) {
			listener.onClientMessageReceivedForDevice(user, message);
		
	}
	}
}