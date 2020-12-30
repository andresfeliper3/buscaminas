package buscaminas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BuscaminasGUI extends JFrame {
	public static final int HEIGHT = 100, WIDTH = 80;
	public static int ROWS = 8, COLS = 8;
	private int casillaSize = 30;

	private Casilla[][] casillas;
	private Cronometro cronometro;
	private JPanel header, panelJuego;
	private JComboBox nivelesCaja;
	private String[] niveles = { "Principante", "Intermedio", "Avanzado" };

	private Escucha escucha;

	public BuscaminasGUI() {

		Casilla.setCasillaSize(casillaSize);
		initGUI();

		this.setTitle("Buscaminas");
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private void initGUI() {
		// layout

		// escucha
		escucha = new Escucha();

		// componentes
		header = new JPanel();
		add(header, BorderLayout.NORTH);
		// caja de niveles
		nivelesCaja = new JComboBox(niveles);
		nivelesCaja.addActionListener(escucha);
		header.add(nivelesCaja);
		// panel juego
		panelJuego = new JPanel(new GridLayout(ROWS, COLS));
		add(panelJuego, BorderLayout.SOUTH);
		// matriz de casillas
		ponerCasillas();

	}

	private void ponerCasillas() {
		panelJuego.removeAll();
		panelJuego.setLayout(new GridLayout(ROWS, COLS));
		this.setLocationRelativeTo(null);
		casillas = new Casilla[ROWS][COLS];
		int id = 1;
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				casillas[row][col] = new Casilla(row, col, id);
				casillas[row][col].addActionListener(escucha);
				panelJuego.add(casillas[row][col]);
				id++;
			}
		}
	}

	private class Escucha implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			if (e.getSource() instanceof JComboBox) {
				JComboBox cb = (JComboBox) e.getSource();
				switch(cb.getSelectedIndex()) {
				case 0:
					ROWS = 8;
					COLS = 8;
					break;
				case 1:
					ROWS = 16;
					COLS = 16;
					break;
				case 2:
					ROWS = 16;
					COLS = 30;
					break;
				}
				ponerCasillas();
				pack();
			} else if (e.getSource() instanceof Casilla) {
				Casilla casilla = (Casilla) e.getSource();
				System.out.println("Soy la casilla " + casilla.getId());

			}

		}

	}
}
