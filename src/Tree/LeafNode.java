package Tree;

/**
 * @version v1.0
 * @ClassName: LeafNode
 * @Description: 叶子结点
 * @Author: ChenQ
 * @Date: 2021/11/7 on 19:41
 */
public class LeafNode<T,V extends Comparable<V>> extends Node<T,V> {
    @Override
    T find(V key) {
        return null;
    }

    @Override
    Node insert(Object value, Comparable key) {
        return null;
    }
}
