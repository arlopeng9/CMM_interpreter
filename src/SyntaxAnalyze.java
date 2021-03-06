//import java.io.StreamTokenizer;
import java.util.List;

public class SyntaxAnalyze {
    private List<Token> tokenList;
    private Token curToken;
    private int tokenIndex = 0;

    private TreeNode curNode;
    private TreeNode headNode;
    private String errorInfo = "";

    private int loop = 0;
    public SyntaxAnalyze(List<Token> tokenList){
        this.tokenList = tokenList;
    }
    public TreeNode getHeadNode() {
        return this.headNode;
    }

    public void addNonTerminal(int type, String name){
        TreeNode childNode = new TreeNode(type, curNode, name);
        childNode.setParentNode(curNode);
        curNode.getChildren().add(childNode);
        curNode = childNode;//前驱遍历
    }

    public void addTerminal(Token token){
        TreeNode childNode = new TreeNode(TreeNodeType.TERMINAL_SYMBOL, token, curNode, token.strval);
        curNode.getChildren().add(childNode);
    }

    public void printTree(TreeNode headNode,String space){
        System.out.println(space  + headNode.getCallName());
        for(TreeNode tn : headNode.getChildren()){
            printTree(tn, space + "  ");
        }
        return;
    }
    public String parse(){
        String result = "";
        if(tokenList.size() > 0){
            curToken = tokenList.get(0);
            headNode = new TreeNode(TreeNodeType.PROGRAM, null, "program");
            curNode = headNode;
            while (curToken.type != TokenType.STOP_SIGN){
                if(curToken.type >= 0 && curToken.type<= 7){//保留字或标识符
                    declar_stmt();
                }else {
                    
                    break;
                }
            }
        }else {
            //报错：程序为空
            errorInfo += "error: 程序为空\n";
        }
        result += this.errorInfo;

        return result;
    }

    //函数定义或声明
    public void declar_stmt(){
        //当前token为类型时
        if((curToken.type>=5 )&& (curToken.type <=8)){
            addNonTerminal(TreeNodeType.declar_stmt, "函数定义或声明");
            type();
            variable();
            if(curToken.type == TokenType.LEFT_PARENTHESIS){
                matchToken();
            }else{
                errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
            }
            para_stmt();
            if(curToken.type == TokenType.RIGHT_PARENTHESIS){
                matchToken();
            }else{
                
                errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
            }
            declar_stmt_closure();
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }
    //类型
    public void type(){
        //当前token为类型时
        if((curToken.type>=5 )&& (curToken.type <=8)){
            addNonTerminal(TreeNodeType.type, "类型");
            matchToken();
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

    //变量
    public void variable(){
        //当前token为标识符时
        if(curToken.type == TokenType.IDENTIFIER){
            addNonTerminal(TreeNodeType.variable, "变量");
            matchToken();
            arrayIndex();
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

    //数组下标
    public void arrayIndex(){
        //当前token为[时
        if(curToken.type == TokenType.L_SQUARE_BRACKET){
            addNonTerminal(TreeNodeType.arrayIndex, "数组下标");
            matchToken();
            factor();
            if(curToken.type == TokenType.R_SQUARE_BRACKET){
                matchToken();
            }else{
                getPreToken();
               getNextToken();
                errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
            }
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

   
    //因式
    public void factor(){
        //当前token为常数时
        if((curToken.type == TokenType.FLOATVAL) || (curToken.type == TokenType.INTVAL)){
            addNonTerminal(TreeNodeType.factor, "因式");
            matchToken();
            curNode = curNode.getParentNode();
             //当前token为标识符时
        }else if(curToken.type == TokenType.IDENTIFIER){
            addNonTerminal(TreeNodeType.factor, "因式");
            variable();
            curNode = curNode.getParentNode();
             //当前token为(时
        }else if(curToken.type == TokenType.LEFT_PARENTHESIS){
            addNonTerminal(TreeNodeType.factor, "因式");
            matchToken();
            arith_expression();
            if(curToken.type == TokenType.RIGHT_PARENTHESIS){
                matchToken();
            }else{
                getPreToken();
               getNextToken();
                errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
            }
            curNode = curNode.getParentNode();
        }else if(curToken.type == TokenType.SUBSTRACT){
            addNonTerminal(TreeNodeType.factor, "因式");
            matchToken();
            if((curToken.type == TokenType.FLOATVAL) || (curToken.type == TokenType.INTVAL)){
                matchToken();
                curNode = curNode.getParentNode();
                 //当前token为标识符时
            }else if(curToken.type == TokenType.IDENTIFIER){
                variable();
                curNode = curNode.getParentNode();
                 //当前token为(时
            }else if(curToken.type == TokenType.LEFT_PARENTHESIS){
                matchToken();
                arith_expression();
                if(curToken.type == TokenType.RIGHT_PARENTHESIS){
                    matchToken();
                }else{
                    getPreToken();
                   getNextToken();
                    errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
                }
                curNode = curNode.getParentNode();
            }else{
                return;
            }
             //当前token为(时
        }else{
            return;
        }
    }

    //算数表达式
    public void arith_expression(){
         //当前token为常数、标识符、(、++、--、-时
         if((curToken.type == TokenType.FLOATVAL) || (curToken.type == TokenType.INTVAL)||(curToken.type == TokenType.IDENTIFIER)||(curToken.type == TokenType.LEFT_PARENTHESIS)||(curToken.type == TokenType.ADD_ADD)||(curToken.type == TokenType.SUBSTRACT_SUBSTRACT)||(curToken.type == TokenType.SUBSTRACT)){
            addNonTerminal(TreeNodeType.arith_expression, "算数表达式");
            factors();
            term();
            curNode = curNode.getParentNode();
             //当前token为标识符时
        }else{
            return;
        }
    }

     //因子
     public void factors(){
        //当前token为常数、标识符、(、++、--、-时
        if((curToken.type == TokenType.FLOATVAL) || (curToken.type == TokenType.INTVAL)||(curToken.type == TokenType.IDENTIFIER)||(curToken.type == TokenType.LEFT_PARENTHESIS)||(curToken.type == TokenType.ADD_ADD)||(curToken.type == TokenType.SUBSTRACT_SUBSTRACT)||(curToken.type == TokenType.SUBSTRACT)){
           addNonTerminal(TreeNodeType.factors, "因子");
           factor1();
           factor_recursion();
           curNode = curNode.getParentNode();
            //当前token为标识符时
       }else{
           return;
       }
   }

 //因式1
    public void factor1(){
        //当前token为++、--时
        if((curToken.type == TokenType.ADD_ADD)||(curToken.type == TokenType.SUBSTRACT_SUBSTRACT)){
            addNonTerminal(TreeNodeType.factor1, "因式1");
            matchToken();
            factor();
            curNode = curNode.getParentNode();
             //当前token为标识符时
        }else{
            addNonTerminal(TreeNodeType.factor1, "因式1");
            factor();
            if((curToken.type == TokenType.ADD_ADD)||(curToken.type == TokenType.SUBSTRACT_SUBSTRACT)){
                matchToken();
            }
            curNode = curNode.getParentNode();
        }
    }
   //项
   public void term(){
    //当前token为+、-时
    if((curToken.type == TokenType.ADD)||(curToken.type == TokenType.SUBSTRACT)){
       addNonTerminal(TreeNodeType.term, "项");
       matchToken();
       factors();
       term();
       curNode = curNode.getParentNode();
        //当前token为标识符时
   }else{
       return;
   }
}

    //因式递归
    public void factor_recursion(){
        //当前token为*、/时
        if((curToken.type == TokenType.MULTIPLY)||(curToken.type == TokenType.DIVIDE)){
           addNonTerminal(TreeNodeType.factor_recursion, "因式递归");
           matchToken();
           factor1();
           factor_recursion();
           curNode = curNode.getParentNode();
            //当前token为标识符时
       }else{
           return;
       }
   }
    //参数声明
    public void para_stmt(){
        //当前token为标识符时
        if((curToken.type>=5 )&& (curToken.type <=8)){
            addNonTerminal(TreeNodeType.para_stmt, "参数声明");
            stmt();
            stmt_closure();
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

   

      //赋初值
      public void initialvalue(){
        //当前token为=时
        if(curToken.type == TokenType.EQUAL){
            addNonTerminal(TreeNodeType.initialvalue, "赋初值");
            matchToken();
            rightVal();
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

    //右值
    public void rightVal(){
        //当前token为{时
        if(curToken.type == TokenType.L_BRACKET){
            addNonTerminal(TreeNodeType.rightVal, "右值");
            matchToken();
            manyConstant();
            if(curToken.type == TokenType.R_BRACKET){
                matchToken();
            }else{
                errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:右值出错 ;\n";
            }
            curNode = curNode.getParentNode();
            //当前token为常数、标识符、(、++、--时
        }else if((curToken.type == TokenType.FLOATVAL)|| (curToken.type == TokenType.INTVAL)||(curToken.type == TokenType.IDENTIFIER)||(curToken.type == TokenType.LEFT_PARENTHESIS)||(curToken.type == TokenType.ADD_ADD)||(curToken.type == TokenType.SUBSTRACT_SUBSTRACT)||(curToken.type == TokenType.SUBSTRACT)){
            addNonTerminal(TreeNodeType.rightVal, "右值");
            if(islogical1(tokenIndex)){
                logical_expression();
            }else{
            arith_expression();
            }
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

    //多个数据
    public void manyConstant(){
        //当前token为常数时
        if((curToken.type == TokenType.FLOATVAL) || (curToken.type == TokenType.INTVAL)){
            addNonTerminal(TreeNodeType.manyConstant, "多个数据");
            matchToken();
            constant_closure();
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

    //常数闭包
    public void constant_closure(){
        //当前token为,时
        if(curToken.type==TokenType.COMMA){
            addNonTerminal(TreeNodeType.constant_closure, "常数闭包");
            matchToken();
            //当前token为常数时
            if((curToken.type==TokenType.FLOATVAL) || (curToken.type == TokenType.INTVAL)){
                matchToken();
                constant_closure();
            }else{
                getPreToken();
               getNextToken();
                errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
            }
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

    //声明闭包
    public void stmt_closure(){
        //当前token为标识符时
        if(curToken.type==TokenType.COMMA){
            addNonTerminal(TreeNodeType.stmt_closure, "声明闭包");
            matchToken();
            stmt();
            stmt_closure();
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

    //函数定义或声明闭包
    public void declar_stmt_closure(){
        //当前token为;时
        if(curToken.type == TokenType.COLON){
            matchToken();
            //当前token为{时
        }else if(curToken.type == TokenType.L_BRACKET){
            addNonTerminal(TreeNodeType.declar_stmt_closure, "函数定义与声明闭包");
            co_function();
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }
    
    //复合函数块
    public void co_function(){
        //当前token为{时
        if(curToken.type == TokenType.L_BRACKET){
            addNonTerminal(TreeNodeType.co_function, "复合函数块");
            matchToken();
            functionBlock();
            if(curToken.type == TokenType.R_BRACKET){
                matchToken();
            }else{
                errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
            }
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

    //函数块
    public void functionBlock(){
        if((curToken.type == TokenType.IDENTIFIER)||((curToken.type>=5 ) && (curToken.type <=8))||(curToken.type == TokenType.FOR)||(curToken.type == TokenType.WHILE)
        ||(curToken.type == TokenType.IF)||(curToken.type == TokenType.RETURN)||(curToken.type == TokenType.SCAN)||(curToken.type == TokenType.SCANF)||(curToken.type == TokenType.PRINT)
        ||(curToken.type == TokenType.PRINTF)||(curToken.type == TokenType.BREAK)||(curToken.type == TokenType.CONTINUE)){
            addNonTerminal(TreeNodeType.functionBlock, "函数块");
            stmt_expression_closure();
            functionBlock_closure();
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

    //函数块闭包
    public void functionBlock_closure(){
        //当前token为类型或标识符时
        if((curToken.type == TokenType.IDENTIFIER)||((curToken.type>=5 ) && (curToken.type <=8))){
            addNonTerminal(TreeNodeType.functionBlock_closure, "函数块闭包");
            assignment();
            functionBlock();
            curNode = curNode.getParentNode();
            //当前token为for时
        }else if(curToken.type == TokenType.FOR){
            addNonTerminal(TreeNodeType.functionBlock_closure, "函数块闭包");
            forloop();
            functionBlock();
            curNode = curNode.getParentNode();
            //当前token为while时
        }else if(curToken.type == TokenType.WHILE){
            addNonTerminal(TreeNodeType.functionBlock_closure, "函数块闭包");
            whileloop();
            functionBlock();
            curNode = curNode.getParentNode();
            //当前token为if时
        }else if(curToken.type == TokenType.IF){
            addNonTerminal(TreeNodeType.functionBlock_closure, "函数块闭包");
            if_statement();
            functionBlock();
            curNode = curNode.getParentNode();
            //当前token为return时
        }else if(curToken.type == TokenType.RETURN){
            addNonTerminal(TreeNodeType.functionBlock_closure, "函数块闭包");
            return_statement();
            functionBlock();
            curNode = curNode.getParentNode();
        }else if(curToken.type == TokenType.PRINT){
            addNonTerminal(TreeNodeType.functionBlock_closure, "函数块闭包");
            print_function();
            functionBlock();
            curNode = curNode.getParentNode();
        }else if(curToken.type == TokenType.PRINTF){
            addNonTerminal(TreeNodeType.functionBlock_closure, "函数块闭包");
            printf_function();
            functionBlock();
            curNode = curNode.getParentNode();
        }else if(curToken.type == TokenType.SCAN){
            addNonTerminal(TreeNodeType.functionBlock_closure, "函数块闭包");
            scan_function();
            functionBlock();
            curNode = curNode.getParentNode();
        }else if(curToken.type == TokenType.SCANF){
            addNonTerminal(TreeNodeType.functionBlock_closure, "函数块闭包");
            scanf_function();
            functionBlock();
            curNode = curNode.getParentNode();
        }else if(curToken.type == TokenType.CONTINUE){
            if(loop>0){
                addNonTerminal(TreeNodeType.functionBlock_closure, "函数块闭包");
                continue_statment();
                functionBlock();
                curNode = curNode.getParentNode();
            }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: continue不能在loop外;\n";
            }
        }else if(curToken.type == TokenType.BREAK){
            if(loop>0){
                addNonTerminal(TreeNodeType.functionBlock_closure, "函数块闭包");
                break_statment();
                functionBlock();
                curNode = curNode.getParentNode();
            }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: brrak不能在loop外;\n";
            }
        }else{
            return;
        }
    }

//print函数
public void print_function(){
    //当前token为print时
    if((curToken.type==TokenType.PRINT)){
        addNonTerminal(TreeNodeType.print_function, "print函数");
        matchToken();
        if(curToken.type == TokenType.LEFT_PARENTHESIS){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:print函数缺少左括号 ;\n";
        }
        para();
        if(curToken.type == TokenType.RIGHT_PARENTHESIS){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:print函数缺少右括号 ;\n";
        }
        if(curToken.type == TokenType.COLON){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:缺少分号 ;\n";
        }
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}


//printf函数
public void printf_function(){
    //当前token为print时
    if((curToken.type==TokenType.PRINTF)){
        addNonTerminal(TreeNodeType.printf_function, "printf函数");
        matchToken();
        if(curToken.type == TokenType.LEFT_PARENTHESIS){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:print函数缺少左括号 ;\n";
        }
        para_list();
        if(curToken.type == TokenType.RIGHT_PARENTHESIS){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:print函数缺少右括号 ;\n";
        }
        if(curToken.type == TokenType.COLON){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:缺少分号 ;\n";
        }
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}


//scan函数
public void scan_function(){
    //当前token为print时
    if((curToken.type==TokenType.SCAN)){
        addNonTerminal(TreeNodeType.scan_functon, "scan函数");
        matchToken();
        if(curToken.type == TokenType.LEFT_PARENTHESIS){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:print函数缺少左括号 ;\n";
        }
        if(curToken.type==TokenType.IDENTIFIER)
        para();
        if(curToken.type == TokenType.RIGHT_PARENTHESIS){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:print函数缺少右括号 ;\n";
        }
        if(curToken.type == TokenType.COLON){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:缺少分号 ;\n";
        }
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}
//scanf函数
public void scanf_function(){
    //当前token为print时
    if((curToken.type==TokenType.SCANF)){
        addNonTerminal(TreeNodeType.scanf_function, "scanf函数");
        matchToken();
        if(curToken.type == TokenType.LEFT_PARENTHESIS){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:print函数缺少左括号 ;\n";
        }
        if(curToken.type==TokenType.IDENTIFIER)
        para_list();
        if(curToken.type == TokenType.RIGHT_PARENTHESIS){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:print函数缺少右括号 ;\n";
        }
        if(curToken.type == TokenType.COLON){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:缺少分号 ;\n";
        }
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}
 
//参数列表
public void para_list(){
    if((curToken.type==TokenType.IDENTIFIER)||(curToken.type==TokenType.FLOATVAL)||(curToken.type==TokenType.INTVAL)){
        addNonTerminal(TreeNodeType.para_list, "参数列表");
        para();
        para_closure();
        curNode = curNode.getParentNode();
    }
}

 
//参数
public void para(){
    if((curToken.type==TokenType.FLOATVAL)||(curToken.type==TokenType.INTVAL)){
        addNonTerminal(TreeNodeType.para, "参数");
        matchToken();
        curNode = curNode.getParentNode();
    }else if((curToken.type==TokenType.IDENTIFIER)){
        addNonTerminal(TreeNodeType.para, "参数");
        variable();
        curNode = curNode.getParentNode();
    }
}
 
//参数闭包
public void para_closure(){
    if(curToken.type == TokenType.COMMA){
        addNonTerminal(TreeNodeType.para_closure, "参数闭包");
        matchToken();
    if((curToken.type==TokenType.IDENTIFIER)||(curToken.type==TokenType.FLOATVAL)||(curToken.type==TokenType.INTVAL)){
       para();
    curNode = curNode.getParentNode();
    }
}
}

 //for循环
 public void forloop(){
    //当前token为(时
    if((curToken.type==TokenType.FOR)){
        loop++;
        addNonTerminal(TreeNodeType.forloop, "for循环");
        matchToken();
        forblock();
        curNode = curNode.getParentNode();
        loop--;
    }else{
        return;
    }
}

//for循环块
public void forblock(){
    ////当前token为(时
    if((curToken.type==TokenType.LEFT_PARENTHESIS)){
        addNonTerminal(TreeNodeType.forblock, "for循环块");
        matchToken();
        assignment();
        logical_expression();
         //当前token为分号时
        if((curToken.type==TokenType.COLON)){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
       }
       if((curToken.type==TokenType.IDENTIFIER)){
        factor1();
    }else{
        errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
    }
       
        //当前token为)时
        if((curToken.type==TokenType.RIGHT_PARENTHESIS)){
            matchToken();
        }else{
           errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
       }
       if(curToken.type == TokenType.L_BRACKET){
            co_function();
       }else{
           astament();
       }
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}

//一条语句
public void  astament(){
     //当前token为类型或标识符时
     if((curToken.type == TokenType.IDENTIFIER)||((curToken.type>=5 ) && (curToken.type <=8))){
        addNonTerminal(TreeNodeType.astament, "一条语句");
        assignment();
        curNode = curNode.getParentNode();
        //当前token为for时
    }else if(curToken.type == TokenType.FOR){
        addNonTerminal(TreeNodeType.astament, "一条语句");
        forloop();
        curNode = curNode.getParentNode();
        //当前token为while时
    }else if(curToken.type == TokenType.WHILE){
        addNonTerminal(TreeNodeType.astament, "一条语句");
        whileloop();
        curNode = curNode.getParentNode();
        //当前token为if时
    }else if(curToken.type == TokenType.IF){
        addNonTerminal(TreeNodeType.astament, "一条语句");
        if_statement();
        curNode = curNode.getParentNode();
        //当前token为return时
    }else if(curToken.type == TokenType.RETURN){
        addNonTerminal(TreeNodeType.astament, "一条语句");
        return_statement();
        curNode = curNode.getParentNode();
    }else if(curToken.type == TokenType.PRINT){
        addNonTerminal(TreeNodeType.astament, "一条语句");
        print_function();
        curNode = curNode.getParentNode();
    }else if(curToken.type == TokenType.PRINTF){
        addNonTerminal(TreeNodeType.astament, "一条语句");
        printf_function();
        curNode = curNode.getParentNode();
    }else if(curToken.type == TokenType.SCAN){
        addNonTerminal(TreeNodeType.astament, "一条语句");
        scan_function();
        curNode = curNode.getParentNode();
    }else if(curToken.type == TokenType.SCANF){
        addNonTerminal(TreeNodeType.astament, "一条语句");
        scanf_function();
        curNode = curNode.getParentNode();
    }else if(curToken.type == TokenType.CONTINUE){
        if(loop>0){
            addNonTerminal(TreeNodeType.astament, "一条语句");
            continue_statment();
            curNode = curNode.getParentNode();
        }else{
        errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: continue不能再loop外;\n";
        }
    }else if(curToken.type == TokenType.BREAK){
        if(loop>0){
            addNonTerminal(TreeNodeType.astament, "一条语句");
            break_statment();
            curNode = curNode.getParentNode();
        }else{
        errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: break不能在loop外;\n";
        }
    }else{
        return;
    }
}
   
//continue语句
public void continue_statment(){
    //当前token为(时
    if((curToken.type==TokenType.CONTINUE)){
        addNonTerminal(TreeNodeType.continue_statment, "continue语句");
        matchToken();
        if(curToken.type == TokenType.COLON){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:缺少 ;\n";
        }
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}

//break语句
public void break_statment(){
    //当前token为(时
    if((curToken.type==TokenType.BREAK)){
        addNonTerminal(TreeNodeType.break_statment, "break语句");
        matchToken();
        if(curToken.type == TokenType.COLON){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect:缺少 ;\n";
        }
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}

 //while循环块
 public void whileloop(){
    //当前token为(时
    if((curToken.type==TokenType.WHILE)){
        loop++;
        addNonTerminal(TreeNodeType.whileloop, "while循环块");
        matchToken();
        whileblock();
        curNode = curNode.getParentNode();
        loop--;
    }else{
        return;
    }
}

//while循环块
public void whileblock(){
    //当前token为(时
    if((curToken.type==TokenType.LEFT_PARENTHESIS)){
        addNonTerminal(TreeNodeType.whileblock, "while循环块");
        matchToken();
        logical_expression();
         //当前token为)时
        if((curToken.type==TokenType.RIGHT_PARENTHESIS)){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
       }
       if(curToken.type == TokenType.L_BRACKET){
        co_function();
    }else{
       astament();
   }
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}

//条件语句
public void if_statement(){
    //当前token为(时
    if((curToken.type==TokenType.IF)){
        addNonTerminal(TreeNodeType.if_statement, "条件语句");
        matchToken();
        ifblock();
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}

//条件语句块
public void ifblock(){
    //当前token为(时
    if((curToken.type==TokenType.LEFT_PARENTHESIS)){
        addNonTerminal(TreeNodeType.ifblock, "条件语句块");
        matchToken();
        logical_expression();
         //当前token为)时
        if((curToken.type==TokenType.RIGHT_PARENTHESIS)){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
       }
       if(curToken.type == TokenType.L_BRACKET){
        co_function();
   }else{
       astament();
   }
        else_statement();
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}

//否则语句
public void else_statement(){
    //当前token为(时
    if((curToken.type==TokenType.ELSE)){
        addNonTerminal(TreeNodeType.else_statement, "否则语句");
        matchToken();
        if(curToken.type == TokenType.L_BRACKET){
            co_function();
       }else{
           astament();
       }
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}

//返回语句
public void return_statement(){
    //当前token为(时
    if((curToken.type==TokenType.RETURN)){
        addNonTerminal(TreeNodeType.return_statement, "返回语句");
        matchToken();
        factor();
        if(curToken.type == TokenType.COLON){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
       }
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}
//赋值语句
public void assignment(){
    //当前token为类型时
    if((curToken.type>=5 )&& (curToken.type <=8)){
        addNonTerminal(TreeNodeType.assignment, "赋值语句");
        type();
        variable();
        assign();
        curNode = curNode.getParentNode();
    }else if(curToken.type == TokenType.IDENTIFIER){
        addNonTerminal(TreeNodeType.assignment, "赋值语句");
        variable();
        assign();
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}

//逻辑表达式
public void logical_expression(){
    //当前token为常数、标识符、(、True、False时
    if((curToken.type == TokenType.FLOATVAL) || (curToken.type == TokenType.INTVAL)||(curToken.type == TokenType.IDENTIFIER)||(curToken.type == TokenType.LEFT_PARENTHESIS)||(curToken.type == TokenType.FALSE)||(curToken.type == TokenType.TRUE)){
        addNonTerminal(TreeNodeType.logical_expression, "逻辑表达式");
        logical_factor1();
        //当前token为||时
        if((curToken.type == TokenType.OR)){
            matchToken();
            logical_expression();
        }
        curNode = curNode.getParentNode();
    }else{
        return;
    }
}
//逻辑因式1
public void logical_factor1(){
    //当前token为常数、标识符、(时
    if((curToken.type == TokenType.FLOATVAL) || (curToken.type == TokenType.INTVAL)||(curToken.type == TokenType.IDENTIFIER)||(curToken.type == TokenType.LEFT_PARENTHESIS)||(curToken.type == TokenType.FALSE)||(curToken.type == TokenType.TRUE)){
        addNonTerminal(TreeNodeType.logical_factor1, "逻辑因式1");
        logical_factor2();
        //当前token为&&时
        if((curToken.type == TokenType.AND)){
            matchToken();
            logical_factor1();
        }       
        curNode = curNode.getParentNode();
         //当前token为标识符时
    }else{
        return;
    }
}
//判断是否是逻辑表达式,逻辑表达式返回true。
public boolean islogical(int index){
    Token token = tokenList.get(index);
    int count = 0;
    while(token.type == TokenType.LEFT_PARENTHESIS){
            index++;
            token = tokenList.get(index);
            count++;
    }
    while(count>=1){
        if((count == 1)&&((token.type == TokenType.MAIOR)||(token.type == TokenType.MENOR)||(token.type == TokenType.EQEQ)||(token.type == TokenType.UNEQ)||(token.type == TokenType.MEEQ)||(token.type == TokenType.MAEQ)||(token.type == TokenType.AND)||(token.type == TokenType.OR)))
            return true;

        if(token.type == TokenType.RIGHT_PARENTHESIS){
                if(count > 1){ 
                    count--;
                    if((token.type == TokenType.RIGHT_PARENTHESIS)&&(count == 1))
                    return true;
                }
                else if(count == 1) return false;
            }
        if(token.type == TokenType.LEFT_PARENTHESIS){
                count++;
        }
        index++;
        token = tokenList.get(index);
    }
        
        return false;
}

//判断是否是逻辑表达式,逻辑表达式返回true。<右值> -> <逻辑表达式>此时使用
public boolean islogical1(int index){
    Token token = tokenList.get(index);
    while(token.type != TokenType.COLON){
        if(((token.type == TokenType.MAIOR)||(token.type == TokenType.MENOR)||(token.type == TokenType.EQEQ)||(token.type == TokenType.UNEQ)||(token.type == TokenType.MEEQ)||(token.type == TokenType.MAEQ)||(token.type == TokenType.AND)||(token.type == TokenType.OR)))
            return true;
        index++;
        token = tokenList.get(index);
    }
        return false;
}

//逻辑因式2
public void logical_factor2(){
    //当前token为常数、标识符、(时
    if((curToken.type == TokenType.FLOATVAL) || (curToken.type == TokenType.INTVAL)||(curToken.type == TokenType.IDENTIFIER)||(curToken.type == TokenType.LEFT_PARENTHESIS)){
        addNonTerminal(TreeNodeType.logical_factor2, "逻辑因式2");
        if(islogical(tokenIndex)){
            matchToken();
            logical_expression();
            if(curToken.type == TokenType.RIGHT_PARENTHESIS){
                matchToken();
            }else{
                errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: 逻辑因式2错误;\n";
            }
        }else{
            arith_expression();
            //当前token为<、<=、>、>=、<>、==、||、&&时
            if((curToken.type == TokenType.MAIOR)||(curToken.type == TokenType.MENOR)||(curToken.type == TokenType.EQEQ)||(curToken.type == TokenType.UNEQ)||(curToken.type == TokenType.MEEQ)||(curToken.type == TokenType.MAEQ)||(curToken.type == TokenType.AND)||(curToken.type == TokenType.OR)){
            matchToken();
            }else{
                errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: 逻辑因式2错误;\n";
       }
       arith_expression();
    }

        curNode = curNode.getParentNode();
    }else if((curToken.type == TokenType.TRUE)||(curToken.type == TokenType.FALSE)){
        addNonTerminal(TreeNodeType.logical_factor2, "逻辑因式2");
        matchToken();
        curNode = curNode.getParentNode();
    }
    else {
        return;
    }
}
//赋值
public void assign(){
    //当前token为=、+=、-=、*=、/=、%=时
    if((curToken.type==TokenType.EQUAL)|| (curToken.type == TokenType.SUBSTRACT_EQUAL)|| (curToken.type == TokenType.ADD_EQUAL)|| (curToken.type == TokenType.MULTIPLY_EQUAL)|| (curToken.type == TokenType.DIVIDE_EQUAL)){
        addNonTerminal(TreeNodeType.assign, "赋值");
        matchToken();
        rightVal();
        if(curToken.type == TokenType.COLON){
            matchToken();
        }else{
            errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
       }
        curNode = curNode.getParentNode();
    }else if((curToken.type==TokenType.ADD_ADD)|| (curToken.type == TokenType.SUBSTRACT_SUBSTRACT)){
        addNonTerminal(TreeNodeType.assign, "赋值");
        matchToken();
        if(curToken.type == TokenType.COLON){
            matchToken();
        }else{
            //errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
       }
        curNode = curNode.getParentNode();
    }
    else{
        errorInfo +=  "error: line" + curToken.line + "," + "  " + curToken.strval + "    非法赋值语句;\n";
        return;
    }
}

    //声明语句闭包
    public void stmt_expression_closure(){
        //当前token为类型时
        if((curToken.type>=5 )&& (curToken.type <=8)){
            addNonTerminal(TreeNodeType.stmt_expression_closure, "声明语句闭包");
            stmt_expression();
            stmt_expression_closure();
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

    //声明语句
    public void stmt_expression(){
        //当前token为类型时
        if((curToken.type>=5 )&& (curToken.type <=8)){
            addNonTerminal(TreeNodeType.stmt_expression, "声明语句");
            para_stmt();
            if(curToken.type == TokenType.COLON)
            {
                matchToken();
            }else{
                getPreToken();
               getNextToken();
                errorInfo += "error: line" + curToken.line + "," + "  " + curToken.strval + "    expect: ;\n";
            }
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

     //声明
     public void stmt(){
        //当前token为类型时
        if((curToken.type>=5 )&& (curToken.type <=8)){
            addNonTerminal(TreeNodeType.stmt, "声明");
            type();
            variable();
            initialvalue();
            curNode = curNode.getParentNode();
        }else{
            return;
        }
    }

    private void matchToken(){
        addTerminal(curToken);
        getNextToken();
    }

    private void getNextToken(){
        tokenIndex++;//读取下一个token
        if(tokenIndex < tokenList.size()){
            curToken = tokenList.get(tokenIndex);
        }else {


        }
    }

    private void getPreToken(){
        tokenIndex--;//读取上一个token
        curToken = tokenList.get(tokenIndex);
    }


    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
    
}