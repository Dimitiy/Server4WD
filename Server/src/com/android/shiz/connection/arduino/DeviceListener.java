package com.android.shiz.connection.arduino;


public interface DeviceListener {
	/****** ������� *******/
	 
    // ������ ����������
    public void serverForDeviceStarted(String ip, int port);
 
    // ������ ��������� ������
    public void serverForDeviceStopped();
 
    // ����������� ����� ������������
    public void onDeviceConnected(DeviceThread device);
 
    // ������������ ����������
    public void onDeviceDisconnected(DeviceThread device);
 
    // �������� ��������� �� ������������
    public void onDeviceMessageReceived(DeviceThread device, String message);
    
    // �������� ��������� �� ������������
    public void onDeviceMessageReceivedForClient(DeviceThread device, String message);
    
}
