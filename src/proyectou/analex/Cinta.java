package proyectou.analex;

import javax.swing.JTextPane;

/**
 *
 * @author Sergio_W
 */
public class Cinta {

    public static final char EOF    = 0;
    public static final char EOLN   = 10;
    public static final char EOLNN  = 13;
    public static final char ESP    = 32;
    public static final char TAB    = 9;
    
    private JTextPane cinta;
    int i;

    public Cinta() {
        cinta = new JTextPane();
        i = 0;
    }

    public Cinta(JTextPane h) {
        cinta = h;
        i = 0;  
    }

    public void init() {
        i = 0;  //// ya comenzo
    }
    
    public int getPosicion(){
        return this.i;
    }

    public JTextPane getPane(){
        return this.cinta;
    }
    
    public char cc() {
        if (i == cinta.getText().length()) {
            return EOF;
        }
        return cinta.getText().charAt(i);
    }

    public boolean avanzar() {
        if (i == -1 || i >= cinta.getText().length()) {
            return false;
        }
        i++;

        return true;
    }

    public boolean cargarCinta(JTextPane h) {
        this.cinta = h;        
        i = -1;
        return true;
    }

    @Override
    public String toString() {
        String s = "";
        for (int j = 0; j <= cinta.getText().length(); j++) {

            if (cinta.getText().length() == j) {
                s = s + "c[" + j + "] : " + "EOF" + "\n";
                return s;
            } else if (cinta.getText().charAt(j) == ESP) {
                s = s + "c[" + j + "] : " + "ESP" + "\n";
            } else if (cinta.getText().charAt(j) == EOLN) {
                s = s + " c[" + j + "] : " + "EOLN" + "\n";
            } else if (cinta.getText().charAt(j) == EOLNN) {
                s = s + " c[" + j + "] : " + "EOLNN" + "\n";
            } else {
                s = s + "c[" + j + "] : " + cinta.getText().charAt(j) + "\n";
            }

        }
        return "";
    }
}
