import dataStructment.Column;
import dataStructment.Table;
import javafx.scene.control.Tab;
import org.junit.Test;
import utils.ReadAndWrite;
import utils.TableUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version v1.0
 * @ClassName: splitTest
 * @Description: 测试类
 * @Author: ChenQ
 * @Date: 2021/10/24 on 14:13
 */
public class splitTest {
    private final String keyWord = "[a-zA-Z]+(\\w)*";//关键字
    private final String space_1 = "\\s+";//一个或多个空格
    private final String space_0 = "\\s*";//零个或多个空格
    private final String type = "(string|int|float)";//属性
    private final String type1 = "((string"+space_0+"\\("+space_0+"\\d+"+space_0+"\\))|int|float)";//属性
    private final String typeSize = "("+space_0+"\\("+space_0+"\\d+"+space_0+"\\))*";//属性大小
    private final String bind = "\\s+(primary key|not null)?";//约束条件
    private final String bind_1 = "(primary key|not null)";
    private final String keyValue = "(\"\\s*\\S+\\s*\"|\\d+(\\.\\d+)*)";//属性值为字符串或者整数
    private final String whereOp = "(>=|<=|!=|=|>|<)";//where查询条件
    private final Pattern create_table = Pattern.compile("create"+space_1+"table"+space_1+keyWord+space_0
            + "\\(("+space_0+keyWord+space_1+type+"("+bind+")*"+space_0+",)*"
            +space_0+keyWord+space_1+type+"("+bind+")*"+space_0+"\\)"+space_0);
    private final Pattern drop_table = Pattern.compile("drop"+space_1+"table"+space_1+keyWord+space_0);

    private final Pattern insert_line = Pattern.compile("insert"+space_1+"into"+space_1+keyWord+space_1
            +"values"+space_0+"\\("+"("+space_0+keyValue+space_0+","+")*"+space_0+keyValue+"\\)");
    private final Pattern selectAll = Pattern.compile("select"+space_1+"\\*"+space_1+"from"+space_1+keyWord);
    private final Pattern alter_table_add = Pattern.compile("alter"+space_1+"table"+space_1+keyWord
            +space_1+"(add"+space_1+keyWord+space_1+type+"("+bind+")*"+space_0+",)*"+space_0
            +"(add"+space_1+keyWord+space_1+type+"("+bind+")*"+space_0+")");
    private final Pattern alter_table_drop = Pattern.compile("alter"+space_1+"table"+space_1+keyWord
            +space_1+"(drop"+space_1+keyWord+space_0+",)*"+space_0
            +"(drop"+space_1+keyWord+space_0+")");
    private final Pattern delete_all = Pattern.compile("delete"+space_1+"from"+space_1+keyWord);
    private final Pattern delete_where = Pattern.compile("delete"+space_1+"from"+space_1+keyWord+space_1+"where"+space_1+keyWord+space_0
            +"="+space_0+keyValue+"("+space_1+"and"+space_1+keyWord+space_0
            +"="+space_0+keyValue+")*");
    private final Pattern update_all = Pattern.compile("update"+space_1+keyWord+space_1+"set"+space_1+keyWord
            +space_0+"="+space_0+keyValue+space_0+"(,"+space_0+keyWord+space_0+"="+space_0+keyValue+space_0+")*");
    private final Pattern update_where = Pattern.compile("update"+space_1+keyWord+space_1+"set"+space_1+keyWord
            +space_0+"="+space_0+keyValue+space_0+"(,"+space_0+keyWord+space_0+"="+space_0+keyValue+space_0+")*"
            +space_1+"where"+space_1+keyWord+space_0
            +"="+space_0+keyValue+"("+space_1+"and"+space_1+keyWord+space_0
            +"="+space_0+keyValue+")*");
    private final Pattern select_projection = Pattern.compile("select"+space_1+"("+keyWord+"\\.)?"+keyWord+"("+space_0+","+space_0
            +"("+keyWord+"\\.)?"+keyWord+")*"+space_1
            +"from"+space_1+keyWord+"("+space_1+keyWord+")?");//单表无条件属性投影
    private final Pattern select_where = Pattern.compile("select"+space_1+"\\*"+space_1
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
            +"where"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+"((("+keyWord+"\\.)?"+keyWord+")|"+keyValue+")"
            +"("+space_1+"and"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+"((("+keyWord+"\\.)?"+keyWord+")|"+keyValue+"))*"
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
            +"where"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+"((("+keyWord+"\\.)?"+keyWord+")|"+keyValue+")"
            +"("+space_1+"and"+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+whereOp+space_0+"((("+keyWord+"\\.)?"+keyWord+")|"+keyValue+"))*"
    );
    @Test
    public void test(){
        String s = "CREATE TABLE employee(\n" +
                "ssn CHAR PRIMARY KEY,\n" +
                ")";
        String[] split = s.split("\\(|\\)");
        System.out.println(split.length);
        for (String ss :split){
            System.out.println(ss);
        }
    }
    @Test
    public void test_2(){
        //select * from employee ,works_on where essn = ssn
        String s1 = "select * from employee e1,employee e2 where e1.ssn = e2.ssn";
        s1 =s1.toLowerCase();
        Pattern s2 =  Pattern.compile("("+space_1+"("+keyWord+"\\.)?"+keyWord+space_0+","+space_0+")*"
                + space_1+"(("+keyWord+"\\.)?"+keyWord+")");
        System.out.println(s1+" "+select_link.matcher(s1).matches());
        /*String[] split = s1.split(whereOp);
        for (int i=0;i<split.length;i++){
            System.out.println(i+": "+split[i]);
        }*/
    }
    @Test
    public void test3(){
        String s1 = "\"王二\"";
        s1 = s1.toLowerCase();
        s1 = s1.trim();
        String[] split = s1.split("\"");
        s1 = s1.replace("\"","");
        System.out.println(s1.length());
        System.out.println(s1);
    }
    @Test
    public void text_4(){
        String command = "update employee set name=\"王二\",sex=\"女\" where salary=3000 and dno = \"d1\"";
        String[] split = command.split("set|where");
        for (int i=0;i<split.length;i++){
            System.out.println("i:"+i+split[i]);
        }
    }
    @Test
    public void test_5(){
        Column column = new Column();
        column.addColValue(8,"null");
        List colValue = column.getColValue();
        for (Object o:colValue){
            System.out.println("oo:"+o);
        }
    }
    @Test
    public void test_6(){
        List<Integer> index = new ArrayList<>();
        List<String> colValue = new ArrayList();
        index.add(0);
        index.add(1);
        index.add(2);
        colValue.add("a");
        colValue.add("9");
        colValue.add("89");
        String  o = "ml";
        Iterator<String> iterator = colValue.iterator();

        /*for (Object o1:colValue){
            System.out.println(o1);
        }*/
    }

    @Test
    public void test_group(){
        String s = "CREATE TABLE department(\n" +
                "dNAME string NOT NULL,\n" +
                "dnumber string PRIMARY KEY,\n" +
                "mgrssn string NOT NULL,\n" +
                "mgrstartdate string NOT NULL\n" +
                ")";
        s = s.toLowerCase();
        String s1 = "CREATE TABLE department(\n" +
                "dNAME string(11) NOT NULL,\n" +
                "dnumber string(12) PRIMARY KEY,\n" +
                "mgrssn string(13) NOT NULL,\n" +
                "mgrstartdate string(14) NOT NULL\n" +
                ")";
        s1 = s1.toLowerCase();
        Pattern p = Pattern.compile("create"+space_1+"table"+space_1+keyWord+space_0
                + "\\((?<g1>("+space_0+keyWord+space_1+type1+"("+bind+")*"+space_0+",)*"
                +space_0+keyWord+space_1+type1+"("+bind+")*"+space_0+"\\))"+space_0);
        Matcher matcher = create_table.matcher(s1);
        System.out.println(s1+p.matcher(s1).matches());
        String s2 = s1.substring(s1.indexOf("(")+1,s1.lastIndexOf(")"));
        System.out.println("s2:"+s2);
        String g1 = matcher.group("g1");
        System.out.println(g1);
        /*for (int i=0;i<matcher.groupCount();i++){
            System.out.println(i+": "+matcher.group(i));
        }*/

//        System.out.println(matcher.groupCount());
    }
    @Test
    public void test_7(){
        String a= "abc";
        String[] split = a.split("\\(|\\)");
        for (int i=0;i<split.length;i++){
            System.out.println(i+split[i]);
        }
    }
    @Test
    public void test_8(){
        /*List<String > list = new LinkedList<>();
        list.add(1,"aa");
        list.add(3,"uu");
        list.add(5,"kk");
        System.out.println(list.size());*/
        // 创建一个数组
        ArrayList<String> sites = new ArrayList<>();

        // 在该数组末尾插入元素
        /*sites.add("Google");
        sites.add("Runoob");
        sites.add("Taobao");*/
        System.out.println("ArrayList: " + sites);

        // 在第一个位置插入元素
        sites.add(0, null);
        sites.add(1,"weibo");
        System.out.println("更新 ArrayList: " + sites);
    }
    @Test
    public void test_9(){
        List<Table> tableList = new ArrayList<>();
        Table table = ReadAndWrite.formTable("employee");
        ReadAndWrite.exportTableValue(table);
        tableList.add(table);
        Table table1 = tableList.get(0);
        table1.showTable();
        List<Integer> line = new ArrayList<>();
        line.add(1);
        line.add(3);
        table1.remainListAll(line);
        table1.showTable();
        System.out.println("------------------------------------");
        tableList.get(0).showTable();
//        System.out.println(table1 == tableList.get(0));
    }
    @Test
    public void test_10(){
        List list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        for (int i=0;i<list.size();i++){
            list.remove(i);
            list.remove(i+1);
        }
        System.out.println(list.size());
    }
    @Test
    public void test_11(){
        Table t1 = ReadAndWrite.formTable("employee");
        ReadAndWrite.exportTableValue(t1);
        Table t2 = ReadAndWrite.formTable("works_on");
        ReadAndWrite.exportTableValue(t2);
        TableUtils tableUtils = TableUtils.getTableUtils();
        Table table = tableUtils.infoNewTable(t1, t2);
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        table.combine(t1,t2,2,list);
        table.showTable();
    }
}
