package br.com.karlosimoreira.fcvarzea.domain.util;

/**
 * Created by Carlos on 04/07/2016.
 */
public enum Positions {

    GOLEIRO(1),
    ZAGUEIRO(2),
    LATERAL_DIREITO(3),
    LATERAL_ESQUERDO(4),
    VOLANTE(5),
    MEIA_ESQUERDA(6),
    MEIA_DIREITA(7),
    PONTA_ESQUERDA(8),
    SEGUNDO_ATACANTE(9),
    MEIA_OFENCIVO(10),
    PONTA_DIREITA(12),
    CENTRO_AVANTE(11);


    public int valorPosition;

    Positions(int valor) {valorPosition = valor;}

    public void setValor(int valor){
        valorPosition = valor;
    }
    public  int getValor(){
        return valorPosition;
    }
}
