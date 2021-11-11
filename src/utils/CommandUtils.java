package utils;


import dataStructment.Column;
import dataStructment.Table;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @version v1.0
 * @ClassName: CommandUtils
 * @Description: 语法语义检查
 * @Author: ChenQ
 * @Date: 2021/10/23 on 19:33
 */
public class CommandUtils {
    private static CommandUtils instance;
    private final String ROOT = "DATA/";
    public static final String suffix = ".txt";
    private final String keyWord = "[a-zA-Z]+(\\w)*";//关键字
    private final String space_1 = "\\s+";//一个或多个空格
    private final String space_0 = "\\s*";//零个或多个空格
    private final String type = "(string|int|float)";//属性
    private final String typeSize = "("+space_0+"\\("+space_0+"\\d+"+space_0+"\\))*";//属性大小
    private final String bind = "\\s+(primary key|not null)?";//约束条件
    private final String bind_1 = "(primary key|not null)";
    private final String keyValue = "(\"\\s*\\S+\\s*\"|\\d+(\\.\\d+)*)";//属性值为字符串或者数值
    private final String whereOp = "(>=|<=|!=|=|>|<)";//where查询条件
    private final Pattern create_table = Pattern.compile("create"+space_1+"table"+space_1+keyWord+space_0
            + "\\(("+space_0+keyWord+space_1+type+typeSize+"("+bind+")*"+space_0+",)*"
            +space_0+keyWord+space_1+type+typeSize+"("+bind+")*"+space_0+"\\)"+space_0);
    private final Pattern desc_table = Pattern.compile("desc"+space_1+keyWord+space_0);
    private final Pattern drop_table = Pattern.compile("drop"+space_1+"table"+space_1+keyWord+space_0);
    private final Pattern insert_line = Pattern.compile("insert"+space_1+"into"+space_1+keyWord+space_1
            +"values"+space_0+"\\("+"("+space_0+keyValue+space_0+","+")*"+space_0+keyValue+"\\)");
    private final Pattern selectAll = Pattern.compile("select"+space_1+"\\*"+space_1+"from"+space_1+keyWord);
    private final Pattern alter_table_add = Pattern.compile("alter"+space_1+"table"+space_1+keyWord
            +space_1+"(add"+space_1+keyWord+space_1+type+typeSize+"("+bind+")*"+space_0+",)*"+space_0
            +"(add"+space_1+keyWord+space_1+type+typeSize+"("+bind+")*"+space_0+")");
    private final Pattern alter_table_drop = Pattern.compile("alter"+space_1+"table"+space_1+keyWord
            +space_1+"(drop"+space_1+keyWord+space_0+",)*"+space_0
            +"(drop"+space_1+keyWord+space_0+")");
    private final Pattern delete_all = Pattern.compile("delete"+space_1+"from"+space_1+keyWord);
    private final Pattern delete_where = Pattern.compile("delete"+space_1+"from"+space_1+keyWord+space_1+"where"+space_1+keyWord+space_0
            +whereOp+space_0+keyValue+"("+space_1+"and"+space_1+keyWord+space_0
            +whereOp+space_0+keyValue+")*");
    private final Pattern update_all = Pattern.compile("update"+space_1+keyWord+space_1+"set"+space_1+keyWord
            +space_0+"="+space_0+keyValue+space_0+"(,"+space_0+keyWord+space_0+"="+space_0+keyValue+space_0+")*");
    private final Pattern update_where = Pattern.compile("update"+space_1+keyWord+space_1+"set"+space_1+keyWord
            +space_0+"="+space_0+keyValue+space_0+"(,"+space_0+keyWord+space_0+"="+space_0+keyValue+space_0+")*"
            +space_1+"where"+space_1+keyWord+space_0
            +whereOp+space_0+keyValue+"("+space_1+"and"+space_1+keyWord+space_0
            +whereOp+space_0+keyValue+")*");
    //单表属性投影
    private final Pattern select_projection = Pattern.compile("select"+space_1+"("+keyWord+"\\.)?"+keyWord+"("+space_0+","+space_0
            +"("+keyWord+"\\.)?"+keyWord+")*"+space_1
            +"from"+space_1+keyWord+"("+space_1+keyWord+")?");
    //单表属性选择
    private final Pattern select_where = Pattern.compile("select"+space_1+"\\*"+space_1
            +"from"+space_1+keyWord+"("+space_1+keyWord+")?"+space_1
            +"where"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+keyValue
            +"("+space_1+"and"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+keyValue+")*");
    //单表属性选择投影
    private final Pattern select_p_where = Pattern.compile("select"+space_1+"("+keyWord+"\\.)?"+keyWord
            +"("+space_0+","+space_0+"("+keyWord+"\\.)?"+keyWord+")*"+space_1
            +"from"+space_1+keyWord+"("+space_1+keyWord+")?"+space_1
            +"where"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+keyValue
            +"("+space_1+"and"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+keyValue+")*");
    //两个关系和多个关系的连接操作
    private final Pattern select_link = Pattern.compile("select"+space_1+"\\*"+space_1
            +"from"+space_1+keyWord+"("+space_1+keyWord+")?"+"("+space_0+","+space_0+keyWord+"("+space_1+keyWord+")?)+"+space_1
            +"where"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+"("+keyWord+"\\.)?"+keyWord
            +"("+space_1+"and"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+"("+keyWord+"\\.)?"+keyWord+")*"
    );
    //两个关系和多个关系的选择和连接操作
    private final Pattern select_link_w = Pattern.compile("select"+space_1+"\\*"+space_1
            +"from"+space_1+keyWord+"("+space_1+keyWord+")?"+"("+space_0+","+space_0+keyWord+"("+space_1+keyWord+")?)+"+space_1
            +"where"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+"(("+keyWord+"\\.)?"+keyWord+")|"+keyValue
            +"("+space_1+"and"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+"(("+keyWord+"\\.)?"+keyWord+")|"+keyValue
    );
    //两个关系和多个关系的投影和连接操作
    private final Pattern select_link_p = Pattern.compile("select"+space_1+"("+keyWord+"\\.)?"+keyWord
            +"("+space_0+","+space_0+"("+keyWord+"\\.)?"+keyWord+")*"+space_1
            +"from"+space_1+keyWord+"("+space_1+keyWord+")?"+"("+space_0+","+space_0+keyWord+"("+space_1+keyWord+")?)+"+space_1
            +"where"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+"("+keyWord+"\\.)?"+keyWord
            +"("+space_1+"and"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+"("+keyWord+"\\.)?"+keyWord+")*"
    );
    //多个关系的选择、投影和连接操作
    private final Pattern select_link_p_a_w = Pattern.compile("select"+space_1+"("+keyWord+"\\.)?"+keyWord
            +"("+space_0+","+space_0+"("+keyWord+"\\.)?"+keyWord+")*"+space_1
            +"from"+space_1+keyWord+"("+space_1+keyWord+")?"+"("+space_0+","+space_0+keyWord+"("+space_1+keyWord+")?)+"+space_1
            +"where"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+"(("+keyWord+"\\.)?"+keyWord+")|"+keyValue
            +"("+space_1+"and"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+"(("+keyWord+"\\.)?"+keyWord+")|"+keyValue
    );
    public void execute(String command){
        command = command.toLowerCase();
        if (create_table.matcher(command).matches()){
            create(command);
        }else if (desc_table.matcher(command).matches()){
            desc(command);
        } else if (drop_table.matcher(command).matches()){
            dropTable(command);
        }else if (insert_line.matcher(command).matches()){
            insert(command);
        }else if (selectAll.matcher(command).matches()){
            selectAll(command);
        }else if (alter_table_add.matcher(command).matches()){
            alterTableAdd(command);
        }else if (alter_table_drop.matcher(command).matches()){
            alterTableDrop(command);
        }else if (delete_all.matcher(command).matches()){
            deleteAll(command);
        }else if (delete_where.matcher(command).matches()){
            deleteWhere(command);
        }else if (update_all.matcher(command).matches()){
            updateAll(command);
        }else if (update_where.matcher(command).matches()){
            updateWhere(command);
        }else if (select_projection.matcher(command).matches()){
            selectProjection(command);
        }else if (select_where.matcher(command).matches()) {
            selectWhere(command);
        }else if (select_p_where.matcher(command).matches()){
            selectPAWhere(command);
        }
        else {
            System.out.println("语法错误");
        }
    }
    private CommandUtils(){
    }
    public static CommandUtils getInstance(){
        if (instance == null){
            instance = new CommandUtils();
        }
        return instance;
    }
    //删除表
    public void dropTable(String command){
        String[] drop = command.split("\\s+");
        if (!isExitsTable(drop[2])){
            System.out.println("删除失败:"+drop[2]+"表不存在");
            return;
        }
        boolean delete = ReadAndWrite.dropTableTxt(drop[2]);
        if (delete){
            System.out.println("删除成功");
        }else {
            System.out.println("删除失败");
        }
    }
    //创建表
    public void create(String command){
        String substring = command.substring(command.indexOf("(")+1, command.lastIndexOf(")")).trim();
        String substring1 = command.substring(0, command.indexOf("(")).trim();
        String[] create_table = substring1.split("\\s+");
        if (isExitsTable(create_table[2])){
            System.out.println("创建失败，该表已经存在");
            return;
        }
        Table table = new Table();
        table.setTable_name(create_table[2]);
        String[] attribute = substring.split(",");
        table.setAttribute_count(attribute.length);
        int primary_index = -1;
        for (int i=0;i<attribute.length;i++){
            attribute[i] = attribute[i].trim();
            String[] split1 = attribute[i].split("((?="+bind_1+")|(?<="+bind_1+"))");//分成属性和主干
            split1[0] = split1[0].trim();
            String[] attr_info = split1[0].split("\\s+");
            if (table.getAttribute().contains(attr_info[0])){
                System.out.println("出错了，属性重复");
                return;
            }
            String attrName = attr_info[0].trim();//属性名
            String attrTypeInfo = attr_info[1].trim();//属性类型
            String[] split = attrTypeInfo.split("\\(|\\)");
            String attrType = split[0].trim();
            int attrTypeSize = split.length==2?Integer.parseInt(split[1].trim()):-1;
            if (attrTypeSize>100){
                System.out.println(attrName+"属性值太大了，请重新分配");
                return;
            }
            Column column = new Column(attrName,attrType,i,attrTypeSize);
            if (split1.length>1){
                column.setBind(split1[1]);
            }
            if ("primary key".equals(column.getBind())){
                if (primary_index==-1){
                    primary_index=i;
                    table.setPrimary_index(primary_index);
                    table.setPrimaryKey(attrName);
                }else {
                    System.out.println("出错了，主键不唯一");
                    return;
                }
            }
            table.getColumns().add(column);
            table.getAttribute().add(column.getName());
        }
        //生成数据字典文件
        ReadAndWrite.writeDictionaryTxt(table);
        //生成表数据文件
        ReadAndWrite.writeTableTxt(table);
        System.out.println("创建成功");
    }
    //插入数据
    public void insert(String command){
        String[] insert = command.split("\\(|\\)");
        String[] insert_name = insert[0].split("\\s+");
        if (!isExitsTable(insert_name[2])){
            System.out.println("插入失败，该表不存在");
            return;
        }
        Table table = ReadAndWrite.formTable(insert_name[2]);
        //导入表数据
        ReadAndWrite.exportTableValue(table);

        insert[1] = insert[1].trim();
        String[] insert_values = insert[1].split(",");
        if (table.getAttribute().size() != insert_values.length){
            System.out.println("插入失败，属性个数不对");
            return;
        }
        List<Column> columns = table.getColumns();
        for (int i=0;i<insert_values.length;i++){
            Column column = columns.get(i);
            insert_values[i] = insert_values[i].trim();
            if ("string".equals(column.getType())){
                if ("not null".equals(column.getBind())){
                    Pattern pattern = Pattern.compile("\"(.)+\"");
                    if (!pattern.matcher(insert_values[i]).matches()){
                        System.out.println("插入失败，第"+(i+1)+"个属性值类型错误");
                        return;
                    }
                }else if ("primary key".equals(column.getBind())){;
                    Pattern pattern = Pattern.compile("\"(.)+\"");
                    if (!pattern.matcher(insert_values[i]).matches()){
                        System.out.println("插入失败，第"+(i+1)+"个属性值类型错误");
                        return;
                    }
                    String[] split = insert_values[i].split("\"");
                    boolean contains = column.getColValue().contains(split[1]);
                    if (contains){
                        System.out.println("插入失败，主属性有重复的值");
                        return;
                    }
                }else {
                    Pattern pattern = Pattern.compile("\"(.)*\"");
                    if (!pattern.matcher(insert_values[i]).matches()){
                        System.out.println("插入失败，第"+(i+1)+"个属性值类型错误");
                        return;
                    }
                }
                String[] split = insert_values[i].split("\"");
                String value = split[1];
                if (value.length()>column.getLength()){
                    System.out.println("插入失败,"+value+"超出数据大小范围");
                    return;
                }
                column.getColValue().add(split[1]);
            }else if ("int".equals(column.getType())){
                if ("not null".equals(column.getBind())){
                    Pattern pattern = Pattern.compile("\\d+");
                    if (!pattern.matcher(insert_values[i]).matches()){
                        System.out.println("插入失败，第"+(i+1)+"个属性值类型错误");
                        return;
                    }
                }else if ("primary key".equals(column.getBind())){
                    Pattern pattern = Pattern.compile("\\d+");
                    if (!pattern.matcher(insert_values[i]).matches()){
                        System.out.println("插入失败，第"+(i+1)+"个属性值类型错误");
                        return;
                    }
                    int value = Integer.valueOf(insert_values[i]);
                    boolean contains = column.getColValue().contains(value);
                    if (contains){
                        System.out.println("插入失败，主属性有重复的值");
                        return;
                    }
                }else {
                    Pattern pattern = Pattern.compile("\\d*");
                    if (!pattern.matcher(insert_values[i]).matches()){
                        System.out.println("插入失败，第"+(i+1)+"个属性值类型错误");
                        return;
                    }
                }
                int value = Integer.valueOf(insert_values[i]);
                column.getColValue().add(value);
            }else if ("float".equals(column.getType())){
                if ("not null".equals(column.getBind())){
                    Pattern pattern = Pattern.compile("\\d+(\\.\\d+)*");
                    if (!pattern.matcher(insert_values[i]).matches()){
                        System.out.println("插入失败，第"+(i+1)+"个属性值类型错误");
                        return;
                    }
                }else if ("primary key".equals(column.getBind())){
                    Pattern pattern = Pattern.compile("\\d+(\\.\\d+)*");
                    if (!pattern.matcher(insert_values[i]).matches()){
                        System.out.println("插入失败，第"+(i+1)+"个属性值类型错误");
                        return;
                    }
                    Float value = Float.valueOf(insert_values[i]);
                    boolean contains = column.getColValue().contains(value);
                    if (contains){
                        System.out.println("插入失败，主属性有重复的值");
                        return;
                    }
                }else {
                    Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)*)*");
                    if (!pattern.matcher(insert_values[i]).matches()){
                        System.out.println("插入失败，第"+(i+1)+"个属性值类型错误");
                        return;
                    }
                }
                float value = Float.valueOf(insert_values[i]);
                column.getColValue().add(value);
            }else {

            }
        }
        ReadAndWrite.insertValue(table);
        System.out.println("插入成功");
    }
    //显示表结构
    public void desc(String command){
        String[] split = command.split("\\s+");
        String name = split[1];
        if (!isExitsTable(name)){
            System.out.println(name+"表不存在");
            return;
        }
        ReadAndWrite.readDictionary(name);
    }
    //查询表的所有数据
    public void selectAll(String command){
        String[] split = command.split("\\s+");
        String tableName = split[split.length - 1];
        if (!isExitsTable(split[split.length - 1])){
            System.out.println("查找失败，"+tableName+"不存在");
            return;
        }
        Table table = ReadAndWrite.formTable(tableName);
        ReadAndWrite.exportTableValue(table);
        table.showTable();
    }
    //增加表属性
    public void alterTableAdd(String command){
        //ALTER TABLE dependent ADD employer string not null, ADD occupation string not null
        String[] split = command.split(",");//[ALTER TABLE dependent ADD employer string not null][ADD occupation string not null]
        String[] split1 = split[0].split("((?="+bind_1+")|(?<="+bind_1+"))");//[ALTER TABLE dependent ADD employer string ][not null]
        String[] split2 = split1[0].split("\\s+");//[ALTER] [TABLE] [dependent] [ADD] [employer] [string(20)]
        List<String > attrAdd = new ArrayList<>();
        if (!isExitsTable(split2[2])){
            System.out.println("属性添加失败："+split2[2]+"不存在");
            return;
        }
        Table table = ReadAndWrite.formTable(split2[2]);
        ReadAndWrite.exportTableValue(table);
        List<Column> columnList = new ArrayList<>();
        String primaryKey = table.getPrimaryKey();
        int primaryKeyIndex = table.getPrimary_index();
        String attrName = split2[4].trim();//属性名
        if (table.getAttribute().contains(attrName)){
            System.out.println("属性添加失败："+attrName+"属性已存在");
            return;
        }
        String attrTypeInfo = split2[5].trim();//属性类型
        String[] split_1 = attrTypeInfo.split("\\(|\\)");
        String attrType = split_1[0].trim();
        int attrTypeSize = split_1.length==2?Integer.parseInt(split_1[1].trim()):-1;
        Column column = new Column(attrName,attrType,table.getAttribute_count(),attrTypeSize);
        if (split1.length>1){
            if ("primary key".equals(split1[1]) && table.getPrimaryKey()!=null){
                System.out.println("属性添加失败：存在多个主键");
                return;
            }else {
                primaryKey = column.getName();
                primaryKeyIndex = column.getIndex();
            }
            column.setBind(split1[1]);
        }
        attrAdd.add(split2[4]);
        column.addColValue(table.getColumns().get(0).getColValue().size(),"-");
        columnList.add(column);

        for (int i=1;i<split.length;i++){
            String[] split3 = split[i].split("((?=" + bind_1 + ")|(?<=" + bind_1 + "))");//[ADD occupation string] [not null]
            split3[0] = split3[0].trim();
            String[] split4 = split3[0].split("\\s+");//[ADD] [occupation] [string(12)]
            attrName = split4[1].trim();
            if (table.getAttribute().contains(attrName)||attrAdd.contains(attrName)){
                System.out.println("属性添加失败："+attrName+"属性已存在");
                return;
            }
            attrTypeInfo = split4[2].trim();//属性类型
            split_1 = attrTypeInfo.split("\\(|\\)");//[string][12]
            attrType = split_1[0].trim();
            attrTypeSize = split_1.length==2?Integer.parseInt(split_1[1].trim()):-1;
            Column column1 = new Column(attrName,attrType,table.getAttribute_count()+columnList.size(),attrTypeSize);
            if (split3.length>1){
                if ("primary key".equals(split3[1]) && primaryKey!=null){
                    System.out.println("属性添加失败：存在多个主键");
                    return;
                }else {
                    primaryKey = column1.getName();
                    primaryKeyIndex = column1.getIndex();
                }
                column1.setBind(split3[1]);
            }
            column1.addColValue(table.getColumns().get(0).getColValue().size(),"-");
            columnList.add(column1);
            attrAdd.add(attrName);
        }
        table.getColumns().addAll(columnList);
        table.getAttribute().addAll(attrAdd);
        table.setPrimary_index(primaryKeyIndex);
        table.setPrimaryKey(primaryKey);
        table.setAttribute_count(table.getAttribute().size());
        ReadAndWrite.writeDictionaryTxt(table);
        ReadAndWrite.writeTableTxt(table);
        System.out.println("属性添加成功");
    }
    //删除表属性
    public void alterTableDrop(String command){
        int line = 0;//受影响的行数
        //ALTER TABLE dependent DROP employer,DROP occupation
        String[] split = command.split(",");//[ALTER TABLE dependent DROP employer][DROP occupation]
        split[0] = split[0].trim();
        String[] split1 = split[0].split("\\s+");//[ALTER] [TABLE] [dependent] [DROP] [employer]
        String tableName = split1[2];
        if (!isExitsTable(tableName)){
            System.out.println("属性删除失败："+tableName+"不存在");
            return;
        }
        Table table = ReadAndWrite.formTable(tableName);
        ReadAndWrite.exportTableValue(table);
        List<String> attribute = table.getAttribute();
        List<String> attrRemove = new ArrayList<>();

        String attrName = split1[split1.length-1];
        if (!attribute.contains(attrName)){
            System.out.println("属性删除失败："+attrName+"属性不存在");
            return;
        }
        attrRemove.add(attrName);
        for (int i=1;i<split.length;i++){
            split[i] = split[i].trim();
            String[] split2 = split[i].split("\\s+");//[DROP] [occupation]
            attrName = split2[split2.length-1];
            if (!attribute.contains(attrName)){
                System.out.println("属性删除失败："+attrName+"属性不存在");
                return;
            }
            attrRemove.add(attrName);
        }

        if (attrRemove.contains(table.getPrimaryKey())){
            table.setPrimaryKey("");
            table.setPrimary_index(-1);
        }
        table.getAttribute().removeAll(attrRemove);
        table.setAttribute_count(table.getAttribute().size());
        List<Column> columns = table.getColumns();
        List<Column> columnRemove = new ArrayList<>();
        for (int i=0;i<columns.size();i++){
            for (int j=0;j<attrRemove.size();j++){
                if (columns.get(i).getName().equals(attrRemove.get(j))){
                    columnRemove.add(columns.get(i));
//                    columns.remove(columns.get(i));
                    line++;
                }
            }
        }
        columns.removeAll(columnRemove);
        table.updateColumns();
        ReadAndWrite.writeDictionaryTxt(table);
        ReadAndWrite.writeTableTxt(table);
        System.out.println("属性删除成功，共"+line+"行受到影响");
    }
    //删除表中所有元组
    public void deleteAll(String command){
        //DELETE FROM employee
        String[] split = command.split("\\s+");//[DELETE] [FROM] [employee]
        String name = split[2];
        if (!isExitsTable(name)){
            System.out.println("删除表数据失败："+name+"不存在");
            return;
        }
        Table table = ReadAndWrite.formTable(name);
        ReadAndWrite.writeTableTxt(table);
        System.out.println("删除表数据成功");
    }
    //删除表中符合条件的元组
    public void deleteWhere(String command){
        //DELETE FROM employee WHERE NAME = "王二" AND ssn = "230101198204078121"
        String[] split = command.split("where");//[DELETE FROM employee][NAME = "王二" AND ssn = "230101198204078121"]
        split[0] = split[0].trim();
        String[] split1 = split[0].split("\\s+");//[DELETE] [FROM] [employee]
        String name = split1[2];
        if (!isExitsTable(name)){
            System.out.println("删除表数据失败："+name+"不存在");
            return;
        }
        Table table = ReadAndWrite.formTable(name);
        ReadAndWrite.exportTableValue(table);
        List<Integer> index = findWhere(table,"删除表数据失败：",split[1].trim(),null);
        if (index.size()!=0){
            for (int i=index.size()-1;i>=0;i--){
                table.deleteLine(index.get(i));
            }
            ReadAndWrite.writeTableTxt(table);
            System.out.println("删除表数据成功，共"+index.size()+"行受到影响");
        }else {
            System.out.println("删除表数据失败：该数据不存在");
        }
    }
    //找出符合where条件的行
    public List<Integer> findWhere(Table table,String log,String split,String reName){
        List<Integer> index = new ArrayList<>();//满足条件的行
        List<String> attribute = table.getAttribute();
        split = split.trim();
        String[] split2 = split.split("and");//[NAME = "王二"][ssn = "230101198204078121"]
        List<String> key = new ArrayList<>();
        List value = new ArrayList();
        List<String> op = new ArrayList<>();//操作运算符
        for (int i=0;i<split2.length;i++){
            split2[i] = split2[i].trim();
            String[] split3;
            if (split2[i].indexOf(">=")!=-1){
                split3 = split2[i].split("(?=>=)|(?<=>=)");
            }else if (split2[i].indexOf("<=")!=-1){
                split3 = split2[i].split("(?=<=)|(?<=<=)");
            }else if (split2[i].indexOf("!=")!=-1){
                split3 = split2[i].split("(?=!=)|(?<=!=)");
            }else {
                split3 = split2[i].split("(?="+whereOp+")|(?<="+whereOp+")");//[NAME][=] ["王二"]
            }
            if (reName!=null){
                if (split3[0].startsWith(reName)){
                    split3[0] = split3[0].substring(split3[0].indexOf(".")+1);
                }
            }
            key.add(split3[0].trim());
            split3[1] = split3[1].trim();
            op.add(split3[1]);
            split3[2] = split3[2].replace("\"","");
            value.add(split3[2].trim());
        }
        if (!attribute.containsAll(key)){
            System.out.println(log+"查询属性不存在");
            return index;
        }
        List<Column> columns = table.getColumns();
        if (columns.size()>0&&columns.get(0)!=null){
            for (int i=0;i<columns.get(0).getColValue().size();i++){
                int k = 0;
                for (int j=0;j<key.size();j++){
                    Column column = columns.get(attribute.indexOf(key.get(j)));
                    if (column.getType().equals("string")){
                        if (!op.get(j).equals("=")&&!op.get(j).equals("!=")){
                            System.out.println(log+"字符串不支持大小比较");
                            return index;
                        }
                        if (whereOperate(op.get(j),column.getColValue().get(i),value.get(j),"string")){
                            k++;
                        }
                    }else if (column.getType().equals("int")){
                        try {
                            int value1 = Integer.valueOf((String) value.get(j));
                            if (whereOperate(op.get(j),Integer.parseInt((String) column.getColValue().get(i)),value1,"int")){
                                k++;
                            }
                        }catch (NumberFormatException e){
                            System.out.println(log+"查询属性类型不对");
                            e.printStackTrace();
                            return index;
                        }
                    }else if (column.getType().equals("float")){
                        try {
                            float value1 = Float.valueOf((String) value.get(j));
                            if (whereOperate(op.get(j),Float.valueOf((String)column.getColValue().get(i)),value1,"float")){
                                k++;
                            }
                        }catch (NumberFormatException e){
                            System.out.println(log+"查询属性类型不对");
                            e.printStackTrace();
                            return index;
                        }
                    }
                }
                if (k == key.size()){
                    index.add(i);
                }
            }
        }
        return index;
    }
    //where操作运算符判定
    public boolean whereOperate(String op,Object o2,Object v,String type){
        boolean flag = false;//条件是否符合
        switch (op){
            case ">=":{
                if ("int".equals(type)){
                    int value = (int) v;
                    int o = (int) o2;//属性值
                    if (o>=value){
                        flag = true;
                    }else flag = false;
                }else if ("float".equals(type)){
                    float value = (float) v;
                    float o = (float) o2;
                    if (o>=value)
                        flag = true;
                    else
                        flag = false;
                }
            }
            break;
            case "<=":{
                if ("int".equals(type)){
                    int value = (int) v;
                    int o = (int) o2;//属性值
                    if (o<=value){
                        flag = true;
                    }else flag = false;
                }else if ("float".equals(type)){
                    float value = (float) v;
                    float o = (float) o2;
                    if (o<=value)
                        flag = true;
                    else
                        flag = false;
                }
            }
            break;
            case ">":{
                if ("int".equals(type)){
                    int value = (int) v;
                    int o = (int) o2;//属性值
                    if (o>value){
                        flag = true;
                    }else flag = false;
                }else if ("float".equals(type)){
                    float value = (float) v;
                    float o = (float) o2;
                    if (o>value)
                        flag = true;
                    else
                        flag = false;
                }
            }
            break;
            case "<":{
                if ("int".equals(type)){
                    int value = (int) v;
                    int o = (int) o2;//属性值
                    if (o<value){
                        flag = true;
                    }else flag = false;
                }else if ("float".equals(type)){
                    float value = (float) v;
                    float o = (float) o2;
                    if (o<value)
                        flag = true;
                    else
                        flag = false;
                }
            }
            break;
            case "=":{
                if ("string".equals(type)){
                    String value = (String) v;
                    String o = (String) o2;
                    if (o.equals(value))
                        flag = true;
                    else
                        flag = false;
                }
                else if ("int".equals(type)){
                    int value = (int) v;
                    int o = (int) o2;//属性值
                    if (o>=value){
                        flag = true;
                    }else flag = false;
                }else if ("float".equals(type)){
                    float value = (float) v;
                    float o = (float) o2;
                    if (o>=value)
                        flag = true;
                    else
                        flag = false;
                }
            }
            break;
            case "!=":{
                if ("string".equals(type)){
                    String value = (String) v;
                    String o = (String) o2;
                    if (!o.equals(value))
                        flag = true;
                    else
                        flag = false;
                }
                else if ("int".equals(type)){
                    int value = (int) v;
                    int o = (int) o2;//属性值
                    if (o!=value){
                        flag = true;
                    }else flag = false;
                }else if ("float".equals(type)){
                    float value = (float) v;
                    float o = (float) o2;
                    if (o!=value)
                        flag = true;
                    else
                        flag = false;
                }
            }
            break;
        }
        return flag;
    }
    //修改所有元组指定属性的值
    public void updateAll(String command){
        //update employee set salary=3000 , dno="d1"
        String[] split = command.split("set");//[update employee][salary=3000 , dno="d1"]
        split[0] = split[0].trim();
        String[] split1 = split[0].split("\\s+");//[update][ employee]
        String tableName = split1[1].trim();
        if (!isExitsTable(tableName)){
            System.out.println("修改数据失败："+tableName+"不存在");
            return;
        }
        Table table = ReadAndWrite.formTable(tableName);
        ReadAndWrite.exportTableValue(table);

        List<Integer> index = new ArrayList<>();
        if (table.getColumns().size()>0){
            for (int k=0;k<table.getColumns().get(0).getColValue().size();k++){
                index.add(k);
            }
        }
        if (index.size()==0){
            return;
        }
        boolean typeRight = isTypeRight(split[1].trim(), table, index);
        if (typeRight){
            ReadAndWrite.writeTableTxt(table);
            System.out.println("修改数据成功，共"+index.size()+"行受到影响");
        }

    }
    //判断修改的属性值是否符合属性类型，如果符合则修改table
    public boolean isTypeRight(String split,Table table,List<Integer> index){
        List<String> attribute = table.getAttribute();
        List<Column> columns = table.getColumns();
        split = split.trim();
        String[] split2 = split.split(",");////[name="王二"][sex="女" ]
        List<String> key = new ArrayList<>();
        List value = new ArrayList();
        for (int i=0;i<split2.length;i++){
            split2[i] = split2[i].trim();
            String[] split3 = split2[i].split("=");//[name]["王二"]
            if (split3[0].trim().equals(table.getPrimaryKey())){
                System.out.println("修改数据失败：主键属性不允许修改");
                return false;
            }
            key.add(split3[0].trim());
            split3[1] = split3[1].replace("\"","");
            value.add(split3[1].trim());
        }
        if (!attribute.containsAll(key)){
            System.out.println("修改数据失败：属性不存在");
            return false;
        }

        for (int i=0;i<key.size();i++){
            Column column = columns.get(attribute.indexOf(key.get(i)));

            switch (column.getType()){
                case "string":
                    column.setColValue(index,value.get(i));
                    break;
                case "int":{
                    try {
                        int value1 = Integer.valueOf((String) value.get(i));
                        column.setColValue(index,value1);
                    }catch (NumberFormatException e){
                        System.out.println("修改数据失败：查询属性类型不对");
                        e.printStackTrace();
                        return false;
                    }
                    break;
                }
                case "float":{
                    try {
                        float value1 = Float.valueOf((String) value.get(i));
                        column.setColValue(index,value1);
                    }catch (NumberFormatException e){
                        System.out.println("修改数据失败：查询属性类型不对");
                        e.printStackTrace();
                        return false;
                    }
                    break;
                }
            }
        }
        return true;
    }
    //修改满足where条件的元组的指定属性值
    public void updateWhere(String command){
        //update employee set name="王二",sex="女" where salary=3000 and dno = "d1"
        String[] split = command.split("set");//[update employee][name="王二",sex="女" where salary=3000 and dno = "d1"]
        split[0] = split[0].trim();
        String[] split1 = split[0].split("\\s+");//[update][ employee]
        String tableName = split1[1].trim();
        if (!isExitsTable(tableName)){
            System.out.println("修改数据失败："+tableName+"不存在");
            return;
        }
        Table table = ReadAndWrite.formTable(tableName);
        ReadAndWrite.exportTableValue(table);
        split[1]= split[1].trim();
        String[] split2 = split[1].split("where");//[name="王二",sex="女" ][ salary=3000 and dno = "d1"]
        List<Integer> index = findWhere(table, "修改数据失败", split2[1].trim(),null);
        if (index.size()==0){
            return;
        }
        boolean typeRight = isTypeRight(split2[0].trim(), table, index);
        if (typeRight){
            ReadAndWrite.writeTableTxt(table);
            System.out.println("修改数据成功，共"+index.size()+"行受到影响");
        }
    }
    //单关系投影操作
    public void selectProjection(String command){
        //select e.ssn,e.name,dno,ssn from employee e
        Map<String, String> infoMap = projectionSplit(command);
        String tableName = infoMap.get("tableName");
        String reName = infoMap.get("reName");
        String proAttrs = infoMap.get("proAttrs");
        if (!isExitsTable(tableName)){
            System.out.println("查找失败，"+tableName+"不存在");
            return;
        }
        Table table = ReadAndWrite.formTable(tableName);
        ReadAndWrite.exportTableValue(table);
        List<Integer> attributeIdList = projection(proAttrs,reName,table.getAttribute());
        if (attributeIdList != null){
            table.showTableColumn(attributeIdList);
        }
    }
    /**
     * 分割投影查询语句
     * @param command select e.ssn,e.name,pno,essn from employee e
     * @return 获得表名，表重命名，属性
     */
    public Map<String,String> projectionSplit(String command){
        //select e.ssn,e.name,pno,essn from employee e
        String[] split = command.split("from");//[select e.ssn,e.name,pno,essn ][ employee e]
        String tableInfo = split[1].trim();//[employee e]
        String[] split1 = tableInfo.split("\\s+");//[employee][e]
        String tableName = split1[0].trim();
        String reName = split1.length==2?split1[1].trim():null;
        String proAttrs = split[0].substring(split[0].indexOf("select")+7).trim();//[e.ssn,e.name,pno,essn];
        Map<String,String> infoMap= new HashMap<>();
        infoMap.put("tableName",tableName);
        infoMap.put("reName",reName);
        infoMap.put("proAttrs",proAttrs);
        return infoMap;
    }
    /**
     * 进行投影操作
     * @param attribute 需要投影的属性
     * @param reName 表的重命名
     * @param attributeList 表的所有属性列表
     * @return 返回需要投影的属性的列标
     */
    public List<Integer> projection(String attribute,String reName,List<String> attributeList){
        String[] attributes = attribute.split(",");//[e.ssn][e.name][pno][essn]
        List<Integer> attributeIdList = new ArrayList<>();
        for (int i=0;i<attributes.length;i++){
            attributes[i] = attributes[i].trim();
            if (attributes[i].contains(".")){
                if (attributes[i].startsWith(reName)){
                    attributes[i] = attributes[i].substring(attributes[i].indexOf(".")+1);
                }else {
                    System.out.println("查找失败，请检查"+attributes[i]);
                    return null;
                }
            }
            int attributeId = attributeList.indexOf(attributes[i]);
            if (attributeId == -1){
                System.out.println("查找失败，"+attributes[i]+"属性不存在");
                return null;
            }
            attributeIdList.add(attributeId);
        }
        return attributeIdList;
    }
    //单关系选择操作
    public void selectWhere(String command){
        //select * from employee e where e.sex="男" and salary >3000
        String[] split = command.split("where");//[select * from employee][ sex="男" and salary >3000]
        String[] split1 = split[0].trim().split("\\s+");//[select][ *][ from ][employee][e]
        String tableName = split1[3].trim();
        if (!isExitsTable(tableName)){
            System.out.println("查找失败，"+tableName+"表不存在");
            return;
        }
        String reName = split1.length==5?split1[4].trim():null;
        Table table = ReadAndWrite.formTable(tableName);
        ReadAndWrite.exportTableValue(table);
        //找出满足条件的行
        List<Integer> list = findWhere(table, "查找失败", split[1].trim(),reName);
        table.showTableLine(list);
    }
    //单关系选择投影操作
    public void selectPAWhere(String command){
        //select e.ssn,e.name,dno,ssn from employee e where e.sex="男" and salary >3000
        String[] split = command.split("where");//[select e.ssn,e.name,dno,essn from employee e ][ e.sex="男" and salary >3000]
        Map<String, String> infoMap = projectionSplit(split[0].trim());
        String tableName = infoMap.get("tableName");
        String reName = infoMap.get("reName");
        String proAttrs = infoMap.get("proAttrs");
        if (!isExitsTable(tableName)){
            System.out.println("查找失败，"+tableName+"不存在");
            return;
        }
        Table table = ReadAndWrite.formTable(tableName);
        ReadAndWrite.exportTableValue(table);
        //先找出满足条件的行
        List<Integer> lineList = findWhere(table, "查找失败", split[1].trim(),reName);
        if (lineList.size()==0){
            System.out.println("查找失败，符合要求的行为0");
            return;
        }
        //找出需要投影的列标
        List<Integer> attributeIdList = projection(proAttrs,reName,table.getAttribute());
        if (attributeIdList != null && lineList.size()>0){
            table.showLineAndColumn(lineList,attributeIdList);
        }
    }
    //两个关系和多个关系的连接操作
    public void selectLink(String command){

    }
    public boolean isExitsTable (String tableName){
        tableName = tableName.trim();
        File file = new File(ROOT+tableName+suffix);
        if (!file.isDirectory()&&file.exists()){
            return true;
        }
        return false;
    }
}
