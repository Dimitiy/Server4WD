package com.java.shiz.connection.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This class is designed for create client thread and send data to device
 **/
public class ClientThread extends Thread {

	private Socket clientSocket = null;
	private final ClientThread[] threads;
	private int maxClientsCount;
	String status;
	ClientObserverThreadServer multiServer;
	InputStream sin;
	OutputStream sout;
	// IP ������������
	public String userIp = "";
	public DataInputStream in = null;
	public DataOutputStream out = null;

	public ClientThread(Socket clientSocket, ClientThread[] threads) {
		this.clientSocket = clientSocket;
		this.threads = threads;
		maxClientsCount = threads.length;
		multiServer = new ClientObserverThreadServer();
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
		ClientThread[] threads = this.threads;

		try {
			// IP ������������
			userIp = clientSocket.getInetAddress().getHostAddress();
			// ����������� � ����������� ������ ������������
			multiServer.onClientConnected(this);
			// �������� ������������ � ������ �������������
			multiServer.clientlist.add(this);
			// ���������� ���� ���������
			multiServer.sendClientMessage(null, "��������� ������������: "
					+ userIp);

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
						
						System.out.println("this line ForDevice: " + status);
						multiServer.onClientMessageReceivedForDevice(this,
								status);
						
						
					}

				}

				// out.writeUTF("received"); // �������� ��������� ������ ������
				// // �������.
				// out.flush();

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
								+ " entered the chat room !!! ***");
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
			multiServer.onClientDisconnected(this);
			// ���������� ���� ���������
			multiServer.sendClientMessage(null, "�������� ������������: "
					+ userIp);
			// ������� ������������ �� ������ ������
			multiServer.clientlist.remove(this);
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