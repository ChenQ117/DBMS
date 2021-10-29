import dataStructment.Column;
import org.junit.Test;

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
    private final String tableNamePath = "DATA/tableInfo.txt";
    private final String keyWord = "[a-zA-Z]+(\\w)*";//关键字
    private final String space_1 = "\\s+";//一个或多个空格
    private final String space_0 = "\\s*";//零个或多个空格
    private final String type = "(string|int|float)";//属性
    private final String bind = "\\s+(primary key|not null)?";//约束条件
    private final String keyValue = "(\"\\s*\\S+\\s*\"|\\d+(\\.\\d+)*)";//属性值为字符串或者整数
    private final Pattern create_table = Pattern.compile("create"+space_1+"table"+space_1+keyWord+space_0
            + "\\(("+space_0+keyWord+space_1+type+bind+space_0+",)*"
            +space_0+keyWord+space_1+type+bind+space_0+"\\)"+space_0);
    private final Pattern drop_table = Pattern.compile("drop"+space_1+"table"+space_1+keyWord+space_0);
    private final Pattern insert_line = Pattern.compile("insert"+space_1+"into"+space_1+keyWord+space_1
            +"values"+space_0+"\\("+"("+space_0+keyValue+space_0+","+")*"+space_0+keyValue+"\\)");
    private final Pattern selectAll = Pattern.compile("select"+space_1+"\\*"+space_1+"from"+space_1+keyWord);
    private final Pattern alter_table = Pattern.compile("alter"+space_1+"table"+space_1+keyWord
            +space_1+"(add"+space_1+keyWord+space_1+type+bind+space_0+",)*"+space_0
            +"(add"+space_1+keyWord+space_1+type+bind+space_0+")");
    private final Pattern alter_table_drop = Pattern.compile("alter"+space_1+"table"+space_1+keyWord
            +space_1+"(drop"+space_1+keyWord+space_0+",)*"+space_0
            +"(drop"+space_1+keyWord+space_0+")");
    private final Pattern delete_where = Pattern.compile("delete"+space_1+"from"+space_1+keyWord+space_1+"where"+space_1+keyWord+space_0
            +"="+space_0+keyValue+"("+space_1+"and"+space_1+keyWord+space_0
            +"="+space_0+keyValue+")*");
    private final Pattern updata_all = Pattern.compile("update"+space_1+keyWord+space_1+"set"+space_1+keyWord
            +space_0+"="+space_0+keyValue+space_0+"(,"+space_0+keyWord+space_0+"="+space_0+keyValue+space_0+")*");
    private final Pattern update_where = Pattern.compile("update"+space_1+keyWord+space_1+"set"+space_1+keyWord
            +space_0+"="+space_0+keyValue+space_0+"(,"+space_0+keyWord+space_0+"="+space_0+keyValue+space_0+")*"
            +space_1+"where"+space_1+keyWord+space_0
            +"="+space_0+keyValue+"("+space_1+"and"+space_1+keyWord+space_0
            +"="+space_0+keyValue+")*");
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
        String s1 = "(m int)";
        s1 =s1.toLowerCase();
        Pattern s2 =  Pattern.compile(keyWord+space_1+type+bind);
        System.out.println(s1+" "+s2.matcher(s1).matches());
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
}