package com.android.shiz.connection.arduino;


public interface DeviceListener {
	/****** События *******/
	 
    // Сервер запустился
    public void serverForDeviceStarted(String ip, int port);
 
    // Сервер прекратил работу
    public void serverForDeviceStopped();
 
    // Подключился новый пользователь
    public void onDeviceConnected(DeviceThread device);
 
    // Пользователь отключился
    public void onDeviceDisconnected(DeviceThread device);
 
    // Получено сообщение от пользователя
    public void onDeviceMessageReceived(DeviceThread device, String message);
    
    // Получено сообщение от пользователя
    public void onDeviceMessageReceivedForClient(DeviceThread device, String message);
    
}
