package com.java.shiz.connection.client;

public interface ClientListener {

	/****** События *******/

	// Сервер запустился
	public void serverForClientStarted(String ip, int port);

	// Сервер прекратил работу
	public void serverForClientStopped();

	// Подключился новый пользователь
	public void onClientConnected(ClientThread user);

	// Пользователь отключился
	public void onClientDisconnected(ClientThread user);

	// Получено сообщение от пользователя
	public void onClientMessageReceived(ClientThread user, String message);

	// Получено сообщение от пользователя
	public void onClientMessageReceivedForDevice(ClientThread user,
			String message);

}