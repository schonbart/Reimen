package reimen;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Reimemaschine2 extends JFrame {

	private JPanel contentPane;
	private JTextField tfWelchesWort;
	private DefaultListModel<String> reimeModel;
	private JList<String> listReime;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Reimemaschine2 frame = new Reimemaschine2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Reimemaschine2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblWelchesWort = new JLabel("Orgio Verbum eingeben");
		lblWelchesWort.setBounds(10, 11, 213, 14);
		contentPane.add(lblWelchesWort);

		tfWelchesWort = new JTextField();
		tfWelchesWort.setBounds(10, 34, 171, 20);
		contentPane.add(tfWelchesWort);
		tfWelchesWort.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 111, 393, 139);
		contentPane.add(scrollPane);

		listReime = new JList<>();
		scrollPane.setViewportView(listReime);
		reimeModel = new DefaultListModel<String>();
		listReime.setModel(reimeModel);
		JButton btnReim = new JButton("\u00E4 \u00F6 \u00FC");
		btnReim.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				/*
				 * Ich habe einiges verändert (wie man sieht :D) Ich habe das Suchen nach
				 * Vokalen in einem String in eine extra Methode ausgelagert. Du hattest den
				 * gleichen Code mehrmals stehen, was redundant war. Durch die Auslagerung in
				 * eine extra-Methode ist der Code weniger redundant und kann flexibler geändert
				 * werden.
				 * 
				 * Das Durchgehen durch eine Datei (oder eines Strings) mit einem Scanner geht
				 * vorzugsweise in einer while-Schleife. Die Methode hasNextLine() fungiert hier
				 * als Abbruchsbedingung. Beispiele finden sich reihenweise bei Google.
				 * 
				 * Den Kontrollfluss eines Programms sollte man generell nicht mit
				 * einkalkulierten Fehlern modellieren. Deshalb hat der Scanner beispielsweise
				 * so welche Methoden, um einer Exception vorzubeugen.
				 * 
				 * Dazu habe ich hier ein paar lokale Variablen umbenannt, da ich so einen
				 * besseren Überblick habe. Das ist aber wohl Meinungssache.
				 * 
				 */

				reimeModel.clear();
				String textFieldText = tfWelchesWort.getText();

				String textFieldVokale = vokaleVonString(textFieldText);

				File woerterDatei = new File("." + File.separator + "woerter.txt");
				try (Scanner wortDateiScanner = new Scanner(woerterDatei)) {
					String line;
					while (wortDateiScanner.hasNextLine()) {
						line = wortDateiScanner.nextLine();
						if (vokaleVonString(line).equals(textFieldVokale)) {
							reimeModel.addElement(line);
						}
					}
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}

			}
		});
		btnReim.setBounds(98, 75, 231, 23);
		contentPane.add(btnReim);

		JButton btnUnsauber = new JButton("unsauber reimen");
		btnUnsauber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnUnsauber.setBounds(215, 33, 198, 23);
		contentPane.add(btnUnsauber);

	}

	/**
	 * Geht durch einen String durch und gibt in der Reihenfolge, in der es die
	 * Vokale darin findet genau diese zurück.
	 * 
	 * 
	 * Könnte noch so modifiziert werden, dass nicht nur einzelne Vokale, sondern
	 * z.B. auch Sachen wie "eu" und so weiter extra behandelt werden. Das überlasse
	 * ich dann dir ;)
	 * 
	 * @param s Der zu traversierende String.
	 * @return Die Vokale.
	 */
	private String vokaleVonString(String s) {
		StringBuilder vokale = new StringBuilder();
		String gueltigeVokale = "aeiouy\u00E4\u00F6\u00FC";
		String sLowerCase = s.toLowerCase();

		for (int i = 0; i < sLowerCase.length(); i++) {
			if (gueltigeVokale.contains(String.valueOf(sLowerCase.charAt(i)))) {
				
				/*
				 * Dieser Code ist ziemlich ähnlich zu deinem (wenn nicht sogar gleich...)
				 * Das Problem, was ich festgestellt habe ist, dass sich 'ei' auf 'a' reimt. Deshalb wird
				 * ein 'a' eingefügt. Genauso reimt sich 'er' auf 'a', weshalb auch dabei ein 'a' 
				 * eingefügt wird. Das führt dazu, dass das Programm vorschlägt, 'er' auf 'ei' zu reimen.
				 * Ob man das hier in eine extra Methode auslagern kann weiß ich nicht genau.
				 */
				char buchstabe = sLowerCase.charAt(i);
				if (i + 1 < s.length()) {
					char buchstabe2 = sLowerCase.charAt(i + 1); 
					if (buchstabe == 'e') {
						if(buchstabe2 == 'i') {
							vokale.append('a');
							i++;
						} else if (buchstabe2 == 'e') {
							vokale.append('e');
							i++;
						} else if (buchstabe2 == 'u') {
							vokale.append('q');
							i++;
						} else if (buchstabe2 == 'r') {
							vokale.append('a');
							i++;
						} else if (i + 2 < s.length() && buchstabe2 == 'a' && sLowerCase.charAt(i + 2) == 'u') {
							vokale.append('o');
							i += 2;
						} else {
							vokale.append(buchstabe);
						}
					} else if (buchstabe == 'i') {
						if (buchstabe2 == 'e') {
							vokale.append('i');
							i++;
						} else {
							vokale.append(buchstabe);
						}
					} else if (buchstabe == 'a') {
						if (buchstabe2 == 'u') {
							vokale.append('w');
							i++;
						} else {
							vokale.append(buchstabe);
						}
					} else {
						vokale.append(buchstabe);
					}
					
				} else {
					vokale.append(buchstabe);
				}
				
//				// jetzt wirds kompliziert :) -> wie die ganzen if Anweisungen durch eine
//				// weitere Methode ersetzen?
//				if (s.charAt(i) == 'e') {
//					if (i + 1 < sLowerCase.length()) {// gibt es �berhaupt noch einen Buchstaben danach?
//
//						if (s.charAt(i + 1) == 'i') {// wenn e und danach i im zu traversierenden String stehen
//							vokale.append('a');// steht ein a in dem Vokale StringBuilder, dann steht das
//												// stellvertretend f�r ei -> weil 'a' reimt sich ja auch auf 'ei'
//							i++;// einen weitergehen, damit wir nicht zus�tzlich das i nach dem e noch als Vokal
//								// ansehen
//						} else {
//							vokale.append(s.charAt(i));
//						}
//					} else {
//						vokale.append(s.charAt(i));
//					}
//				}
//
//				if (s.charAt(i) == 'i') {
//					if (i + 1 < sLowerCase.length()) {
//						if (s.charAt(i + 1) == 'e') {
//							vokale.append('i');
//							i++;
//						} else {
//							vokale.append(s.charAt(i));
//						}
//					} else {
//						vokale.append(s.charAt(i));
//					}
//				}
//
//				if (s.charAt(i) == 'e') {
//					if (i + 1 < sLowerCase.length()) {
//
//						if (s.charAt(i + 1) == 'e') {
//							vokale.append('e');
//							i++;
//						} else {
//							vokale.append(s.charAt(i));
//						}
//					} else {
//						vokale.append(s.charAt(i));
//					}
//				}
//
//				if (s.charAt(i) == 'e') {/* warum findet er keine Reime auf Niveau */
//					if (i + 2 < sLowerCase.length()) {
//
//						if (s.charAt(i + 1) == 'a') {
//
//							if (s.charAt(i + 2) == 'u') {
//								vokale.append('o');
//								i = i + 2;
//							} else {
//								vokale.append(s.charAt(i));
//							}
//						} else {
//							vokale.append(s.charAt(i));
//						}
//					} else {
//						vokale.append(s.charAt(i));
//					}
//				}
//
//				if (s.charAt(i) == 'a') {
//					if (i + 1 < sLowerCase.length()) {
//
//						if (s.charAt(i + 1) == 'u') {
//							vokale.append('w');// 'au' wird gegen 'w' substituiert
//							i++;
//						} else {
//							vokale.append(s.charAt(i));
//						}
//					} else {
//						vokale.append(s.charAt(i));
//					}
//				}
//
//				if (s.charAt(i) == 'e') {
//					if (i + 1 < sLowerCase.length()) {
//
//						if (s.charAt(i + 1) == 'u') {
//							vokale.append('q');// 'eu' wird gegen 'q' substituiert
//							i++;
//						} else {
//							vokale.append(s.charAt(i));
//						}
//					} else {
//						vokale.append(s.charAt(i));
//					}
//				}
//
//				if (s.charAt(i) == 'e') {/* er wird nicht auf a gereimt */
//					if (i + 1 < sLowerCase.length()) {
//
//						if (s.charAt(i + 1) == 'r') {
//							vokale.append('a');// 'er' reimt sich ja auch auf 'a'
//							i++;
//						} else {
//							vokale.append(s.charAt(i));
//						}
//					} else {
//						vokale.append(s.charAt(i));
//					}
//
//					// letztes if am Ende kriegt diese zugehende Klammer
//				} else {
//					vokale.append(s.charAt(i));
//				} // ganz am Ende das else f�r wenn wir alle ifs durchhaben (also alle
//					// Spezialf�lle bei bestimmten Buchstaben)
			}
		}
		return vokale.toString();
	}
}
