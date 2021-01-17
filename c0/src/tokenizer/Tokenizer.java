package tokenizer;

import error.ErrorCode;
import error.TokenizeError;
import util.Pos;

public class Tokenizer {
    private StringIter it;

    public Tokenizer(StringIter it) {
        this.it = it;
    }

    /**
     * 获取下一个 Token
     *
     * @return
     * @throws TokenizeError 如果解析有异常则抛出
     */
    public Token nextToken() throws TokenizeError {
        it.readAll();

        // 跳过之前的所有空白字符
        skipSpaceCharacters();

        if (it.isEOF()) {
            return new Token(TokenType.EOF, "", it.currentPos(), it.currentPos());
        }

        char peek = it.peekChar();
        if (Character.isDigit(peek)) {
            return lexUIntOrDouble();
        } else if (Character.isAlphabetic(peek) || peek == '_') {
            return lexIdentOrKeyword();
        } else if (peek == '"') {
            return lexString();
        } else if (peek == '\'') {
            return lexChar();
        } else {
            return lexOperatorOrCommentOrUnknown();
        }
    }

    private Token lexOperatorOrCommentOrUnknown() throws TokenizeError {
        Pos startPos;
        switch (it.nextChar()) {
            case '+':
                return new Token(TokenType.PLUS, '+', it.previousPos(), it.currentPos());
            case '-':
                startPos = it.previousPos();
                if (it.peekChar() == '>') {
                    it.nextChar();
                    return new Token(TokenType.ARROW, "->", startPos, it.currentPos());
                }
                return new Token(TokenType.MINUS, '-', startPos, it.currentPos());
            case '*':
                return new Token(TokenType.MUL, '*', it.previousPos(), it.currentPos());
            case '/':
                startPos = it.previousPos();
                if (it.peekChar() == '/') {
                    it.nextChar();
//                    StringBuilder tmpToken = new StringBuilder();
//                    tmpToken.append("//");
                    while (it.nextChar() != '\n');
//                        tmpToken.append(it.nextChar());
//                    tmpToken.append(it.nextChar());
//                    return new Token(TokenType.COMMENT, tmpToken.toString(), startPos, it.currentPos());
                    return nextToken();
                }
                return new Token(TokenType.DIV, '/', it.previousPos(), it.currentPos());
            case '=':
                startPos = it.previousPos();
                if (it.peekChar() == '=') {
                    it.nextChar();
                    return new Token(TokenType.EQ, "==", startPos, it.currentPos());
                }
                return new Token(TokenType.ASSIGN, '=', startPos, it.currentPos());
            case '!':
                startPos = it.previousPos();
                if (it.peekChar() == '=') {
                    it.nextChar();
                    return new Token(TokenType.NEQ, "!=", startPos, it.currentPos());
                } else throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
            case '<':
                startPos = it.previousPos();
                if (it.peekChar() == '=') {
                    it.nextChar();
                    return new Token(TokenType.LE, "<=", startPos, it.currentPos());
                }
                return new Token(TokenType.LT, '<', startPos, it.currentPos());
            case '>':
                startPos = it.previousPos();
                if (it.peekChar() == '=') {
                    it.nextChar();
                    return new Token(TokenType.GE, ">=", startPos, it.currentPos());
                }
                return new Token(TokenType.GT, '>', startPos, it.currentPos());
            case '(':
                return new Token(TokenType.L_PAREN, '(', it.previousPos(), it.currentPos());
            case ')':
                return new Token(TokenType.R_PAREN, ')', it.previousPos(), it.currentPos());
            case '{':
                return new Token(TokenType.L_BRACE, '{', it.previousPos(), it.currentPos());
            case '}':
                return new Token(TokenType.R_BRACE, '}', it.previousPos(), it.currentPos());
            case ',':
                return new Token(TokenType.COMMA, ',', it.previousPos(), it.currentPos());
            case ':':
                return new Token(TokenType.COLON, ':', it.previousPos(), it.currentPos());
            case ';':
                return new Token(TokenType.SEMICOLON, ';', it.previousPos(), it.currentPos());
            default:
                throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
        }
    }

    private Token lexChar() throws TokenizeError {
        Pos startPos = it.currentPos();
        char value;
        it.nextChar();
        char peek = it.peekChar();
        if (peek != '\'') {
            if (peek == '\\') {
                it.nextChar();
                switch (it.nextChar()) {
                    case '\\':
                        value = '\\';
                        break;
                    case '"':
                        value = '"';
                        break;
                    case '\'':
                        value = '\'';
                        break;
                    case 'n':
                        value = '\n';
                        break;
                    case 'r':
                        value = '\r';
                        break;
                    case 't':
                        value = '\t';
                        break;
                    default:
                        throw new TokenizeError(ErrorCode.IllegalEscapeSequence, it.previousPos());
                }
            } else value = it.nextChar();
            if (it.peekChar() == '\'') {
                it.nextChar();
                return new Token(TokenType.CHAR, value, startPos, it.currentPos());
            } else throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
        }
        throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
    }
    
public class QuickSort {
	private static int count;
	
	public static void main(String[] args) {
		int[] num = {3,45,78,64,52,11,64,55,99,11,18};
		System.out.println(arrayToString(num,"未排序"));
		QuickSort(num,0,num.length-1);
		System.out.println(arrayToString(num,"排序"));
		System.out.println("数组个数："+num.length);
		System.out.println("循环次数："+count);
		
	}
	/**
	 * 快速排序
	 * @param num	排序的数组
	 * @param left	数组的前针
	 * @param right 数组后针
	 */
	private static void QuickSort(int[] num, int left, int right) {
		//如果left等于right，即数组只有一个元素，直接返回
		if(left>=right) {
			return;
		}
		//设置最左边的元素为基准值
		int key=num[left];
		//数组中比key小的放在左边，比key大的放在右边，key值下标为i
		int i=left;
		int j=right;
		while(i<j){
			//j向左移，直到遇到比key小的值
			while(num[j]>=key && i<j){
				j--;
			}
			//i向右移，直到遇到比key大的值
			while(num[i]<=key && i<j){
				i++;
			}
			//i和j指向的元素交换
			if(i<j){
				int temp=num[i];
				num[i]=num[j];
				num[j]=temp;
			}
		}
		num[left]=num[i];
		num[i]=key;
		count++;
		QuickSort(num,left,i-1);
		QuickSort(num,i+1,right);
	}
	/**
	 * 将一个int类型数组转化为字符串
	 * @param arr
	 * @param flag
	 * @return
	 */
	private static String arrayToString(int[] arr,String flag) {
		String str = "数组为("+flag+")：";
		for(int a : arr) {
			str += a + "\t";
		}
		return str;


    private Token lexString() throws TokenizeError {
        Pos startPos = it.currentPos();
        StringBuilder tmpToken = new StringBuilder();
        it.nextChar();
        char peek;
        while (!it.isEOF() && (peek = it.peekChar()) != '"') {
            if (peek == '\\') {
                it.nextChar();
                switch (it.nextChar()) {
                    case '\\':
                        tmpToken.append('\\');
                        break;
                    case '"':
                        tmpToken.append('"');
                        break;
                    case '\'':
                        tmpToken.append('\'');
                        break;
                    case 'n':
                        tmpToken.append('\n');
                        break;
                    case 'r':
                        tmpToken.append('\r');
                        break;
                    case 't':
                        tmpToken.append('\t');
                        break;
                    default:
                        throw new TokenizeError(ErrorCode.IllegalEscapeSequence, it.previousPos());
                }
            } else tmpToken.append(it.nextChar());
        }
        if (it.isEOF()) {
            throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
        }
        it.nextChar();
        return new Token(TokenType.STRING, tmpToken.toString(), startPos, it.currentPos());
    }

    private Token lexIdentOrKeyword() {
        Pos startPos = it.currentPos();
        StringBuilder tmpToken = new StringBuilder();
        do {
            tmpToken.append(it.nextChar());
        } while (Character.isLetterOrDigit(it.peekChar()) || it.peekChar() == '_');
        String readyToken = tmpToken.toString();
        TokenType tmpType;
        switch (readyToken) {
            case "fn":
                tmpType = TokenType.FN_KW;
                break;
            case "let":
                tmpType = TokenType.LET_KW;
                break;
            case "const":
                tmpType = TokenType.CONST_KW;
                break;
            case "as":
                tmpType = TokenType.AS_KW;
                break;
            case "while":
                tmpType = TokenType.WHILE_KW;
                break;
            case "if":
                tmpType = TokenType.IF_KW;
                break;
            case "else":
                tmpType = TokenType.ELSE_KW;
                break;
            case "return":
                tmpType = TokenType.RETURN_KW;
                break;
            case "break":
                tmpType = TokenType.BREAK_KW;
                break;
            case "continue":
                tmpType = TokenType.CONTINUE_KW;
                break;
            case "int":
                tmpType=TokenType.INT_TY;
                break;
            case "void":
                tmpType=TokenType.VOID_TY;
                break;
            case "double":
                tmpType=TokenType.DOUBLE_TY;
                break;
            default:
                tmpType = TokenType.IDENT;
        }
        return new Token(tmpType, readyToken, startPos, it.currentPos());
    }

    private Token lexUIntOrDouble() throws TokenizeError {
        Pos startPos = it.currentPos();
        StringBuilder tmpToken = new StringBuilder();
        do {
            tmpToken.append(it.nextChar());
        } while (Character.isDigit(it.peekChar()));
        if (it.peekChar() == '.') {
            tmpToken.append(it.nextChar());
            if (Character.isDigit(it.peekChar())) {
                do {
                    tmpToken.append(it.nextChar());
                } while (Character.isDigit(it.peekChar()));
                if (it.peekChar() == 'E' || it.peekChar() == 'e') {
                    tmpToken.append(it.nextChar());
                    if(it.peekChar()=='+'||it.peekChar()=='-')
                        tmpToken.append(it.nextChar());
                    if (Character.isDigit(it.peekChar())) {
                        do {
                            tmpToken.append(it.nextChar());
                        } while (Character.isDigit(it.peekChar()));
                        return new Token(TokenType.DOUBLE, Double.valueOf(tmpToken.toString()), startPos, it.currentPos());
                    } else throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
                } else
                    return new Token(TokenType.DOUBLE, Double.valueOf(tmpToken.toString()), startPos, it.currentPos());
            } else throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
        }
        return new Token(TokenType.UINT, Long.valueOf(tmpToken.toString()), startPos, it.currentPos());
    }

    private void skipSpaceCharacters() {
        while (!it.isEOF() && Character.isWhitespace(it.peekChar())) {
            it.nextChar();
        }
    }
}
