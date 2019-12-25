import java.util.Scanner;

public class SemanticAnalyze {
    private int loop = 0;
    private TreeNode headNode;
    private SymbalTable symbalTable;
    private TreeNode curNode;
    private Token curToken;
    private int level = 0;
    private String result = "\n";
    private String errorInfo = "";
    private boolean notcontinue = true;
    public SemanticAnalyze(TreeNode headNode) {
        this.headNode = headNode;
        this.symbalTable = new SymbalTable();
    }

    public void printRecord(Record record) {
        if (record.getType() == Record.tFloat)
            System.out.println(record.getfloatVal());
        if (record.getType() == Record.tFloatArray)
            System.out.println(record.getfloatArray()[record.getArrayIndex()]);
        if (record.getType() == Record.tInt)
            System.out.println(record.getintVal());
        if (record.getType() == Record.tIntArray)
            System.out.println(record.getIntArray()[record.getArrayIndex()]);
        if (record.getType() == Record.tbool)
            System.out.println(record.getboolVal());
        if (record.getType() == Record.tboolArray)
            System.out.println(record.getboolArray()[record.getArrayIndex()]);
    }

    public void printSymbalTable() {
        for (Record r : this.symbalTable.getTable()) {
            System.out.print(r.getName() + "\t" + r.getType() );
            this.printRecord(r);
        }
    }

    public String semantic() {
        curNode = headNode;
        for (int i = 0; i < curNode.getChildren().size(); i++) {
            curNode = curNode.getChildren().get(i);// 函数定义或声明
            declar_stmt();
            curNode = curNode.getParentNode();
        }
        if (errorInfo != "") {
            result = "Error: \n" + errorInfo + result;
        }
        return result;
    }

    // 函数定义或声明
    public void declar_stmt() {
        curNode = curNode.getChildren().get(0);// 类型
        int type = type();
        curNode = curNode.getParentNode().getChildren().get(1);// 变量
        Record var = variable();
        if (var == null) {// 如果该变量未声明过
            String name = curNode.getChildren().get(0).getValue().strval;
            if (curNode.getChildren().size() == 1) {// variable: identifier
                var = new Record(level, curNode.getValue(), type, name, 0);
            } else {// 数组
                if(curNode.getChildren().get(1).getChildren().size()>2){
                    curNode = curNode.getChildren().get(1).getChildren().get(1);
                    var = factor();
                    int arrayNum = 0;
                    curNode = curNode.getParentNode().getParentNode();
                    if (var != null && var.getType() == Record.tInt) {
                        arrayNum = var.getintVal();
                    } 
                    if (var != null && var.getType() == Record.tIntArray) {
                        arrayNum = var.getIntArray()[var.getArrayIndex()];
                    } 
                            var = new Record(level, curNode.getValue(), type + 3, name, new int[arrayNum]);
                
                }  else {
                    errorInfo += "数组未指明大小或大小为浮点数：line " + curNode.getChildren().get(0).getValue().line + " "
                            + curNode.getChildren().get(0).getValue().strval + "\n";

                }
            }
            symbalTable.getTable().add(var);
        } else {
            errorInfo += "重复声明变量：line " + curNode.getChildren().get(0).getValue().line + " "
                    + curNode.getChildren().get(0).getValue().strval + "\n";

        }
        curNode = curNode.getParentNode().getChildren().get(3);// 参数声明
        if(curNode.getType() == TreeNodeType.para_stmt)
        {
        para_stmt();
        curNode = curNode.getParentNode().getChildren().get(5);// 函数定义或声明闭包
        declar_stmt_closure();
        }else {
        curNode = curNode.getParentNode().getChildren().get(4);// 函数定义或声明闭包
        declar_stmt_closure();
        }
        curNode = curNode.getParentNode();

    }

    // 类型
    public int type() {
        curNode = curNode.getChildren().get(0);
        int type = 0;
        switch (curNode.getValue().strval) {
        case "int":
            type = Record.tInt;
            break;
        case "bool":
            type = Record.tbool;
            break;
        case "float":
            type = Record.tFloat;
            break;
        default:
            break;
        }
        curNode = curNode.getParentNode();
        return type;
    }

    // 变量
    public Record variable() {
        curNode = curNode.getChildren().get(0);// 标识符
        Record var = symbalTable.getRecordByName(curNode.getValue().strval);
        if (var == null) {
            curNode = curNode.getParentNode();// 变量
            return null;
        }
        curNode = curNode.getParentNode();// 变量
        if (curNode.getChildren().size() > 1) {// 数组下标不为空
            if(curNode.getChildren().get(1).getChildren().size()>2){
            curNode = curNode.getChildren().get(1).getChildren().get(1);// 因式

            if ((factor() != null) && factor().getType() == Record.tInt) {
                int arrayIndex = factor().getintVal();
                if (var.getType() == Record.tIntArray || var.getType() == Record.tFloatArray
                        || var.getType() == Record.tboolArray) {
                    var.setArrayIndex(arrayIndex);
                    if (arrayIndex >= var.getArrayNum()) {
                        errorInfo += "数组越界: line" + curNode.getChildren().get(0).getValue().line + " " + var.getName()
                                + "[" + var.getArrayIndex() + "] in array " + var.getName() + "[" + var.getArrayNum()
                                + "]\n";
                    }
                } else {// 普通变量但是有下标
                    errorInfo += "错误的变量使用方式：变量" + var.getName() + "不是数组\n";
                }
            }
           else if ((factor() != null) && factor().getType() == Record.tIntArray) {
                int arrayIndex = factor().getIntArray()[factor().getArrayNum()];
                if (var.getType() == Record.tIntArray || var.getType() == Record.tFloatArray
                        || var.getType() == Record.tboolArray) {
                    var.setArrayIndex(arrayIndex);
                    if (arrayIndex >= var.getArrayNum()) {
                        errorInfo += "数组越界: line" + curNode.getChildren().get(0).getValue().line + " " + var.getName()
                                + "[" + var.getArrayIndex() + "] in array " + var.getName() + "[" + var.getArrayNum()
                                + "]\n";
                    }
                } else {// 普通变量但是有下标
                    errorInfo += "错误的变量使用方式：变量" + var.getName() + "不是数组\n";
                }
            } else if ((factor() != null)) {
                errorInfo += "数组下标不为整数" + var.getName() + "\n";
            } 
            curNode = curNode.getParentNode().getParentNode();
        }else {
                errorInfo += "数组没有下标" + var.getName() + "\n";
            }
        }
        
        
        return var;
    }

    // 因式
    public Record factor() {
        Record var = null;
        if (curNode.getChildren() != null) {
            curNode = curNode.getChildren().get(0);
            if ((curNode.getValue()!=null)&&curNode.getValue().type == TokenType.LEFT_PARENTHESIS) {
                curNode = curNode.getParentNode().getChildren().get(1);
                var =  arith_expression();
            } else if ((curNode.getValue()!=null)&&curNode.getValue().type == TokenType.FLOATVAL) {
                var = new Record(level, curNode.getValue(), Record.tFloat, "",
                        Float.parseFloat(curNode.getValue().strval));
            } else if ((curNode.getValue()!=null)&&curNode.getValue().type == TokenType.INTVAL) {
                var = new Record(level, curNode.getValue(), Record.tInt, "",
                        Integer.parseInt(curNode.getValue().strval));
            } else if (curNode.getType() == TreeNodeType.variable) {
                var = variable();
                if (var != null) {
                    if (var.getType() != Record.tbool && var.getType() != Record.tboolArray)
                    curNode = curNode.getParentNode();
                    return var;
                } 
            }else if ((curNode.getValue()!=null)&&curNode.getValue().type == TokenType.SUBSTRACT) {
                curNode = curNode.getParentNode().getChildren().get(1);
                if ((curNode.getValue()!=null)&&curNode.getValue().type == TokenType.LEFT_PARENTHESIS) {
                    curNode = curNode.getParentNode().getChildren().get(1);
                    var =  arith_expression();
                    Record var1 = null;
                    if(var!=null)
                    var1 = (Record)var.clone();
                    var1.substractValue(var1);
                    var1.substractValue(var1);   //减两次相当于取反  
                } else if ((curNode.getValue()!=null)&&curNode.getValue().type == TokenType.FLOATVAL) {
                    var = new Record(level, curNode.getValue(), Record.tFloat, "",
                            -Float.parseFloat(curNode.getValue().strval));
                } else if ((curNode.getValue()!=null)&&curNode.getValue().type == TokenType.INTVAL) {
                    var = new Record(level, curNode.getValue(), Record.tInt, "",
                            -Integer.parseInt(curNode.getValue().strval));
                } else if (curNode.getType() == TreeNodeType.variable) {
                    var = variable();
                    Record var1 = null;
                    if(var!=null)
                    var1 = (Record)var.clone();
                    var1.substractValue(var1);
                    var1.substractValue(var1);   //减两次相当于取反  
                }
            }
            curNode = curNode.getParentNode();
        }
        return var;

    }

    // 算数表达式
    public Record arith_expression() {
        curNode = curNode.getChildren().get(0); // 因子
        Record var = factors();
        if(var!=null){
            var = (Record) var.clone();
        }
        if(curNode.getParentNode().getChildren().size()>1){
        curNode = curNode.getParentNode().getChildren().get(1);// 项
        if (var != null) {
            var = term(var);
        }
    }
        curNode = curNode.getParentNode();
        return var;
    }

    // 因子
    public Record factors() {
        curNode = curNode.getChildren().get(0); // 因式1
        Record var = null;
        if(factor1()!=null){
            var = (Record) factor1().clone();
            }
        if(curNode.getParentNode().getChildren().size()>1){
        curNode = curNode.getParentNode().getChildren().get(1);// 因式递归
        if (var != null) {
            var = factor_recursion(var);
        }
        }
        curNode = curNode.getParentNode();
        return var;

    }

    // 因式1
    public Record factor1() {
        curNode = curNode.getChildren().get(0); // 因式
        Record var = null;
        if (curNode.getType() == TreeNodeType.factor) {
            if(factor()!=null){
            var = (Record) factor().clone();
            }
            if(var!=null){
            if (curNode.getParentNode().getChildren().size() > 1) {
                if (var != null && var.getType() == Record.tInt) {
                    if (curNode.getParentNode().getChildren().get(1).getValue().type == TokenType.ADD_ADD) {
                        int[] tmp = var.getIntArray();
                        tmp[var.getArrayIndex()] = var.getIntArray()[var.getArrayIndex()]+1;
                        var.setIntArray(tmp);
                    }
                    if (curNode.getParentNode().getChildren().get(1).getValue().type == TokenType.SUBSTRACT_SUBSTRACT) {
                        int[] tmp = var.getIntArray();
                        tmp[var.getArrayIndex()] = var.getIntArray()[var.getArrayIndex()]-1;
                        var.setIntArray(tmp);
                    }
                }
                if (var != null && var.getType() == Record.tIntArray) {
                    if (curNode.getParentNode().getChildren().get(1).getValue().type == TokenType.ADD_ADD) {
                        int[] tmp = var.getIntArray();
                        tmp[var.getArrayIndex()] = var.getIntArray()[var.getArrayIndex()]+1;
                        var.setIntArray(tmp);
                    }
                    if (curNode.getParentNode().getChildren().get(1).getValue().type == TokenType.SUBSTRACT_SUBSTRACT) {
                        int[] tmp = var.getIntArray();
                        tmp[var.getArrayIndex()] = var.getIntArray()[var.getArrayIndex()]-1;
                        var.setIntArray(tmp);
                    }
                } else if (var != null) {
                    errorInfo += "只有整数类型才能++或--：line " + curNode.getChildren().get(0).getValue().line + " "
                            + curNode.getChildren().get(0).getValue().strval + "\n";
                }
            }
        }
        }
        if ((curNode.getValue()!=null)&&curNode.getValue().type == TokenType.ADD_ADD) {
            if(factor()!=null){
                var = (Record) factor().clone();
                }
            if (var != null && var.getType() == Record.tInt) {
                var.setintVal(var.getintVal() + 1);
            } else if (var != null && var.getType() == Record.tIntArray) {
                int[] tmp = var.getIntArray();
                tmp[var.getArrayIndex()] = var.getIntArray()[var.getArrayIndex()]+1;
                var.setIntArray(tmp);
            } else if (var != null) {
                errorInfo += "只有整数类型才能++或--：line " + curNode.getChildren().get(0).getValue().line + " "
                        + curNode.getChildren().get(0).getValue().strval + "\n";
            }
        }
        if ((curNode.getValue()!=null)&&curNode.getValue().type == TokenType.SUBSTRACT_SUBSTRACT) {
            if(factor()!=null){
                var = (Record) factor().clone();
                }
            if (var != null && var.getType() == Record.tInt) {
                var.setintVal(var.getintVal() - 1);
            } else if (var != null && var.getType() == Record.tIntArray) {
                int[] tmp = var.getIntArray();
                tmp[var.getArrayIndex()] = var.getIntArray()[var.getArrayIndex()]-1;
                var.setIntArray(tmp);
            } else if (var != null) {
                errorInfo += "只有整数类型才能++或--：line " + curNode.getChildren().get(0).getValue().line + " "
                        + curNode.getChildren().get(0).getValue().strval + "\n";
            }

        }
       
        curNode = curNode.getParentNode();
        return var;
    }

    // 项
    public Record term(Record record) {
        if (curNode.getChildren().size() > 0) {
            curNode = curNode.getChildren().get(0);
            Record var= null;
            if(curNode.getValue()!=null){
            switch (curNode.getValue().type) {
            case TokenType.ADD:
            if(curNode.getParentNode().getChildren().size() > 1){
                if(curNode.getParentNode().getChildren().size()>1){
                curNode = curNode.getParentNode().getChildren().get(1);
                if(factors()!=null)
                var = (Record) factors().clone();
                if ((record.getType() != Record.tbool && record.getType() != Record.tboolArray))
                    record.addValue(var);
                if(curNode.getParentNode().getChildren().size() > 2)
                {
                    curNode = curNode.getParentNode().getChildren().get(2);
                    record = term(record);
                }
            }
                

            }
                curNode = curNode.getParentNode();
                return record;
            case TokenType.SUBSTRACT:
            if(curNode.getParentNode().getChildren().size() > 1){
                curNode = curNode.getParentNode().getChildren().get(1);
                if(factors()!=null)
                var = (Record) factors().clone();

                if ((record.getType() != Record.tbool && record.getType() != Record.tboolArray))
                    record.substractValue(var);

                if(curNode.getParentNode().getChildren().size() > 2)
                {
                    curNode = curNode.getParentNode().getChildren().get(2);
               
                    record = term(record);
                }

            }
              
               
                curNode = curNode.getParentNode();
                return record;
            default:
                curNode = curNode.getParentNode();
                return null;
            }
        }
        curNode = curNode.getParentNode();
        return null;
        } else {
            return null;
        }
    }

    // 因式递归
    public Record factor_recursion(Record record) {
        if (curNode.getChildren().size() > 0) {
            curNode = curNode.getChildren().get(0);
            Record var = null;
            if((curNode.getValue()!=null)){
            switch (curNode.getValue().type) {
            case TokenType.MULTIPLY:
            if(curNode.getParentNode().getChildren().size() > 1){
                curNode = curNode.getParentNode().getChildren().get(1);
                
                if(factor1()!=null){
                    var = (Record) factor1().clone();
                 }

                 if ((record.getType() != Record.tbool && record.getType() != Record.tboolArray))
                    record.mutiplyValue(var);
                }
                 if(curNode.getParentNode().getChildren().size()>2){
                 curNode = curNode.getParentNode().getChildren().get(2);
                 record = factor_recursion(record);
                 }
                
                curNode = curNode.getParentNode();
                return record;
            case TokenType.DIVIDE:
            if(curNode.getParentNode().getChildren().size() > 1){
                curNode = curNode.getParentNode().getChildren().get(1);
                if(factor1()!=null){
                    var = (Record) factor1().clone();
                }
                if (var.getValue() != 0) {
                    if ((record.getType() != Record.tbool && record.getType() != Record.tboolArray))
                        record.divideValue(var);
                } else {
                    errorInfo += "除数不能为0：line " + curNode.getChildren().get(0).getValue().line + " "
                            + curNode.getChildren().get(0).getValue().strval + "\n";
                            curNode = curNode.getParentNode();
                            return null;
                }
                if(curNode.getParentNode().getChildren().size()>2){
                curNode = curNode.getParentNode().getChildren().get(2);
                 record = factor_recursion(record);
                 }
            }
            
            curNode = curNode.getParentNode();
            return record;
               
            
            default:
                curNode = curNode.getParentNode();
                return null;
            }
           
            }
            curNode = curNode.getParentNode();
            return null;
        } else {
            return null;
        }
    }

    // 参数声明
    public void para_stmt() {
        if(curNode.getChildren().size()>0){
        curNode = curNode.getChildren().get(0);
        stmt();
        if(curNode.getParentNode().getChildren().size()>1){
        curNode = curNode.getParentNode().getChildren().get(1);
        stmt_closure();
        }
        curNode = curNode.getParentNode();
        }
    }

    // 声明
    public void stmt() {
        curNode = curNode.getChildren().get(0);// 类型
        int type = type();
        curNode = curNode.getParentNode().getChildren().get(1);// 变量
        Record var = variable();
        if (var == null) {// 如果该变量未声明过
            String name = curNode.getChildren().get(0).getValue().strval;
            if (curNode.getChildren().size() == 1) {// variable: identifier
                var = new Record(level, curNode.getValue(), type, name, 0);
            } else {// 数组
                if(curNode.getChildren().get(1).getChildren().size()>2){
                    curNode = curNode.getChildren().get(1).getChildren().get(1);
                    var = factor();
                    int arrayNum = 0;
                    curNode = curNode.getParentNode().getParentNode();
                    if (var != null && var.getType() == Record.tInt) {
                        arrayNum = var.getintVal();
                    } 
                    if (var != null && var.getType() == Record.tIntArray) {
                        arrayNum = var.getIntArray()[var.getArrayIndex()];
                    } 
                            var = new Record(level, curNode.getValue(), type + 3, name, new int[arrayNum]);
                
                } else {
                    errorInfo += "数组未指明大小或大小为浮点数：line " + curNode.getChildren().get(0).getValue().line + " "
                            + curNode.getChildren().get(0).getValue().strval + "\n";

                }
            }
            symbalTable.getTable().add(var);
        } else {
            errorInfo += "重复声明变量：line " + curNode.getChildren().get(0).getValue().line + " "
                    + curNode.getChildren().get(0).getValue().strval + "\n";

        }
        if(curNode.getParentNode().getChildren().size()>2){
        curNode = curNode.getParentNode().getChildren().get(2);// 赋初值
        Record initval = initialvalue(var);
        if (initval != null) {
           
                var.setValue(initval);
            }
        }
    
        curNode = curNode.getParentNode();

    }

    // 赋初值
    public Record initialvalue(Record record) {
        if (curNode.getChildren().size() > 0) {
            curNode = curNode.getChildren().get(1);
            Record var = rightVal(record);
            curNode = curNode.getParentNode();
            return var;
        } else {
            return null;
        }

    }

    // 右值
    public Record rightVal(Record record) {
        curNode = curNode.getChildren().get(0);
        Record var = null;
        if (curNode.getType() == TreeNodeType.arith_expression)
            var = arith_expression();

        if (curNode.getType() == TreeNodeType.logical_expression)
            var = new Record(level, null, Record.tbool, "logical",  logical_expression());

        if (curNode.getType() == TreeNodeType.TERMINAL_SYMBOL)
            var = manyConstant(record);

        curNode = curNode.getParentNode();
        return var;
    }

    // 多个数据
    public Record manyConstant(Record record) {
        curNode = curNode.getChildren().get(0);
        record.setArrayIndex(0);
        if ((curNode.getValue()!=null)&&curNode.getValue().type == TokenType.FLOATVAL && record.getType() == Record.tFloatArray) {
            record.setValue(new Record(level, curNode.getValue(), Record.tFloat, "float_val",
                    Float.parseFloat(curNode.getValue().strval)));
        } else if (curToken.type == TokenType.INTVAL
                && (record.getType() == Record.tFloatArray || record.getType() == Record.tIntArray)) {
            record.setValue(new Record(level, curNode.getValue(), Record.tFloatArray, "int_val",
                    Integer.parseInt(curNode.getValue().strval)));
        } else if ((curToken.type == TokenType.TRUE) && record.getType() == Record.tboolArray) {
            record.setValue(new Record(level, curNode.getValue(), Record.tFloatArray, "true_val", true));
        } else if ((curToken.type == TokenType.FALSE) && record.getType() == Record.tboolArray) {
            record.setValue(new Record(level, curNode.getValue(), Record.tFloatArray, "false_val", false));
        } else {
            errorInfo += "数组初始化赋值类型出错：line " + record.getToken().line + " " + record.getToken().strval + "\n";
        }
        constant_closure(record);

        curNode = curNode.getParentNode();
        return record;
    }

    // 常数闭包
    public void constant_closure(Record record) {
        if (curNode.getChildren().size() > 0) {
            curNode = curNode.getChildren().get(1);
            record.setArrayIndex(record.getArrayIndex() + 1);
            if (record.getArrayIndex() > record.getArrayNum()) {
                errorInfo += "数组初始化越界：line " + record.getToken().line + " " + record.getToken().strval + "\n";
                return;
            }
            if ((curNode.getValue()!=null)&&curNode.getValue().type == TokenType.FLOATVAL && record.getType() == Record.tFloatArray) {
                record.setValue(new Record(level, curNode.getValue(), Record.tFloat, "float_val",
                        Float.parseFloat(curNode.getValue().strval)));
            } else if (curToken.type == TokenType.INTVAL
                    && (record.getType() == Record.tFloatArray || record.getType() == Record.tIntArray)) {
                record.setValue(new Record(level, curNode.getValue(), Record.tFloatArray, "int_val",
                        Integer.parseInt(curNode.getValue().strval)));
            } else if ((curToken.type == TokenType.TRUE) && record.getType() == Record.tboolArray) {
                record.setValue(new Record(level, curNode.getValue(), Record.tFloatArray, "true_val", true));
            } else if ((curToken.type == TokenType.FALSE) && record.getType() == Record.tboolArray) {
                record.setValue(new Record(level, curNode.getValue(), Record.tFloatArray, "false_val", false));
            } else {
                errorInfo += "数组初始化赋值类型出错：line " + record.getToken().line + " " + record.getToken().strval + "\n";
            }
            curNode = curNode.getParentNode().getChildren().get(2);
            constant_closure(record);
            curNode = curNode.getParentNode();
        }
    }

    // 声明闭包
    public void stmt_closure() {
        if (curNode.getChildren().size() > 0) {
            curNode = curNode.getChildren().get(1);
            stmt();
            if(curNode.getParentNode().getChildren().size()>2){
            curNode = curNode.getParentNode().getChildren().get(2);
            stmt_closure();
            }
            curNode = curNode.getParentNode();
        }

    }

    // 函数定义或声明闭包
    public void declar_stmt_closure() {
        curNode = curNode.getChildren().get(0);
        co_function();
        curNode = curNode.getParentNode();
    }

    // 复合函数块
    public boolean co_function() {
        level++;
        curNode = curNode.getChildren().get(1);
        boolean notbreak = functionBlock();
        curNode = curNode.getParentNode();
        symbalTable.s(level);
        level--;
        return notbreak;
    }

    // 函数块
    public boolean functionBlock() {
        boolean notbreak = true;
        if(curNode.getChildren().size()>1){
        curNode = curNode.getChildren().get(0);
        stmt_expression_closure();
        curNode = curNode.getParentNode().getChildren().get(1);
        notbreak = functionBlock_closure();
        curNode = curNode.getParentNode();
    }else if(curNode.getChildren().size() == 1){
        if(curNode.getChildren().get(0).getType() == TreeNodeType.stmt_expression_closure)
        {
            curNode = curNode.getChildren().get(0);
            stmt_expression_closure();
            curNode = curNode.getParentNode();
        }else if(curNode.getChildren().get(0).getType() == TreeNodeType.functionBlock_closure){
            curNode = curNode.getChildren().get(0);
            notbreak =functionBlock_closure();
            curNode = curNode.getParentNode();
        }
    }
        return notbreak;
    }

    // 函数块闭包
    public boolean functionBlock_closure() {
        boolean notbreak = true;
        if (curNode.getChildren().size() > 0) {
            curNode = curNode.getChildren().get(0);

            if ((curNode.getType() == TreeNodeType.assignment)) {
                assignment();
                if(curNode.getParentNode().getChildren().size()>1){
                curNode = curNode.getParentNode().getChildren().get(1);
                notbreak =functionBlock();
                }
            } else if ((curNode.getType() == TreeNodeType.forloop)) {
                forloop();
                if(curNode.getParentNode().getChildren().size()>1){
                curNode = curNode.getParentNode().getChildren().get(1);
                notbreak =functionBlock();
                }
            } else if (curNode.getType() == TreeNodeType.whileloop) {
                whileloop();
                if(curNode.getParentNode().getChildren().size()>1){
                curNode = curNode.getParentNode().getChildren().get(1);
                notbreak =functionBlock();
                }
            } else if (curNode.getType() == TreeNodeType.if_statement) {
                notbreak =if_statement();
                if(notbreak && notcontinue){
                    if(curNode.getParentNode().getChildren().size()>1){
                    curNode = curNode.getParentNode().getChildren().get(1);
                    notbreak =functionBlock();
                }
            }
                notcontinue = true;
            } else if (curNode.getType() == TreeNodeType.return_statement) {
                return_statement();
                if(curNode.getParentNode().getChildren().size()>1){
                curNode = curNode.getParentNode().getChildren().get(1);
                notbreak =functionBlock();
                }
            } else if (curNode.getType() == TreeNodeType.scan_functon) {
                scan_function();
                if(curNode.getParentNode().getChildren().size()>1){
                curNode = curNode.getParentNode().getChildren().get(1);
                notbreak =functionBlock();
                }
            } else if (curNode.getType() == TreeNodeType.scanf_function) {
                scanf_function();
                if(curNode.getParentNode().getChildren().size()>1){
                curNode = curNode.getParentNode().getChildren().get(1);
                notbreak =functionBlock();
                }
            } else if (curNode.getType() == TreeNodeType.print_function) {
                print_function();
                if(curNode.getParentNode().getChildren().size()>1){
                curNode = curNode.getParentNode().getChildren().get(1);
                notbreak =functionBlock();
                }
            } else if (curNode.getType() == TreeNodeType.printf_function) {
                printf_function();
                if(curNode.getParentNode().getChildren().size()>1){
                curNode = curNode.getParentNode().getChildren().get(1);
                notbreak =functionBlock();
                }
            }else if (curNode.getType() == TreeNodeType.break_statment) {
                if(loop>0){
                    notbreak = false;
                }
            }else if (curNode.getType() == TreeNodeType.continue_statment) {
                if(loop>0){
                    notcontinue = false;
                }
            }
            curNode = curNode.getParentNode();
        }
        return notbreak;
}

    // 一条语句
    public boolean astament() {
        boolean notbreak = true;
        if (curNode.getChildren().size() > 0) {
            curNode = curNode.getChildren().get(0);
            level ++;
            if ((curNode.getType() == TreeNodeType.assignment)) {
                assignment();
            } else if ((curNode.getType() == TreeNodeType.forloop)) {
                forloop();
            } else if (curNode.getType() == TreeNodeType.whileloop) {
                whileloop();
            } else if (curNode.getType() == TreeNodeType.if_statement) {
                notbreak = if_statement();
            } else if (curNode.getType() == TreeNodeType.return_statement) {
                return_statement();
            } else if (curNode.getType() == TreeNodeType.scan_functon) {
                scan_function();
            } else if (curNode.getType() == TreeNodeType.scanf_function) {
                scanf_function();
            } else if (curNode.getType() == TreeNodeType.print_function) {
                print_function();
            } else if (curNode.getType() == TreeNodeType.printf_function) {
                printf_function();
            }else if (curNode.getType() == TreeNodeType.break_statment) {
                if(loop>0){
                notbreak = false;
                }
            }else if (curNode.getType() == TreeNodeType.continue_statment) {
                if(loop>0){
                    notbreak = true;
                 }
            }
            symbalTable.s(level);
            level--;
           
            curNode = curNode.getParentNode();
        }
        
        return notbreak;
    }

    public void whereisloop(){
        curNode = curNode.getParentNode();
        while((curNode.getType() == TreeNodeType.forloop || curNode.getType() == TreeNodeType.whileloop)&&(curNode != headNode)){
            curNode = curNode.getParentNode();
        }
    }


    
    // print函数
    public void print_function() {
        curNode = curNode.getChildren().get(2);
        if(curNode.getType() == TreeNodeType.para){
        curNode = curNode.getChildren().get(0);
        if (curNode.getValue()!= null &&((curNode.getValue().type == TokenType.FLOATVAL) || (curNode.getValue().type == TokenType.INTVAL)))
            System.out.println(curNode.getValue().strval);
        if (curNode.getType() == TreeNodeType.variable) {
            Record var = variable();
            printRecord(var);
        }
        curNode = curNode.getParentNode();
    }
        curNode = curNode.getParentNode();

    }

    // printf函数
    public void printf_function() {
        curNode = curNode.getChildren().get(2);
        if(curNode.getType() == TreeNodeType.para){
        TreeNode tnode = curNode.getChildren().get(0);
        if (tnode.getValue()!= null &&((tnode.getValue().type == TokenType.FLOATVAL) || (tnode.getValue().type == TokenType.INTVAL)))
            System.out.println(tnode.getValue().strval);
        if (tnode.getType() == TreeNodeType.variable) {
            Record var = symbalTable.getRecordByName(tnode.getChildren().get(0).getValue().strval);
            printRecord(var);
        }
    }
        curNode = curNode.getParentNode();
    }

    // scan函数
    public void scan_function() {
        curNode = curNode.getChildren().get(2);
        curNode = curNode.getChildren().get(0);
        if (curNode.getType() == TreeNodeType.variable) {
                Scanner sc = new Scanner(System.in); 
                String m = sc.nextLine();
                Record var = variable();
                curNode = curNode.getParentNode();
                if (var.getType() == Record.tInt )
                    var.setintVal(Integer.parseInt(m));
                else if (var.getType() == Record.tIntArray )
                    var.getIntArray()[var.getArrayIndex()] = Integer.parseInt(m);
                else if (var.getType() == Record.tFloat)
                    var.setfloatVal(Float.parseFloat(m));
                else if (var.getType() == Record.tFloatArray)
                    var.getfloatArray()[var.getArrayIndex()] = Float.parseFloat(m);
                else errorInfo += "scan函数无法使用该类型参数：line " + var.getToken().line + " " + var.getToken().strval + "\n";

        }
        curNode = curNode.getParentNode();
    }

    // scanf函数
    public void scanf_function()  {
        curNode = curNode.getChildren().get(2);
        curNode = curNode.getChildren().get(0).getChildren().get(0);
        if (curNode.getType() == TreeNodeType.variable) {
                Scanner sc = new Scanner(System.in); 
                String m = sc.nextLine();
                Record var = variable();
                curNode = curNode.getParentNode();
                if (var.getType() == Record.tInt )
                    var.setintVal(Integer.parseInt(m));
                else if (var.getType() == Record.tIntArray )
                    var.getIntArray()[var.getArrayIndex()] = Integer.parseInt(m);
                else if (var.getType() == Record.tFloat)
                    var.setfloatVal(Float.parseFloat(m));
                else if (var.getType() == Record.tFloatArray)
                    var.getfloatArray()[var.getArrayIndex()] = Float.parseFloat(m);
                else errorInfo += "scan函数无法使用该类型参数：line " + var.getToken().line + " " + var.getToken().strval + "\n";

        }
        curNode = curNode.getParentNode().getParentNode();
    }


    // for循环
    public void forloop() {
        loop++;
        curNode = curNode.getChildren().get(1);
        forblock();
        curNode = curNode.getParentNode();
        loop--;
    }

    // for循环块
    public void forblock() {
        curNode = curNode.getChildren().get(1);
        assignment();
        curNode = curNode.getParentNode().getChildren().get(2);
        boolean var = logical_expression();
        boolean notbreak = true;
        while (var&&notbreak) {
            curNode = curNode.getParentNode().getChildren().get(4);
            factor1();
            curNode = curNode.getParentNode().getChildren().get(6);
            if(curNode.getType() == TreeNodeType.co_function){
                 notbreak = co_function();
                }else if(curNode.getType() == TreeNodeType.astament){
                notbreak = astament();
                }else{
                    errorInfo += "cssssss";
                }
            curNode = curNode.getParentNode().getChildren().get(2);
            var = logical_expression();
        }
        curNode = curNode.getParentNode();
    }

    // while循环块
    public void whileloop() {
        loop++;
        curNode = curNode.getChildren().get(1);
        whileblock();
        curNode = curNode.getParentNode();
        loop--;
    }

    // while循环块
    public void whileblock() {
        curNode = curNode.getChildren().get(1);
        boolean var = logical_expression();
        boolean notbreak = true;
        while (var && notbreak) {
            curNode = curNode.getParentNode().getChildren().get(3);
            if(curNode.getType() == TreeNodeType.co_function){
                notbreak = co_function();
                }else if(curNode.getType() == TreeNodeType.astament){
                notbreak = astament();
                }else{
                    errorInfo += "cssssss";
                }
            curNode = curNode.getParentNode().getChildren().get(1);
            var = logical_expression();
        }
        curNode = curNode.getParentNode();
    }

    // 条件语句
    public boolean if_statement() {
        boolean notbreak = true;
        curNode = curNode.getChildren().get(1);
        notbreak = ifblock();
        curNode = curNode.getParentNode();
        return notbreak;
    }

    // 条件语句块
    public boolean ifblock() {
        curNode = curNode.getChildren().get(1);
        boolean var = logical_expression();
        boolean notbreak = true;
        if(notbreak){
        if (var ) {
            curNode = curNode.getParentNode().getChildren().get(3);
            if(curNode.getType() == TreeNodeType.co_function){
            notbreak = co_function();
            }else if(curNode.getType() == TreeNodeType.astament){
            notbreak = astament();
            }else{
                errorInfo += "cssssss";
            }
        } else {
            if(curNode.getParentNode().getChildren().size()>4){
            curNode = curNode.getParentNode().getChildren().get(4);
            notbreak = else_statement();
            }
        }
    }
        curNode = curNode.getParentNode();
        return notbreak;
    }

    // 否则语句
    public boolean else_statement() {
        boolean notbreak = true;
        if (curNode.getChildren().size() > 0) {
            curNode = curNode.getChildren().get(1);
            if(curNode.getType() == TreeNodeType.co_function){
                notbreak = co_function();
                }else if(curNode.getType() == TreeNodeType.astament){
                notbreak = astament();
                }else{
                    errorInfo += "cssssss";
                }
            curNode = curNode.getParentNode();
        }
        return notbreak;
    }

    // 返回语句
    public void return_statement() {
        curNode = curNode.getChildren().get(1);
        factor();
        curNode = curNode.getParentNode();
    }

    // 赋值语句
    public void assignment() {
        if (curNode.getChildren().get(0).getType() == TreeNodeType.type) // 先声明再赋值
        {
            curNode = curNode.getChildren().get(0);// 类型
            int type = type();
            curNode = curNode.getParentNode().getChildren().get(1);// 变量
            Record var = variable();
            if (var == null) {// 如果该变量未声明过
                String name = curNode.getChildren().get(0).getValue().strval;
                if (curNode.getChildren().size() == 1) {// variable: identifier
                    var = new Record(level, curNode.getValue(), type, name, 0);
                } else {// 数组
                    if(curNode.getChildren().get(1).getChildren().size()>2){
                        curNode = curNode.getChildren().get(1).getChildren().get(1);
                        var = factor();
                        int arrayNum = 0;
                        curNode = curNode.getParentNode().getParentNode();
                        if (var != null && var.getType() == Record.tInt) {
                            arrayNum = var.getintVal();
                        } 
                        if (var != null && var.getType() == Record.tIntArray) {
                            arrayNum = var.getIntArray()[var.getArrayIndex()];
                        } 
                                var = new Record(level, curNode.getValue(), type + 3, name, new int[arrayNum]);
                    
                    }  else {
                        errorInfo += "数组未指明大小或大小为浮点数：line " + curNode.getChildren().get(0).getValue().line + " "
                                + curNode.getChildren().get(0).getValue().strval + "\n";

                    }
                }
                symbalTable.getTable().add(var);
            } else {
                errorInfo += "重复声明变量：line " + curNode.getChildren().get(0).getValue().line + " "
                        + curNode.getChildren().get(0).getValue().strval + "\n";

            }
            curNode = curNode.getParentNode().getChildren().get(3);// 赋初值
            Record initval = initialvalue(var);
            if (initval != null) {
                if ((initval.getType() == Record.tFloat || initval.getType() == Record.tFloatArray)
                        && (var.getType() == Record.tInt || var.getType() == Record.tIntArray)) {
                    errorInfo += "无法从float类型转为int类型：line " + curNode.getChildren().get(0).getValue().line + " "
                            + curNode.getChildren().get(0).getValue().strval + "\n";
                } else if ((var.getType() == Record.tbool || var.getType() == Record.tboolArray)
                        && (initval.getType() != Record.tbool || initval.getType() != Record.tboolArray)) {
                    errorInfo += "无法从其他类型转为bool类型：line " + curNode.getChildren().get(0).getValue().line + " "
                            + curNode.getChildren().get(0).getValue().strval + "\n";
                } else if ((var.getType() == Record.tIntArray || var.getType() == Record.tboolArray
                        || var.getType() == Record.tFloatArray)
                        && (initval.getType() == Record.tInt || initval.getType() != Record.tbool
                                || initval.getType() == Record.tFloat)) {
                    errorInfo += "无法给数组赋值：line " + curNode.getChildren().get(0).getValue().line + " "
                            + curNode.getChildren().get(0).getValue().strval + "\n";
                } else if ((initval.getType() == Record.tIntArray || initval.getType() == Record.tboolArray
                        || initval.getType() == Record.tFloatArray)
                        && (var.getType() == Record.tInt || var.getType() != Record.tbool
                                || var.getType() == Record.tFloat)) {
                    errorInfo += "数组无法给其他类型赋值：line " + curNode.getChildren().get(0).getValue().line + " "
                            + curNode.getChildren().get(0).getValue().strval + "\n";
                } else {
                    var.setValue(initval);
                }
            }
            curNode = curNode.getParentNode();
        } else { // 直接赋值
            curNode = curNode.getChildren().get(0);// 变量
            Record var1 = variable();
            if (var1 == null) {
                errorInfo += "未声明该变量：line " + curNode.getChildren().get(0).getValue().line + " "
                        + curNode.getChildren().get(0).getValue().strval + "\n";

            } else {
                curNode = curNode.getParentNode().getChildren().get(1);// 赋值
                assign(var1);
            }
            curNode = curNode.getParentNode();
        }
    }

    // 逻辑表达式
    public boolean logical_expression() {
       curNode = curNode.getChildren().get(0);
        if (curNode.getParentNode().getChildren().size() > 1) {
            boolean leftpart = logical_factor1();
            if(leftpart == true){
                curNode = curNode.getParentNode();
                return true;
            }
            boolean rightpart = false;
            if(curNode.getParentNode().getChildren().size()>2){
            curNode = curNode.getParentNode().getChildren().get(2);
            rightpart = logical_expression();
            }
            curNode = curNode.getParentNode();
            return leftpart || rightpart;
        } else {
            boolean result =  logical_factor1();
            curNode = curNode.getParentNode();
            return result;
        }
    }

    // 逻辑因式1
    public boolean logical_factor1() {
        if(curNode.getChildren().size()>0){
        curNode = curNode.getChildren().get(0);
        if (curNode.getParentNode().getChildren().size() > 1) {
            boolean leftpart = logical_factor2();
            if(leftpart == false){
                curNode = curNode.getParentNode();
                return false;
            }
            boolean rightpart = false;
            if(curNode.getParentNode().getChildren().size()>2){
            curNode = curNode.getParentNode().getChildren().get(2);
            rightpart =  logical_factor1();
            }
            curNode = curNode.getParentNode();
            return leftpart && rightpart;
        } else {
            boolean res = logical_factor2();
            curNode = curNode.getParentNode();
            return res;
        }
    }
    return false;
    }

    // 逻辑因式2
    public boolean logical_factor2() {
        if (curNode.getChildren().size() == 1) {
            if(curNode.getChildren().get(0).getValue()!=null){
            if (curNode.getChildren().get(0).getValue().type == TokenType.TRUE)
                return true;
            if (curNode.getChildren().get(0).getValue().type == TokenType.FALSE)
                return false;
            }
        } else if (curNode.getChildren().get(0).getType() == TreeNodeType.arith_expression) {
            curNode = curNode.getChildren().get(0);
            Record var1 = arith_expression();
            curNode = curNode.getParentNode().getChildren().get(2);
            Record var2 = arith_expression();
            curNode = curNode.getParentNode().getChildren().get(1);
            boolean res = false;
            try{
            if((curNode.getValue()!=null)){
            switch (curNode.getValue().type) {
            case TokenType.MAIOR:
                res = var1.getValue() > var2.getValue();
                break;
            case TokenType.MENOR:
                res = var1.getValue() < var2.getValue();
                break;
            case TokenType.MAEQ:
                res = var1.getValue() >= var2.getValue();
                break;
            case TokenType.MEEQ:
                res = var1.getValue() <= var2.getValue();
                break;
            case TokenType.UNEQ:
                res = var1.getValue() != var2.getValue();
                break;
            case TokenType.EQEQ:
                res = var1.getValue() == var2.getValue();
                break;
            default:break;
            }
        }}catch(Exception e){
        }
            curNode = curNode.getParentNode();
            return res;
        } else {
            curNode = curNode.getChildren().get(1);
            boolean res = logical_expression();
            curNode = curNode.getParentNode();
            return res;
        }
        return false;
    }

    // 赋值
    public Record assign(Record record) {
        curNode = curNode.getChildren().get(0);
        Record var = null;
        if((curNode.getValue()!=null)){
        switch(curNode.getValue().type){
            case TokenType.EQUAL:
                curNode = curNode.getParentNode().getChildren().get(1);
                var = rightVal(record);
                curNode = curNode.getParentNode().getParentNode().getChildren().get(0);
                record = variable();
                curNode = curNode.getParentNode().getChildren().get(1);
                if(var!=null){
                if(!record.setValue(var)) 
                errorInfo += "赋值类型不兼容：line " + record.getToken().line + " "
                + record.getToken().strval + "\n";
                }
                break;
            case TokenType.ADD_EQUAL:
                 curNode = curNode.getParentNode().getChildren().get(1);
                var = rightVal(record);
                curNode = curNode.getParentNode().getParentNode().getChildren().get(0);
                record = variable();
                curNode = curNode.getParentNode().getChildren().get(1);
                if(!record.addValue(var)) 
                errorInfo += "赋值类型不兼容：line " + record.getToken().line + " "
                + record.getToken().strval + "\n";
                break;
            case TokenType.SUBSTRACT_EQUAL:
                curNode = curNode.getParentNode().getChildren().get(1);
                var = rightVal(record);
                curNode = curNode.getParentNode().getParentNode().getChildren().get(0);
                record = variable();
                curNode = curNode.getParentNode().getChildren().get(1);
                if(!record.substractValue(var)) 
                errorInfo += "赋值类型不兼容：line " + record.getToken().line + " "
                + record.getToken().strval + "\n";
                break;
            case TokenType.MULTIPLY_EQUAL:
                curNode = curNode.getParentNode().getChildren().get(1);
                var = rightVal(record);
                curNode = curNode.getParentNode().getParentNode().getChildren().get(0);
                record = variable();
                curNode = curNode.getParentNode().getChildren().get(1);
                if(!record.mutiplyValue(var)) 
                errorInfo += "赋值类型不兼容：line " + record.getToken().line + " "
                + record.getToken().strval + "\n";
                break;
            case TokenType.ADD_ADD:
                if(record.getType() == Record.tInt ){
                record.setintVal(record.getintVal() + 1);
                }
                else if(record.getType() == Record.tIntArray ){
                    int[] tmp = record.getIntArray();
                    tmp[record.getArrayIndex()] = record.getIntArray()[record.getArrayIndex()]+1;
                    record.setIntArray(tmp);
                }else{
                    errorInfo += "只有整数类型才能++或--：line " + record.getToken().line + " "
                    + record.getToken().strval + "\n";
                }
                curNode = curNode.getParentNode();
                break;
            case TokenType.SUBSTRACT_SUBSTRACT:
                if(record.getType() == Record.tInt ){
                record.setintVal(record.getintVal() - 1);
                }
                else if(record.getType() == Record.tIntArray ){
                    int[] tmp = record.getIntArray();
                    tmp[record.getArrayIndex()] = record.getIntArray()[record.getArrayIndex()]-1;
                    record.setIntArray(tmp);
                }else{
                    errorInfo += "只有整数类型才能++或--：line " + record.getToken().line + " "
                    + record.getToken().strval + "\n";
                }
                curNode = curNode.getParentNode();
                break;
            default:break;
                
        }
    }
        return record;
       
    }
    
        //声明语句闭包
        public void stmt_expression_closure(){
                if(curNode.getChildren().size()>0 ){
                curNode = curNode.getChildren().get(0);
                stmt_expression();
                if(curNode.getParentNode().getChildren().size()>1){
                curNode = curNode.getParentNode().getChildren().get(1);
                stmt_expression_closure();
                }
                curNode = curNode.getParentNode();
            }
        }
    
        //声明语句
        public void stmt_expression(){
            curNode = curNode.getChildren().get(0);
            para_stmt();
            curNode = curNode.getParentNode();
           
        }
}