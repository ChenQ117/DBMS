package dataStructment;

import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0
 * @ClassName: Column
 * @Description: 列
 * @Author: ChenQ
 * @Date: 2021/10/23 on 16:14
 */
public class Column {
    private String name;//列名
    private String type;//属性类型
    private List colValue;//属性值
    private int index;//属性在字典中的位置
    private String bind;//约束
    private int length;//属性长度 默认大小为10

    /*public Column(String name, String type, int index) {
        this.name = name;
        this.type = type;
        this.colValue = new ArrayList();
        this.index = index;
        bind="null";
    }*/
    public Column(String name, String type, int index,int length) {
        this.name = name;
        this.type = type;
        this.colValue = new ArrayList();
        this.index = index;
        this.length = length>0?length:10;//默认大小为10
        bind = null;
    }
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getValue(int index){
        return colValue.get(index).toString();
    }
    public Column(String name, String type, int index,String bind) {
        this.name = name;
        this.type = type;
        this.colValue = new ArrayList();
        this.index = index;
        this.bind = bind;
    }
    public Column() {
        this.colValue = new ArrayList();
        this.bind = "null";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List getColValue() {
        return colValue;
    }
    public void addColValue(int size,Object o) {
        colValue = new ArrayList();
        for (int i=0;i<size;i++){
            colValue.add(o);
        }
    }
    public void setColValue(List<Integer> index,Object o){
        List col = new ArrayList();
        for (int i=0,j=0;i<colValue.size();i++){
            if (j<index.size()&&i==index.get(j)){
                col.add(o);
                j++;
            }else {
                col.add(colValue.get(i));
            }
        }
        colValue = col;
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }



    @Override
    public String toString() {
        return index + " " + name + " " + type +" "+ length + " " + bind;
    }
    public void showValues(String line){
        System.out.println("showValues:"+line+name);
        System.out.println("colValue.size():"+line+colValue.size());
        for (Object o:colValue){
            System.out.println(o);
        }

    }
}
