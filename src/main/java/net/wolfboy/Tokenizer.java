package net.wolfboy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Tokenizer {

    public static List<TokenType> tokenTypes = new ArrayList<TokenType>();
    public static List<Token> tokens = new ArrayList<Token>();

    public static void addTokensToList() {
        tokenTypes.add(new TokenType("int", "Declarator", "\\W*((?i)int(?-i))\\W*"));
        tokenTypes.add(new TokenType("float", "Declarator", "\\W*((?i)float(?-i))\\W*"));
        tokenTypes.add(new TokenType("out", "Function", "\\W*((?i)out(?-i))\\W*"));
        tokenTypes.add(new TokenType("in", "Function", "\\W*((?i)in(?-i))\\W*"));
        tokenTypes.add(new TokenType("integer", "Constant", "\\d+"));
        tokenTypes.add(new TokenType("operator", "Operator", "[=+/*-]"));
        tokenTypes.add(new TokenType("comment", "Comment", "[#]"));
    }

    public static void tokenizer(String line) {
        tokens.clear();
        line = prepareSourceCode(line);
        String[] arrOfTokens = line.split(" ", 0);
        outerLoop:
        for (String a : arrOfTokens) {
            for (int i = 0; i < tokenTypes.size(); i++) {
                TokenType currentToken = tokenTypes.get(i);
                if (Pattern.matches(currentToken.regex, a)) {
                    //System.out.println("[" + currentToken.type + " : " + a + "]");
                    if (Objects.equals(currentToken.type, "Comment")) {
                        break outerLoop;
                    } else {
                        tokens.add(new Token(a, currentToken.type));
                        break;
                    }
                } else if (i == tokenTypes.size() - 1) {
                    if (!Objects.equals(a, "")) {
                        //System.out.println("[Identifier : " + a + "]");
                        tokens.add(new Token(a, "Identifier"));
                    }
                }
            }
        }
    }

    public static String prepareSourceCode(String line) {
        line = line.replace("(", " ( ");
        line = line.replace(")", " ) ");
        line = line.replace(":", " : ");
        line = line.replace(";", " ; ");
        line = line.replace("[", " [ ");
        line = line.replace("]", " ] ");
        line = line.replace("-", " - ");
        line = line.replace("+", " + ");
        line = line.replace("/", " / ");
        line = line.replace("*", " * ");
        line = line.replace("#", " # ");
        return line;
    }

}




class TokenType {
    String name;
    String type;
    String regex;

    public TokenType(String name, String type, String regex) {
        this.name = name;
        this.type = type;
        this.regex = regex;
    }
}

class Token {
    String value;
    String type;

    public Token(String value, String type) {
        this.value = value;
        this.type = type;
    }
}