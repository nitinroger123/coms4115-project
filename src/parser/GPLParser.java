// Output created by jacc on Sun May 08 13:00:00 EDT 2011

package parser;

  import java.util.*;
  import java.io.*;
  
  import interpreter.types.NumberType;
  import interpreter.types.*;
  import helper.*;

  class SemanticWrapper {
    public Object val;
    public SemanticWrapper(Object val) {
        this.val = val;
    }
  }

  class GPLHelper {
      public static boolean isTrue(Object obj) {
      
        if (obj == null) return false;
      
        if (((Double)(obj)).doubleValue()!=0)
            return true;
        else
            return false;
      }
  }
  
  class GPLLexer implements GPLTokens {
        private int lastChar = 32;
    private int token;
    private SemanticWrapper yylval;
    private boolean toSkip = false;
    private int skipCounter = 0;
    
    //This tells the lexer to start recording the character stream (used for backtracking)
    private boolean record = false;
    private int recordCap = 0;
    private int recordLow = 0;
    private Stack<String> whileBodies = new Stack<String>();
    private Stack<String> whileConditions = new Stack<String>();
    
    private boolean inLiteral = false;
    private boolean inComment = false;
    private boolean waitingForReturn = false;
    private Stack<Integer> blockTypes = new Stack<Integer>();
    
    private String s;
    private Stack<HashMap<String, Object>> scopes = new Stack<HashMap<String, Object>>();
    
    private Stack<Reader> inputs = new Stack<Reader>();
  
    public GPLLexer() throws IOException{
        scopes.push(new HashMap<String, Object>());
        inputs.push(new InputStreamReader(System.in));
    }
    
    public GPLLexer(StringReader reader, Stack<HashMap<String, Object>> scopes) {
        inputs.push(reader);
        this.scopes = scopes;
    }
  
    public Stack<HashMap<String, Object>> getScopes() {
        return scopes;
    }
  
    /** Read a single input character from standard input.
     */
    private int nextChar() {
        try {
            int c = inputs.peek().read();
            if (record) {
                for (int i = recordLow; i < recordCap; i++) {
                    whileBodies.set(i, whileBodies.get(i) + (char)c);
                }
            }
            //if (!toSkip) System.out.println(((char)c=='\n' ? "NL" : (char)c) + " : " + c);
            return c;
        } catch (Exception e) {
            return -1;
        }
    }
    
    private String getLine() {
        try {
            String out = "";
            int c = inputs.peek().read();
            while (c!='\n' && c!='\r') {
                if (record) {
                    for (int i = recordLow; i < recordCap; i++) {
                        whileBodies.set(i, whileBodies.get(i) + (char)c);
                    }
                }
                out+=(char)c;
                c = inputs.peek().read();
            }
            
            if (record) {
                    for (int i = recordLow; i < recordCap; i++) {
                        whileBodies.set(i, whileBodies.get(i) + "\n");
                    }
                }
            
            return out;
        } catch (Exception e) {
          return "";
        }
    }
    
    //Skip to the next elsif, else, or end
    private int skip() {
        toSkip = true;
        skipCounter=0;
        return nextToken();
    }
    
    /** Read the next token and return the
     *  corresponding integer code.
     */
    public int nextToken() {
    
        //System.out.println("Tokening...");
            int c = lastChar;
        s="";
        
        if (inComment) {
            while (c!='\n') {
                c = nextChar();
            }
            inComment = false;
            lastChar = '\n';
            return nextToken();
        }
        if (inLiteral) {
            while (c!='"') {
                s += Character.toString((char)c);
                c = nextChar();
            }
            inLiteral = false;
            if (!toSkip) {
                yylval = new SemanticWrapper(s.substring(1,s.length()));
                return token=STRING_LITERAL;
            } else {
                return nextToken();
            }
        }
                while (c==32 || c==11 || c==13 || c=='\t') {
                        c=nextChar();
                }
                if (c<0) {
            return (token=ENDINPUT);
        }
        if (c == '\n') {
            if (!toSkip) {
                return token=NL;
            } else {
                return nextToken();
            }
        }
        
        lastChar=32;


        
                //Take care of all the special one-character tokens
                switch (c) {
          case '+' : 
                { 
                    if (!toSkip) {
                        return token='+';
                    } else {
                        return nextToken();
                    }
                }
          case '-' : 
                { 
                    if (!toSkip) {
                        return token='-';
                    } else {
                        return nextToken();
                    }
                }
          case '*' : 
                { 
                    if (!toSkip) {
                        return token='*';
                    } else {
                        return nextToken();
                    }
                }
          case '/' : 
                { 
                    if (!toSkip) {
                        return token='/';
                    } else {
                        return nextToken();
                    }
                }
          case '%' : 
                { 
                    if (!toSkip) {
                        return token='%';
                    } else {
                        return nextToken();
                    }
                }
          case '^' : 
                { 
                    if (!toSkip) {
                        return token='^';
                    } else {
                        return nextToken();
                    }
                }
                  case '(' : 
                { 
                    if (!toSkip) {
                        return token='(';
                    } else {
                        return nextToken();
                    }
                }
          case ')' : 
                { 
                    if (!toSkip) {
                        return token=')';
                    } else {
                        return nextToken();
                    }
                }
                  case '=' : 
                    /*
                    lastChar = c;
                    c = nextChar();
                    if (c=='=') {
                        lastChar=32;
                        return token=EQ;
                    }
                    */
                { 
                    if (!toSkip) {
                        return token='=';
                    } else {
                        return nextToken();
                    }
                }
          case '>' : 
                { 
                    if (!toSkip) {
                        return token='>';
                    } else {
                        return nextToken();
                    }
                }
                  case '<' : 
                { 
                    if (!toSkip) {
                        return token='<';
                    } else {
                        return nextToken();
                    }
                }
          case ',' : 
                { 
                    if (!toSkip) {
                        return token=',';
                    } else {
                        return nextToken();
                    }
                }
                  case '.' : 
                { 
                    if (!toSkip) {
                        return token='.';
                    } else {
                        return nextToken();
                    }
                }
                  case '!' : 
                { 
                    if (!toSkip) {
                        return token='!';
                    } else {
                        return nextToken();
                    }
                }
                  case '&' : 
                { 
                    if (!toSkip) {
                        return token='&';
                    } else {
                        return nextToken();
                    }
                }
          case '|' :
                { 
                    if (!toSkip) {
                        return token='|';
                    } else {
                        return nextToken();
                    }
                }
          case '"' :
                     {inLiteral = true; return nextToken();}
          case '#' :
                     {inComment = true; return nextToken();}
                }
        
                while ((c>=65 && c<=90) || (c>=97 && c<=122) || (c>=48 && c<=57) || (c==34) || (s.matches("[0-9]+") && c=='.')) {
                        if (c<0) break;
                        s+=Character.toString((char)c);
                        c=nextChar();
                }
        
        //System.out.println("And s is: " + s);
        
        lastChar = c;
        
            if (s.matches("([+-]?[0-9]*\\.[0-9]+)|([+-]?[0-9]+)")) {
                if (!toSkip) {
                    yylval = new SemanticWrapper(new NumberType(Double.parseDouble(s)));
                    return token=NUMBER;
                } else {
                    return nextToken();
                }
            } else if (s.equals("Number")||s.equals("Graph")||s.equals("String")||s.equals("Function")) {
                if (!toSkip) {
                    yylval = new SemanticWrapper(s);
                    return token=TYPE;
                } else {
                    return nextToken();
                }
            } else if (s.equals("print")) {
                //System.out.println("Reached print!");
                if (!toSkip) {
                    return token=PRINT;
                } else {
                    return nextToken();
                }
            } else if (s.equals("include")) {
                if (!toSkip) {
                    return token=INCLUDE;
                } else {
                    return nextToken();
                }
            } else if (s.equals("def")) {
                if (!toSkip) {
                    scopes.push(new HashMap<String, Object>());
                    return token=DEF;
                } else {
                    skipCounter++;
                    return nextToken();
                }
            } else if (s.equals("if")) {
                //System.out.println("reached if!");
            
                if (!toSkip) {
                    blockTypes.push(0);
                    boolean decision = eval(getLine());
                    
                    if (decision) {
                        scopes.push(new HashMap<String, Object>());
                        return nextToken();
                    } else {
                        return skip();
                    }
                } else {
                    skipCounter++;
                    return nextToken();
                }
                
            } else if (s.equals("elsif")) {
                if (!toSkip) {
                    scopes.pop();
                    return skip();
                    
                } else {
                    if (skipCounter>0) {
                        return nextToken();
                    }
                    String restOfLine = getLine();
                    nextToken();
                    boolean decision = eval(restOfLine);
                    
                    if (decision) {
                        scopes.push(new HashMap<String, Object>());
                        return nextToken();
                    } else {
                        return skip();
                    }
                }
            } else if (s.equals("else")) {
                if (!toSkip) {
                    scopes.pop();
                    return skip();
                } else {
                    if (skipCounter>0) {
                        return nextToken();
                    }
                    toSkip=false;
                    scopes.push(new HashMap<String, Object>());
                    return nextToken();
                }
            } else if (s.equals("while")) {
            
                if (!toSkip) {
                    blockTypes.push(1);
                    String cond = getLine();
                    boolean decision = eval(cond);
                    
                    if (decision) {
                        if (!record) record = true;
                        whileBodies.push("");
                        whileConditions.push(cond);
                        
                        scopes.push(new HashMap<String, Object>());
                        recordCap++;
                        
                        //System.out.println("Noticed while..." + recordLow + "," + recordCap);
                        
                        return nextToken();
                    } else {
                        return skip();
                    }
                } else {
                    skipCounter++;
                    return nextToken();
                }
            } else if (s.equals("next")) {
                if (!toSkip) {
                    scopes.pop();
                    return token=NEXT;
                } else {
                    return nextToken();
                }
            } else if (s.equals("last")) {
                if (!toSkip) {
                    scopes.pop();
                    return token=LAST;
                } else {
                    return nextToken();
                }
                
            } else if (s.equals("end")) {
                if (!toSkip) {
                    //This is the end tag for a while loop
                    if (blockTypes.peek()==1) {
                        //Strip the "end" line from the while body
                        
                        String whileBody = whileBodies.peek().substring(0,whileBodies.peek().length()-4);
                        
                        String cond = whileConditions.peek();
                        
                        int lastRecordLow = recordLow;
                        int lastRecordCap = recordCap;
                        
                        //System.out.println("First noticed end..." + recordLow + "," + recordCap);
                        
                        while (eval(cond)) {
                            
                        
                            recordLow = whileBodies.size();
                            recordCap = whileBodies.size();
                            
                            //System.out.println("During while..." + recordLow + "," + recordCap);
                            scopes.pop();
                            scopes.push(new HashMap<String, Object>());
                            eval(whileBody,false);
                        }
                        recordLow=lastRecordLow;
                        recordCap=lastRecordCap - 1;
                        
                        //System.out.println("After end..." + recordLow + "," + recordCap);
                        
                        whileBodies.pop();
                        whileConditions.pop();
                        if (whileBodies.size()==0) record = false;
                    } 
                    blockTypes.pop();
                    scopes.pop();
                    return nextToken();
                } else {
                    if (skipCounter>0) {
                        skipCounter--;
                        return nextToken();
                    }
                    blockTypes.pop();
                    toSkip=false;
                    return nextToken();
                }
            } else if (s.equals("exit")) {
                return token=EXIT;
            } else if (s.equals("return")) {
                return token=RETURN;
            } else if (s.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                yylval = new SemanticWrapper(s);
                return token=ID;
            }
            
                return -1;
    }
    
    /** Return the token code for the current lexeme.
     */
    public int getToken() {
        return token;
    }
        
    private boolean eval(String code) {
        return eval(code, true);
    }
    
    /**
        Does not handle scopes, caller takes care of it!
    */
    private boolean eval(String code, boolean interactive) {
        
        
        System.out.println("---------------- Eval: --------------");
        System.out.println(code);
        System.out.println("-------------------------------------");
        
        inputs.push(new StringReader(code));
        
        if (token == 0) token = 12;
        
        boolean savedRecord = false;
        if (interactive) {
            savedRecord = record;
            record = false;
        }
        
        GPLParser liner = new GPLParser(this);
        liner.setScopes(scopes);
        liner.setInteractive(interactive);
        liner.parse();
        inputs.pop();
        
        if (interactive) {
            record = savedRecord;
        }
        
        //System.out.println(liner.getReturn());
        
        if (liner.getReturn()==null) return false;
        return GPLHelper.isTrue(((NumberType)(liner.getReturn().val)).getDouble());
    }
    
        SemanticWrapper getSemantic() {
                return yylval;
        }
  }

  class Main {
    public static void main(String[] args) throws IOException {
      GPLLexer  lexer  = new GPLLexer();
      lexer.nextToken();
      GPLParser parser = new GPLParser(lexer);
      parser.parse();
    }

    static void error(String msg) {
      System.out.println("ERROR: " + msg);
      System.exit(1);
    }
  }

class GPLParser implements GPLTokens {
    private int yyss = 100;
    private int yytok;
    private int yysp = 0;
    private int[] yyst;
    protected int yyerrno = (-1);
    private SemanticWrapper[] yysv;
    private SemanticWrapper yyrv;

    public boolean parse() {
        int yyn = 0;
        yysp = 0;
        yyst = new int[yyss];
        yysv = new SemanticWrapper[yyss];
        yytok = (lexer.getToken()
                 );
    loop:
        for (;;) {
            switch (yyn) {
                case 0:
                    yyst[yysp] = 0;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 111:
                    yyn = yys0();
                    continue;

                case 1:
                    yyst[yysp] = 1;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 112:
                    switch (yytok) {
                        case ENDINPUT:
                            yyn = 222;
                            continue;
                        case NL:
                            yyn = 24;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 2:
                    yyst[yysp] = 2;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 113:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr19();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 3:
                    yyst[yysp] = 3;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 114:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr2();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 4:
                    yyst[yysp] = 4;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 115:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr18();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 5:
                    yyst[yysp] = 5;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 116:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr4();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 6:
                    yyst[yysp] = 6;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 117:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr20();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 7:
                    yyst[yysp] = 7;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 118:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr3();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 8:
                    yyst[yysp] = 8;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 119:
                    yyn = yys8();
                    continue;

                case 9:
                    yyst[yysp] = 9;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 120:
                    yyn = yys9();
                    continue;

                case 10:
                    yyst[yysp] = 10;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 121:
                    switch (yytok) {
                        case ID:
                            yyn = 33;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 11:
                    yyst[yysp] = 11;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 122:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr17();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 12:
                    yyst[yysp] = 12;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 123:
                    yyn = yys12();
                    continue;

                case 13:
                    yyst[yysp] = 13;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 124:
                    switch (yytok) {
                        case '(':
                            yyn = 37;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 14:
                    yyst[yysp] = 14;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 125:
                    switch (yytok) {
                        case '(':
                            yyn = 38;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 15:
                    yyst[yysp] = 15;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 126:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr14();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 16:
                    yyst[yysp] = 16;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 127:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr13();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 17:
                    yyst[yysp] = 17;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 128:
                    yyn = yys17();
                    continue;

                case 18:
                    yyst[yysp] = 18;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 129:
                    switch (yytok) {
                        case '(':
                            yyn = 39;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 19:
                    yyst[yysp] = 19;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 130:
                    yyn = yys19();
                    continue;

                case 20:
                    yyst[yysp] = 20;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 131:
                    yyn = yys20();
                    continue;

                case 21:
                    yyst[yysp] = 21;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 132:
                    switch (yytok) {
                        case ID:
                            yyn = 42;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 22:
                    yyst[yysp] = 22;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 133:
                    switch (yytok) {
                        case '(':
                            yyn = 43;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 23:
                    yyst[yysp] = 23;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 134:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 45;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 24:
                    yyst[yysp] = 24;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 135:
                    yyn = yys24();
                    continue;

                case 25:
                    yyst[yysp] = 25;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 136:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 45;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 26:
                    yyst[yysp] = 26;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 137:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 45;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 27:
                    yyst[yysp] = 27;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 138:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 45;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 28:
                    yyst[yysp] = 28;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 139:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 45;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 29:
                    yyst[yysp] = 29;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 140:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 45;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 30:
                    yyst[yysp] = 30;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 141:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 45;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 31:
                    yyst[yysp] = 31;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 142:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 45;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 32:
                    yyst[yysp] = 32;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 143:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 45;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 33:
                    yyst[yysp] = 33;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 144:
                    switch (yytok) {
                        case '(':
                            yyn = 55;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 34:
                    yyst[yysp] = 34;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 145:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 41;
                            continue;
                        case ')':
                            yyn = yyr37();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 35:
                    yyst[yysp] = 35;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 146:
                    switch (yytok) {
                        case ID:
                            yyn = 59;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 36:
                    yyst[yysp] = 36;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 147:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 41;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 37:
                    yyst[yysp] = 37;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 148:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 41;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 38:
                    yyst[yysp] = 38;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 149:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 41;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 39:
                    yyst[yysp] = 39;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 150:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 41;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 40:
                    yyst[yysp] = 40;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 151:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr15();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 41:
                    yyst[yysp] = 41;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 152:
                    yyn = yys41();
                    continue;

                case 42:
                    yyst[yysp] = 42;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 153:
                    switch (yytok) {
                        case '=':
                            yyn = 64;
                            continue;
                        case NL:
                        case ENDINPUT:
                            yyn = yyr39();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 43:
                    yyst[yysp] = 43;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 154:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 41;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 44:
                    yyst[yysp] = 44;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 155:
                    yyn = yys44();
                    continue;

                case 45:
                    yyst[yysp] = 45;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 156:
                    yyn = yys45();
                    continue;

                case 46:
                    yyst[yysp] = 46;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 157:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr1();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 47:
                    yyst[yysp] = 47;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 158:
                    yyn = yys47();
                    continue;

                case 48:
                    yyst[yysp] = 48;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 159:
                    yyn = yys48();
                    continue;

                case 49:
                    yyst[yysp] = 49;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 160:
                    yyn = yys49();
                    continue;

                case 50:
                    yyst[yysp] = 50;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 161:
                    yyn = yys50();
                    continue;

                case 51:
                    yyst[yysp] = 51;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 162:
                    yyn = yys51();
                    continue;

                case 52:
                    yyst[yysp] = 52;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 163:
                    yyn = yys52();
                    continue;

                case 53:
                    yyst[yysp] = 53;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 164:
                    yyn = yys53();
                    continue;

                case 54:
                    yyst[yysp] = 54;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 165:
                    yyn = yys54();
                    continue;

                case 55:
                    yyst[yysp] = 55;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 166:
                    switch (yytok) {
                        case TYPE:
                            yyn = 69;
                            continue;
                        case ')':
                            yyn = yyr33();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 56:
                    yyst[yysp] = 56;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 167:
                    switch (yytok) {
                        case ')':
                            yyn = yyr36();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 57:
                    yyst[yysp] = 57;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 168:
                    switch (yytok) {
                        case ')':
                            yyn = 70;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 58:
                    yyst[yysp] = 58;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 169:
                    switch (yytok) {
                        case ',':
                            yyn = 71;
                            continue;
                        case ')':
                            yyn = yyr35();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 59:
                    yyst[yysp] = 59;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 170:
                    switch (yytok) {
                        case '(':
                            yyn = 72;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 60:
                    yyst[yysp] = 60;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 171:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr40();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 61:
                    yyst[yysp] = 61;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 172:
                    switch (yytok) {
                        case ')':
                            yyn = 73;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 62:
                    yyst[yysp] = 62;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 173:
                    switch (yytok) {
                        case ')':
                            yyn = 74;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 63:
                    yyst[yysp] = 63;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 174:
                    switch (yytok) {
                        case ')':
                            yyn = 75;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 64:
                    yyst[yysp] = 64;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 175:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 41;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 65:
                    yyst[yysp] = 65;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 176:
                    switch (yytok) {
                        case ')':
                            yyn = 77;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 66:
                    yyst[yysp] = 66;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 177:
                    yyn = yys66();
                    continue;

                case 67:
                    yyst[yysp] = 67;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 178:
                    switch (yytok) {
                        case ')':
                            yyn = yyr32();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 68:
                    yyst[yysp] = 68;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 179:
                    switch (yytok) {
                        case ')':
                            yyn = 78;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 69:
                    yyst[yysp] = 69;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 180:
                    switch (yytok) {
                        case ID:
                            yyn = 79;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 70:
                    yyst[yysp] = 70;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 181:
                    switch (yytok) {
                        case ',':
                        case ')':
                        case NL:
                        case ENDINPUT:
                            yyn = yyr42();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 71:
                    yyst[yysp] = 71;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 182:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 41;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 72:
                    yyst[yysp] = 72;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 183:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 41;
                            continue;
                        case ')':
                            yyn = yyr37();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 73:
                    yyst[yysp] = 73;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 184:
                    switch (yytok) {
                        case NL:
                            yyn = 82;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 74:
                    yyst[yysp] = 74;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 185:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr11();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 75:
                    yyst[yysp] = 75;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 186:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr12();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 76:
                    yyst[yysp] = 76;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 187:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr38();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 77:
                    yyst[yysp] = 77;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 188:
                    switch (yytok) {
                        case NL:
                            yyn = 83;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 78:
                    yyst[yysp] = 78;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 189:
                    switch (yytok) {
                        case NL:
                            yyn = 84;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 79:
                    yyst[yysp] = 79;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 190:
                    switch (yytok) {
                        case ',':
                            yyn = 85;
                            continue;
                        case ')':
                            yyn = yyr31();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 80:
                    yyst[yysp] = 80;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 191:
                    switch (yytok) {
                        case ')':
                            yyn = yyr34();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 81:
                    yyst[yysp] = 81;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 192:
                    switch (yytok) {
                        case ')':
                            yyn = 86;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 82:
                    yyst[yysp] = 82;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 193:
                    yyn = yys82();
                    continue;

                case 83:
                    yyst[yysp] = 83;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 194:
                    yyn = yys83();
                    continue;

                case 84:
                    yyst[yysp] = 84;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 195:
                    yyn = yys84();
                    continue;

                case 85:
                    yyst[yysp] = 85;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 196:
                    switch (yytok) {
                        case TYPE:
                            yyn = 69;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 86:
                    yyst[yysp] = 86;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 197:
                    switch (yytok) {
                        case ',':
                        case ')':
                        case NL:
                        case ENDINPUT:
                            yyn = yyr41();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 87:
                    yyst[yysp] = 87;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 198:
                    switch (yytok) {
                        case NL:
                            yyn = 92;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 88:
                    yyst[yysp] = 88;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 199:
                    switch (yytok) {
                        case NL:
                            yyn = yyr7();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 89:
                    yyst[yysp] = 89;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 200:
                    switch (yytok) {
                        case NL:
                            yyn = 93;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 90:
                    yyst[yysp] = 90;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 201:
                    switch (yytok) {
                        case NL:
                            yyn = 94;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 91:
                    yyst[yysp] = 91;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 202:
                    switch (yytok) {
                        case ')':
                            yyn = yyr30();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 92:
                    yyst[yysp] = 92;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 203:
                    yyn = yys92();
                    continue;

                case 93:
                    yyst[yysp] = 93;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 204:
                    yyn = yys93();
                    continue;

                case 94:
                    yyst[yysp] = 94;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 205:
                    yyn = yys94();
                    continue;

                case 95:
                    yyst[yysp] = 95;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 206:
                    switch (yytok) {
                        case END:
                            yyn = 101;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 96:
                    yyst[yysp] = 96;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 207:
                    switch (yytok) {
                        case NL:
                            yyn = yyr8();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 97:
                    yyst[yysp] = 97;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 208:
                    yyn = yys97();
                    continue;

                case 98:
                    yyst[yysp] = 98;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 209:
                    switch (yytok) {
                        case '(':
                            yyn = 103;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 99:
                    yyst[yysp] = 99;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 210:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr10();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 100:
                    yyst[yysp] = 100;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 211:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr6();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 101:
                    yyst[yysp] = 101;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 212:
                    switch (yytok) {
                        case NL:
                        case ENDINPUT:
                            yyn = yyr9();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 102:
                    yyst[yysp] = 102;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 213:
                    switch (yytok) {
                        case NL:
                            yyn = 104;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 103:
                    yyst[yysp] = 103;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 214:
                    switch (yytok) {
                        case NUMBER:
                            yyn = 17;
                            continue;
                        case STRING_LITERAL:
                            yyn = 20;
                            continue;
                        case '(':
                            yyn = 23;
                            continue;
                        case ID:
                            yyn = 41;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 104:
                    yyst[yysp] = 104;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 215:
                    switch (yytok) {
                        case END:
                            yyn = yyr21();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 105:
                    yyst[yysp] = 105;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 216:
                    switch (yytok) {
                        case ')':
                            yyn = 106;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 106:
                    yyst[yysp] = 106;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 217:
                    switch (yytok) {
                        case NL:
                            yyn = 107;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 107:
                    yyst[yysp] = 107;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 218:
                    yyn = yys107();
                    continue;

                case 108:
                    yyst[yysp] = 108;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 219:
                    switch (yytok) {
                        case NL:
                            yyn = 109;
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 109:
                    yyst[yysp] = 109;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 220:
                    switch (yytok) {
                        case ELSE:
                            yyn = 97;
                            continue;
                        case ELSIF:
                            yyn = 98;
                            continue;
                        case END:
                            yyn = yyr23();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 110:
                    yyst[yysp] = 110;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 221:
                    switch (yytok) {
                        case END:
                            yyn = yyr22();
                            continue;
                    }
                    yyn = 225;
                    continue;

                case 222:
                    return true;
                case 223:
                    yyerror("stack overflow");
                case 224:
                    return false;
                case 225:
                    yyerror("syntax error");
                    return false;
            }
        }
    }

    protected void yyexpand() {
        int[] newyyst = new int[2*yyst.length];
        SemanticWrapper[] newyysv = new SemanticWrapper[2*yyst.length];
        for (int i=0; i<yyst.length; i++) {
            newyyst[i] = yyst[i];
            newyysv[i] = yysv[i];
        }
        yyst = newyyst;
        yysv = newyysv;
    }

    private int yys0() {
        switch (yytok) {
            case DEF:
                return 10;
            case EXIT:
                return 11;
            case ID:
                return 12;
            case IF:
                return 13;
            case INCLUDE:
                return 14;
            case LAST:
                return 15;
            case NEXT:
                return 16;
            case NUMBER:
                return 17;
            case PRINT:
                return 18;
            case RETURN:
                return 19;
            case STRING_LITERAL:
                return 20;
            case TYPE:
                return 21;
            case WHILE:
                return 22;
            case '(':
                return 23;
            case NL:
            case ENDINPUT:
                return yyr5();
        }
        return 225;
    }

    private int yys8() {
        switch (yytok) {
            case '/':
            case '-':
            case ',':
            case '+':
            case '*':
            case ')':
            case NL:
            case '&':
            case '%':
            case ENDINPUT:
            case '|':
            case '^':
                return yyr53();
        }
        return 225;
    }

    private int yys9() {
        switch (yytok) {
            case '%':
                return 25;
            case '&':
                return 26;
            case '*':
                return 27;
            case '+':
                return 28;
            case '-':
                return 29;
            case '/':
                return 30;
            case '^':
                return 31;
            case '|':
                return 32;
            case ',':
            case ')':
            case NL:
            case ENDINPUT:
                return yyr43();
        }
        return 225;
    }

    private int yys12() {
        switch (yytok) {
            case '(':
                return 34;
            case '.':
                return 35;
            case '=':
                return 36;
            case '/':
            case '-':
            case '+':
            case '*':
            case NL:
            case '&':
            case '%':
            case ENDINPUT:
            case '|':
            case '^':
                return yyr54();
        }
        return 225;
    }

    private int yys17() {
        switch (yytok) {
            case '/':
            case '-':
            case ',':
            case '+':
            case '*':
            case ')':
            case NL:
            case '&':
            case '%':
            case ENDINPUT:
            case '|':
            case '^':
                return yyr55();
        }
        return 225;
    }

    private int yys19() {
        switch (yytok) {
            case NUMBER:
                return 17;
            case STRING_LITERAL:
                return 20;
            case '(':
                return 23;
            case ID:
                return 41;
            case NL:
            case ENDINPUT:
                return yyr16();
        }
        return 225;
    }

    private int yys20() {
        switch (yytok) {
            case '/':
            case '-':
            case ',':
            case '+':
            case '*':
            case ')':
            case NL:
            case '&':
            case '%':
            case ENDINPUT:
            case '|':
            case '^':
                return yyr56();
        }
        return 225;
    }

    private int yys24() {
        switch (yytok) {
            case DEF:
                return 10;
            case EXIT:
                return 11;
            case ID:
                return 12;
            case IF:
                return 13;
            case INCLUDE:
                return 14;
            case LAST:
                return 15;
            case NEXT:
                return 16;
            case NUMBER:
                return 17;
            case PRINT:
                return 18;
            case RETURN:
                return 19;
            case STRING_LITERAL:
                return 20;
            case TYPE:
                return 21;
            case WHILE:
                return 22;
            case '(':
                return 23;
            case NL:
            case ENDINPUT:
                return yyr5();
        }
        return 225;
    }

    private int yys41() {
        switch (yytok) {
            case '(':
                return 34;
            case '.':
                return 35;
            case '/':
            case '-':
            case ',':
            case '+':
            case '*':
            case ')':
            case NL:
            case '&':
            case '%':
            case ENDINPUT:
            case '|':
            case '^':
                return yyr54();
        }
        return 225;
    }

    private int yys44() {
        switch (yytok) {
            case '%':
                return 25;
            case '&':
                return 26;
            case '*':
                return 27;
            case '+':
                return 28;
            case '-':
                return 29;
            case '/':
                return 30;
            case '^':
                return 31;
            case '|':
                return 32;
            case ')':
                return 66;
        }
        return 225;
    }

    private int yys45() {
        switch (yytok) {
            case '/':
            case '-':
            case ',':
            case '+':
            case '*':
            case ')':
            case NL:
            case '&':
            case '%':
            case ENDINPUT:
            case '|':
            case '^':
                return yyr54();
        }
        return 225;
    }

    private int yys47() {
        switch (yytok) {
            case '&':
                return 26;
            case '^':
                return 31;
            case '|':
                return 32;
            case '/':
            case '-':
            case ',':
            case '+':
            case '*':
            case ')':
            case NL:
            case '%':
            case ENDINPUT:
                return yyr49();
        }
        return 225;
    }

    private int yys48() {
        switch (yytok) {
            case '/':
            case '-':
            case ',':
            case '+':
            case '*':
            case ')':
            case NL:
            case '&':
            case '%':
            case ENDINPUT:
            case '|':
            case '^':
                return yyr51();
        }
        return 225;
    }

    private int yys49() {
        switch (yytok) {
            case '&':
                return 26;
            case '^':
                return 31;
            case '|':
                return 32;
            case '/':
            case '-':
            case ',':
            case '+':
            case '*':
            case ')':
            case NL:
            case '%':
            case ENDINPUT:
                return yyr46();
        }
        return 225;
    }

    private int yys50() {
        switch (yytok) {
            case '%':
                return 25;
            case '&':
                return 26;
            case '*':
                return 27;
            case '/':
                return 30;
            case '^':
                return 31;
            case '|':
                return 32;
            case '-':
            case ',':
            case '+':
            case ')':
            case NL:
            case ENDINPUT:
                return yyr44();
        }
        return 225;
    }

    private int yys51() {
        switch (yytok) {
            case '%':
                return 25;
            case '&':
                return 26;
            case '*':
                return 27;
            case '/':
                return 30;
            case '^':
                return 31;
            case '|':
                return 32;
            case '-':
            case ',':
            case '+':
            case ')':
            case NL:
            case ENDINPUT:
                return yyr45();
        }
        return 225;
    }

    private int yys52() {
        switch (yytok) {
            case '&':
                return 26;
            case '^':
                return 31;
            case '|':
                return 32;
            case '/':
            case '-':
            case ',':
            case '+':
            case '*':
            case ')':
            case NL:
            case '%':
            case ENDINPUT:
                return yyr47();
        }
        return 225;
    }

    private int yys53() {
        switch (yytok) {
            case '&':
                return 26;
            case '^':
                return 31;
            case '|':
                return 32;
            case '/':
            case '-':
            case ',':
            case '+':
            case '*':
            case ')':
            case NL:
            case '%':
            case ENDINPUT:
                return yyr48();
        }
        return 225;
    }

    private int yys54() {
        switch (yytok) {
            case '&':
                return 26;
            case '/':
            case '-':
            case ',':
            case '+':
            case '*':
            case ')':
            case NL:
            case '%':
            case ENDINPUT:
            case '|':
            case '^':
                return yyr50();
        }
        return 225;
    }

    private int yys66() {
        switch (yytok) {
            case '/':
            case '-':
            case ',':
            case '+':
            case '*':
            case ')':
            case NL:
            case '&':
            case '%':
            case ENDINPUT:
            case '|':
            case '^':
                return yyr52();
        }
        return 225;
    }

    private int yys82() {
        switch (yytok) {
            case EXIT:
                return 11;
            case ID:
                return 12;
            case IF:
                return 13;
            case INCLUDE:
                return 14;
            case LAST:
                return 15;
            case NEXT:
                return 16;
            case NUMBER:
                return 17;
            case PRINT:
                return 18;
            case RETURN:
                return 19;
            case STRING_LITERAL:
                return 20;
            case TYPE:
                return 21;
            case WHILE:
                return 22;
            case '(':
                return 23;
        }
        return 225;
    }

    private int yys83() {
        switch (yytok) {
            case EXIT:
                return 11;
            case ID:
                return 12;
            case IF:
                return 13;
            case INCLUDE:
                return 14;
            case LAST:
                return 15;
            case NEXT:
                return 16;
            case NUMBER:
                return 17;
            case PRINT:
                return 18;
            case RETURN:
                return 19;
            case STRING_LITERAL:
                return 20;
            case TYPE:
                return 21;
            case WHILE:
                return 22;
            case '(':
                return 23;
        }
        return 225;
    }

    private int yys84() {
        switch (yytok) {
            case EXIT:
                return 11;
            case ID:
                return 12;
            case IF:
                return 13;
            case INCLUDE:
                return 14;
            case LAST:
                return 15;
            case NEXT:
                return 16;
            case NUMBER:
                return 17;
            case PRINT:
                return 18;
            case RETURN:
                return 19;
            case STRING_LITERAL:
                return 20;
            case TYPE:
                return 21;
            case WHILE:
                return 22;
            case '(':
                return 23;
        }
        return 225;
    }

    private int yys92() {
        switch (yytok) {
            case EXIT:
                return 11;
            case ID:
                return 12;
            case IF:
                return 13;
            case INCLUDE:
                return 14;
            case LAST:
                return 15;
            case NEXT:
                return 16;
            case NUMBER:
                return 17;
            case PRINT:
                return 18;
            case RETURN:
                return 19;
            case STRING_LITERAL:
                return 20;
            case TYPE:
                return 21;
            case WHILE:
                return 22;
            case '(':
                return 23;
            case ELSE:
                return 97;
            case ELSIF:
                return 98;
            case END:
                return yyr23();
        }
        return 225;
    }

    private int yys93() {
        switch (yytok) {
            case EXIT:
                return 11;
            case ID:
                return 12;
            case IF:
                return 13;
            case INCLUDE:
                return 14;
            case LAST:
                return 15;
            case NEXT:
                return 16;
            case NUMBER:
                return 17;
            case PRINT:
                return 18;
            case RETURN:
                return 19;
            case STRING_LITERAL:
                return 20;
            case TYPE:
                return 21;
            case WHILE:
                return 22;
            case '(':
                return 23;
            case END:
                return 99;
        }
        return 225;
    }

    private int yys94() {
        switch (yytok) {
            case EXIT:
                return 11;
            case ID:
                return 12;
            case IF:
                return 13;
            case INCLUDE:
                return 14;
            case LAST:
                return 15;
            case NEXT:
                return 16;
            case NUMBER:
                return 17;
            case PRINT:
                return 18;
            case RETURN:
                return 19;
            case STRING_LITERAL:
                return 20;
            case TYPE:
                return 21;
            case WHILE:
                return 22;
            case '(':
                return 23;
            case END:
                return 100;
        }
        return 225;
    }

    private int yys97() {
        switch (yytok) {
            case EXIT:
                return 11;
            case ID:
                return 12;
            case IF:
                return 13;
            case INCLUDE:
                return 14;
            case LAST:
                return 15;
            case NEXT:
                return 16;
            case NUMBER:
                return 17;
            case PRINT:
                return 18;
            case RETURN:
                return 19;
            case STRING_LITERAL:
                return 20;
            case TYPE:
                return 21;
            case WHILE:
                return 22;
            case '(':
                return 23;
        }
        return 225;
    }

    private int yys107() {
        switch (yytok) {
            case EXIT:
                return 11;
            case ID:
                return 12;
            case IF:
                return 13;
            case INCLUDE:
                return 14;
            case LAST:
                return 15;
            case NEXT:
                return 16;
            case NUMBER:
                return 17;
            case PRINT:
                return 18;
            case RETURN:
                return 19;
            case STRING_LITERAL:
                return 20;
            case TYPE:
                return 21;
            case WHILE:
                return 22;
            case '(':
                return 23;
        }
        return 225;
    }

    private int yyr1() { // start : start NL block
        yysp -= 3;
        return 1;
    }

    private int yyr2() { // start : block
        yysp -= 1;
        return 1;
    }

    private int yyr7() { // actions : action
        yysp -= 1;
        return yypactions();
    }

    private int yyr8() { // actions : actions NL action
        yysp -= 3;
        return yypactions();
    }

    private int yypactions() {
        switch (yyst[yysp-1]) {
            case 83: return 89;
            case 82: return 87;
            default: return 90;
        }
    }

    private int yyr34() { // args : expr ',' args
        {((ArrayList<Object>)yyrv.val).add(0,yysv[yysp-3].val);}
        yysv[yysp-=3] = yyrv;
        return yypargs();
    }

    private int yyr35() { // args : expr
        {yyrv = new SemanticWrapper(new ArrayList<Object>()); ((ArrayList<Object>)(yyrv.val)).add(yysv[yysp-1].val);}
        yysv[yysp-=1] = yyrv;
        return yypargs();
    }

    private int yypargs() {
        switch (yyst[yysp-1]) {
            case 71: return 80;
            default: return 56;
        }
    }

    private int yyr36() { // argswrapper : args
        yysp -= 1;
        return yypargswrapper();
    }

    private int yyr37() { // argswrapper : /* empty */
        return yypargswrapper();
    }

    private int yypargswrapper() {
        switch (yyst[yysp-1]) {
            case 34: return 57;
            default: return 81;
        }
    }

    private int yyr40() { // assignment : ID '=' expr
        {modify((String)yysv[yysp-3].val, yysv[yysp-1].val); setReturn(yysv[yysp-1]); }
        yysv[yysp-=3] = yyrv;
        return 2;
    }

    private int yyr3() { // block : action
        yysp -= 1;
        return yypblock();
    }

    private int yyr4() { // block : defun
        yysp -= 1;
        return yypblock();
    }

    private int yyr5() { // block : /* empty */
        return yypblock();
    }

    private int yypblock() {
        switch (yyst[yysp-1]) {
            case 0: return 3;
            default: return 46;
        }
    }

    private int yyr38() { // declaration : TYPE ID '=' expr
        {declare((String)yysv[yysp-4].val,(String)yysv[yysp-3].val,yysv[yysp-1].val);}
        yysv[yysp-=4] = yyrv;
        return 4;
    }

    private int yyr39() { // declaration : TYPE ID
        {declare((String)yysv[yysp-2].val,(String)yysv[yysp-1].val,null);}
        yysv[yysp-=2] = yyrv;
        return 4;
    }

    private int yyr30() { // defargs : TYPE ID ',' defargs
        yysp -= 4;
        return yypdefargs();
    }

    private int yyr31() { // defargs : TYPE ID
        yysp -= 2;
        return yypdefargs();
    }

    private int yypdefargs() {
        switch (yyst[yysp-1]) {
            case 55: return 67;
            default: return 91;
        }
    }

    private int yyr32() { // defargswrapper : defargs
        yysp -= 1;
        return 68;
    }

    private int yyr33() { // defargswrapper : /* empty */
        return 68;
    }

    private int yyr6() { // defun : DEF ID '(' defargswrapper ')' NL actions NL END
        yysp -= 9;
        return 5;
    }

    private int yyr21() { // elsec : ELSE action NL
        yysp -= 3;
        return yypelsec();
    }

    private int yyr22() { // elsec : ELSIF '(' expr ')' NL action NL elsec
        yysp -= 8;
        return yypelsec();
    }

    private int yyr23() { // elsec : /* empty */
        return yypelsec();
    }

    private int yypelsec() {
        switch (yyst[yysp-1]) {
            case 92: return 95;
            default: return 110;
        }
    }

    private int yyr41() { // expr : ID '.' ID '(' argswrapper ')'
        {yyrv = new SemanticWrapper(invoke((String)(yysv[yysp-6].val), (String)(yysv[yysp-4].val), (ArrayList<Object>)(yysv[yysp-2].val)));}
        yysv[yysp-=6] = yyrv;
        return yypexpr();
    }

    private int yyr42() { // expr : ID '(' argswrapper ')'
        yysp -= 4;
        return yypexpr();
    }

    private int yyr43() { // expr : valexpr
        {yyrv.val = yysv[yysp-1].val;}
        yysv[yysp-=1] = yyrv;
        return yypexpr();
    }

    private int yypexpr() {
        switch (yyst[yysp-1]) {
            case 103: return 105;
            case 72: return 58;
            case 71: return 58;
            case 64: return 76;
            case 43: return 65;
            case 39: return 63;
            case 38: return 62;
            case 37: return 61;
            case 36: return 60;
            case 34: return 58;
            case 19: return 40;
            default: return 6;
        }
    }

    private int yyr9() { // action : IF '(' expr ')' NL actions NL elsec END
        yysp -= 9;
        return yypaction();
    }

    private int yyr10() { // action : WHILE '(' expr ')' NL actions NL END
        yysp -= 8;
        return yypaction();
    }

    private int yyr11() { // action : INCLUDE '(' expr ')'
        yysp -= 4;
        return yypaction();
    }

    private int yyr12() { // action : PRINT '(' expr ')'
        {System.out.println(((Type)yysv[yysp-2].val).getValue()); setReturn(null); }
        yysv[yysp-=4] = yyrv;
        return yypaction();
    }

    private int yyr13() { // action : NEXT
        yysp -= 1;
        return yypaction();
    }

    private int yyr14() { // action : LAST
        yysp -= 1;
        return yypaction();
    }

    private int yyr15() { // action : RETURN expr
        yysp -= 2;
        return yypaction();
    }

    private int yyr16() { // action : RETURN
        yysp -= 1;
        return yypaction();
    }

    private int yyr17() { // action : EXIT
        {System.exit(0); setReturn(null); }
        yysv[yysp-=1] = yyrv;
        return yypaction();
    }

    private int yyr18() { // action : declaration
        yysp -= 1;
        return yypaction();
    }

    private int yyr19() { // action : assignment
        yysp -= 1;
        return yypaction();
    }

    private int yyr20() { // action : expr
        yysp -= 1;
        return yypaction();
    }

    private int yypaction() {
        switch (yyst[yysp-1]) {
            case 107: return 108;
            case 97: return 102;
            case 84: return 88;
            case 83: return 88;
            case 82: return 88;
            case 24: return 7;
            case 0: return 7;
            default: return 96;
        }
    }

    private int yyr54() { // symbol : ID
        {yyrv = new SemanticWrapper(lookup((String)yysv[yysp-1].val)); setReturn(yyrv); }
        yysv[yysp-=1] = yyrv;
        return 8;
    }

    private int yyr55() { // symbol : NUMBER
        {yyrv = new SemanticWrapper(yysv[yysp-1].val); setReturn(yyrv); }
        yysv[yysp-=1] = yyrv;
        return 8;
    }

    private int yyr56() { // symbol : STRING_LITERAL
        {yyrv = new SemanticWrapper(new StringType((String)(yysv[yysp-1].val)));}
        yysv[yysp-=1] = yyrv;
        return 8;
    }

    private int yyr44() { // valexpr : valexpr '+' valexpr
        {yyrv = new SemanticWrapper(NumberType.add((NumberType)(yysv[yysp-3].val),(NumberType)(yysv[yysp-1].val))); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr45() { // valexpr : valexpr '-' valexpr
        {yyrv = new SemanticWrapper(NumberType.subtract((NumberType)(yysv[yysp-3].val),(NumberType)(yysv[yysp-1].val))); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr46() { // valexpr : valexpr '*' valexpr
        {yyrv = new SemanticWrapper(NumberType.multiply((NumberType)(yysv[yysp-3].val),(NumberType)(yysv[yysp-1].val))); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr47() { // valexpr : valexpr '/' valexpr
        {yyrv = new SemanticWrapper(NumberType.divide((NumberType)(yysv[yysp-3].val),(NumberType)(yysv[yysp-1].val))); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr48() { // valexpr : valexpr '^' valexpr
        {yyrv = new SemanticWrapper(NumberType.pow((NumberType)(yysv[yysp-3].val),(NumberType)(yysv[yysp-1].val))); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr49() { // valexpr : valexpr '%' valexpr
        {yyrv = new SemanticWrapper(NumberType.modulo((NumberType)(yysv[yysp-3].val),(NumberType)(yysv[yysp-1].val))); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr50() { // valexpr : valexpr '|' valexpr
        {yyrv = new SemanticWrapper((GPLHelper.isTrue(((NumberType)(yysv[yysp-3].val)).getDouble()) ? TRUE : (GPLHelper.isTrue(((NumberType)(yysv[yysp-1].val)).getDouble()) ? TRUE : FALSE))); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr51() { // valexpr : valexpr '&' valexpr
        {yyrv = new SemanticWrapper((GPLHelper.isTrue(((NumberType)(yysv[yysp-3].val)).getDouble()) ? (GPLHelper.isTrue(((NumberType)(yysv[yysp-1].val)).getDouble()) ? TRUE : FALSE) : FALSE)); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr52() { // valexpr : '(' valexpr ')'
        {yyrv = new SemanticWrapper(yysv[yysp-2].val); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr53() { // valexpr : symbol
        {yyrv = new SemanticWrapper(yysv[yysp-1].val); setReturn(yyrv); }
        yysv[yysp-=1] = yyrv;
        return yypvalexpr();
    }

    private int yypvalexpr() {
        switch (yyst[yysp-1]) {
            case 32: return 54;
            case 31: return 53;
            case 30: return 52;
            case 29: return 51;
            case 28: return 50;
            case 27: return 49;
            case 26: return 48;
            case 25: return 47;
            case 23: return 44;
            default: return 9;
        }
    }

    protected String[] yyerrmsgs = {
    };

  private GPLLexer lexer;
  private final Double TRUE = new Double(1);
  private final Double FALSE = new Double(0);
  
  private HashMap<String, Object> symbolTable = new HashMap<String, Object>();
  private Stack<HashMap<String, Object>> scopes;
  private boolean interactive;
  private SemanticWrapper returnVal = null;

  public GPLParser(GPLLexer lexer) { 
    this.lexer = lexer; 
    scopes = lexer.getScopes();
    interactive = false;
  }
  
  //This activates the interpreted modes!
  public void setInteractive(boolean interactive) {
    this.interactive = interactive;
  }
  
  public void setScopes(Stack<HashMap<String, Object>> scopes) {
    this.scopes = scopes;
  }
  
  private void yyerror(String msg) {
    Main.error(yyerrno<0 ? msg : yyerrmsgs[yyerrno]);
  }
  
  private Object lookup(String name) {
    int index = scopes.size()-1;
    
    while (index>=0 && scopes.get(index).get(name)==null) {
        index--;
    }
    
    if (index>=0) {
        return scopes.get(index).get(name);
    } else {
        //TODO: throw an error, nothing was found
        return null;
    }
  }
  
  private void modify(String name, Object val) {
  
    int index = scopes.size()-1;
    
    boolean declared = false;
    while (index>=0) {
        if (scopes.get(index).get(name)!=null) {
            declared = true;
            break;
        }
        index--;
    }
    
    if (declared) {
        scopes.get(index).put(name, val);
    } else {
        System.out.println("Undeclared variable name: " + name);
        System.exit(1);
    }
    
  }
  
  private Object invoke(String name, String method, ArrayList<Object> args) {
    Object obj = lookup(name);
    String typeName = obj.getClass().toString();
    
    MethodHelper helper = new MethodHelper();
    Object[] actualArgs = args.toArray();
    
    Object toReturn = null;
    
    
    System.out.println();
    
    
    try {
        Class[] types = helper.map.get(typeName).get(method).getParameterTypes();
        if (types.length!=actualArgs.length) {
            System.out.println("Wrong number of parameters for method: " + method);
        }
        
        for (int i = 0; i < actualArgs.length; i++) {
            System.out.println(actualArgs[i]);
            System.out.println(types[i]);
            actualArgs[i] = types[i].cast(actualArgs[i]);
        }
        
        toReturn = helper.map.get(typeName).get(method).invoke(obj, actualArgs);
        
    } catch (Exception e) {
        System.out.println(e);
        System.out.println("Unknown method name: " + method);
    }
    return toReturn;
  }
  
  private void declare(String type, String name, Object val) {
    if (type.equals("Number")) {
        scopes.peek().put(name, (val==null ? new NumberType() : (NumberType)val));
    } else if (type.equals("Graph")) {
        scopes.peek().put(name, (val==null ? new MyGraph() : (MyGraph)val));
    } else if (type.equals("String")) {
        scopes.peek().put(name, (val==null ? new StringType() : (StringType)val));
    }
  }
  
  private void setReturn(SemanticWrapper toReturn) {
    if (interactive) {
        returnVal = toReturn;
    }
  }
  
  
  
  public SemanticWrapper getReturn() {
    return returnVal;
  }
  

}
