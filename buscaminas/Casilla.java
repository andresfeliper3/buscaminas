package buscaminas;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Casilla extends JButton {
	public static int casillaSize = 0;
	private int numero, row, col, id;
	private boolean tieneBomba, descubierta, marcada, bgRed;
	private ImageIcon imagen;
	
	public Casilla(int row, int col, int id) {
		this.row = row;
		this.col = col;
		this.id = id;
		
		numero = 0;
		tieneBomba = false;
		descubierta = false;
		marcada = false;
		bgRed = false;
		this.setPreferredSize(new Dimension(casillaSize, casillaSize));
		//this.setBackground(Color.WHITE);
	}
	//Cambia la imagen y el ícono de la casilla
	public void setImagen(ImageIcon imagen) {
		this.imagen = imagen;
		this.setIcon(imagen);
	}
	//Cambia solamente el ícono de la casilla
	public void cambiarSoloIcono(ImageIcon imagen) {
		this.setIcon(imagen); 
	}
	
	public static void setCasillaSize(int tamanho) {
		casillaSize = tamanho;
	}
	
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;	
	}
	public void mostrarNumero() {
		this.setText(String.valueOf(numero));
	}

	public boolean isTieneBomba() {	
		return tieneBomba;
	}

	public void setTieneBomba(boolean tieneBomba) {
		this.tieneBomba = tieneBomba;
	}

	public boolean isDescubierta() {
		return descubierta;
	}

	public void setDescubierta(boolean descubierta) {
		this.descubierta = descubierta;
	}

	public boolean isMarcada() {
		return marcada;
	}

	public void setMarcada(boolean marcada) {
		this.marcada = marcada;
	}

	public boolean isBgRed() {
		return bgRed;
	}

	public void setBgRed(boolean bgRed) {
		this.bgRed = bgRed;
	}

	public ImageIcon getImagen() {
		return imagen;
	}

	

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getId() {
		return id;
	}
}
