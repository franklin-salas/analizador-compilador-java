package proyectou.analex;

import CodigoC3.Cuadrupla;
import CodigoC3.TSVAR;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import paraTexto.*;

/**
 * @author Sergio Weimar Ricaldez Zapata
 * @email  ser61_ol@yahoo.es
 */
public class Analex {
    private List<Token> token;
    private List<String> lexema;
    private Hashtable<String, PintarPalabra.TIPO> palabraReservada;
    private int i;
    private Cinta c;
    private String cadena;
    private PintarPalabra pintar;
    private ResaltarPalabras resaltar;

    public Analex(Cinta cinta) {
        token = new ArrayList<>();
        lexema = new ArrayList<>();
        palabraReservada = new Hashtable<>();
        i = -1;
        c = cinta;
        pintar = new PintarPalabra(c.getPane());
        resaltar = new ResaltarPalabras(c.getPane());
        cargarPalabrasReservadas();
        cadena = "";
    }

    public void Init() {
        c.init();
        i = -1;
        cadena = "";
        token.clear();
        lexema.clear();
        resaltar.clear();
    }

    public void avanzar() {
        i++;
        resaltar.resetIniFin();
        dt0();
    }

    public Token preanalisis() {
        return token.get(i);
    }

    public String lexema() {
        return lexema.get(i);
    }

    public void pintar(){
        pintar.pintar();
    }
    
    public void refreshPane(){
        pintar.defaultt();
        pintar.pintar();
    }

    private void dt0() {
        comeEsp();
        resaltar.setLetIni(c.getPosicion());
        switch (cc()) {
            //<editor-fold defaultstate="collapsed" desc="ARITMETICA-COMENTARIO">
            case Cinta.EOF:
                dt1_FIN();
                break;
            case '*':
                c.avanzar();
                dt3();
                break;
            case '+':
                c.avanzar();
                dt4();
                break;
            case '-':
                c.avanzar();
                dt5();
                break;
            case '%':
                c.avanzar();
                dt6();
                break;
            case '/':
                c.avanzar();
                dt7();
                //</editor-fold>
                break;
            //<editor-fold defaultstate="collapsed" desc="OPERACIONES-LOGICAS">
            case '=':
                c.avanzar();
                dt16();
                break;
            case '<':
                cadena += c.cc();
                c.avanzar();
                dt12();
                break;
            case '>':
                cadena += c.cc();
                c.avanzar();
                dt17();
                cadena = "";
                break;
            case '!':
                cadena += c.cc();
                c.avanzar();
                dta1();
                cadena = "";
            //</editor-fold> 
                  break;
            //<editor-fold defaultstate="collapsed" desc="AUXILIARES">
            case ',':
                c.avanzar();
                dt20();
                break;
            case ':':
                cadena += c.cc();
                c.avanzar();
                dt21();
                cadena = "";
                break;
            case '.':
                c.avanzar();
                dt24();
                break;
            case ';':
                c.avanzar();
                dt25();
                break;
            case '{':
                c.avanzar();
                dt26();
                break;
            case '}':
                c.avanzar();
                dt27();
                break;
            case '(':
                c.avanzar();
                dt28();
                break;
            case ')':
                c.avanzar();
                dt29();
                //</editor-fold>
                break;
          
            //<editor-fold defaultstate="collapsed" desc="STRING CTTE">
            case '"':
                cadena += c.cc();
                c.avanzar();
                dt30();
                cadena = "";
            //</editor-fold>
                break;
            default:
                //<editor-fold defaultstate="collapsed" desc="ID - NUMEROS" - "PALABRAS RESERVADAS">
                if (let(c.cc())) {
                    cadena += c.cc();
                    c.avanzar();
                    dt32();
                    cadena = "";
                } else if (num(c.cc())) {
                    cadena += c.cc();
                    c.avanzar();
                    dt35();
                    cadena = "";
                } else {
                    //</editor-fold>
                    dt2_ERROR();
                }
                break;
        }
    }

    private void dt1_FIN() {
        token.add(new Token("FIN"));
        lexema.add("");
        resaltar.clear();
    }

    private void dt2_ERROR() {
        token.add(new Token("ERROR"));
        lexema.add("");
    }
        
    /**
     * @return : Si el char es letra la retorna en Mayuscula
     */
    private char cc() {
        if ((Character.toUpperCase(c.cc()) >= 'A') && (Character.toUpperCase(c.cc()) <= 'Z')) {
            return Character.toUpperCase(c.cc());
        } else {
            return c.cc();
        }
    }

    private void comeEsp() {
        while ((c.cc() == Cinta.ESP) || (c.cc() == Cinta.TAB) || (c.cc() == Cinta.EOLN) || (c.cc() == 13)) {
            c.avanzar();
        }
    }
    
//<editor-fold defaultstate="collapsed" desc="ARITMETICA-COMENTARIOS">
    private void dt3() {
        token.add(new Token("MUL"));
        lexema.add("*");
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt4() {
        token.add(new Token("SUM"));
        lexema.add("+");
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt5() {
        token.add(new Token("RES"));
        lexema.add("-");
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt6() {
        token.add(new Token("MOD"));
        lexema.add("%");
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt7() {
        switch (c.cc()) {
            case '/':
                c.avanzar();
                dt8();
                break;
            case '*':
                c.avanzar();
                dt9();
                break;
            default:    //Otro
                dt10();
                break;
        }
    }

    private void dt8() {
        while ((c.cc() != Cinta.EOLN) && (c.cc() != Cinta.EOF)) {
            c.avanzar();
        }
        dt0();  //Otro
    }

    private void dt9() {
        while ((c.cc() != Cinta.EOF) && (c.cc() != '*')) {
            c.avanzar();
        }
        if (c.cc() == '*') {
            c.avanzar();
            dt11();
        } else { // Otro
            dt2_ERROR();
        }
    }

    private void dt10() {
        token.add(new Token("DIV"));
        lexema.add("/");
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt11() {
        if (c.cc() == '/') {
            c.avanzar();
            dt0();
        } else {  // Otro
            dt9();
        }
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="OPERACIONES LOGICAS - AUXLIARES">
    private void dt12() {
        switch (c.cc()) {
            case '=':
                cadena += c.cc();
                c.avanzar();
                dt14();
                break;
            case '>':
                cadena += c.cc();
                c.avanzar();
                dt13();
                break;
            default: // Otro
                dt15();
                break;
        }
        cadena = "";
    }

    private void dt13() {
        token.add(new Token("OPREL", Cuadrupla.OPDIS));
        lexema.add(cadena);
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt14() {
        token.add(new Token("OPREL", Cuadrupla.OPMEI));
        lexema.add(cadena);
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt15() {
        token.add(new Token("OPREL", Cuadrupla.OPMEN));
        lexema.add("<");
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt16() {
        token.add(new Token("OPREL", Cuadrupla.OPIGUAL));
        lexema.add("=");
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt17() {
        if (c.cc() == '=') {
            cadena += c.cc();
            c.avanzar();
            dt18();
        } else { // Otro
            dt19();
        }
    }

    private void dt18() {
        token.add(new Token("OPREL", Cuadrupla.OPMAI));
        lexema.add(cadena);
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt19() {
        token.add(new Token("OPREL", Cuadrupla.OPMAY));
        lexema.add(cadena);
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt20() {
        token.add(new Token("COM"));
        lexema.add(",");
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt21() {
        if (c.cc() == '=') {
            cadena += c.cc();
            c.avanzar();
            dt22();
        } else { //Otro
            dt23();
        }
    }

    private void dt22() {
        token.add(new Token("ASIGN"));
        lexema.add(cadena);
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt23() {
        token.add(new Token("DPTO"));
        lexema.add(cadena);
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt24() {
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
        token.add(new Token("PTO"));
        lexema.add(".");
    }

    private void dt25() {
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
        token.add(new Token("PTOCOM"));
        lexema.add(";");
    }

    private void dt26() {
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
        token.add(new Token("OPENK"));
        lexema.add("{");
    }

    private void dt27() {
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
        token.add(new Token("CLOSEK"));
        lexema.add("}");
    }

    private void dt28() {
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
        token.add(new Token("OPENP"));
        lexema.add("(");
    }

    private void dt29() {
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
        token.add(new Token("CLOSEP"));
        lexema.add(")");
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ID - NUMERO - STRING - PALABRAS RESERVADAS">
    private boolean num(char c) {
        return (c >= '0') && (c <= '9');
    }

    private boolean let(char c) {
        return (Character.toUpperCase(c) >= 'A') && (Character.toUpperCase(c) <= 'Z');
    }

    private void dt30() {
        while ((c.cc() != Cinta.EOLN) && (c.cc() != Cinta.EOF) && (c.cc() != '"')) {
            cadena += c.cc();
            c.avanzar();
        }
        if (c.cc() == '"') {
            cadena += c.cc();
            c.avanzar();
            dt31();
        } else {
            dt2_ERROR();
        }
    }

    private void dt31() {
        token.add(new Token("STRCTTE", 2));
        lexema.add(cadena);
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    private void dt32() {
        while (let(c.cc())) {
            cadena += c.cc();
            c.avanzar();
        }
        if (num(c.cc())) {
            cadena += c.cc();
            c.avanzar();
            dt33();
        } else { //Otro
            dt34_ID();
        }
    }

    private void dt33() {
        while (num(c.cc())) {
            cadena += c.cc();
            c.avanzar();
        }
        if (let(c.cc())) {
            cadena += c.cc();
            c.avanzar();
            dt32();
        } else { // Otro
            dt34_ID();
        }
    }
    
    private void dt34_ID() {
        if (palabraReservada.containsKey(cadena.toUpperCase())) {
            generarToken();
        }else{
            token.add(new Token("ID", -1));
            lexema.add(cadena);
            resaltar.setLetFin(c.getPosicion());
            resaltar.resaltarUna();
        }
    }

    private void dt35() {
        while (num(c.cc())) {
            cadena += c.cc();
            c.avanzar();
        }
        if (let(c.cc())) {
            c.avanzar();
            dt32();
        } else { //Otro             
            dt36();
        }
    }

    private void dt36() {
        token.add(new Token("NUM"));
        lexema.add(cadena);
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }
//</editor-fold>    

    private void cargarPalabrasReservadas() {
        palabraReservada.put("AND", PintarPalabra.TIPO.KEYWORD);
        palabraReservada.put("BEGIN", PintarPalabra.TIPO.KEYWORD);
        palabraReservada.put("BOOLEAN", PintarPalabra.TIPO.KEYWORD);
        palabraReservada.put("ELSE", PintarPalabra.TIPO.KEYWORD);
        palabraReservada.put("END", PintarPalabra.TIPO.KEYWORD);
        palabraReservada.put("FOR", PintarPalabra.TIPO.KEYWORD);
        palabraReservada.put("INTEGER", PintarPalabra.TIPO.KEYWORD);
        palabraReservada.put("IF", PintarPalabra.TIPO.KEYWORD);
        palabraReservada.put("VAR", PintarPalabra.TIPO.KEYWORD);
        palabraReservada.put("NOT", PintarPalabra.TIPO.KEYWORD);
        palabraReservada.put("OR", PintarPalabra.TIPO.KEYWORD);
        palabraReservada.put("VAR", PintarPalabra.TIPO.KEYWORD);
        palabraReservada.put("VOID", PintarPalabra.TIPO.KEYWORD);
        palabraReservada.put("WHILE", PintarPalabra.TIPO.KEYWORD);
        pintar.setPalabars(palabraReservada);
    }
    
    public PintarPalabra getPintor() {
        return pintar;
    }
    
    public ResaltarPalabras getResaltador() {
        return resaltar;
    }

    private void generarToken() {
        if (palabraReservada.get(cadena.toUpperCase()) == PintarPalabra.TIPO.KEYWORD) {
            switch (cadena.toUpperCase()) {
                case "INTEGER":
                    token.add(new Token("TIPO", TSVAR.TIPOINT));
                    lexema.add(cadena);
                    break;
                case "BOOLEAN":
                    token.add(new Token("TIPO", TSVAR.TIPOBOOLEAN));
                    lexema.add(cadena);
                    break;
                default:
                    token.add(new Token(cadena.toUpperCase()));
                    lexema.add(cadena);
                    break;
            }
            resaltar.setLetFin(c.getPosicion());
            resaltar.resaltarUna();
        }
    }

    private void dta1() { 
         if (c.cc() == '=') {
            cadena += c.cc();
            c.avanzar();
            dt13();
        } else { // Otro
           dta3();
        }   
    }

    private void dta3() {
        token.add(new Token("NOT"));
        lexema.add(cadena);
        resaltar.setLetFin(c.getPosicion());
        resaltar.resaltarUna();
    }

    public static class Token {

        private String token;
        private int atributo;

        public Token(String token) {
            this.token = token;
            this.atributo = -1;
        }

        public Token(String token, int atrib) {
            this.token = token;
            this.atributo = atrib;
        }

        public String getToken() {
            String s = "";
            if (atributo != -1) {
                s += "<" + token + ", " + atributo + ">";
            } else {
                s += "<" + token + ",__>";
            }
            return s;
        }

        public int getAtributo() {
            return this.atributo;
        }
    }
}
