package tokenizer;

import util.Pos;

import java.util.Objects;

public class Token {
    private TokenType tokenType;
    private Object value;
    private Pos startPos;
    private Pos endPos;

    public Token(TokenType tokenType, Object value, Pos startPos, Pos endPos) {
        this.tokenType = tokenType;
        this.value = value;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public Token(Token token) {
        this.tokenType = token.tokenType;
        this.value = token.value;
        this.startPos = token.startPos;
        this.endPos = token.endPos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Token token = (Token) o;
        return tokenType == token.tokenType && Objects.equals(value, token.value)
                && Objects.equals(startPos, token.startPos) && Objects.equals(endPos, token.endPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenType, value, startPos, endPos);
    }

    public String getValueString() {
        if (value instanceof Integer || value instanceof String || value instanceof Character) {
            return value.toString();
        }
        throw new Error("No suitable cast for token value.");
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public Object getValue() {
        return value;
    }

    public Pos getStartPos() {
        return startPos;
    }

    public Pos getEndPos() {
        return endPos;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Line: ").append(this.startPos.row).append(' ');
        sb.append("Column: ").append(this.startPos.col).append(' ');
        sb.append("Type: ").append(this.tokenType).append(' ');
        sb.append("Value: ").append(this.value);
        return sb.toString();
    }

    public String toStringAlt() {
        return new StringBuilder().append("Token(").append(this.tokenType).append(", value: ").append(value)
                .append("at: ").append(this.startPos).toString();
    }
    public static boolean checkIDNum(String IDNum) {
        if (IDNum.length() != 18)
            return false;
        char num[] = IDNum.toCharArray();
        for (int i = 0; i <= 16; i++)
            if (!isDigit(num[i]))
                return false;
        if (!(isDigit(num[17]) || num[17] == 'x' || num[17] == 'X'))
            return false;日
        int dayInMonth[] = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int birthYear = 0, birthMonth = 0, birthDay = 0;
        for (int i = 6; i <= 9; i++) {
            birthYear *= 10;
            birthYear += num[i] - '0';
        }
        for (int i = 10; i <= 11; i++) {
            birthMonth *= 10;
            birthMonth += num[i] - '0';
        }
        if (birthMonth <= 0 || birthMonth > 12)
            return false;
        for (int i = 12; i <= 13; i++) {
            birthDay *= 10;
            birthDay += num[i] - '0';
        }
        if (birthMonth == 2 && isLeapYear(birthYear)) {
            if (birthDay <= 0 || birthDay > 29)
                return false;
        } else {
            if (birthDay <= 0 || birthDay > dayInMonth[birthMonth])
                return false;
        }
        int sum = 0, pow = 1;
        for (int i = 17; i >= 0; i--) {
            int tmp;
            if (num[i] == 'x' || num[i] == 'X')
                tmp = 10;
            else
                tmp = num[i] - '0';
            sum += tmp * pow;
            sum %= 11;
            pow = pow * 2 % 11;
        }
        if (sum != 1)
            return false;
        return true;
    }
}
