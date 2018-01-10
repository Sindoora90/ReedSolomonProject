package mvcview;

import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import rshelper.Logger;

/**
 * Die View-Klasse diese Enthält nur die Präsentation hier sollte man keinerlei
 * Programmlogik finden alle Berechnungen und Reaktionen auf Benutzeraktionen
 * sollten allesammt im Controller stehen
 */
public class RSView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger logger; 
	
	String stuetzString = "stuetz";
	String systemString = "system";

	String bwelchString = "bwelch";
	String bmasseyString = "bmassey";

	ButtonGroup groupcod;
	ButtonGroup groupdec;

	private JPanel panel = new JPanel();
	private JPanel top_panel_titel = new JPanel();
	private JPanel top_panel_gf = new JPanel();
	private JPanel middle_panel_rs = new JPanel();
	private JPanel bottom_panel = new JPanel();

	private JLabel lbl_festlegen = new JLabel("Festlegen der Variablen: ");
	private String[] dropdownListe = { "benutzerdefiniert", "2D Barcodes", "MIDS", "CD", "Voyager"};
	private JComboBox<String> dropdown = new JComboBox<String>(dropdownListe);

	private JLabel lbl_endlk = new JLabel("Endlicher Körper GF(p^m): ");
	private JLabel lbl_p = new JLabel("p = ");
	private JTextField txt_p = new JTextField(3);
	private JLabel lbl_m = new JLabel("m = ");
	private JTextField txt_m = new JTextField(3);

	private JLabel lbl_primelem = new JLabel("primitives Element");
	private JTextField txt_primelem = new JTextField(5);
	private JLabel lbl_irred = new JLabel("irreduzibles Polynom");
	private JTextField txt_irred = new JTextField(5);

	private JLabel lbl_rscode = new JLabel("Reed-Solomon Code RS(n,k,d): ");
	private JLabel lbl_n = new JLabel("n = ");
	private JTextField txt_n = new JTextField(3);
	private JLabel lbl_k = new JLabel("k = ");
	private JTextField txt_k = new JTextField(3);

	private JPanel tab_panel = new JPanel();
	private JTabbedPane tabpane = new JTabbedPane();
	
	// Inhalt von Kodierungstab
	private JPanel panel_tab_kod = new JPanel();
	private JPanel panel_kod = new JPanel();
	private JLabel lbl_kod = new JLabel("Kodierung");
	//private JPanel panel_kod_log = new JPanel();
	
	// Public damit der log überall erzeugt werden kann....
	public JTextArea kod_log_textarea = new JTextArea("Zwischenschritte: ", 15,20);
	public JTextArea dek_log_textarea = new JTextArea("Zwischenschritte: ", 15,20);
	

	private JLabel lbl_m_cod = new JLabel("m = ");
	private JTextField txt_m_cod = new JTextField(20);
	private JLabel lbl_m_dec = new JLabel("m = ");
	private JTextField txt_m_dec = new JTextField(20);

	// private JCheckBox check = new JCheckBox("Systematisch");
	private JRadioButton stuetz_radio = new JRadioButton("Stützstellen basiert");
	private JRadioButton system_radio = new JRadioButton(
			"systematische Kodierung");

	private JButton startcod = new JButton("Start Cod");
	private JButton startdec = new JButton("Start Dec");
	private JButton randmessage = new JButton("Random");

	//private JLabel lbl_zwischen = new JLabel("Zwischenschritte: ");
	//private JLabel lbl_zwischen2 = new JLabel("");

	private JLabel lbl_c_cod = new JLabel("c = ");
	private JTextField txt_c_cod = new JTextField(20);
	private JLabel lbl_c_dec = new JLabel("c = ");
	private JTextField txt_c_dec = new JTextField(20);

	// Inhalt von Dekodierungstab
	private JPanel panel_tab_dek = new JPanel();
	private JPanel panel_dek = new JPanel();
	private JLabel lbl_dek = new JLabel("Dekodierung");
	//private JPanel panel_dek_log = new JPanel();

	private JRadioButton bwelch = new JRadioButton(
			"Berlekamp Welch Algorithmus");
	private JRadioButton bmassey = new JRadioButton(
			"Berlekamp Massey Algorithmus");

	public RSView(Logger logger) {
		super("RS Berechnen");
		this.logger = logger; 
		initForm();
		// this.setSize(400,300);
	}

	public void initForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setBounds(100, 100, 1200, 600);
		
		this.setSize(1200,600);
		this.setLocationRelativeTo( null );

		this.getContentPane().add(panel);
		this.setVisible(true);

		Container pane = getContentPane();

		pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		// natural height, maximum width
		c.fill = GridBagConstraints.HORIZONTAL;

		panel.setLayout(new GridLayout(3, 1));
		top_panel_titel.setLayout(new FlowLayout());
		top_panel_gf.setLayout(new FlowLayout());
		middle_panel_rs.setLayout(new FlowLayout());
		bottom_panel.setLayout(new GridBagLayout());
		GridBagConstraints myConstraints = new GridBagConstraints();
		myConstraints.ipadx = 600;
		myConstraints.ipady = 150;

		top_panel_titel.add(lbl_festlegen);
		top_panel_titel.add(dropdown);
		c.weightx = 0.0;
		c.weighty = 1.0;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(top_panel_titel, c);

		top_panel_gf.add(lbl_endlk);
		top_panel_gf.add(lbl_p);
		txt_p.setText("2");
		top_panel_gf.add(txt_p);
		top_panel_gf.add(lbl_m);
		txt_m.setText("3");
		top_panel_gf.add(txt_m);
		top_panel_gf.add(lbl_primelem);
		txt_primelem.setText("2");
		top_panel_gf.add(txt_primelem);
		top_panel_gf.add(lbl_irred);
		txt_irred.setText("13");
		top_panel_gf.add(txt_irred);
		c.weightx = 0.0;
		c.weighty = 1.0;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(top_panel_gf, c);

		middle_panel_rs.add(lbl_rscode);
		middle_panel_rs.add(lbl_n);
		txt_n.setText("7");
		middle_panel_rs.add(txt_n);
		middle_panel_rs.add(lbl_k);
		txt_k.setText("3");
		middle_panel_rs.add(txt_k);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.weightx = 0.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(middle_panel_rs, c);
		
		panel_kod.setLayout(new GridBagLayout());
		GridBagConstraints cc = new GridBagConstraints();
		cc.fill = GridBagConstraints.HORIZONTAL;
		cc.gridwidth = 2;
		cc.weightx = 0.0;
		cc.weighty = 1.0;
		cc.gridx = 0;
		cc.gridy = 0;
		panel_kod.add(lbl_kod, cc);
		cc.gridwidth = 2;
		cc.weightx = 0.0;
		cc.weighty = 1.0;
		cc.gridx = 1;
		cc.gridy = 0;
		panel_kod.add(randmessage, cc);
		cc.gridwidth = 1;
		cc.weightx = 0.0;
		cc.weighty = 1.0;
		cc.gridx = 0;
		cc.gridy = 1;
		panel_kod.add(lbl_m_cod, cc);
		cc.gridwidth = 1;
		cc.weightx = 0.0;
		cc.weighty = 1.0;
		cc.gridx = 1;
		cc.gridy = 1;
		panel_kod.add(txt_m_cod, cc);
		// Group the radio buttons.
		groupcod = new ButtonGroup();
		groupcod.add(stuetz_radio);
		groupcod.add(system_radio);
		cc.gridwidth = 1;
		cc.weightx = 0.0;
		cc.weighty = 1.0;
		cc.gridx = 0;
		cc.gridy = 2;
		stuetz_radio.setSelected(true);
		panel_kod.add(stuetz_radio, cc);
		cc.gridwidth = 1;
		cc.weightx = 0.0;
		cc.weighty = 1.0;
		cc.gridx = 0;
		cc.gridy = 3;
		panel_kod.add(system_radio, cc);
		cc.gridwidth = 1;
		cc.weightx = 0.0;
		cc.weighty = 1.0;
		cc.gridx = 0;
		cc.gridy = 4;
		panel_kod.add(startcod, cc);
		cc.gridwidth = 1;
		cc.weightx = 0.0;
		cc.weighty = 1.0;
		cc.gridx = 0;
		cc.gridy = 5;
		panel_kod.add(lbl_c_cod, cc);
		cc.gridwidth = 1;
		cc.weightx = 0.0;
		cc.weighty = 1.0;
		cc.gridx = 1;
		cc.gridy = 5;
		panel_kod.add(txt_c_cod, cc);

		// panel_dek.setLayout(new FlowLayout());
		// panel_dek.add(lbl_dek);

		panel_dek.setLayout(new GridBagLayout());
		GridBagConstraints cd = new GridBagConstraints();
		cd.fill = GridBagConstraints.HORIZONTAL;
		cd.gridwidth = 2;
		cd.weightx = 0.0;
		cd.weighty = 1.0;
		cd.gridx = 0;
		cd.gridy = 0;
		panel_dek.add(lbl_dek, cd);
		cd.gridwidth = 1;
		cd.weightx = 0.0;
		cd.weighty = 1.0;
		cd.gridx = 0;
		cd.gridy = 1;
		panel_dek.add(lbl_c_dec, cd);
		cd.gridwidth = 1;
		cd.weightx = 0.0;
		cd.weighty = 1.0;
		cd.gridx = 1;
		cd.gridy = 1;
		panel_dek.add(txt_c_dec, cd);
		// Group the radio buttons.
		groupdec = new ButtonGroup();
		groupdec.add(bwelch);
		groupdec.add(bmassey);
		cd.gridwidth = 1;
		cd.weightx = 0.0;
		cd.weighty = 1.0;
		cd.gridx = 0;
		cd.gridy = 2;
		bwelch.setSelected(true);
		panel_dek.add(bwelch, cd);
		cd.gridwidth = 1;
		cd.weightx = 0.0;
		cd.weighty = 1.0;
		cd.gridx = 0;
		cd.gridy = 3;
		panel_dek.add(bmassey, cd);
		cd.gridwidth = 1;
		cd.weightx = 0.0;
		cd.weighty = 1.0;
		cd.gridx = 0;
		cd.gridy = 4;
		panel_dek.add(startdec, cd);
		cd.gridwidth = 1;
		cd.weightx = 0.0;
		cd.weighty = 1.0;
		cd.gridx = 0;
		cd.gridy = 5;
		panel_dek.add(lbl_m_dec, cd);
		cd.gridwidth = 1;
		cd.weightx = 0.0;
		cd.weighty = 1.0;
		cd.gridx = 1;
		cd.gridy = 5;
		panel_dek.add(txt_m_dec, cd);

		kod_log_textarea.setEditable(false);
		dek_log_textarea.setEditable(false);		
		
		GridLayout tabGridLayout = new GridLayout(0, 2);
		panel_tab_kod.setLayout(tabGridLayout);
		panel_tab_kod.add(panel_kod);
		panel_tab_kod.add(new JScrollPane(kod_log_textarea));
		panel_tab_dek.setLayout(tabGridLayout);
		panel_tab_dek.add(panel_dek);
		panel_tab_dek.add(new JScrollPane(dek_log_textarea));

		// tabpane.setSize(500,500);
		tabpane.add("Kodierung", panel_tab_kod);
		tabpane.add("Dekodierung", panel_tab_dek);
		// tab_panel.setSize(1000,300);
		// tab_panel.setLayout(new FlowLayout());
		// The following line enables to use scrolling tabs.
		tabpane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		// tab_panel.add(tabpane);
		System.out.println("tabpanel size: " + tab_panel.getSize());
		bottom_panel.add(tabpane, myConstraints);
		bottom_panel.setSize(1000, 300);
		System.out.println("bottompanel size: " + bottom_panel.getSize());

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 2;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 3;
		pane.add(bottom_panel, c);
		System.out.println("pane size: " + pane.getSize());

		// pack drückt alles zusammen in der view, ignoriert size angaben
		// this.pack();
		this.setVisible(true);

		// panel.add(top_panel);
		// panel.add(middle_panel);
		// panel.add(bottom_panel);

	}

	/**
	 * Returns values for the Galois Field - p = Primzahl - n = Anzahl der
	 * Elemente in GF - k = Länge der Nachricht - irr = irreduzibles Polynom
	 * 
	 * @return array of values (siehe oben)
	 */
	public int[] getFieldValues() {
		int[] values = new int[6];
		values[0] = Integer.parseInt(this.txt_p.getText());
		values[1] = Integer.parseInt(this.txt_m.getText());
		values[2] = Integer.parseInt(this.txt_primelem.getText());
		values[3] = Integer.parseInt(this.txt_irred.getText());
		values[4] = Integer.parseInt(this.txt_n.getText());
		values[5] = Integer.parseInt(this.txt_k.getText());
		return values;
	}

	public int[] getMessage() {

		String messageString = this.txt_m_cod.getText();
		String[] splittedMessage = messageString.split(",");
		int[] message = new int[splittedMessage.length];
		for (int i = 0; i < message.length; i++) {
			message[message.length - 1 - i] = Integer
					.parseInt(splittedMessage[i]);

		}
		return message;
	}

	public String getCodingStyle() {
		String selected = "";
		if (this.system_radio.isSelected()) {
			selected = systemString;
		} else {
			selected = stuetzString;
		}
		return selected;
	}

	/**
	 * Funktionen bereitstellen, mit denen man später aus dem Controller die
	 * nötigen Listener hinzufügen kann
	 */
	public void setStartCodingListener(ActionListener l) {
		this.startcod.addActionListener(l);
	}

	public void setStartDecodingListener(ActionListener l) {
		this.startdec.addActionListener(l);
	}
	
	public void setComboBoxListener(ItemListener l) {
		this.dropdown.addItemListener(l);
		
	}
	
	public void setRandButtonListener(ActionListener l) {
		this.randmessage.addActionListener(l);		
	}
	public void setResetFormListener(ActionListener l) {
		// this.startdec.addActionListener(l);
	}

	public void setCode(String code) {
		this.txt_c_cod.setText(code);
	}

	public String getDecodingStyle() {
		String selected = "";
		if (this.bwelch.isSelected()) {
			selected = bwelchString;
		} else {
			selected = bmasseyString;
		}
		return selected;
	}

	public int[] getCode() {
		String codeString = this.txt_c_dec.getText();
		String[] splittedCode = codeString.split(",");

		int[] code = new int[splittedCode.length];
		for (int i = 0; i < code.length; i++) {
			code[code.length - 1 - i] = Integer.parseInt(splittedCode[i]);
		}

		return code;
	}

	public void setMessage(String message) {
		this.txt_m_dec.setText(message);

	}
	public void setInputMessage(String message) {
		this.txt_m_cod.setText(message);

	}

//   alle felder auf leer setzen
//	public void resetView() {
//		this.txtEingabe.setText("");
//	    this.txtErg.setText("");
//
//	}

	public void changeGaloisFields(String m, String irred, String n, String k) {
		this.txt_m.setText(m);
		this.txt_irred.setText(irred);
		this.txt_n.setText(n);
		this.txt_k.setText(k);
		
	}

}
