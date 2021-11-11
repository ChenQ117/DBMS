package Tree;

import java.util.List;

/**
 * @version v1.0
 * @ClassName: Node
 * @Description: 结点
 * @Author: ChenQ
 * @Date: 2021/11/7 on 19:01
 */
public abstract class Node<T,V extends Comparable<V>> {
    protected static final int maxNumber = 3;
    protected Node<T,V> parentNode;//父结点
    protected Node<T,V>[] children;//子结点
    protected Integer number;//子结点数量
    protected Object keys[];//子结点的值
    public Node(){
        parentNode = null;
        children = new Node[maxNumber];
        number = 0;
        keys = new Object[maxNumber];
    }

    abstract T find(V key);//查找
    abstract Node<T,V> insert(T value,V key);//插入

    public static int getMaxNumber() {
        return maxNumber;
    }

    public Node<T, V> getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node<T, V> parentNode) {
        this.parentNode = parentNode;
    }

    public Node<T, V>[] getChildren() {
        return children;
    }

    public void setChildren(Node<T, V>[] children) {
        this.children = children;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Object[] getKeys() {
        return keys;
    }

    public void setKeys(Object[] keys) {
        this.keys = keys;
    }
}
