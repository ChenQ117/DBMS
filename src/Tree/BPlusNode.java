package Tree;

/**
 * @version v1.0
 * @ClassName: BPlusNode
 * @Description: 非叶子结点
 * @Author: ChenQ
 * @Date: 2021/11/7 on 19:42
 */
public class BPlusNode<T,V extends Comparable<V>> extends Node<T,V> {
    @Override
    T find(V key) {
        int i = 0;
        while (i<number){
            if (key.compareTo((V)keys[i])<=0){
                break;
            }
            i++;
        }
        if (i == number){
            return null;
        }
        else {
            return children[i].find(key);
        }
    }

    @Override
    Node<T, V> insert(T value, V key) {
        int i=0;

        return null;
    }
}
