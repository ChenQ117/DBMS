package utils;

import dataStructment.Column;
import dataStructment.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0
 * @ClassName: TableUtils
 * @Description: 对表的基本操作
 * @Author: ChenQ
 * @Date: 2021/10/23 on 19:02
 */
public class TableUtils {
    private static TableUtils tableUtils;
    private TableUtils(){
    }
    public static TableUtils getTableUtils(){
        if (tableUtils == null){
            tableUtils = new TableUtils();
        }
        return tableUtils;
    }
    //表集合归一，对表进行笛卡尔积运算
    public void cartesian(List<Table> tableList,List<String> reTnameList){
        while (tableList.size()>1){
            Table t1 = tableList.get(0);
            Table t2 = tableList.get(1);
            Table table = tableUtils.infoNewTable(t1, t2);
            List<Integer> lineList = new ArrayList<>();//存储t1的行号
            //t1的每一行需要和t2的每一行的值进行连接
            for (int i=0;i<t1.getColumns().get(0).getColValue().size();i++){
                lineList.add(i);
            }
            for (int i=0;i<t2.getColumns().get(0).getColValue().size();i++){
                table.combine(t1,t2,i,lineList);
            }
            //更新集合
            tableList.remove(t1);
            tableList.remove(t2);
            reTnameList.remove(1);
            reTnameList.remove(0);
            tableList.add(table);
            reTnameList.add(table.getTable_name());
        }

    }
    //根据两张表初始化一张新的表
    public Table infoNewTable(Table tbLeft,Table tbRight){
        Table table = new Table();
        table.setTable_name(tbLeft.getTable_name());
        table.setAttribute_count(tbLeft.getAttribute_count()+tbRight.getAttribute_count());
        List<String> linkAttributes = new ArrayList<>();
        linkAttributes.addAll(tbLeft.getAttribute());
        linkAttributes.addAll(tbRight.getAttribute());
        table.setAttribute(linkAttributes);
        List<Column> linkColumn = new ArrayList<>();
        //列的浅拷贝
        for (int k=0;k<table.getAttribute_count();k++){
            Column column = new Column();
            if (k<tbLeft.getAttribute_count())
                column.copy(tbLeft.getColumns().get(k),k);
            else
                column.copy(tbRight.getColumns().get(k-tbLeft.getAttribute_count()),k);
            linkColumn.add(column);
        }
        table.setColumns(linkColumn);
        return table;
    }
}
