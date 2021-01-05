package buscaminas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BuscaminasGUI extends JFrame {
	//Config general
	public static final int HEIGHT = 100, WIDTH = 80;
	public static int ROWS = 8, COLS = 8;
	private int casillaSize = 30;
	//Gráficos
	private Casilla[][] casillas;
	private Cronometro cronometro;
	private JPanel header, panelJuego;
	private JComboBox nivelesCaja;
	private String[] niveles = { "Principante", "Intermedio", "Avanzado" };

	//Atributos del juego
	/*
	 * Niveles 1: principiantes 
	 * 2: intermedio 
	 * 3: avanzado
	 */
	private int nivel = 1;
	private int cantidadBombas = 0;
	//Adicionales necesarios
	private Random random;
	private Escucha escucha;
	private ImageIcon imagen;

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

		// escucha y random
		escucha = new Escucha();
		random = new Random();
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
		repartirBombas();
	}

	// Coloca gráficamente todas las casillas del juego según el nivel escogido
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

	// Reparte las bombas aleatoriamente por el juego
	private void repartirBombas() {
		switch (nivel) {
		case 1:
			cantidadBombas = 10;
			break;
		case 2:
			cantidadBombas = 40;
			break;
		case 3:
			cantidadBombas = 99;
			break;
		}
		//
		
		for(int bomba = 0; bomba < cantidadBombas; bomba++) {
			int col, row;
			//Ciclo para garantizar que las bombas se repartan en casillas diferentes
			do {
				col = random.nextInt(COLS);
				row = random.nextInt(ROWS);
			} while(casillas[row][col].isTieneBomba());	
			imagen = new ImageIcon("src/recursos/bomba.jpg");
			casillas[row][col].setTieneBomba(true);
			casillas[row][col].setImagen(imagen);
		}
		
	}

	private class Escucha implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub

			if (e.getSource() instanceof JComboBox) {
				JComboBox cb = (JComboBox) e.getSource();
				switch (cb.getSelectedIndex()) {
				case 0:
					ROWS = 8;
					COLS = 8;
					nivel = 1;
					break;
				case 1:
					ROWS = 16;
					COLS = 16;
					nivel = 2;
					break;
				case 2:
					ROWS = 16;
					COLS = 30;
					nivel = 3;
					break;
				}
				ponerCasillas();
				repartirBombas();
				pack();
				setLocationRelativeTo(null);
			} else if (e.getSource() instanceof Casilla) {
				Casilla casilla = (Casilla) e.getSource();
				System.out.println("Soy la casilla " + casilla.getId());

			}

		}

	}
}
