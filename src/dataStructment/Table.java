package dataStructment;

import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0
 * @ClassName: Table
 * @Description: 表
 * @Author: ChenQ
 * @Date: 2021/10/23 on 16:10
 */
public class Table {
    String table_name;//表名
    String primaryKey;//主键
    int primary_index;
    int attribute_count;//属性个数
    List<Column> columns;//属性集合
    List<String> attribute;//属性

    public Table() {
        columns = new ArrayList<>();
        attribute = new ArrayList<>();
    }

    public List<String> getAttribute() {
        return attribute;
    }

    public void setAttribute(List<String> attribute) {
        this.attribute = attribute;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public int getAttribute_count() {
        return attribute_count;
    }

    public void setAttribute_count(int attribute_count) {
        this.attribute_count = attribute_count;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public int getPrimary_index() {
        return primary_index;
    }

    public void setPrimary_index(int primary_index) {
        this.primary_index = primary_index;
    }

    public void showTable(){
        for (String attr:attribute){
            System.out.print(attr+"\t\t");
//            System.out.print(String.format("%-20s",attr));
        }
        System.out.println();
        if (!columns.isEmpty()){
            for (int i=0;i<columns.get(0).getColValue().size();i++){
                for (int j=0;j<attribute_count;j++){
                    System.out.print(columns.get(j).getColValue().get(i)+"\t\t");
//                    int length = 21-((String)columns.get(j).getColValue().get(i)).length()+1;
//                    System.out.print(String.format("%-"+length+"s",columns.get(j).getColValue().get(i)));
                }
                System.out.println();
            }
        }
    }
    //删除一行的数据
    public void deleteLine(int index){
        for (int i=0;i<attribute_count;i++){
            columns.get(i).getColValue().remove(index);
        }
    }
    public void updateColumns(){
        for (int i=0;i<columns.size();i++){
            columns.get(i).setIndex(i);
        }
    }
}
