public class Record {
    private int level;//变量的层次

    private Token token;//对应的标识符（不一定有）
    private String name;
    private int type;//数据类型
    private int intVal;//int类型数据的值
    private boolean boolVal;//boolean类型数据的值
    private float floatVal;//float类型数据的值

    private int arrayNum;//数组类型数据的大小
    private Integer arrayIndex;//数组下标,未赋值时为空
    private int[] intArray;//int类型数组
    private boolean[] boolArray;//boolean类型数据的值
    private float[] floatArray;//float类型数组

    public Record(int level, Token token, int type, String name, int intVal){
        this.level = level;
        this.token = token;
        this.type = type;
        this.name = name;
        this.intVal = intVal;
    }

    public Record(int level, Token token, int type, String name, boolean boolVal){
        this.level = level;
        this.token = token;
        this.type = type;
        this.name = name;
        this.boolVal = boolVal;
    }

    public Record(int level, Token token, int type, String name, float realValue){
        this.level = level;
        this.token = token;
        this.type = type;
        this.name = name;
        this.floatVal = realValue;
    }

    public Record(int level, Token token, int type, String name, int[] intArray){
        this.level = level;
        this.token = token;
        this.type = type;
        this.name = name;
        this.intArray = intArray;
        this.arrayNum = intArray.length;
    }

    public Record(int level, Token token, int type, String name, boolean[] boolArray){
        this.level = level;
        this.token = token;
        this.type = type;
        this.name = name;
        this.boolArray = boolArray;
        this.arrayNum = intArray.length;
    }

    public Record(int level, Token token, int type, String name, float[] floatArray){
        this.level = level;
        this.token = token;
        this.type = type;
        this.name = name;
        this.floatArray = floatArray;
        this.arrayNum = floatArray.length;
    }

    public float getValue(){
        if(this.getType() == Record.tInt){
            return this.intVal;
        }
        if (this.getType() == Record.tFloat){
            return this.floatVal;
        }
        if (this.getType() == Record.tIntArray){
            return intArray[this.getArrayIndex()];
        }
        if (this.getType() == Record.tFloatArray){
            return this.floatArray[this.arrayIndex];
        }
        return 0;
    }

    public boolean setValue(Record value){
        if(value.getType() == this.getType())
        {
        if(this.getType() == Record.tInt){
            this.setintVal(value.getintVal());
        }
        if(this.getType() == Record.tbool){
            this.setboolVal(value.getboolVal());
        }
        if (this.getType() == Record.tFloat){
             this.setfloatVal(value.getfloatVal());
        }
        if (this.getType() == Record.tIntArray){
            this.getIntArray()[getArrayIndex()] = value.getIntArray()[value.getArrayIndex()];
        }
        if (this.getType() == Record.tboolArray){
            this.getboolArray()[getArrayIndex()] = value.getboolArray()[value.getArrayIndex()];
        }
        if (this.getType() == Record.tFloatArray){
             this.getfloatArray()[getArrayIndex()] = value.getfloatArray()[value.getArrayIndex()];
        }
        return true;
    }
    if( (value.getType() == Record.tInt && this.getType() == Record.tFloat)||( value.getType() == Record.tIntArray && this.getType() == Record.tFloat)||( value.getType() == Record.tFloatArray && this.getType() == Record.tFloat)){
        this.setfloatVal(value.getValue());
        return true;
    }
    if( value.getType() == Record.tIntArray && this.getType() == Record.tInt){
        this.setintVal(value.getIntArray()[value.getArrayIndex()]);
        return true;
    }
    if( value.getType() == Record.tboolArray && this.getType() == Record.tbool){
        this.setboolVal(value.getboolArray()[value.getArrayIndex()]);
        return true;
    }
    return false;
    }

    public boolean addValue(Record value){
        if(value.getType() == this.getType())
        {
        if(this.getType() == Record.tInt){
            this.setintVal(this.getintVal()+value.getintVal());
        }
        if (this.getType() == Record.tFloat){
             this.setfloatVal(this.getfloatVal()+value.getfloatVal());
        }
        if (this.getType() == Record.tIntArray){
            this.getIntArray()[getArrayIndex()] += value.getIntArray()[value.getArrayIndex()];
        }
        if (this.getType() == Record.tFloatArray){
             this.getfloatArray()[getArrayIndex()] += value.getfloatArray()[value.getArrayIndex()];
        }
        return true;
    }
    if((value.getType() == Record.tInt && this.getType() == Record.tFloat) || ( value.getType() == Record.tFloat && this.getType() == Record.tInt)||( value.getType() == Record.tIntArray && this.getType() == Record.tFloat)||( value.getType() == Record.tFloat && this.getType() == Record.tIntArray)||( value.getType() == Record.tFloatArray && this.getType() == Record.tFloat)){
        this.setType(Record.tFloat);
        this.setfloatVal(this.getValue() + value.getValue());
        return true;
    }
    // if( value.getType() == Record.tFloat && this.getType() == Record.tInt){
       
    //     this.setfloatVal(this.getintVal() + value.getfloatVal());
    // }
    // if( value.getType() == Record.tIntArray && this.getType() == Record.tFloat){
    //     this.setfloatVal(this.getfloatVal() + value.getIntArray()[value.getArrayIndex()]);
    // }
    // if( value.getType() == Record.tFloat && this.getType() == Record.tIntArray){
    //     this.setType(Record.tFloat);
    //     this.setfloatVal(this.getIntArray()[this.getArrayIndex()] + value.getfloatVal());
    // }
    // if( value.getType() == Record.tFloatArray && this.getType() == Record.tFloat){
    //     this.setfloatVal(this.getfloatVal() + value.getfloatArray()[value.getArrayIndex()]);
    // }
    if( value.getType() == Record.tIntArray && this.getType() == Record.tInt){
        this.setintVal(this.getintVal() +value.getIntArray()[value.getArrayIndex()]);
        return true;
    }
    return false;
    }

    public boolean substractValue(Record value){
        if(value.getType() == this.getType())
        {
        if(this.getType() == Record.tInt){
            this.setintVal(this.getintVal()-value.getintVal());
        }
        if (this.getType() == Record.tFloat){
             this.setfloatVal(this.getfloatVal()-value.getfloatVal());
        }
        if (this.getType() == Record.tIntArray){
            this.getIntArray()[getArrayIndex()] -= value.getIntArray()[value.getArrayIndex()];
        }
        if (this.getType() == Record.tFloatArray){
             this.getfloatArray()[getArrayIndex()] -= value.getfloatArray()[value.getArrayIndex()];
        }
        return true;
    }
    if((value.getType() == Record.tInt && this.getType() == Record.tFloat) || ( value.getType() == Record.tFloat && this.getType() == Record.tInt)||( value.getType() == Record.tIntArray && this.getType() == Record.tFloat)||( value.getType() == Record.tFloat && this.getType() == Record.tIntArray)||( value.getType() == Record.tFloatArray && this.getType() == Record.tFloat)){
        this.setType(Record.tFloat);
        this.setfloatVal(this.getValue() - value.getValue());
        return true;
    }
    // if( value.getType() == Record.tFloat && this.getType() == Record.tInt){
    //     this.setType(Record.tFloat);
    //     this.setfloatVal(this.getintVal() - value.getfloatVal());
    // }
    // if( value.getType() == Record.tIntArray && this.getType() == Record.tFloat){
    //     this.setfloatVal(this.getfloatVal() - value.getIntArray()[value.getArrayIndex()]);
    // }
    // if( value.getType() == Record.tFloat && this.getType() == Record.tIntArray){
    //     this.setType(Record.tFloat);
    //     this.setfloatVal(this.getIntArray()[this.getArrayIndex()] - value.getfloatVal());
    // }
    // if( value.getType() == Record.tFloatArray && this.getType() == Record.tFloat){
    //     this.setfloatVal(this.getfloatVal() + value.getfloatArray()[value.getArrayIndex()]);
    // }
    if( value.getType() == Record.tIntArray && this.getType() == Record.tInt){
        this.setintVal(this.getintVal() -value.getIntArray()[value.getArrayIndex()]);
        return true;
    }
    return false;
    }

    public boolean mutiplyValue(Record value){
        if(value.getType() == this.getType())
        {
        if(this.getType() == Record.tInt){
            this.setintVal(this.getintVal()*value.getintVal());
        }
        if (this.getType() == Record.tFloat){
             this.setfloatVal(this.getfloatVal()*value.getfloatVal());
        }
        if (this.getType() == Record.tIntArray){
            this.getIntArray()[getArrayIndex()] *= value.getIntArray()[value.getArrayIndex()];
        }
        if (this.getType() == Record.tFloatArray){
             this.getfloatArray()[getArrayIndex()] *= value.getfloatArray()[value.getArrayIndex()];
        }
        return true;
    }
    if((value.getType() == Record.tInt && this.getType() == Record.tFloat) || ( value.getType() == Record.tFloat && this.getType() == Record.tInt)||( value.getType() == Record.tIntArray && this.getType() == Record.tFloat)||( value.getType() == Record.tFloat && this.getType() == Record.tIntArray)||( value.getType() == Record.tFloatArray && this.getType() == Record.tFloat)){
        this.setType(Record.tFloat);
        this.setfloatVal(this.getValue() * value.getValue());
        return true;
    }
    // if( value.getType() == Record.tFloat && this.getType() == Record.tInt){
    //     this.setType(Record.tFloat);
    //     this.setfloatVal(this.getintVal() - value.getfloatVal());
    // }
    // if( value.getType() == Record.tIntArray && this.getType() == Record.tFloat){
    //     this.setfloatVal(this.getfloatVal() - value.getIntArray()[value.getArrayIndex()]);
    // }
    // if( value.getType() == Record.tFloat && this.getType() == Record.tIntArray){
    //     this.setType(Record.tFloat);
    //     this.setfloatVal(this.getIntArray()[this.getArrayIndex()] - value.getfloatVal());
    // }
    // if( value.getType() == Record.tFloatArray && this.getType() == Record.tFloat){
    //     this.setfloatVal(this.getfloatVal() + value.getfloatArray()[value.getArrayIndex()]);
    // }
    if( value.getType() == Record.tIntArray && this.getType() == Record.tInt){
        this.setintVal(this.getintVal() *value.getIntArray()[value.getArrayIndex()]);
        return true;
    }
    return false;
    }

    public boolean divideValue(Record value){
        if(value.getType() == this.getType())
        {
        if(this.getType() == Record.tInt){
            this.setintVal(this.getintVal()/value.getintVal());
        }
        if (this.getType() == Record.tFloat){
             this.setfloatVal(this.getfloatVal()/value.getfloatVal());
        }
        if (this.getType() == Record.tIntArray){
            this.getIntArray()[getArrayIndex()] /= value.getIntArray()[value.getArrayIndex()];
        }
        if (this.getType() == Record.tFloatArray){
             this.getfloatArray()[getArrayIndex()] /= value.getfloatArray()[value.getArrayIndex()];
        }
        return true;
    }
    if((value.getType() == Record.tInt && this.getType() == Record.tFloat) || ( value.getType() == Record.tFloat && this.getType() == Record.tInt)||( value.getType() == Record.tIntArray && this.getType() == Record.tFloat)||( value.getType() == Record.tFloat && this.getType() == Record.tIntArray)||( value.getType() == Record.tFloatArray && this.getType() == Record.tFloat)){
        this.setType(Record.tFloat);
        this.setfloatVal(this.getValue() / value.getValue());
        return true;
    }
    // if( value.getType() == Record.tFloat && this.getType() == Record.tInt){
    //     this.setType(Record.tFloat);
    //     this.setfloatVal(this.getintVal() - value.getfloatVal());
    // }
    // if( value.getType() == Record.tIntArray && this.getType() == Record.tFloat){
    //     this.setfloatVal(this.getfloatVal() - value.getIntArray()[value.getArrayIndex()]);
    // }
    // if( value.getType() == Record.tFloat && this.getType() == Record.tIntArray){
    //     this.setType(Record.tFloat);
    //     this.setfloatVal(this.getIntArray()[this.getArrayIndex()] - value.getfloatVal());
    // }
    // if( value.getType() == Record.tFloatArray && this.getType() == Record.tFloat){
    //     this.setfloatVal(this.getfloatVal() + value.getfloatArray()[value.getArrayIndex()]);
    // }
    if( value.getType() == Record.tIntArray && this.getType() == Record.tInt){
        this.setintVal(this.getintVal() /value.getIntArray()[value.getArrayIndex()]);
        return true;
    }
    return false;
    }


    public static int tInt = 0;
    public static int tbool = 1;
    public static int tFloat = 2;
    public static int tIntArray = 3;
    public static int tboolArray = 4;
    public static int tFloatArray = 5;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getintVal() {
        return intVal;
    }

    public void setintVal(int intVal) {
        this.intVal = intVal;
    }

    public boolean getboolVal() {
        return boolVal;
    }

    public void setboolVal(boolean boolVal) {
        this.boolVal = boolVal;
    }
    public float getfloatVal() {
        return floatVal;
    }

    public void setfloatVal(float floatVal) {
        this.floatVal = floatVal;
    }

    public int[] getIntArray() {
        return intArray;
    }

    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }

    public boolean[] getboolArray(){
        return boolArray;
    }

    public void setboolArray(boolean[] boolArray){
        this.boolArray = boolArray;
    }

    public float[] getfloatArray() {
        return floatArray;
    }

    public void setfloatArray(float[] floatArray) {
        this.floatArray = floatArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getArrayNum() {
        return arrayNum;
    }

    public void setArrayNum(int arrayNum) {
        this.arrayNum = arrayNum;
    }

    public Integer getArrayIndex() {
        return arrayIndex;
    }

    public void setArrayIndex(Integer arrayIndex) {
        this.arrayIndex = arrayIndex;
    }
}