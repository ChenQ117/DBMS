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
//                    System.out.print(columns.get(j).getColValue().get(i)+"\t\t");
//                    int length = 21-((String)columns.get(j).getColValue().get(i)).length()+1;
                    System.out.print(String.format("%-"+columns.get(j).getLength()+"s ",columns.get(j).getColValue().get(i)));
//                    System.out.println("%-"+columns.get(i).getLength()+"s");
//                    System.out.println("88:"+columns.get(j).getName()+" 长度："+columns.get(j).getLength());
//                    System.out.printf("%0-"+columns.get(j).getLength()+"s\t",columns.get(j).getColValue().get(i));

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
    }
}
