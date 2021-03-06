package buscaminas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BuscaminasGUI extends JFrame {
	// Config general
	public static final int HEIGHT = 100, WIDTH = 80;
	private static final String rutaBandera = "src/recursos/bandera.jpg";
	private static final String rutaBomba = "src/recursos/bomba.jpg";
	public static int ROWS = 8, COLS = 8;
	private int casillaSize = 40;
	// Gr�ficos
	private Casilla[][] casillas;
	private Cronometro cronometro;
	private JPanel header, panelJuego;
	private JComboBox nivelesCaja;
	private JButton bandera;
	private boolean estadoBandera = false; // true si pone bandera, false en caso contrario (bomba)
	private String[] niveles = { "Principante", "Intermedio", "Avanzado" };

	// Atributos del juego
	/*
	 * Niveles 1: principiantes 2: intermedio 3: avanzado
	 */
	private int nivel = 1;
	private int cantidadBombas = 0;
	/*
	 * Estado 1: en juego 2: perdiste 3: ganaste
	 */
	private int estado = 1;
	// Adicionales necesarios
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
		// bot�n de bandera
		bandera = new JButton(new ImageIcon(rutaBomba));
		bandera.setBorder(null);
		bandera.setContentAreaFilled(true);
		bandera.addActionListener(escucha);
		header.add(bandera);
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
		repartirNumeros();
	}

	// Coloca gr�ficamente todas las casillas del juego seg�n el nivel escogido
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

		for (int bomba = 0; bomba < cantidadBombas; bomba++) {
			int col, row;
			// Ciclo para garantizar que las bombas se repartan en casillas diferentes
			do {
				col = random.nextInt(COLS);
				row = random.nextInt(ROWS);
			} while (casillas[row][col].isTieneBomba());
			imagen = new ImageIcon(rutaBomba);
			casillas[row][col].setTieneBomba(true);
			casillas[row][col].setImagen(imagen);
		}

	}

	// Reparte los n�meros de las casillas que no tienen bombas

	private void repartirNumeros() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				// Si la casilla no tiene bomba recibe n�mero
				System.out.println("Casilla a revisar, row " + row + " col " + col);
				if (!casillas[row][col].isTieneBomba()) {
					// Dentro de la matriz
					if (row > 0 && row < ROWS - 1 && col > 0 && col < COLS - 1) {
						revisarBombas(row, col, -1, -1, 1, 1);
					}
					// L�mite superior
					else if (row == 0) {
						// Superior izquierdo
						if (col == 0) {
							revisarBombas(row, col, 0, 0, 1, 1);
						}
						// Superior derecho
						else if (col == COLS - 1) {
							revisarBombas(row, col, 0, -1, 1, 0);
						}
						// s�lo superior
						else {
							revisarBombas(row, col, 0, -1, 1, 1);
						}
					}
					// L�mite inferior
					else if (row == ROWS - 1) {
						// inferior izquierdo
						if (col == 0) {
							revisarBombas(row, col, -1, 0, 0, 1);
						}
						// inferior derecho
						else if (col == COLS - 1) {
							revisarBombas(row, col, -1, -1, 0, 0);
						}
						// s�lo inferior
						else {
							revisarBombas(row, col, -1, -1, 0, 1);
						}
					}
					// s�lo izquierda
					else if (col == 0) {
						revisarBombas(row, col, -1, 0, 1, 1);
					}
					// s�lo derecha
					else if (col == COLS - 1) {
						revisarBombas(row, col, -1, -1, 1, 0);
					}
				}
			}
		}
	}

	// Revisa cu�ntas bombas hay alrededor de una casilla. Recibe las coordenadas de
	// la casilla en la matriz y otros par�metros que ayudan a la abstracci�n en la
	// iteraci�n
	private void revisarBombas(int row, int col, int rowInit, int colInit, int rowFin, int colFin) {
		System.out.println("row " + row + " col " + col + " rowinit " + rowInit + " colInit " + colInit + " rowFin "
				+ rowFin + " colFin " + colFin);
		int counter = 0;
		for (int i = row + rowInit; i <= row + rowFin; i++) {
			for (int j = col + colInit; j <= col + colFin; j++) {
				System.out.println("i vale " + i + " y j vale " + j);
				if (casillas[i][j].isTieneBomba()) { // Revisa uno de m�s, la casilla misma
					counter++;
				}
			}
		}
		casillas[row][col].setNumero(counter);
	}

	ArrayList<Casilla> casillasVaciasEnLista = new ArrayList<Casilla>();
	// Descubre las casillas que tienen ceros (vac�as) alrededor de una casilla que
	// ya tiene cero (est� vac�a)
	private void descubrirCasillas(int row, int col, int rowInit, int colInit, int rowFin, int colFin) {
		for (int i = row + rowInit; i <= row + rowFin; i++) {
			for (int j = col + colInit; j <= col + colFin; j++) {
				System.out.println("i vale " + i + " y j vale " + j);
				if (casillas[i][j].getNumero() == 0) { // Revisa uno de m�s, la casilla misma
					casillas[i][j].setDescubierta(true);
					// VER: MOSTRAR N�MERO CERO O ALGO ILUSTRATIVO
					casillas[i][j].setText(String.valueOf(casillas[i][j].getNumero()));
					casillasVaciasEnLista.add(casillas[i][j]);
					//System.out.println("Entra a recursi�n con casilla " + casillas[i][j].getRow() + " y " + casillas[i][j].getCol());
					//clickEnCasillaVacia(casillas[i][j]);
				}
			}
		}
	}
	//Recibe una lista de casillas y las recorre para descubrir a los ceros que la rodean
	private void descubrirCasillasVaciasLejanas(ArrayList<Casilla> casillas) {
		ArrayList<Casilla> copiaDeCasillasVacias = (ArrayList<Casilla>) casillasVaciasEnLista.clone();
		
		
	}
	// Si recibe true activa las escuchas, si recibe false desactiva las escuchas
	private void disponibilidadEscuchas(boolean bool) {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if (bool) {
					casillas[row][col].addActionListener(escucha);
				} else {
					casillas[row][col].removeActionListener(escucha);
				}
			}
		}
	}

	// Cambia la imagen del bot�n bandera y lo que ocurre al hacer click en una
	// casilla
	private void cambiarEstadoBandera() {
		if (estadoBandera) {
			bandera.setIcon(new ImageIcon(rutaBomba));
			estadoBandera = false;
		} else {
			bandera.setIcon(new ImageIcon(rutaBandera));
			estadoBandera = true;
		}
	}

	// Poner la bandera sobre una casilla si est� vac�a, si ya tiene una bandera la
	// quita
	private void ponerQuitarBandera(Casilla casilla) {
		if (!casilla.isDescubierta()) { // verifica que no se haya descubierto lo que tiene la casilla
			if (casilla.isMarcada()) {
				casilla.setMarcada(false);
				casilla.setIcon(null);
			} else {
				casilla.setMarcada(true);
				casilla.setIcon(new ImageIcon(rutaBandera));
			}
		}
	}

	// Descubre casillas adyacentes cuyo n�mero es 0 (no limitan con una bomba)
	private void clickEnCasillaVacia(Casilla casilla) {
		int row = casilla.getRow();
		int col = casilla.getCol();

		// Si la casilla no tiene bomba recibe n�mero
		System.out.println("Casilla a revisar, row " + row + " col " + col);
		if (casillas[row][col].getNumero() == 0 && !casillas[row][col].isDescubierta()) {
			// Dentro de la matriz
			if (row > 0 && row < ROWS - 1 && col > 0 && col < COLS - 1) {
				descubrirCasillas(row, col, -1, -1, 1, 1);
			}
			// L�mite superior
			else if (row == 0) {
				// Superior izquierdo
				if (col == 0) {
					descubrirCasillas(row, col, 0, 0, 1, 1);
				}
				// Superior derecho
				else if (col == COLS - 1) {
					descubrirCasillas(row, col, 0, -1, 1, 0);
				}
				// s�lo superior
				else {
					descubrirCasillas(row, col, 0, -1, 1, 1);
				}
			}
			// L�mite inferior
			else if (row == ROWS - 1) {
				// inferior izquierdo
				if (col == 0) {
					descubrirCasillas(row, col, -1, 0, 0, 1);
				}
				// inferior derecho
				else if (col == COLS - 1) {
					descubrirCasillas(row, col, -1, -1, 0, 0);
				}
				// s�lo inferior
				else {
					descubrirCasillas(row, col, -1, -1, 0, 1);
				}
			}
			// s�lo izquierda
			else if (col == 0) {
				descubrirCasillas(row, col, -1, 0, 1, 1);
			}
			// s�lo derecha
			else if (col == COLS - 1) {
				descubrirCasillas(row, col, -1, -1, 1, 0);
			}
		}

	}

	// Inicia elementos del juego
	private void iniciarJuego() {
		ponerCasillas();
		repartirBombas();
		repartirNumeros();
		pack();
		setLocationRelativeTo(null);
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
				iniciarJuego();
			} else if (e.getSource() instanceof Casilla) {
				Casilla casilla = (Casilla) e.getSource();
				System.out.println("Soy la casilla " + casilla.getId());
				// Si est� seleccionada la bandera
				if (estadoBandera) {
					ponerQuitarBandera(casilla);
				}
				// Si est� seleccionada la bomba
				else {
					
					if (casilla.isTieneBomba()) {
						estado = 2; // perdiste
						disponibilidadEscuchas(false); // desactivar escuchas
						// opci�n de reinicio
						int option = JOptionPane.showConfirmDialog(null, "�Quieres jugar de nuevo?", "�Perdiste!",
								JOptionPane.YES_NO_OPTION);
						if (option == JOptionPane.YES_OPTION) {
							// REINICIAR JUEGO
							iniciarJuego();
						} else if (option == JOptionPane.NO_OPTION) {
							//Cerrar juego
							System.exit(0);
						}
					}
					// Si la casilla no tiene bomba, aparece el n�mero
					else {
						casilla.mostrarNumero();
						//Revisar las que no tengan contacto con una bomba
						clickEnCasillaVacia(casilla);
						descubrirCasillasVaciasLejanas(casillasVaciasEnLista);
					}
					casilla.setDescubierta(true);
				}
			} else if (e.getSource() instanceof JButton) {
				JButton boton = (JButton) e.getSource();
				if (boton == bandera) {
					cambiarEstadoBandera();
				}
			}

		}

	}
}
