package br.com.hotel.model;

public enum DiasSemana {

	SEGUNDA("MONDAY", 1), TERCA("TUESDAY", 2), QUARTA("WEDNESDAY", 3), QUINTA("THURSDAY", 4), SEXTA("FRIDAY", 5),
	SABADO("SATURDAY", 6), DOMINGO("SUNDAY", 7);

	private final int valor;

	private DiasSemana(String dia, int valor) {
		this.valor = valor;
	}

	public int getValor() {
		return this.valor;
	}

}
