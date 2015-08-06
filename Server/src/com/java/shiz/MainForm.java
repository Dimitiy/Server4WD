package com.java.shiz;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;

import com.android.shiz.connection.arduino.DeviceListener;
import com.android.shiz.connection.arduino.DeviceThread;
import com.android.shiz.connection.arduino.DeviceObserverThreadServer;
import com.java.shiz.connection.client.ClientThread;
import com.java.shiz.connection.client.ClientObserverThreadServer;
import com.java.shiz.connection.client.ClientListener;

public class MainForm extends JFrame implements ClientListener, DeviceListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DeviceObserverThreadServer multiDeviceServer = null;
	private ClientObserverThreadServer multiThread = null;

	private JTextArea textArea;
	static InetAddress ip;
	final int portDevice = 10082;
	final int portClient = 10083;
	// static String[] arg;
	private JToggleButton toggleObserver;
	private JTextPane textPane;
	private JFrame frame;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JTextArea textAreaData;
	private static String message = "";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// arg = args;

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					MainForm window = new MainForm();
					window.frame.setVisible(true);
					window.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					try {

						ip = InetAddress.getLocalHost();
						System.out.println("Current IP address : "
								+ ip.getHostAddress());

					} catch (UnknownHostException e) {

						e.printStackTrace();

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainForm() {
		initialize();

	}

	public void callDeviceObserverServer() {
		multiDeviceServer = new DeviceObserverThreadServer();
		multiDeviceServer.addListener(this);
		multiDeviceServer.setFalseStopped();
		new Thread(multiDeviceServer).start();
	}

	private void callClientObserverServer() {
		multiThread = new ClientObserverThreadServer();
		multiThread.addListener(this);
		multiThread.setFalseStopped();

		new Thread(multiThread).start();
	}

	private void stopObserverServer() {
		if (multiThread != null)
			multiThread.stop();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(0, 153, 255));
		frame.setBackground(new Color(0, 153, 255));
		frame.getContentPane().setForeground(new Color(0, 153, 255));
		frame.getContentPane().setFont(
				new Font("Segoe UI Emoji", Font.PLAIN, 13));
		frame.setForeground(new Color(0, 153, 255));
		frame.setFont(new Font("Segoe UI Light", Font.PLAIN, 14));
		frame.setIconImage(Toolkit
				.getDefaultToolkit()
				.getImage(
						"C:\\Users\\\u0414\u043C\u0438\u0442\u0440\u0438\u0439\\workspace\\Server4WD\\cosm.png"));
		frame.setTitle("Пульт управления (стационарный)");
		frame.setBounds(100, 100, 1007, 422);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		System.out.println("test");

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 186, 0, 198, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		frame.getContentPane().setLayout(gridBagLayout);

		final JToggleButton toggleButton = new JToggleButton(
				"Подключиться к БЛА");
		toggleButton.setActionCommand("Подключиться к БЛА");
		toggleButton.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));
		GridBagConstraints gbc_toggleButton = new GridBagConstraints();
		gbc_toggleButton.insets = new Insets(0, 0, 5, 5);
		gbc_toggleButton.gridx = 0;
		gbc_toggleButton.gridy = 0;
		frame.getContentPane().add(toggleButton, gbc_toggleButton);
		toggleButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					toggleButton.setText("Разъединить");
					callDeviceObserverServer();
					// button.setEnabled(true);
					// button_1.setEnabled(true);
					// button_2.setEnabled(true);
					// button_3.setEnabled(true);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					toggleButton.setText("Подключиться к БЛА");
					// button.setEnabled(false);
					// button_1.setEnabled(false);
					// button_2.setEnabled(false);
					// button_3.setEnabled(false);
					multiDeviceServer.stop();
				}
			}
		});

		toggleObserver = new JToggleButton("Открыть порт вещания!");
		GridBagConstraints gbc_tglbtnNewToggleButton = new GridBagConstraints();
		gbc_tglbtnNewToggleButton.insets = new Insets(0, 0, 5, 5);
		gbc_tglbtnNewToggleButton.gridx = 2;
		gbc_tglbtnNewToggleButton.gridy = 0;
		toggleObserver.setFont(new Font("Segoe UI Light", Font.PLAIN, 12));

		frame.getContentPane().add(toggleObserver, gbc_tglbtnNewToggleButton);
		toggleObserver.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					toggleObserver.setText("Закрыть порт вещания!");
					// callConnect(portClient);
					callClientObserverServer();
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					toggleObserver.setText("Открыть порт вещания!");
					stopObserverServer();
					// stopObserverServer();
					// startServer.endConnect();
				}
			}
		});

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setAutoscrolls(true);
		JScrollBar vertical = scrollPane_1.getVerticalScrollBar();
		vertical.setValue(30);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridheight = 6;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 3;
		gbc_scrollPane_1.gridy = 0;
		frame.getContentPane().add(scrollPane_1, gbc_scrollPane_1);

		textAreaData = new JTextArea();
		textAreaData.setRows(50);
		textAreaData.setLineWrap(true);
		textAreaData.setEditable(false);
		textAreaData.setForeground(Color.WHITE);
		textAreaData.setFont(new Font("Segoe UI Light", Font.PLAIN, 13));
		textAreaData.setBackground(new Color(0, 153, 255));
		scrollPane_1.setViewportView(textAreaData);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 5;
		gbc_scrollPane.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);

		textArea = new JTextArea();

		scrollPane.setViewportView(textArea);
		textArea.setForeground(Color.WHITE);
		textArea.setEditable(false);
		textArea.setRows(10);

		textArea.setFont(new Font("Segoe UI Light", Font.PLAIN, 13));
		textArea.setBackground(new Color(0, 153, 255));
		frame.setVisible(true);

	}

	public void setCoordinates(String coordinates) {
		System.out.println("Coodrinates : " + coordinates);

		// textCoordinates.setText(coordinates);
	}

	/******************** методы интерфейса ServerListener *******************/

	public void serverForClientStarted(String ip, int port) {
		// Отображаем в компоненте
		textArea.append("\nСервер для клиентов запущен по адресу: " + ip
				+ " порт: " + port);
	}

	public void serverForClientStopped() {
		// Отображаем в компоненте
		textArea.append("\nCервер для клиентов остановлен ");
	}

	public void onClientConnected(ClientThread client) {
		// Отображаем в компоненте
		textArea.append("\nПодключен новый пользователь: " + client.userIp);
		textAreaData.append("\nПодключен новый пользователь: " + client.userIp);

	}

	public void onClientDisconnected(ClientThread client) {
		// Отображаем в компоненте
		textArea.append("\nПользователь отключился: " + client.userIp);
	}

	public void onClientMessageReceived(ClientThread client, String message) {
		// Отображаем в компоненте новое сообщение
		// if (!MainForm.message.equals(message)) {
		// textAreaData.append("\n<" + client.userIp + "> " + message);
		// MainForm.message = message;
		// }
		// Пишем данные в файл...
	}

	/******************** методы интерфейса DeviceListener *******************/

	public void serverForDeviceStarted(String ip, int port) {
		// Отображаем в компоненте
		textArea.append("\nСервер для исполнительных устройств запущен по адресу: "
				+ ip + " порт: " + port);

	}

	public void serverForDeviceStopped() {
		// Отображаем в компоненте
		textArea.append("\nCервер для исполнительных устройств остановлен ");
	}

	@Override
	public void onDeviceConnected(DeviceThread device) {
		// TODO Auto-generated method stub
		new DeviceFrame(device.userIp, this);
		textArea.append("\nПодключено новое устройство: " + device.userIp);
	}

	@Override
	public void onDeviceDisconnected(DeviceThread device) {
		// TODO Auto-generated method stub
		textArea.append("\nОтключено устройство: " + device.userIp);
	}

	@Override
	public void onDeviceMessageReceived(DeviceThread device, String message) {
		// TODO Auto-generated method stub
		if (!MainForm.message.equals(message)) {
			textAreaData.append("\n<" + device.userIp + "> " + message);
			MainForm.message = message;
		}

	}

	@Override
	public void onDeviceMessageReceivedForClient(DeviceThread device,
			String message) {
		// TODO Auto-generated method stub
		textAreaData.append("\n<" + device.userIp + "> " + message);
		MainForm.message = message;
	}

	@Override
	public void onClientMessageReceivedForDevice(ClientThread client,
			String message) {
		// TODO Auto-generated method stub
		if (!MainForm.message.equals(message)) {
			textAreaData.append("\n<" + client.userIp + "> " + message);
			MainForm.message = message;
		}
	}

}
