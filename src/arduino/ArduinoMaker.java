package arduino;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import processing.core.PApplet;
import arduino.MakerUI.SpecListener;
import cc.arduino.Arduino;

public class ArduinoMaker
{
	private MakerUI ui = new MakerUI();
	
	ArduinoBoard board;
	String port;
	Integer[] pins;
	
	Arduino arduino;
	
	public ArduinoMaker(final PApplet applet)
	{
//		ui.refresh();
		ui.getGenerateButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				//SpecListener spec = MakerUI.SpecListener.getInstance();
				port = MakerUI.SpecListener.getInstance().getPort();
				board = MakerUI.SpecListener.getInstance().getBoardSpec();
				pins = MakerUI.SpecListener.getInstance().getConfigPin();
								
				
				ui.exit();

				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						arduino = make(applet);
					}
				});
				
				thread.start();
			}	
		});
				
	}
	
//	public String getPort() {return port;}
	
	
		
	public Arduino make(PApplet applet)
	{	
		if(board == null) 
			return null;
		
		final int BAUD_RATE = 57600;
		
		System.out.println("Creating Arduino with the following specification: ");
		System.out.println("Board: " + board);
		System.out.println("Port : " + port);
		System.out.println("Baud Rate: " + BAUD_RATE);
		//System.out.println("Pin(s) : " + pins.toString().toString());
		
		try {
			arduino = new Arduino(applet, port, BAUD_RATE);
			testBoard(arduino);	
		} catch(Exception e) {
			System.out.println(arduino);
			JOptionPane.showMessageDialog(null, "Cannot generate arduino", "ERROR", JOptionPane.ERROR_MESSAGE);
			ui.setVisible(true);
		}
		
		return arduino;
	}

	private void testBoard(Arduino arduino) {
		for(Integer pin : pins)
		{
			System.out.print("Setting pin : " + pin);
			arduino.pinMode(pin, Arduino.OUTPUT);
			System.out.println(" OK");
			
			System.out.println("test writing : " + pin);
			arduino.digitalWrite(pin, Arduino.HIGH);
			try {
				Thread.sleep(70);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			arduino.digitalWrite(pin, Arduino.LOW);
		}
		System.out.println("DONE!\n");
	}
	
	public void showUI() {
		ui.setVisible(true);
	}
}



class MakerUI extends JFrame implements WindowListener {
	
	private static JPanel pinPanel = new JPanel(new CardLayout());	
//	private JPanel specPanel = getSpecPanel();
	
//	private static String[] portList = Arduino.list();
	
	private static JComboBox<String> portBox = new JComboBox<String>(Arduino.list());
	
	private static JButton okBt = new JButton("Generate");
	//private JButton cancelBt = new JButton("Cancel");
	private JButton refreshBt = new JButton("Refresh");
		
	public MakerUI() {
		generateUI();
		this.addWindowListener(this);
	}

	JButton getGenerateButton() {
		return okBt;
	}
	
//	JButton getCancelButton() {
//		return cancelBt;
//	}
	
	private void generateUI()
	{
		this.setTitle("Generate Arduino");
		this.setSize(400, 200);
		this.setResizable(false);
		//this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.getContentPane().add(getSpecPanel(), BorderLayout.NORTH);
		
		for(ArduinoBoard board : ArduinoBoard.values())
		{
			JPanel panel = getPinPanel(board);
			pinPanel.add(getPinPanel(board), board.name());
		}
		
		this.getContentPane().add(pinPanel, BorderLayout.CENTER);
		this.getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
	}
	
	private JPanel getSpecPanel()
	{
		JPanel spec = new JPanel();
				
		JComboBox<ArduinoBoard> boardSelect = new JComboBox<ArduinoBoard>();
		for(ArduinoBoard board : ArduinoBoard.values())
			boardSelect.addItem(board);
		
		boardSelect.addItemListener(SpecListener.getInstance());
		portBox.addItemListener(SpecListener.getInstance());
		
		spec.add(new JLabel("Arduino board"));
		spec.add(boardSelect);
		spec.add(new JLabel("COM Port(s)"));
		spec.add(portBox);
		
		return spec;
	}
	
	private JPanel getPinPanel(ArduinoBoard board)
	{
		JPanel pinPanel = new JPanel(new GridLayout(0, 4));
		
		for(Integer pin : board.getPins())
		{
			PinCheckBox checkBox = new PinCheckBox(pin, "PIN " + pin.toString());
			checkBox.setSelected(true);
			checkBox.addItemListener(SpecListener.getInstance());
			pinPanel.add(checkBox);
		}
		
		return pinPanel;
	}
	
	private static void showPinPanel(ArduinoBoard board)
	{
		CardLayout layout = (CardLayout)pinPanel.getLayout();
		layout.show(pinPanel, board.name());
	}
	
	private JPanel getButtonPanel()
	{
		JPanel panel = new JPanel();
		//okBt.addActionListener(new ButtonListener());
		refreshBt.addActionListener(new ButtonListener());
		panel.add(okBt);
//		panel.add(cancelBt);
		panel.add(refreshBt);
		return panel;
	}
	
	public void setVisible(boolean show) {
		refresh();
		super.setVisible(show);
	}
	
	public void exit() {
		this.dispose();
	}
		
	static void refresh() {
		//SpecListener.getInstance().refresh();
		String[] ports = Arduino.list();
		if(ports.length > 0) {
			okBt.setEnabled(true);
			portBox.removeAllItems();
			for(String port : ports)
				portBox.addItem(port);
		}
		else {
			okBt.setEnabled(false);
			portBox.removeAllItems();
		}
	}
	
	static class SpecListener implements ItemListener
	{
		private ArduinoBoard board = ArduinoBoard.UNO;
		private String comPort = "";
		private Set<Integer> pins = new HashSet<Integer>();
				
		private static SpecListener listener = null;
		
		private SpecListener() {
			for(Integer pin : board.getPins())
				pins.add(pin);
			
			//setPort();
			refresh();
		}
		
//		private void setPort()
//		{
//			if(Arduino.list().length > 0) {
//				comPort = Arduino.list()[0];
//				MakerUI.okBt.setEnabled(true);
//			}
//			else {
//				MakerUI.okBt.setEnabled(false);
//				comPort = "";
//			}
//		}
		
		public static SpecListener getInstance()
		{
			if(listener == null)
				listener = new SpecListener();
			return listener;
		}
						
		@Override
		public void itemStateChanged(ItemEvent event) {			
			boolean isSelected = event.getStateChange() == ItemEvent.SELECTED;
			//System.out.println(event);
			
			if(event.getSource() instanceof JComboBox && isSelected)
			{
//				portList = Arduino.list();
				JComboBox box = (JComboBox)event.getSource();
				
				if(box.getSelectedItem() instanceof ArduinoBoard)
				{
					board = (ArduinoBoard)box.getSelectedItem();
					pins.clear();
					for(Integer pin : board.getPins())
						pins.add(pin);
					
					CardLayout layout = (CardLayout)pinPanel.getLayout();
					layout.show(pinPanel, board.name());
				}
				
				else
				{
					comPort = Arduino.list()[box.getSelectedIndex()];
					System.out.println(comPort);
				}
				
			}
			
			else if(event.getSource() instanceof PinCheckBox)
			{
				PinCheckBox checkBox = (PinCheckBox)event.getSource();
				
				if(isSelected)
					pins.add(checkBox.getPin());
				else
				{
					pins.remove(checkBox.getPin());
					if(pins.isEmpty())
						MakerUI.okBt.setEnabled(false);
				}
								
				System.out.println(pins.toString());
			}
			
			if(!comPort.isEmpty() && !pins.isEmpty())
				MakerUI.okBt.setEnabled(true);

		}
		
		public String toString() {
			return String.format("Board: %s Selected pins: %s PORT: %s", board.name(), pins.toString(), comPort);
		}
		
//		public void refresh() {
//			setPort();
//		}
		
		public String getPort() {return comPort;}
		public Integer[] getConfigPin() {return pins.toArray(new Integer[pins.size()]);}
		public ArduinoBoard getBoardSpec() {return board;}
	}
	
	// TODO
	class ButtonListener implements ActionListener
	{
		SpecListener listener = SpecListener.getInstance();
		@Override
		public void actionPerformed(ActionEvent event) {
			JButton bt = (JButton)event.getSource();	
//			if(bt == cancelBt)
//				exit();
			if(bt == refreshBt)
			{
//				portBox.removeAllItems();
//				for(String portName : Arduino.list())
//					portBox.addItem(portName);
//				window.revalidate();
//				SpecListener.getInstance().refresh();
				MakerUI.refresh();
			}	
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
	
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.dispose();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}

class PinCheckBox extends JCheckBox
{
	private final int PIN;
	public PinCheckBox(int pin, String text)
	{
		super(text);
		PIN = pin;
	}
	
	public int getPin() {return PIN;}
}

enum ArduinoBoard {
	UNO(new int[] {3,5,6,9,10,11})/*, MEGA2560(new int[] {2,3,4,5,6,7,8,9,10,11,12,13})*/;
	
	private final int[] pins;
	ArduinoBoard(int[] pins) {
		this.pins = pins;
	}
	
	public int[] getPins() {return pins;}
	
}

