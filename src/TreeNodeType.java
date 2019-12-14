public final class TreeNodeType{
    public static int PROGRAM = 0;  //程序
    public static int TERMINAL_SYMBOL = 1;  //终结符
    public static int declar_stmt = 2;  //函数定义或声明
    public static int type = 3;      //类型

    public static int variable = 4;  //变量
    public static int arrayIndex = 5;  //数组下标
    public static int factor = 6;  //因式
    public static int factor1 = 36;  //因式1
    public static int arith_expression = 7;  //算数表达式
    public static int factors = 8;  //因子
    public static int term = 9;   //项
    public static int factor_recursion = 10; //因式递归
    public static int para_stmt = 11;    //参数声明
    public static int initialvalue = 12;     //赋初值
    public static int rightVal = 13;    //右值
    public static int manyConstant = 14;   //多个数据

    public static int constant_closure = 15;  //常数闭包
    public static int stmt_closure = 16;  //声明闭包
    public static int declar_stmt_closure = 17;   //函数定义或声明闭包
    public static int co_function = 18;//复合函数块
    public static int functionBlock = 19;   //函数块

    public static int functionBlock_closure = 20;//函数块闭包
    public static int forloop = 21; //for循环
    public static int forblock = 22;  //for循环块

    public static int whileloop = 23; //while循环块
    public static int whileblock = 24; //while循环块
    public static int if_statement = 25; //条件语句
    public static int ifblock = 26; //条件语句块
    public static int else_statement = 27; //否则语句
    public static int return_statement = 28; //返回语句
    public static int logical_expression = 29; //逻辑表达式

    public static int assignment = 31; //赋值语句
    public static int assign = 32; //赋值
    public static int stmt_expression_closure = 33; //声明语句闭包
    public static int stmt_expression = 34;  //声明语句
    public static int stmt = 35;  //声明语句

    public static int logical_factor1 = 36;  //逻辑因式1
    public static int logical_factor2 = 37;  //逻辑因式2

    public static int print_function = 38;  //print函数
    public static int printf_function = 39;  //printf函数
    public static int scan_functon = 40;  //scan函数
    public static int scanf_function = 41;  //scanf函数

    public static int para_list = 42;  //参数列表
    public static int para = 43;  //参数列表
    public static int para_closure = 44;  //参数列表
    public static int astament = 45; //一条语句

    public static int continue_statment = 46;  //continue语句
    public static int break_statment = 47; //break语句
}