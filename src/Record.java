public class Record {
    private int level;//变量的层次

    private Token token;//对应的标识符（不一定有）
    private String name;
    private int type;//数据类型
    private int intVal;//int类型数据的值
    private float floatVal;//real类型数据的值
    private int arrayNum;//数组类型数据的大小
    private Integer arrayIndex;//数组下标,为赋值时为空
    private int[] intArray;//int类型数组
    private float[] floatArray;//real类型数组

    public Record(int level, Token token, int type, String name, int intVal){
        this.level = level;
        this.token = token;
        this.type = type;
        this.name = name;
        this.intVal = intVal;
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
        if (this.getType() == Record.tReal){
            return this.floatVal;
        }
        if (this.getType() == Record.tIntArray){
            return intArray[this.getArrayIndex()];
        }
        if (this.getType() == Record.tRealArray){
            return this.floatArray[this.arrayIndex];
        }
        return 0;
    }


    public static int tInt = 0;
    public static int tReal = 1;
    public static int tIntArray = 2;
    public static int tRealArray = 3;

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

    public float getRealValue() {
        return floatVal;
    }

    public void setRealValue(float realValue) {
        this.floatVal = realValue;
    }

    public int[] getIntArray() {
        return intArray;
    }

    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }

    public float[] getRealArray() {
        return floatArray;
    }

    public void setRealArray(float[] floatArray) {
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