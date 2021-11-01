package utils;

import dataStructment.Column;
import dataStructment.Table;

import javax.security.auth.Destroyable;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0
 * @ClassName: ReadAndWrite
 * @Description: IO操作类
 * @Author: ChenQ
 * @Date: 2021/10/24 on 17:38
 */
public class ReadAndWrite {
    public static final String ROOT = "DATA/";
    public static final String suffix = ".txt";
    public static final String dictionary = "_dictionary";
    private final static String bind_1 = "(primary key|not null)";
    //生成数据字典文件
    public static void writeDictionaryTxt(Table table){
        try {
            String path = ROOT+table.getTable_name()+dictionary+suffix;
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            List<Column> columns = table.getColumns();
            for (int i=0;i<columns.size();i++){
                writer.write(columns.get(i).toString()+"\n");
            }
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //生成表数据文件
    public static void writeTableTxt(Table table){
        try{
            String path = ROOT+table.getTable_name()+suffix;
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            List<String> attribute = table.getAttribute();
            for (int i=0;i<attribute.size();i++){
                writer.write(attribute.get(i)+"\t\t");
            }
            writer.write("\n");
            List<Column> columns = table.getColumns();
            if (columns.size()>0){
                for(int i=0;i<columns.get(0).getColValue().size();i++){
                    for (int j=0;j<attribute.size();j++){
                        writer.write(String.valueOf(columns.get(j).getColValue().get(i))+"\t\t");
                    }
                    writer.write("\n");
                }
            }
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //读取数据字典文件
    public static void readDictionary(String tableName){
        String path = ROOT+tableName+dictionary+suffix;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String str = "";
            while ((str = reader.readLine())!=null){
                System.out.println(str);
            }
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public static void insertValue(Table table){
        try{
            String path = ROOT+table.getTable_name()+suffix;
            BufferedWriter writer = new BufferedWriter(new FileWriter(path,true));
            List<Column> columns = table.getColumns();
            if (columns.size()>0){
                for (int j=0;j<table.getAttribute().size();j++){
                    List colValue = columns.get(j).getColValue();
                    writer.write(String.valueOf(colValue.get(colValue.size()-1))+"\t\t");
                }
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //删除表
    public static boolean dropTableTxt(String name){
        File file = new File(ROOT+name+suffix);
        boolean delete_1 = false;
        boolean delete_2 = false;
        if (file.exists()){
            if (!file.isDirectory()){
                delete_1 = file.delete();
            }
        }
        file = new File(ROOT+name+dictionary+suffix);
        if (file.exists()){
            if (!file.isDirectory()){
                delete_2 = file.delete();
            }
        }
        return delete_1&&delete_2;
    }
    //根据数据字典形成表结构
    public static Table formTable(String name){
        Table table = new Table();
        table.setTable_name(name);
        List<Column> columns = table.getColumns();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(ROOT+name+dictionary+suffix));
            String str = "";
            while ((str = bfr.readLine())!=null){
                boolean isPrimary = false;
                Column column = new Column();
                String[] split_bind = str.split("((?="+bind_1+")|(?<="+bind_1+"))");
                if (split_bind.length>1){
                    column.setBind(split_bind[1]);
                    if ("primary key".equals(split_bind[1])){
                        isPrimary = true;
                    }
                }
                String[] split_basic = split_bind[0].split("\\s+");
                column.setIndex(Integer.valueOf(split_basic[0]));
                column.setName(split_basic[1]);
                column.setType(split_basic[2]);
                columns.add(column);
                table.getAttribute().add(split_basic[1]);
                if (isPrimary){
                    table.setPrimary_index(column.getIndex());
                    table.setPrimaryKey(column.getName());
                }
            }
            bfr.close();
            table.setAttribute_count(table.getAttribute().size());
        }catch (IOException e){
            e.printStackTrace();
        }
        return table;
    }
    //导入表数据
    public static void exportTableValue(Table table){
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(ROOT+table.getTable_name()+suffix));
            String sr = bfr.readLine();
            List<Column> columns = table.getColumns();
            while ((sr = bfr.readLine())!=null){
                String[] values = sr.split("\\s+");
                for (int i=0;i<table.getAttribute_count();i++){
                    columns.get(i).getColValue().add(values[i]);
                }
            }
            bfr.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
