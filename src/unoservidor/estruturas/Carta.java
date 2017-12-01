package unoservidor.estruturas;

public class Carta {

    private int cor;
    private int numero;

    public static final int COR_PRETA = 0;
    public static final int COR_VERMELHA = 1;
    public static final int COR_AMARELA = 2;
    public static final int COR_AZUL = 3;
    public static final int COR_VERDE = 4;
    public static final int MAIS_DOIS = 10;
    public static final int REVERTER = 11;
    public static final int BLOQUEAR = 12;
    public static final int MAIS_QUATRO = 13;
    public static final int CORINGA = 14;

    public Carta(int cor, int numero) {
        this.cor = cor;
        this.numero = numero;
    }

    @Override
    public String toString() {
        return cor + "," + numero;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Carta other = (Carta) obj;
        if (this.cor != other.cor) {
            return false;
        }
        return this.numero == other.numero;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.cor;
        hash = 89 * hash + this.numero;
        return hash;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
