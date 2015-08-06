package com.java.shiz;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import com.android.shiz.connection.arduino.DeviceListener;
import com.android.shiz.connection.arduino.DeviceObserverThreadServer;
import com.android.shiz.connection.arduino.DeviceThread;
import com.android.util.RequestList;

public class DeviceFrame extends JFrame implements DeviceListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton button, button_1, button_2, button_3;
	public JTextPane textCoordinates, textOrientation, textCommand;
	private JFrame frame;
	private JTextPane textPane;
	private String ipDevice;
	private static DeviceObserverThreadServer multiDeviceServer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public DeviceFrame(String ip, MainForm listener) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		ipDevice = ip;
		initial();
	}

	/**
	 * Create the frame.
	 */
	public DeviceFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		initial();
	}

	public void pwMotorLeft() {
		// this.control;
		System.out.println("pwMotorLeft");
		multiDeviceServer.sendMessageDevice(null,
				RequestList.sendCommandRequest("2"));

	}

	public void motorLeft() {
		// this.control;
		System.out.println("MotorLeft");
		multiDeviceServer.sendMessageDevice(null,
				RequestList.sendCommandRequest("0"));

	}

	public void pwMotorRight() {
		// this.control;
		System.out.println("pwMotorRight");
		multiDeviceServer.sendMessageDevice(null,
				RequestList.sendCommandRequest("3"));

	}

	public void motorRight() {
		// this.control;
		System.out.println("MotorRight");
		multiDeviceServer.sendMessageDevice(null,
				RequestList.sendCommandRequest("0"));

	}

	public void pwMotorForward() {
		// this.control;
		System.out.println("pwMotorForward");
		multiDeviceServer.sendMessageDevice(null,
				RequestList.sendCommandRequest("1"));

	}

	public void motorForward() {
		// this.control;
		System.out.println("MotorForward");
		multiDeviceServer.sendMessageDevice(null,
				RequestList.sendCommandRequest("0"));

	}

	public void pwMotorBack() {
		// this.control;
		System.out.println("pwMotorBack");
		multiDeviceServer.sendMessageDevice(null,
				RequestList.sendCommandRequest("4"));

	}

	public void motorBack() {
		// this.control;
		System.out.println("MotorBack");
		multiDeviceServer.sendMessageDevice(null,
				RequestList.sendCommandRequest("0"));

	}

	private class MyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent evt) {
			if (evt.getID() == KeyEvent.KEY_PRESSED) {
				int keyCode = evt.getKeyCode();
				multiDeviceServer.addListener(DeviceFrame.this);

				if (keyCode == KeyEvent.VK_LEFT)
					pwMotorLeft();
				else if (keyCode == KeyEvent.VK_RIGHT)
					pwMotorRight();
				else if (keyCode == KeyEvent.VK_UP)
					pwMotorForward();
				else if (keyCode == KeyEvent.VK_DOWN)
					pwMotorBack();
			} else if (evt.getID() == KeyEvent.KEY_RELEASED) {
				int keyCode = evt.getKeyCode();

				if (keyCode == KeyEvent.VK_LEFT)
					motorLeft();
				else if (keyCode == KeyEvent.VK_RIGHT)
					motorRight();
				else if (keyCode == KeyEvent.VK_UP)
					motorForward();
				else if (keyCode == KeyEvent.VK_DOWN)
					motorBack();
			} else if (evt.getID() == KeyEvent.KEY_TYPED) {
				System.out.println("3test3");
			}
			return false;
		}
	}

	@Override
	public void serverForDeviceStarted(String ip, int port) {
		// TODO Auto-generated method stub

	}

	private void initial() {
		frame = new JFrame();
		frame.setVisible(true);
		
		multiDeviceServer = new DeviceObserverThreadServer();
		frame.getContentPane().setBackground(new Color(0, 153, 255));
		frame.setBackground(new Color(0, 153, 255));
		frame.getContentPane().setForeground(new Color(0, 153, 255));
		frame.getContentPane().setFont(
				new Font("Segoe UI Emoji", Font.PLAIN, 13));
		frame.setForeground(new Color(0, 153, 255));
		frame.setFont(new Font("Segoe UI Light", Font.PLAIN, 14));
		frame.setTitle("Управление устройством");
		frame.setBounds(100, 100, 282, 270);
		
		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());

		button_1 = new JButton("Вперед");
		button_1.setBounds(87, 0, 89, 23);
		button_1.setFont(new Font("Segoe UI Light", Font.PLAIN, 13));
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("FormMain Forward");
				System.out.println();
				motorForward();
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(button_1);

		button = new JButton("Влево");
		button.setFont(new Font("Segoe UI Light", Font.PLAIN, 13));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("FormMain Left");
				System.out.println();
				pwMotorLeft();
			}
		});

		button.setBounds(0, 34, 89, 23);
		frame.getContentPane().add(button);

		button_3 = new JButton("Вправо");
		button_3.setFont(new Font("Segoe UI Light", Font.PLAIN, 13));
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("FormMain Right");
				System.out.println();
				motorRight();
			}
		});
		button_3.setBounds(176, 34, 89, 23);
		frame.getContentPane().add(button_3);

		button_2 = new JButton("Назад");
		button_2.setFont(new Font("Segoe UI Light", Font.PLAIN, 13));
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("FormMain Back");
				System.out.println();
				motorBack();
			}
		});
		button_2.setBounds(87, 68, 89, 23);
		frame.getContentPane().add(button_2);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(0, 114, 265, 23);
		frame.getContentPane().add(textArea);

		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBounds(0, 161, 265, 23);
		frame.getContentPane().add(textArea_1);

		JTextArea textArea_2 = new JTextArea();
		textArea_2.setBounds(0, 207, 265, 23);
		frame.getContentPane().add(textArea_2);

		textCoordinates = new JTextPane();
		textCoordinates.setBounds(0, 91, 265, 23);
		textCoordinates.setForeground(new Color(255, 255, 255));
		textCoordinates.setFont(new Font("Segoe UI Light", Font.PLAIN, 13));
		textCoordinates.setBackground(new Color(0, 153, 255));
		textCoordinates.setText("Данные о местоположении:");

		frame.getContentPane().add(textCoordinates);

		textOrientation = new JTextPane();
		textOrientation.setText("Акселерометр / магнитомер");
		textOrientation.setForeground(Color.WHITE);
		textOrientation.setFont(new Font("Segoe UI Light", Font.PLAIN, 13));
		textOrientation.setBackground(new Color(0, 153, 255));
		textOrientation.setBounds(0, 137, 265, 23);
		frame.getContentPane().add(textOrientation);

		textPane = new JTextPane();
		textPane.setText("Управляющие команды");
		textPane.setForeground(Color.WHITE);
		textPane.setFont(new Font("Segoe UI Light", Font.PLAIN, 13));
		textPane.setBackground(new Color(0, 153, 255));
		textPane.setBounds(0, 183, 265, 23);
		frame.getContentPane().add(textPane);
		frame.setVisible(true);
	
	}

	@Override
	public void serverForDeviceStopped() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceConnected(DeviceThread device) {
		// TODO Auto-generated method stub
		System.out.println(" onDeviceConnected : " + ipDevice + " "
				+ device.userIp);
	}

	@Override
	public void onDeviceDisconnected(DeviceThread device) {
		// TODO Auto-generated method stub
		System.out.println("onDeviceDisconnected : " + ipDevice + " "
				+ device.userIp);
		if (device.userIp.equals(ipDevice)) {
			multiDeviceServer.removeListener(this);
			frame.dispose();
			frame.dispatchEvent(new WindowEvent(frame,
					WindowEvent.WINDOW_CLOSING));
		}
	}

	@Override
	public void onDeviceMessageReceived(DeviceThread device, String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceMessageReceivedForClient(DeviceThread device,
			String message) {
		// TODO Auto-generated method stub

	}
}
