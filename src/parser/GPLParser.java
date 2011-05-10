// Output created by jacc on Tue May 10 11:11:11 EDT 2011

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
    private Stack<Boolean> toSkip = new Stack<Boolean>();
    private Stack<Boolean> ifEvaluatedToTrue = new Stack<Boolean>();
    private Stack<Integer> skipCounter = new Stack<Integer>();
    
    public Object lastReturn;
    
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
    
    public void addInput(Reader reader) {
        inputs.push(reader);
    }
    
    public void setScopes(Stack<HashMap<String, Object>> scopes) {
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
            //System.out.println((toSkip.size()==0 || !toSkip.peek() ? "" : " -- ") + ((char)c=='\n' ? "NL" : (char)c) + " : " + c);
            
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
    
    /** Read the next token and return the
     *  corresponding integer code.
     */
    public int nextToken() {
    
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
            if (toSkip.size()==0 || !toSkip.peek()) {
                yylval = new SemanticWrapper(new StringType(s.substring(1,s.length())));
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
            if (toSkip.size()==0 || !toSkip.peek()) {
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
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token='+';
                    } else {
                        return nextToken();
                    }
                }
          case '-' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token='-';
                    } else {
                        return nextToken();
                    }
                }
          case '*' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token='*';
                    } else {
                        return nextToken();
                    }
                }
          case '/' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token='/';
                    } else {
                        return nextToken();
                    }
                }
          case '%' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token='%';
                    } else {
                        return nextToken();
                    }
                }
          case '^' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token='^';
                    } else {
                        return nextToken();
                    }
                }
                  case '(' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token='(';
                    } else {
                        return nextToken();
                    }
                }
          case ')' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token=')';
                    } else {
                        return nextToken();
                    }
                }
           case '[' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token='[';
                    } else {
                        return nextToken();
                    }
                }
          case ']' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token=']';
                    } else {
                        return nextToken();
                    }
                }
                  case '=' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token='=';
                    } else {
                        return nextToken();
                    }
                }
          case '>' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token=GT;
                    } else {
                        return nextToken();
                    }
                }
          case '~' :
                {
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token=EQ;
                    } else {
                        return nextToken();
                    }
                }
          case '`' :
                {
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token=NE;
                    } else {
                        return nextToken();
                    }
                }
                  case '<' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token=LT;
                    } else {
                        return nextToken();
                    }
                }
          case '@' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token=GE;
                    } else {
                        return nextToken();
                    }
                }
          case '$' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token=LE;
                    } else {
                        return nextToken();
                    }
                }
          case ',' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token=',';
                    } else {
                        return nextToken();
                    }
                }
                  case '.' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token='.';
                    } else {
                        return nextToken();
                    }
                }
                  case '!' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token='!';
                    } else {
                        return nextToken();
                    }
                }
                  case '&' : 
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
                        return token='&';
                    } else {
                        return nextToken();
                    }
                }
          case '|' :
                { 
                    if (toSkip.size()==0 || !toSkip.peek()) {
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
        
        lastChar = c;
            if (s.matches("([+-]?[0-9]*\\.[0-9]+)|([+-]?[0-9]+)")) {
                if (toSkip.size()==0 || !toSkip.peek()) {
                    yylval = new SemanticWrapper(new NumberType(Double.parseDouble(s)));
                    
                    return token=NUMBER;
                } else {
                    return nextToken();
                }
            } else if (s.equals("Number")||s.equals("Graph")||s.equals("String")||s.equals("Function")) {
                if (toSkip.size()==0 || !toSkip.peek()) {
                    yylval = new SemanticWrapper(new StringType(s));
                    return token=TYPE;
                } else {
                    return nextToken();
                }
            } else if (s.equals("print")) {
                
                if (toSkip.size()==0 || !toSkip.peek()) {
                    return token=PRINT;
                } else {
                    return nextToken();
                }
            } else if (s.equals("include")) {
                if (toSkip.size()==0 || !toSkip.peek()) {
                
                    String line = getLine();
                    
                    eval(line, true, false);
                    String randomFile = (int)(Math.random()*1000) + ".tmp";
                    Preprocessor preprocessor2 = null;
                    BufferedReader reader = null;
                    
                    try {
                        preprocessor2 = new Preprocessor(((Type)(lastReturn)).getValue(), randomFile);
                        reader = new BufferedReader(new FileReader(randomFile));
                    } catch (Exception e) {
                        System.out.println("Bad file name '" + ((Type)(lastReturn)).getValue() + "', include failed.");
                        System.exit(1);
                    }
                    
                    if (preprocessor2==null) return nextToken();
                    loadFunctions(preprocessor2);
                    if (reader!=null) {
                        String code = "";
                        String s;
                        try {
                            while((s = reader.readLine()) != null){
                                code = code + s + (char)13 + "\n";
                            }
                            
                            File aFile = new File(randomFile);
                            aFile.delete();
                        } catch (Exception e) {}
                        
                        eval(code, true, false);
                    }
                    
                    return nextToken();
                } else {
                    return nextToken();
                }
            } else if (s.equals("if")) {
            
                //System.out.println("reached if at depth " + (toSkip.size()+1));
                
                if (toSkip.size()>0 && toSkip.peek()) {
                    //System.out.println("this if is to be ignored due to inheritance!");
                    skipCounter.set(skipCounter.size()-1, skipCounter.peek()+1);
                    //System.out.println("- if pushed toSkip!");
                    ifEvaluatedToTrue.push(false);
                    toSkip.push(true);
                    blockTypes.push(0);
                    skipCounter.push(0);
                    return nextToken();
                }
                
                if (toSkip.size()==0 || !toSkip.peek()) {
                    //System.out.println("this if is not to be ignored!");
                    blockTypes.push(0);
                    boolean decision = eval(getLine());
                    //System.out.println("if evaluated to: " + decision);
                    //System.out.println("- if pushed toSkip!");
                    if (decision) {
                        toSkip.push(false);
                        ifEvaluatedToTrue.push(true);
                        skipCounter.push(0);
                        scopes.push(new HashMap<String, Object>());
                        return nextToken();
                    } else {
                        //System.out.println("About to skip!");
                        ifEvaluatedToTrue.push(false);
                        toSkip.push(true);
                        skipCounter.push(0);
                        return nextToken();
                    }
                } else {
                    //System.out.println("this if is to be ignored!");
                    skipCounter.set(skipCounter.size()-1, skipCounter.peek()+1);
                    System.out.println("- if pushed toSkip!");
                    ifEvaluatedToTrue.push(false);
                    toSkip.push(true);
                    blockTypes.push(0);
                    skipCounter.push(0);
                    return nextToken();
                }
                
            } else if (s.equals("elsif")) {
            
                //System.out.println("reached elsif at depth " + toSkip.size());
                //System.out.println("   toSkip: " + toSkip.peek());
                //System.out.println("   skipCounter: " + skipCounter.peek());
                //System.out.println("   ifEvaluatedToTrue: " + ifEvaluatedToTrue.peek());
                
                if (toSkip.size()>1 && toSkip.get(toSkip.size()-2)) {
                    //System.out.println("this elsif is to be ignored due to inheritance!");
                    return nextToken();
                }
                
                if (!toSkip.peek()) {
                    //System.out.println("this elsif is not to be ignored!");
                    //System.out.println("if evaluated to true, ignore elsif!");
                    toSkip.set(toSkip.size()-1, true);
                    scopes.pop();
                    return nextToken();
                    
                } else {
                    //System.out.println("this elsif is to be ignored!");
                    if (skipCounter.peek()>0) {
                        return nextToken();
                    }
                    String restOfLine = getLine();
                    
                    //System.out.println("Line to be evaluated:" + restOfLine);
                    toSkip.set(toSkip.size()-1, false);
                    nextToken();
                    lastChar=10;
                    
                    boolean decision = eval(restOfLine);
                    
                    //System.out.println("elsif decision: "+ decision);
                    if (decision) {
                        
                        scopes.push(new HashMap<String, Object>());
                        return nextToken();
                    } else {
                        toSkip.set(toSkip.size()-1, true);
                        skipCounter.set(skipCounter.size()-1, 0);
                        return nextToken();
                    }
                }
            } else if (s.equals("else")) {
                //System.out.println("else reached at depth " + toSkip.size());
                //System.out.println("   toSkip: " + toSkip.peek());
                //System.out.println("   skipCounter: " + skipCounter.peek());
                //System.out.println("   ifEvaluatedToTrue: " + ifEvaluatedToTrue.peek());
                
                if (toSkip.size()>1 && toSkip.get(toSkip.size()-2)) {
                    System.out.println("this else is to be ignored due to inheritance!");
                    return nextToken();
                }
                
                if (!toSkip.peek()) {
                    //System.out.println("this else is not to be ignored!");
                    scopes.pop();
                    toSkip.set(toSkip.size()-1, true);
                    skipCounter.set(skipCounter.size()-1, 0);
                    return nextToken();
                } else if (toSkip.peek() && ifEvaluatedToTrue.peek()) {
                    //System.out.println("this else is to be ignored since if evaluated to true!");
                    toSkip.set(toSkip.size()-1, true);
                    skipCounter.set(skipCounter.size()-1, 0);
                    return nextToken();
                } else{
                    //System.out.println("this else is to be ignored!");
                
                    if (skipCounter.peek()>0) {
                        return nextToken();
                    }
                    
                    toSkip.set(toSkip.size()-1, false);
                    scopes.push(new HashMap<String, Object>());
                    return nextToken();
                }
            } else if (s.equals("while")) {
            
                if (toSkip.size()==0 || !toSkip.peek()) {
                    blockTypes.push(1);
                    String cond = getLine();
                    boolean decision = eval(cond);
                    
                    if (decision) {
                    
                        if (!record) record = true;
                        whileBodies.push("");
                        whileConditions.push(cond);
                        
                        scopes.push(new HashMap<String, Object>());
                        recordCap++;
                        
                        return nextToken();
                    } else {
                        
                        scopes.push(new HashMap<String, Object>());
                        return nextToken();
                    }
                } else {
                    skipCounter.set(skipCounter.size()-1, skipCounter.peek()+1);
                    return nextToken();
                }
            } else if (s.equals("next")) {
                if (toSkip.size()==0 || !toSkip.peek()) {
                    scopes.pop();
                    return token=NEXT;
                } else {
                    return nextToken();
                }
            } else if (s.equals("last")) {
                if (!toSkip.peek()) {
                    scopes.pop();
                    return token=LAST;
                } else {
                    return nextToken();
                }
                
            } else if (s.equals("end")) {
            
                //Not to be skipped
                if (toSkip.size()==0 || !toSkip.peek()) {
                
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
                    } else {
                        //System.out.println("- end popped toSkip 1!");
                        blockTypes.pop();
                        toSkip.pop();
                        ifEvaluatedToTrue.pop();
                        skipCounter.pop();
                        scopes.pop();
                    }
                    return nextToken();
                } else {
                        if (skipCounter.size()>1 && skipCounter.get(skipCounter.size()-2)>0) {
                            skipCounter.set(skipCounter.size()-2, skipCounter.get(skipCounter.size()-2)-1);
                        }
                        
                        //System.out.println("- end popped toSkip 2!");
                        blockTypes.pop();
                        toSkip.pop();
                        ifEvaluatedToTrue.pop();
                        skipCounter.pop();
                        return nextToken();
                }
            } else if (s.equals("exit")) {
                return token=EXIT;
            } else if (s.equals("return")) {
                if (toSkip.size()==0 || !toSkip.peek()) {
                    eval(getLine());
                    
                    //This forces the current input stream to end
                    lastChar = -1;
                    return token=RETURN;
                } else {
                    return nextToken();
                }
            } else if (s.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                yylval = new SemanticWrapper(new StringType(s));
                return token=ID;
            }
            
                return -1;
    }
    
    /** Return the token code for the current lexeme.
     */
    public int getToken() {
        //System.out.println("getToken called, to return: " + token);
        if (token==0) return nextToken();
        return token;
    }
        
    private boolean eval(String code) {
        return eval(code, true, true);
    }
    
    private boolean eval(String code, boolean interactive) {
        return eval(code, interactive, true);
    }
    
    /**
        Does not handle scopes, caller takes care of it!
    */
    private boolean eval(String code, boolean interactive, boolean strip) {
        
        lastChar = 32;
        
        lastReturn = null;
        
        //Strip off all extra new lines
        if (strip) {
            while (code.charAt(0)=='\n' || code.charAt(0)==13) {
                code = code.substring(1,code.length());
            }
        }
        
        inputs.push(new StringReader(code));
        
        /*
        System.out.println("---------------- Eval: --------------");
        System.out.println(code);
        System.out.println("-----------Scope content ------------");
        for (HashMap<String,Object> h : scopes) {
            System.out.println(h);
        }
        System.out.println("-------- Input stack size ----------");
        System.out.println(inputs.size());
        System.out.println("------------------------------------");
        */
        
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
        
        lastChar = 32;
        
        if (interactive) {
            record = savedRecord;
        }
        
        
        if (liner.getReturn()==null) return false;
        
        lastReturn = liner.getReturn().val;
        
        boolean theReturn = false;
        
        try {
            theReturn = GPLHelper.isTrue(((NumberType)(liner.getReturn().val)).getDouble());
        } catch (Exception e) {}
        return theReturn;
    }
    
    public Object evalFunction(String code, ArrayList<String> formalArgs, ArrayList<Object> actualArgs, ArrayList<String> paramsType) {
        scopes.push(new HashMap<String, Object>());
        
        int i = 0;
        for (String a : formalArgs) {
            if (paramsType.get(i).equals("Function")) {
                scopes.get(0).put(a, actualArgs.get(i));
            } else {
                scopes.peek().put(a, actualArgs.get(i));
            }
            i++;
        }
        eval(code);
        scopes.pop();
        return lastReturn;
    }
    
    public void loadFunctions(Preprocessor p) {
        for (FunctionDef ff : p.functions) {
            Function f = new Function(ff.name, ff.code, ff.paramsType);
            f.args = ff.args;
            scopes.get(0).put(f.name, f);
        }
    }
    
        SemanticWrapper getSemantic() {
                return yylval;
        }
  }

  class Main {
    public static void main(String[] args) throws Exception {
      if (args.length==0) {
          System.out.println("Welcome to GPL, enter commands to run:");
            System.out.print("> ");
            Scanner scanner = new Scanner(System.in);
            String line;
            
            Stack<HashMap<String, Object>> scopes = null;
            
            
            while ((line = scanner.nextLine())!=null) {
                line = line + (char)13 + "\n";
                GPLLexer  lexer  = new GPLLexer();
                if (scopes==null) {
                    scopes = lexer.getScopes();
                } else {
                    lexer.setScopes(scopes);
                }
                
                GPLParser liner = new GPLParser(lexer);
                long start = System.currentTimeMillis();
                lexer.addInput(new StringReader("\n"+replace(line)));
                lexer.nextToken();
                liner.setInteractive(true);
                liner.parse();
                
                if (liner.getReturn()!=null && liner.getReturn().val!=null) {
                    System.out.println(((Type)(liner.getReturn().val)).getValue());
                }
                System.out.println("Successfully executed in " + ((System.currentTimeMillis() - start)/1000.0) + " seconds");
                
                System.out.print("> ");
            }
            scanner.close();

      } else {
          String file = args[0];
          Preprocessor p = new Preprocessor(file, "notused.tmp");
          GPLLexer  lexer  = new GPLLexer();
          lexer.nextToken();
          GPLParser parser = new GPLParser(lexer);
          parser.setPreprocessor(p);
          parser.parse();
      }
    }

    private static String replace(String s) {
        s = s.replace("==", "~");
        s = s.replace("!=", "`");
        s = s.replace(">=", "@");
                s = s.replace("<=", "$");
                s = s.replace("&&", "&");
                s = s.replace("||", "|");
        return s;
    }
    
    public static void error(String msg) {
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
                case 134:
                    yyn = yys0();
                    continue;

                case 1:
                    yyst[yysp] = 1;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 135:
                    switch (yytok) {
                        case ENDINPUT:
                            yyn = 268;
                            continue;
                        case NL:
                            yyn = 24;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 2:
                    yyst[yysp] = 2;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 136:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr19();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 3:
                    yyst[yysp] = 3;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 137:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr2();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 4:
                    yyst[yysp] = 4;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 138:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr18();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 5:
                    yyst[yysp] = 5;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 139:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr4();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 6:
                    yyst[yysp] = 6;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 140:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr20();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 7:
                    yyst[yysp] = 7;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 141:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr3();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 8:
                    yyst[yysp] = 8;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 142:
                    yyn = yys8();
                    continue;

                case 9:
                    yyst[yysp] = 9;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 143:
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
                case 144:
                    switch (yytok) {
                        case ID:
                            yyn = 39;
                            continue;
                    }
                    yyn = 271;
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
                case 145:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr17();
                            continue;
                    }
                    yyn = 271;
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
                case 146:
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
                case 147:
                    switch (yytok) {
                        case '(':
                            yyn = 43;
                            continue;
                    }
                    yyn = 271;
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
                case 148:
                    switch (yytok) {
                        case '(':
                            yyn = 44;
                            continue;
                    }
                    yyn = 271;
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
                case 149:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr14();
                            continue;
                    }
                    yyn = 271;
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
                case 150:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr13();
                            continue;
                    }
                    yyn = 271;
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
                case 151:
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
                case 152:
                    switch (yytok) {
                        case '(':
                            yyn = 45;
                            continue;
                    }
                    yyn = 271;
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
                case 153:
                    switch (yytok) {
                        case '(':
                            yyn = 46;
                            continue;
                        case ENDINPUT:
                        case NL:
                            yyn = yyr16();
                            continue;
                    }
                    yyn = 271;
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
                case 154:
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
                case 155:
                    switch (yytok) {
                        case ID:
                            yyn = 47;
                            continue;
                    }
                    yyn = 271;
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
                case 156:
                    switch (yytok) {
                        case '(':
                            yyn = 48;
                            continue;
                    }
                    yyn = 271;
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
                case 157:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 158:
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
                case 159:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 160:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 161:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 162:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 163:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 164:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 165:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 166:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 167:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 168:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 169:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 170:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 171:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 172:
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
                            yyn = 50;
                            continue;
                    }
                    yyn = 271;
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
                case 173:
                    switch (yytok) {
                        case '(':
                            yyn = 66;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 40:
                    yyst[yysp] = 40;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 174:
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
                            yyn = 70;
                            continue;
                        case ')':
                            yyn = yyr31();
                            continue;
                    }
                    yyn = 271;
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
                case 175:
                    switch (yytok) {
                        case ID:
                            yyn = 71;
                            continue;
                    }
                    yyn = 271;
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
                case 176:
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
                            yyn = 70;
                            continue;
                        case '[':
                            yyn = 73;
                            continue;
                    }
                    yyn = 271;
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
                case 177:
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
                            yyn = 70;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 44:
                    yyst[yysp] = 44;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 178:
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
                            yyn = 70;
                            continue;
                    }
                    yyn = 271;
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
                case 179:
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
                            yyn = 70;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 46:
                    yyst[yysp] = 46;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 180:
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
                            yyn = 70;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 47:
                    yyst[yysp] = 47;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 181:
                    switch (yytok) {
                        case '=':
                            yyn = 78;
                            continue;
                        case ENDINPUT:
                        case NL:
                            yyn = yyr34();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 48:
                    yyst[yysp] = 48;
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
                            yyn = 70;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 49:
                    yyst[yysp] = 49;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 183:
                    yyn = yys49();
                    continue;

                case 50:
                    yyst[yysp] = 50;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 184:
                    yyn = yys50();
                    continue;

                case 51:
                    yyst[yysp] = 51;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 185:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr1();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 52:
                    yyst[yysp] = 52;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 186:
                    yyn = yys52();
                    continue;

                case 53:
                    yyst[yysp] = 53;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 187:
                    yyn = yys53();
                    continue;

                case 54:
                    yyst[yysp] = 54;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 188:
                    yyn = yys54();
                    continue;

                case 55:
                    yyst[yysp] = 55;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 189:
                    yyn = yys55();
                    continue;

                case 56:
                    yyst[yysp] = 56;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 190:
                    yyn = yys56();
                    continue;

                case 57:
                    yyst[yysp] = 57;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 191:
                    yyn = yys57();
                    continue;

                case 58:
                    yyst[yysp] = 58;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 192:
                    yyn = yys58();
                    continue;

                case 59:
                    yyst[yysp] = 59;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 193:
                    yyn = yys59();
                    continue;

                case 60:
                    yyst[yysp] = 60;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 194:
                    yyn = yys60();
                    continue;

                case 61:
                    yyst[yysp] = 61;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 195:
                    yyn = yys61();
                    continue;

                case 62:
                    yyst[yysp] = 62;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 196:
                    yyn = yys62();
                    continue;

                case 63:
                    yyst[yysp] = 63;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 197:
                    yyn = yys63();
                    continue;

                case 64:
                    yyst[yysp] = 64;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 198:
                    yyn = yys64();
                    continue;

                case 65:
                    yyst[yysp] = 65;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 199:
                    yyn = yys65();
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
                case 200:
                    switch (yytok) {
                        case TYPE:
                            yyn = 83;
                            continue;
                        case ')':
                            yyn = yyr27();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 67:
                    yyst[yysp] = 67;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 201:
                    switch (yytok) {
                        case ')':
                            yyn = yyr30();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 68:
                    yyst[yysp] = 68;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 202:
                    switch (yytok) {
                        case ')':
                            yyn = 84;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 69:
                    yyst[yysp] = 69;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 203:
                    switch (yytok) {
                        case ',':
                            yyn = 85;
                            continue;
                        case ')':
                            yyn = yyr29();
                            continue;
                    }
                    yyn = 271;
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
                case 204:
                    yyn = yys70();
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
                case 205:
                    switch (yytok) {
                        case '(':
                            yyn = 86;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 72:
                    yyst[yysp] = 72;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 206:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr35();
                            continue;
                    }
                    yyn = 271;
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
                case 207:
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
                            yyn = 70;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 74:
                    yyst[yysp] = 74;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 208:
                    switch (yytok) {
                        case ')':
                            yyn = 89;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 75:
                    yyst[yysp] = 75;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 209:
                    switch (yytok) {
                        case ')':
                            yyn = 90;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 76:
                    yyst[yysp] = 76;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 210:
                    switch (yytok) {
                        case ')':
                            yyn = 91;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 77:
                    yyst[yysp] = 77;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 211:
                    switch (yytok) {
                        case ')':
                            yyn = 92;
                            continue;
                    }
                    yyn = 271;
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
                case 212:
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
                            yyn = 70;
                            continue;
                        case '[':
                            yyn = 94;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 79:
                    yyst[yysp] = 79;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 213:
                    switch (yytok) {
                        case ')':
                            yyn = 95;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 80:
                    yyst[yysp] = 80;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 214:
                    yyn = yys80();
                    continue;

                case 81:
                    yyst[yysp] = 81;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 215:
                    switch (yytok) {
                        case ')':
                            yyn = yyr26();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 82:
                    yyst[yysp] = 82;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 216:
                    switch (yytok) {
                        case ')':
                            yyn = 96;
                            continue;
                    }
                    yyn = 271;
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
                case 217:
                    switch (yytok) {
                        case ID:
                            yyn = 97;
                            continue;
                    }
                    yyn = 271;
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
                case 218:
                    switch (yytok) {
                        case ')':
                        case ENDINPUT:
                        case ']':
                        case NL:
                        case ',':
                            yyn = yyr40();
                            continue;
                    }
                    yyn = 271;
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
                case 219:
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
                            yyn = 70;
                            continue;
                    }
                    yyn = 271;
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
                case 220:
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
                            yyn = 70;
                            continue;
                        case ')':
                            yyn = yyr31();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 87:
                    yyst[yysp] = 87;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 221:
                    switch (yytok) {
                        case ',':
                            yyn = 100;
                            continue;
                        case ']':
                            yyn = yyr38();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 88:
                    yyst[yysp] = 88;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 222:
                    switch (yytok) {
                        case ']':
                            yyn = 101;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 89:
                    yyst[yysp] = 89;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 223:
                    switch (yytok) {
                        case NL:
                            yyn = 102;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 90:
                    yyst[yysp] = 90;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 224:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr11();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 91:
                    yyst[yysp] = 91;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 225:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr12();
                            continue;
                    }
                    yyn = 271;
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
                case 226:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr15();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 93:
                    yyst[yysp] = 93;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 227:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr32();
                            continue;
                    }
                    yyn = 271;
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
                case 228:
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
                            yyn = 70;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 95:
                    yyst[yysp] = 95;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 229:
                    switch (yytok) {
                        case NL:
                            yyn = 104;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 96:
                    yyst[yysp] = 96;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 230:
                    switch (yytok) {
                        case NL:
                            yyn = 105;
                            continue;
                    }
                    yyn = 271;
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
                case 231:
                    switch (yytok) {
                        case ',':
                            yyn = 106;
                            continue;
                        case ')':
                            yyn = yyr25();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 98:
                    yyst[yysp] = 98;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 232:
                    switch (yytok) {
                        case ')':
                            yyn = yyr28();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 99:
                    yyst[yysp] = 99;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 233:
                    switch (yytok) {
                        case ')':
                            yyn = 107;
                            continue;
                    }
                    yyn = 271;
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
                case 234:
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
                            yyn = 70;
                            continue;
                    }
                    yyn = 271;
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
                case 235:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr36();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 102:
                    yyst[yysp] = 102;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 236:
                    yyn = yys102();
                    continue;

                case 103:
                    yyst[yysp] = 103;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 237:
                    switch (yytok) {
                        case ']':
                            yyn = 111;
                            continue;
                    }
                    yyn = 271;
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
                case 238:
                    yyn = yys104();
                    continue;

                case 105:
                    yyst[yysp] = 105;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 239:
                    yyn = yys105();
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
                case 240:
                    switch (yytok) {
                        case TYPE:
                            yyn = 83;
                            continue;
                    }
                    yyn = 271;
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
                case 241:
                    switch (yytok) {
                        case ')':
                        case ENDINPUT:
                        case ']':
                        case NL:
                        case ',':
                            yyn = yyr39();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 108:
                    yyst[yysp] = 108;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 242:
                    switch (yytok) {
                        case ']':
                            yyn = yyr37();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 109:
                    yyst[yysp] = 109;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 243:
                    switch (yytok) {
                        case NL:
                            yyn = 115;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 110:
                    yyst[yysp] = 110;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 244:
                    switch (yytok) {
                        case NL:
                            yyn = yyr7();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 111:
                    yyst[yysp] = 111;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 245:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr33();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 112:
                    yyst[yysp] = 112;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 246:
                    switch (yytok) {
                        case NL:
                            yyn = 116;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 113:
                    yyst[yysp] = 113;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 247:
                    switch (yytok) {
                        case NL:
                            yyn = 117;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 114:
                    yyst[yysp] = 114;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 248:
                    switch (yytok) {
                        case ')':
                            yyn = yyr24();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 115:
                    yyst[yysp] = 115;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 249:
                    yyn = yys115();
                    continue;

                case 116:
                    yyst[yysp] = 116;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 250:
                    yyn = yys116();
                    continue;

                case 117:
                    yyst[yysp] = 117;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 251:
                    yyn = yys117();
                    continue;

                case 118:
                    yyst[yysp] = 118;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 252:
                    switch (yytok) {
                        case END:
                            yyn = 124;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 119:
                    yyst[yysp] = 119;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 253:
                    switch (yytok) {
                        case NL:
                            yyn = yyr8();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 120:
                    yyst[yysp] = 120;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 254:
                    yyn = yys120();
                    continue;

                case 121:
                    yyst[yysp] = 121;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 255:
                    switch (yytok) {
                        case '(':
                            yyn = 126;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 122:
                    yyst[yysp] = 122;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 256:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr10();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 123:
                    yyst[yysp] = 123;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 257:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr6();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 124:
                    yyst[yysp] = 124;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 258:
                    switch (yytok) {
                        case ENDINPUT:
                        case NL:
                            yyn = yyr9();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 125:
                    yyst[yysp] = 125;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 259:
                    switch (yytok) {
                        case NL:
                            yyn = 127;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 126:
                    yyst[yysp] = 126;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 260:
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
                            yyn = 70;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 127:
                    yyst[yysp] = 127;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 261:
                    switch (yytok) {
                        case END:
                            yyn = yyr21();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 128:
                    yyst[yysp] = 128;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 262:
                    switch (yytok) {
                        case ')':
                            yyn = 129;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 129:
                    yyst[yysp] = 129;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 263:
                    switch (yytok) {
                        case NL:
                            yyn = 130;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 130:
                    yyst[yysp] = 130;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 264:
                    yyn = yys130();
                    continue;

                case 131:
                    yyst[yysp] = 131;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 265:
                    switch (yytok) {
                        case NL:
                            yyn = 132;
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 132:
                    yyst[yysp] = 132;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 266:
                    switch (yytok) {
                        case ELSE:
                            yyn = 120;
                            continue;
                        case ELSIF:
                            yyn = 121;
                            continue;
                        case END:
                            yyn = yyr23();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 133:
                    yyst[yysp] = 133;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 267:
                    switch (yytok) {
                        case END:
                            yyn = yyr22();
                            continue;
                    }
                    yyn = 271;
                    continue;

                case 268:
                    return true;
                case 269:
                    yyerror("stack overflow");
                case 270:
                    return false;
                case 271:
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
            case ENDINPUT:
            case NL:
                return yyr5();
        }
        return 271;
    }

    private int yys8() {
        switch (yytok) {
            case ')':
            case LT:
            case '&':
            case '%':
            case LE:
            case EQ:
            case ENDINPUT:
            case '|':
            case '^':
            case ']':
            case '/':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case '*':
            case NE:
            case GE:
                return yyr57();
        }
        return 271;
    }

    private int yys9() {
        switch (yytok) {
            case EQ:
                return 25;
            case GE:
                return 26;
            case GT:
                return 27;
            case LE:
                return 28;
            case LT:
                return 29;
            case NE:
                return 30;
            case '%':
                return 31;
            case '&':
                return 32;
            case '*':
                return 33;
            case '+':
                return 34;
            case '-':
                return 35;
            case '/':
                return 36;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
            case ENDINPUT:
            case ']':
            case NL:
            case ',':
                return yyr41();
        }
        return 271;
    }

    private int yys12() {
        switch (yytok) {
            case '(':
                return 40;
            case '.':
                return 41;
            case '=':
                return 42;
            case LT:
            case '&':
            case '%':
            case LE:
            case EQ:
            case ENDINPUT:
            case '|':
            case '^':
            case '/':
            case NL:
            case GT:
            case '-':
            case '+':
            case '*':
            case NE:
            case GE:
                return yyr58();
        }
        return 271;
    }

    private int yys17() {
        switch (yytok) {
            case ')':
            case LT:
            case '&':
            case '%':
            case LE:
            case EQ:
            case ENDINPUT:
            case '|':
            case '^':
            case ']':
            case '/':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case '*':
            case NE:
            case GE:
                return yyr59();
        }
        return 271;
    }

    private int yys20() {
        switch (yytok) {
            case ')':
            case LT:
            case '&':
            case '%':
            case LE:
            case EQ:
            case ENDINPUT:
            case '|':
            case '^':
            case ']':
            case '/':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case '*':
            case NE:
            case GE:
                return yyr60();
        }
        return 271;
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
            case ENDINPUT:
            case NL:
                return yyr5();
        }
        return 271;
    }

    private int yys49() {
        switch (yytok) {
            case EQ:
                return 25;
            case GE:
                return 26;
            case GT:
                return 27;
            case LE:
                return 28;
            case LT:
                return 29;
            case NE:
                return 30;
            case '%':
                return 31;
            case '&':
                return 32;
            case '*':
                return 33;
            case '+':
                return 34;
            case '-':
                return 35;
            case '/':
                return 36;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
                return 80;
        }
        return 271;
    }

    private int yys50() {
        switch (yytok) {
            case ')':
            case LT:
            case '&':
            case '%':
            case LE:
            case EQ:
            case ENDINPUT:
            case '|':
            case '^':
            case ']':
            case '/':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case '*':
            case NE:
            case GE:
                return yyr58();
        }
        return 271;
    }

    private int yys52() {
        switch (yytok) {
            case EQ:
                return 25;
            case GE:
                return 26;
            case GT:
                return 27;
            case LE:
                return 28;
            case LT:
                return 29;
            case NE:
                return 30;
            case '%':
                return 31;
            case '&':
                return 32;
            case '*':
                return 33;
            case '+':
                return 34;
            case '-':
                return 35;
            case '/':
                return 36;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
            case ENDINPUT:
            case ']':
            case NL:
            case ',':
                return yyr50();
        }
        return 271;
    }

    private int yys53() {
        switch (yytok) {
            case EQ:
                return 25;
            case GE:
                return 26;
            case GT:
                return 27;
            case LE:
                return 28;
            case LT:
                return 29;
            case NE:
                return 30;
            case '%':
                return 31;
            case '&':
                return 32;
            case '*':
                return 33;
            case '+':
                return 34;
            case '-':
                return 35;
            case '/':
                return 36;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
            case ENDINPUT:
            case ']':
            case NL:
            case ',':
                return yyr54();
        }
        return 271;
    }

    private int yys54() {
        switch (yytok) {
            case EQ:
                return 25;
            case GE:
                return 26;
            case GT:
                return 27;
            case LE:
                return 28;
            case LT:
                return 29;
            case NE:
                return 30;
            case '%':
                return 31;
            case '&':
                return 32;
            case '*':
                return 33;
            case '+':
                return 34;
            case '-':
                return 35;
            case '/':
                return 36;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
            case ENDINPUT:
            case ']':
            case NL:
            case ',':
                return yyr52();
        }
        return 271;
    }

    private int yys55() {
        switch (yytok) {
            case EQ:
                return 25;
            case GE:
                return 26;
            case GT:
                return 27;
            case LE:
                return 28;
            case LT:
                return 29;
            case NE:
                return 30;
            case '%':
                return 31;
            case '&':
                return 32;
            case '*':
                return 33;
            case '+':
                return 34;
            case '-':
                return 35;
            case '/':
                return 36;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
            case ENDINPUT:
            case ']':
            case NL:
            case ',':
                return yyr55();
        }
        return 271;
    }

    private int yys56() {
        switch (yytok) {
            case EQ:
                return 25;
            case GE:
                return 26;
            case GT:
                return 27;
            case LE:
                return 28;
            case LT:
                return 29;
            case NE:
                return 30;
            case '%':
                return 31;
            case '&':
                return 32;
            case '*':
                return 33;
            case '+':
                return 34;
            case '-':
                return 35;
            case '/':
                return 36;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
            case ENDINPUT:
            case ']':
            case NL:
            case ',':
                return yyr53();
        }
        return 271;
    }

    private int yys57() {
        switch (yytok) {
            case EQ:
                return 25;
            case GE:
                return 26;
            case GT:
                return 27;
            case LE:
                return 28;
            case LT:
                return 29;
            case NE:
                return 30;
            case '%':
                return 31;
            case '&':
                return 32;
            case '*':
                return 33;
            case '+':
                return 34;
            case '-':
                return 35;
            case '/':
                return 36;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
            case ENDINPUT:
            case ']':
            case NL:
            case ',':
                return yyr51();
        }
        return 271;
    }

    private int yys58() {
        switch (yytok) {
            case '&':
                return 32;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
            case LT:
            case '%':
            case LE:
            case EQ:
            case ENDINPUT:
            case ']':
            case '/':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case '*':
            case NE:
            case GE:
                return yyr47();
        }
        return 271;
    }

    private int yys59() {
        switch (yytok) {
            case ')':
            case LT:
            case '&':
            case '%':
            case LE:
            case EQ:
            case ENDINPUT:
            case '|':
            case '^':
            case ']':
            case '/':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case '*':
            case NE:
            case GE:
                return yyr49();
        }
        return 271;
    }

    private int yys60() {
        switch (yytok) {
            case '&':
                return 32;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
            case LT:
            case '%':
            case LE:
            case EQ:
            case ENDINPUT:
            case ']':
            case '/':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case '*':
            case NE:
            case GE:
                return yyr44();
        }
        return 271;
    }

    private int yys61() {
        switch (yytok) {
            case '%':
                return 31;
            case '&':
                return 32;
            case '*':
                return 33;
            case '/':
                return 36;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
            case LT:
            case LE:
            case EQ:
            case ENDINPUT:
            case ']':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case NE:
            case GE:
                return yyr42();
        }
        return 271;
    }

    private int yys62() {
        switch (yytok) {
            case '%':
                return 31;
            case '&':
                return 32;
            case '*':
                return 33;
            case '/':
                return 36;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
            case LT:
            case LE:
            case EQ:
            case ENDINPUT:
            case ']':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case NE:
            case GE:
                return yyr43();
        }
        return 271;
    }

    private int yys63() {
        switch (yytok) {
            case '&':
                return 32;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
            case LT:
            case '%':
            case LE:
            case EQ:
            case ENDINPUT:
            case ']':
            case '/':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case '*':
            case NE:
            case GE:
                return yyr45();
        }
        return 271;
    }

    private int yys64() {
        switch (yytok) {
            case '&':
                return 32;
            case '^':
                return 37;
            case '|':
                return 38;
            case ')':
            case LT:
            case '%':
            case LE:
            case EQ:
            case ENDINPUT:
            case ']':
            case '/':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case '*':
            case NE:
            case GE:
                return yyr46();
        }
        return 271;
    }

    private int yys65() {
        switch (yytok) {
            case '&':
                return 32;
            case ')':
            case LT:
            case '%':
            case LE:
            case EQ:
            case ENDINPUT:
            case '|':
            case '^':
            case ']':
            case '/':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case '*':
            case NE:
            case GE:
                return yyr48();
        }
        return 271;
    }

    private int yys70() {
        switch (yytok) {
            case '(':
                return 40;
            case '.':
                return 41;
            case ')':
            case LT:
            case '&':
            case '%':
            case LE:
            case EQ:
            case ENDINPUT:
            case '|':
            case '^':
            case ']':
            case '/':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case '*':
            case NE:
            case GE:
                return yyr58();
        }
        return 271;
    }

    private int yys80() {
        switch (yytok) {
            case ')':
            case LT:
            case '&':
            case '%':
            case LE:
            case EQ:
            case ENDINPUT:
            case '|':
            case '^':
            case ']':
            case '/':
            case NL:
            case GT:
            case '-':
            case ',':
            case '+':
            case '*':
            case NE:
            case GE:
                return yyr56();
        }
        return 271;
    }

    private int yys102() {
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
        return 271;
    }

    private int yys104() {
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
        return 271;
    }

    private int yys105() {
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
        return 271;
    }

    private int yys115() {
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
                return 120;
            case ELSIF:
                return 121;
            case END:
                return yyr23();
        }
        return 271;
    }

    private int yys116() {
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
                return 122;
        }
        return 271;
    }

    private int yys117() {
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
                return 123;
        }
        return 271;
    }

    private int yys120() {
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
        return 271;
    }

    private int yys130() {
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
        return 271;
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
            case 104: return 112;
            case 102: return 109;
            default: return 113;
        }
    }

    private int yyr28() { // args : expr ',' args
        {((ArrayList<Object>)yyrv.val).add(0,yysv[yysp-3].val);}
        yysv[yysp-=3] = yyrv;
        return yypargs();
    }

    private int yyr29() { // args : expr
        {yyrv = new SemanticWrapper(new ArrayList<Object>()); ((ArrayList<Object>)(yyrv.val)).add(yysv[yysp-1].val);}
        yysv[yysp-=1] = yyrv;
        return yypargs();
    }

    private int yypargs() {
        switch (yyst[yysp-1]) {
            case 85: return 98;
            default: return 67;
        }
    }

    private int yyr30() { // argswrapper : args
        {yyrv = yysv[yysp-1];}
        yysv[yysp-=1] = yyrv;
        return yypargswrapper();
    }

    private int yyr31() { // argswrapper : /* empty */
        {yyrv = new SemanticWrapper(null);}
        yysv[yysp-=0] = yyrv;
        return yypargswrapper();
    }

    private int yypargswrapper() {
        switch (yyst[yysp-1]) {
            case 40: return 68;
            default: return 99;
        }
    }

    private int yyr35() { // assignment : ID '=' expr
        {modify(((StringType)(yysv[yysp-3].val)).getValue(), yysv[yysp-1].val); setReturn(yysv[yysp-1]); }
        yysv[yysp-=3] = yyrv;
        return 2;
    }

    private int yyr36() { // assignment : ID '=' '[' listofexpr ']'
        {declareList(null,((StringType)(yysv[yysp-5].val)).getValue(),yysv[yysp-2].val); setReturn(new SemanticWrapper(lookup(((StringType)(yysv[yysp-5].val)).getValue())));}
        yysv[yysp-=5] = yyrv;
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
            default: return 51;
        }
    }

    private int yyr32() { // declaration : TYPE ID '=' expr
        {declare(((StringType)(yysv[yysp-4].val)).getValue(),((StringType)(yysv[yysp-3].val)).getValue(),yysv[yysp-1].val);}
        yysv[yysp-=4] = yyrv;
        return 4;
    }

    private int yyr33() { // declaration : TYPE ID '=' '[' listofexpr ']'
        {declareList(((StringType)(yysv[yysp-6].val)).getValue(),((StringType)(yysv[yysp-5].val)).getValue(),yysv[yysp-2].val); setReturn(new SemanticWrapper(lookup(((StringType)(yysv[yysp-5].val)).getValue())));}
        yysv[yysp-=6] = yyrv;
        return 4;
    }

    private int yyr34() { // declaration : TYPE ID
        {declare(((StringType)(yysv[yysp-2].val)).getValue(),((StringType)(yysv[yysp-1].val)).getValue(),null);}
        yysv[yysp-=2] = yyrv;
        return 4;
    }

    private int yyr24() { // defargs : TYPE ID ',' defargs
        yysp -= 4;
        return yypdefargs();
    }

    private int yyr25() { // defargs : TYPE ID
        yysp -= 2;
        return yypdefargs();
    }

    private int yypdefargs() {
        switch (yyst[yysp-1]) {
            case 66: return 81;
            default: return 114;
        }
    }

    private int yyr26() { // defargswrapper : defargs
        yysp -= 1;
        return 82;
    }

    private int yyr27() { // defargswrapper : /* empty */
        return 82;
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
            case 115: return 118;
            default: return 133;
        }
    }

    private int yyr39() { // expr : ID '.' ID '(' argswrapper ')'
        {yyrv = new SemanticWrapper(invoke(((StringType)(yysv[yysp-6].val)).getValue(), ((StringType)(yysv[yysp-4].val)).getValue(), ( yysv[yysp-2].val==null ? null : (ArrayList<Object>)(yysv[yysp-2].val) ))); setReturn(yyrv);}
        yysv[yysp-=6] = yyrv;
        return yypexpr();
    }

    private int yyr40() { // expr : ID '(' argswrapper ')'
        {yyrv = new SemanticWrapper(invoke(((StringType)(yysv[yysp-4].val)).getValue(), ( yysv[yysp-2].val==null ? null : (ArrayList<Object>)(yysv[yysp-2].val) ))); setReturn(yyrv);}
        yysv[yysp-=4] = yyrv;
        return yypexpr();
    }

    private int yyr41() { // expr : valexpr
        {yyrv.val = yysv[yysp-1].val;}
        yysv[yysp-=1] = yyrv;
        return yypexpr();
    }

    private int yypexpr() {
        switch (yyst[yysp-1]) {
            case 126: return 128;
            case 100: return 87;
            case 94: return 87;
            case 86: return 69;
            case 85: return 69;
            case 78: return 93;
            case 73: return 87;
            case 48: return 79;
            case 46: return 77;
            case 45: return 76;
            case 44: return 75;
            case 43: return 74;
            case 42: return 72;
            case 40: return 69;
            default: return 6;
        }
    }

    private int yyr37() { // listofexpr : expr ',' listofexpr
        {((ArrayList<Object>)yyrv.val).add(0,yysv[yysp-3].val);}
        yysv[yysp-=3] = yyrv;
        return yyplistofexpr();
    }

    private int yyr38() { // listofexpr : expr
        {yyrv = new SemanticWrapper(new ArrayList<Object>()); ((ArrayList<Object>)(yyrv.val)).add(yysv[yysp-1].val);}
        yysv[yysp-=1] = yyrv;
        return yyplistofexpr();
    }

    private int yyplistofexpr() {
        switch (yyst[yysp-1]) {
            case 94: return 103;
            case 73: return 88;
            default: return 108;
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
        {include(((Type)yysv[yysp-2].val).getValue()); setReturn(null);}
        yysv[yysp-=4] = yyrv;
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

    private int yyr15() { // action : RETURN '(' expr ')'
        yysp -= 4;
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
            case 130: return 131;
            case 120: return 125;
            case 105: return 110;
            case 104: return 110;
            case 102: return 110;
            case 24: return 7;
            case 0: return 7;
            default: return 119;
        }
    }

    private int yyr58() { // symbol : ID
        {yyrv = new SemanticWrapper(lookup(((StringType)(yysv[yysp-1].val)).getValue())); setReturn(yyrv); }
        yysv[yysp-=1] = yyrv;
        return 8;
    }

    private int yyr59() { // symbol : NUMBER
        {yyrv = yysv[yysp-1]; setReturn(yyrv); setReturn(yyrv);}
        yysv[yysp-=1] = yyrv;
        return 8;
    }

    private int yyr60() { // symbol : STRING_LITERAL
        {yyrv = yysv[yysp-1]; setReturn(yysv[yysp-1]); setReturn(yyrv);}
        yysv[yysp-=1] = yyrv;
        return 8;
    }

    private int yyr42() { // valexpr : valexpr '+' valexpr
        {yyrv = new SemanticWrapper(NumberType.add((NumberType)(yysv[yysp-3].val),(NumberType)(yysv[yysp-1].val))); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr43() { // valexpr : valexpr '-' valexpr
        {yyrv = new SemanticWrapper(NumberType.subtract((NumberType)(yysv[yysp-3].val),(NumberType)(yysv[yysp-1].val))); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr44() { // valexpr : valexpr '*' valexpr
        {yyrv = new SemanticWrapper(NumberType.multiply((NumberType)(yysv[yysp-3].val),(NumberType)(yysv[yysp-1].val))); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr45() { // valexpr : valexpr '/' valexpr
        {yyrv = new SemanticWrapper(NumberType.divide((NumberType)(yysv[yysp-3].val),(NumberType)(yysv[yysp-1].val))); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr46() { // valexpr : valexpr '^' valexpr
        {yyrv = new SemanticWrapper(NumberType.pow((NumberType)(yysv[yysp-3].val),(NumberType)(yysv[yysp-1].val))); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr47() { // valexpr : valexpr '%' valexpr
        {yyrv = new SemanticWrapper(NumberType.modulo((NumberType)(yysv[yysp-3].val),(NumberType)(yysv[yysp-1].val))); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr48() { // valexpr : valexpr '|' valexpr
        {yyrv = (GPLHelper.isTrue(((NumberType)(yysv[yysp-3].val)).getDouble()) ? TRUE_WRAPPER : (GPLHelper.isTrue(((NumberType)(yysv[yysp-1].val)).getDouble()) ? TRUE_WRAPPER : FALSE_WRAPPER)); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr49() { // valexpr : valexpr '&' valexpr
        {yyrv = ( GPLHelper.isTrue(((NumberType)(yysv[yysp-3].val)).getDouble()) ? 
                                                                        (GPLHelper.isTrue(((NumberType)(yysv[yysp-1].val)).getDouble()) ? TRUE_WRAPPER : FALSE_WRAPPER) : 
                                                                        FALSE_WRAPPER); 
                                             setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr50() { // valexpr : valexpr EQ valexpr
        {yyrv = (compare(yysv[yysp-3], yysv[yysp-1])==0 ? TRUE_WRAPPER : FALSE_WRAPPER); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr51() { // valexpr : valexpr NE valexpr
        {yyrv = (compare(yysv[yysp-3], yysv[yysp-1])==0 ? FALSE_WRAPPER : TRUE_WRAPPER); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr52() { // valexpr : valexpr GT valexpr
        {yyrv = (compare(yysv[yysp-3], yysv[yysp-1])>0 ? TRUE_WRAPPER : FALSE_WRAPPER); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr53() { // valexpr : valexpr LT valexpr
        {yyrv = (compare(yysv[yysp-3], yysv[yysp-1])<0 ? TRUE_WRAPPER : FALSE_WRAPPER); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr54() { // valexpr : valexpr GE valexpr
        {yyrv = (compare(yysv[yysp-3], yysv[yysp-1])>=0 ? TRUE_WRAPPER : FALSE_WRAPPER); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr55() { // valexpr : valexpr LE valexpr
        {yyrv = (compare(yysv[yysp-3], yysv[yysp-1])<=0 ? TRUE_WRAPPER : FALSE_WRAPPER); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr56() { // valexpr : '(' valexpr ')'
        {yyrv = new SemanticWrapper(yysv[yysp-2].val); setReturn(yyrv); }
        yysv[yysp-=3] = yyrv;
        return yypvalexpr();
    }

    private int yyr57() { // valexpr : symbol
        {yyrv = new SemanticWrapper(yysv[yysp-1].val); setReturn(yyrv); }
        yysv[yysp-=1] = yyrv;
        return yypvalexpr();
    }

    private int yypvalexpr() {
        switch (yyst[yysp-1]) {
            case 38: return 65;
            case 37: return 64;
            case 36: return 63;
            case 35: return 62;
            case 34: return 61;
            case 33: return 60;
            case 32: return 59;
            case 31: return 58;
            case 30: return 57;
            case 29: return 56;
            case 28: return 55;
            case 27: return 54;
            case 26: return 53;
            case 25: return 52;
            case 23: return 49;
            default: return 9;
        }
    }

    protected String[] yyerrmsgs = {
    };

  private GPLLexer lexer;
  
  private HashMap<String, Object> symbolTable = new HashMap<String, Object>();
  private Stack<HashMap<String, Object>> scopes;
  private boolean interactive;
  private SemanticWrapper returnVal = null;
  private static final SemanticWrapper TRUE_WRAPPER = new SemanticWrapper(new NumberType(1.0));
  private static final SemanticWrapper FALSE_WRAPPER = new SemanticWrapper(new NumberType(0.0));
  private Preprocessor p;
  
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
  
  private void include(String fileName) {}
  
  /**
  * This invokes a user defined function
  */
  private Object invoke(String name, ArrayList<Object> args) {
  
    if (args==null) args = new ArrayList<Object>();
  
    //All functions are global-scoped, meaning they're at the bottom of the stack
    Function f = null;
    try {
        f = (Function)scopes.get(0).get(name);
    } catch (Exception e) {
        System.out.println("Cannot find function: " + name + " - did you define it?");
        System.exit(1);
    }
    
    if (f==null) {
        System.out.println("Cannot find function: " + name + " - did you define it?");
        System.exit(1);
    }
    
    if (f.args.size()==1 && f.args.get(0).toString().length()==0) f.args = new ArrayList<String>();
    
    if (f.args.size()!=args.size()) {
        System.out.println("Function " + name + " expected " + f.args.size() + " arguments, not " + args.size());
        System.exit(1);
    }
    
    lexer.evalFunction(f.code, f.args, args, f.paramsType);
    
    return lexer.lastReturn;
  }
  
  /**
  * This invokes a built-in function in GPL
  */
  private Object invoke(String name, String method, ArrayList<Object> args) {
  
    MethodHelper helper = new MethodHelper();
  
    Object obj = lookup(name);
    String typeName = obj.getClass().toString();
    
    
    Object[] actualArgs = null;
    
    if (args!=null) {
        actualArgs = args.toArray();
    }
    
    Object toReturn = null;
    
    
    try {
        if (actualArgs!=null) {
            Class[] types = helper.map.get(typeName).get(method).getParameterTypes();
            if (types.length!=actualArgs.length) {
                System.out.println("Wrong number of parameters for method: " + method);
                System.exit(1);
            }
            for (int i = 0; i < actualArgs.length; i++) {
                actualArgs[i] = types[i].cast(actualArgs[i]);
            }
            toReturn = helper.map.get(typeName).get(method).invoke(obj, actualArgs);
        } else {
            toReturn = helper.map.get(typeName).get(method).invoke(obj, new Object[0]);
        }
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
    } else if (type.equals("Function")) {
        scopes.get(0).put(name, (val==null ? new Function() : (Function)val));
    }
  }
  
  private void declareList(String type, String name, Object val){
    //Assignment
    if (type==null) {
        scopes.peek().put(name, (val==null ? new MyGraph() : new MyGraph((List<Type>)val)));
    } else if (type.equals("Graph")){
        scopes.peek().put(name, (val==null ? new MyGraph() : new MyGraph((List<Type>)val)));
    } else {
        System.out.print("Type " + type + " does not support instantiation with a list!");
        System.exit(1);
    }
    
  }
  
  private void setReturn(SemanticWrapper toReturn) {
    if (interactive) {
        returnVal = toReturn;
    }
    
    scopes.peek();
  }
  
  public void setPreprocessor(Preprocessor p) {
    
    this.p = p;
    lexer.loadFunctions(p);
    
  }
  
  private int compare(SemanticWrapper a, SemanticWrapper b) {
    if (a.val==null && b.val==null) {
        return 0;
    } else if (a.val==null || b.val ==null) {
        //Non-null value is larger than null
        return (a.val==null ? -1 : 1);
    }
    
    if (a.val.getClass().toString().indexOf("NumberType")>0 || b.val.getClass().toString().indexOf("NumberType")>0) {
        //System.out.println("Comparing number!");
        return ((Double)(((NumberType)(a.val)).getDouble())).compareTo(((NumberType)(b.val)).getDouble());
    } else {
        //System.out.println("Comparing other!");
        return ((Type)(a.val)).getValue().compareTo(((Type)(b.val)).getValue());
    }
    
  }
  
  public SemanticWrapper getReturn() {
    return returnVal;
  }
  
  

}
