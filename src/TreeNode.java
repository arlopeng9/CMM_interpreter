import java.util.ArrayList;

public class TreeNode {
    private TreeNode parentNode;//父节点
    private ArrayList<TreeNode> children;//孩子节点
    private int type;//节点类型
    private Token value;//终结符树节点的值

    private String callName;
    private String json;


    public TreeNode(int type, TreeNode parentNode, String name){
        this.type = type;
        this.parentNode = parentNode;
        this.callName = name;
        this.children = new ArrayList<TreeNode>();
    }

    public TreeNode(int type, Token value, TreeNode parentNode, String name){
        this.type = type;
        this.value = value;
        this.parentNode = parentNode;
        this.callName = name;
        this.children = new ArrayList<TreeNode>();
    }


    public ArrayList<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<TreeNode> children) {
        this.children = children;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Token getValue() {
        return value;
    }

    public void setValue(Token value) {
        this.value = value;
    }

    public TreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(TreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}