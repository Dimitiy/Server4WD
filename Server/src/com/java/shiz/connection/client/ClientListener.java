package com.java.shiz.connection.client;

public interface ClientListener {

	/****** ������� *******/

	// ������ ����������
	public void serverForClientStarted(String ip, int port);

	// ������ ��������� ������
	public void serverForClientStopped();

	// ����������� ����� ������������
	public void onClientConnected(ClientThread user);

	// ������������ ����������
	public void onClientDisconnected(ClientThread user);

	// �������� ��������� �� ������������
	public void onClientMessageReceived(ClientThread user, String message);

	// �������� ��������� �� ������������
	public void onClientMessageReceivedForDevice(ClientThread user,
			String message);

}