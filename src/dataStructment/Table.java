package dataStructment;

import java.util.ArrayList;
import java.util.Collections;
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
//                    System.out.print(columns.get(j).getColValue().get(i)+"\t\t");
//                    int length = 21-((String)columns.get(j).getColValue().get(i)).length()+1;
                    System.out.print(String.format("%-"+columns.get(j).getLength()+"s ",columns.get(j).getColValue().get(i)));
//                    System.out.println("%-"+columns.get(i).getLength()+"s");
//                    System.out.println("88:"+columns.get(j).getName()+" 长度："+columns.get(j).getLength());
//                    System.out.printf("%0-"+columns.get(j).getLength()+"s\t",columns.get(j).getColValue().get(i));

                }
                System.out.println();
            }
            System.out.println("共显示"+columns.get(0).getColValue().size()+"条记录");
        }
    }
    //删除一行的数据  调用删除函数时一定要排序，且一定要从大往小删！！！！！
    public void deleteLine(int index){
        for (int i=0;i<attribute_count;i++){
            columns.get(i).getColValue().remove(index);
        }
    }
    //只保留行号列表中有的行 调用删除函数时一定要排序，且一定要从大往小删！！！！
    public void remainListAll(List<Integer> lineList){
        Collections.sort(lineList);
        for (int i=columns.get(0).getColValue().size()-1;i>=0;i--){
            if (!lineList.contains(i)) {
                deleteLine(i);
            }
        }
    }
    public void updateColumns(){
        for (int i=0;i<columns.size();i++){
            columns.get(i).setIndex(i);
        }
    }
    public void updateAttrName(){
        attribute.clear();
        for (int i=0;i<attribute_count;i++){
            attribute.add(table_name+"."+columns.get(i).getName());
            columns.get(i).setName(attribute.get(i));
        }
    }

    /**
     * t1的第line行与t2的lineList里的行进行连接，生成的行插入到调用该方法的表t3中
     * @param t1 表1
     * @param t2 表2
     * @param line t1中需要连接的行号
     * @param lineList t2中需要连接的行号的集合
     */
    public void combine(Table t1,Table t2,int line,List<Integer> lineList){
        int j=0;
        for (;j<t1.attribute_count;j++){
            for (int i=0;i<lineList.size();i++){
                columns.get(j).getColValue().add(t1.getColumns().get(j).getColValue().get(lineList.get(i)));
            }
        }
        for (;j<attribute_count;j++){
            for (int i=0;i<lineList.size();i++){
                columns.get(j).getColValue().add(t2.getColumns().get(j-t1.attribute_count).getColValue().get(line));
            }
        }

    }

    /**
     * 根据list的参数显示对应列的数据  对输出结果去重
     * @param list 存的是需要显示的列的列标
     */
    public void showTableColumn(List<Integer> list){
        List<String> showList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int columnLine = columns.get(0).getColValue().size();
        for (int j=0;j<list.size();j++){
            sb.append(attribute.get(list.get(j))+"\t\t");
        }
        String s = sb.toString();
        if (!showList.contains(s)){
            showList.add(s);
        }
        for (int i=0;i<(columnLine>0?columnLine:0);i++){
            sb = new StringBuilder();
            for (int j=0;j<list.size();j++){
                sb.append(String.format("%-"+columns.get(list.get(j)).getLength()+"s ",columns.get(list.get(j)).getColValue().get(i)));
            }
            s = sb.toString();
            if (!showList.contains(s)){
                showList.add(s);
            }
        }
        for (int i=0;i<showList.size();i++){
            System.out.println(showList.get(i));
        }
        System.out.println("共显示"+showList.size()+"条记录");
    }

    /**
     * 根据list参数显示对应行的所有信息,对输出结果去重
     * @param list 存的是需要输出的行的行标
     */
    public void showTableLine(List<Integer> list){
        List<String> showList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (String attr:attribute){
            sb.append(attr+"\t\t");
        }
        String s = sb.toString();
        if (!showList.contains(s)){
            showList.add(s);
        }
        if (list!=null && list.size()>0){
            for (int j=0;j<list.size();j++){
                sb = new StringBuilder();
                for (int i=0;i<attribute_count;i++){
                    sb.append(String.format("%-"+columns.get(i).getLength()+"s ",columns.get(i).getColValue().get(list.get(j))));
                }
                s = sb.toString();
                if (!showList.contains(s)){
                    showList.add(s);
                }
            }
            for (int i=0;i<showList.size();i++){
                System.out.println(showList.get(i));
            }
        }
        System.out.println("共显示"+showList.size()+"条记录");
    }

    /**
     * 根据行标和列标输出对应的行和列 默认去重输出
     * @param lineList 需要输出的行标
     * @param columnList 需要输出的列标
     */
    public void showLineAndColumn(List<Integer> lineList,List<Integer> columnList){
        List<String> showList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int columnLine = columns.get(0).getColValue().size();
        for (int j=0;j<columnList.size();j++){
            sb.append(attribute.get(columnList.get(j))+"\t\t");
        }
        String s = sb.toString();
        if (!showList.contains(s)){
            showList.add(s);
        }
        if (lineList!=null && lineList.size()>0){
            for (int i=0;i<lineList.size();i++){
                sb = new StringBuilder();
                for (int j=0;j<columnList.size();j++){
                    sb.append(String.format("%-"+columns.get(columnList.get(j)).getLength()+"s ",
                            columns.get(columnList.get(j)).getColValue().get(lineList.get(i))));
                }
                s = sb.toString();
                if (!showList.contains(s)){
                    showList.add(s);
                }
            }
        }
        for (int i=0;i<showList.size();i++){
            System.out.println(showList.get(i));
        }
        System.out.println("共显示"+showList.size()+"条记录");
    }
}
