/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Protocol;

import Server.NewConnection;

/**
 *
 * @author Administrator
 */
public class Telnet implements NewProtocol<Telnet> {

    private final int BUFFERSIZE = 1024;
    private char[] m_buffer;
    private int buffersize = 0;
    static private String[][][] telnetcolors = new String[3][3][3];
    final static public char escapekey = 0x1b;
    final static public String reset = escapekey + "[0m";
    final static public String bold = escapekey + "[1m";
    final static public String dim = escapekey + "[2m";
    final static public String under = escapekey + "[4m";
    final static public String reverse = escapekey + "[7m";
    final static public String hide = escapekey + "[8m";
    final static public String clearscreen = escapekey + "[2J";
    final static public String clearline = escapekey + "[2K";
    final static public String black = escapekey + "[30m";
    final static public String red = escapekey + "[31m";
    final static public String green = escapekey + "[32m";
    final static public String yellow = escapekey + "[33m";
    final static public String blue = escapekey + "[34m";
    final static public String magenta = escapekey + "[35m";
    final static public String cyan = escapekey + "[36m";
    final static public String white = escapekey + "[37m";
    final static public String bblack = escapekey + "[40m";
    final static public String bred = escapekey + "[41m";
    final static public String bgreen = escapekey + "[42m";
    final static public String byellow = escapekey + "[43m";
    final static public String bblue = escapekey + "[44m";
    final static public String bmagenta = escapekey + "[45m";
    final static public String bcyan = escapekey + "[46m";
    final static public String bwhite = escapekey + "[47m";
    final static public String newline = "\r\n" + escapekey + "[0m";

    public Telnet() {
        m_buffer = new char[BUFFERSIZE];

        telnetcolors[0][0][0] = black + dim;
        telnetcolors[0][0][1] = blue + dim;
        telnetcolors[0][0][2] = blue + bold;
        telnetcolors[0][1][0] = green + dim;
        telnetcolors[0][1][1] = cyan + dim;
        telnetcolors[0][1][2] = blue + bold;              // *
        telnetcolors[0][2][0] = green + bold;
        telnetcolors[0][2][1] = green + bold;             // *
        telnetcolors[0][2][2] = cyan + bold;

        telnetcolors[1][0][0] = red + dim;
        telnetcolors[1][0][1] = magenta + dim;
        telnetcolors[1][0][2] = magenta + bold;           // *
        telnetcolors[1][1][0] = yellow + dim;
        telnetcolors[1][1][1] = white + dim;
        telnetcolors[1][1][2] = blue + bold;              // *
        telnetcolors[1][2][0] = green + bold;             // *
        telnetcolors[1][2][1] = green + bold;             // *
        telnetcolors[1][2][2] = cyan + bold;              // *

        telnetcolors[2][0][0] = red + bold;
        telnetcolors[2][0][1] = red + bold;               // *
        telnetcolors[2][0][2] = magenta + bold;
        telnetcolors[2][1][0] = yellow + dim;             // **
        telnetcolors[2][1][1] = red + bold;               // **
        telnetcolors[2][1][2] = magenta + bold;           // *
        telnetcolors[2][2][0] = yellow + bold;
        telnetcolors[2][2][1] = yellow + bold;            // *
        telnetcolors[2][2][2] = white + bold;
    }

    public int Buffered() {
        return buffersize;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void Translate(NewConnection<Telnet> connection, char[] buffer, int size) {
        for (int i = 0; i < size; i++) {
            char c = buffer[i];     // pick out a character
            if (c >= 32 && c != 127 && buffersize < BUFFERSIZE) {
                m_buffer[buffersize] = c;  // insert it into buffer if good
                buffersize++;              // increase buffer size
            } else if (c == 8 && buffersize > 0) {
                buffersize--;
            } else if (c == '\n' || c == '\r') {
                if (buffersize > 0) {
                    String result = new String(m_buffer);
                    result = result.substring(0, buffersize);
                    connection.getHandler().handle(result);
                    buffersize = 0;
                }
            }
        }
    }

    public void sendString(NewConnection<Telnet> connection, String stringtosend) {
        connection.BufferData(TranslateColors(stringtosend));
    }

    static String TranslateColors(String p_str) {
        char[] str = p_str.toCharArray();
        int i = p_str.indexOf('<');


        while (i != -1) {
            if (str[i + 1] == '$') {
                int j = p_str.indexOf(">", i);
                if (j != -1) {
                    p_str = TranslateStringColor(i, j, p_str);
                    str = p_str.toCharArray();
                }
            } else if (str[i + 1] == '#') {
                int j = p_str.indexOf(">", i);
                if (j != -1) {
                    p_str = TranslateNumberColor(i, j, p_str);
                    str = p_str.toCharArray();
                }
            }
            i = p_str.indexOf("<", i + 1);
        }

        return p_str;
    }

    static String TranslateStringColor(int i, int j, String p_str) {
        String col = p_str.substring(i, j + 1);
        String replace = null;

        if (col.equals("<$black>")) {
            replace = black;
        } else if (col.equals("<$red>")) {
            replace = red;
        } else if (col.equals("<$green>")) {
            replace = green;
        } else if (col.equals("<$yellow>")) {
            replace = yellow;
        } else if (col.equals("<$blue>")) {
            replace = blue;
        } else if (col.equals("<$magenta>")) {
            replace = magenta;
        } else if (col.equals("<$cyan>")) {
            replace = cyan;
        } else if (col.equals("<$white>")) {
            replace = white;
        } else if (col.equals("<$bold>")) {
            replace = bold;
        } else if (col.equals("<$dim>")) {
            replace = dim;
        } else if (col.equals("<$reset>")) {
            replace = reset;
        }
        return p_str.replace(col, replace);
    }

    static String TranslateNumberColor(int i, int j, String p_str) {
        // just return if the string is invalid
        if ((j - i) != 8) {
            return p_str;
        }

        // chop off the six digits, ie the "XXXXXX" inside "<#XXXXXX>"
        char[] col = p_str.substring(i + 2, j).toCharArray();


        int r = ASCIIToHex(col[0]) * 16 + ASCIIToHex(col[1]);
        int g = ASCIIToHex(col[2]) * 16 + ASCIIToHex(col[3]);
        int b = ASCIIToHex(col[4]) * 16 + ASCIIToHex(col[5]);

        // convert the numbers to the 0-2 range
        // ie:  0 -  85 = 0
        //     86 - 171 = 1
        //    172 - 255 = 2
        // This gives a good approximation of the true color by assigning equal
        // ranges to each value.
        r = r / 86;
        g = g / 86;
        b = b / 86;

        return p_str.replace(p_str.substring(i, j + 1), telnetcolors[r][g][b]);
    }

    static public char ASCIIToHex(char c) {
        if (c >= '0' && c <= '9') {
            return (char) (c - '0');
        }
        if (c >= 'A' && c <= 'F') {
            return (char) (c - 'A' + 10);
        }
        if (c >= 'a' && c <= 'a') {
            return (char) (c - 'a' + 10);
        }
        return 0;
    }
}

