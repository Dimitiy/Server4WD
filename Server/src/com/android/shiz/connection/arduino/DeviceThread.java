package com.android.shiz.connection.arduino;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.java.shiz.connection.client.ClientObserverThreadServer;

/**
 * This class is designed for create arduino device thread and send information
 * data to client
 **/
public class DeviceThread extends Thread {
	private Socket clientSocket = null;
	private final DeviceThread[] threads;
	private int maxClientsCount;
	private String status;
	private DeviceObserverThreadServer multiDeviceServer;
	private InputStream sin;
	private OutputStream sout;
	// IP ������������
	public String userIp = "";
	public DataInputStream in = null;
	public DataOutputStream out = null;
	private ClientObserverThreadServer multiClientThread;

	public DeviceThread(Socket clientSocket, DeviceThread[] threads) {
		this.clientSocket = clientSocket;
		this.threads = threads;
		maxClientsCount = threads.length;
		multiDeviceServer = new DeviceObserverThreadServer();
		// ����� ������� � �������� ������ ������, ������ �����
		// �������� �
		// �������� ������ �������.
		try {
			sin = clientSocket.getInputStream();
			sout = clientSocket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ������������ ������ � ������ ���, ���� ����� ������������
		// ��������� ���������.
		in = new DataInputStream(sin);
		out = new DataOutputStream(sout);
		start();
	}

	public void run() {
		int maxClientsCount = this.maxClientsCount;
		DeviceThread[] threads = this.threads;
		try {
			// IP ������������
			userIp = clientSocket.getInetAddress().getHostAddress();
			// ����������� � ����������� ������ ������������
			multiDeviceServer.onDeviceConnected(this);
			// �������� ������������ � ������ �������������
			multiDeviceServer.clientlist.add(this);
			// ���������� ���� ���������
			multiDeviceServer.sendMessageDevice(null,
					"���������� �������������� ����������: " + userIp);
			while (true) {

				status = in.readUTF();
				if (status == null) {
					// ���������� ��������� ������, ������������ ���������� ��
					// �������
					close();
					// ������������� ����������� ����
					break;
				} else if (!status.isEmpty()) {
					// �����������: �������� ���������
					if (String.valueOf(status).equals("END")) {
						System.out.println("Get end : " + status);
						close();
					} else {
						multiDeviceServer.onDeviceMessageReceivedForClient(
								this, status);
						multiDeviceServer.sendMessageDevice(null, status);
						System.out.println("this line ForClient: " + status);
						System.out
								.println("Sending this line to the server...");
					}

				}

				// -----check isStopped and send quit---------
				if (ClientObserverThreadServer.isStopped()) {
					System.out.println("quit");

					out.writeUTF("quit");
					out.flush();
					close();
				}
				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] != null && threads[i] != this) {
						threads[i].out.writeUTF("*** A new user " + status
								+ " entered the command post !!! ***");
					}
				}

				/*
				 * Clean up. Set the current thread variable to null so that a
				 * new client could be accepted by the server.
				 */

				for (int i = 0; i < maxClientsCount; i++) {
					if (threads[i] == this) {
						threads[i] = null;
					}
				}
			}
		} catch (IOException e) {
		}
	}

	public void close() {
		System.out.println("I'm close...");

		try {
			// �����������: ������������ ����������
			multiDeviceServer.onDeviceDisconnected(this);
			// ���������� ���� ���������
			multiDeviceServer.sendMessageDevice(null,
					"��������� �������������� ����������: " + userIp);
			// ������� ������������ �� ������ ������
			multiDeviceServer.clientlist.remove(this);
			in.close();
			out.close();
			clientSocket.close();
			if (clientSocket != null)
				clientSocket.close();
		} catch (IOException e) {
			// TODO ������������� ��������� ���� catch
			e.printStackTrace();
		}
	}

}