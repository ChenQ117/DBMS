import utils.CommandUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @version v1.0
 * @ClassName: Main
 * @Description:
 * @Author: ChenQ
 * @Date: 2021/10/24 on 19:30
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in).useDelimiter(";");
        CommandUtils instance = CommandUtils.getInstance();
        while (true){
            String sql = sc.next();
            sql = sql.trim();
//            System.out.println("sql:"+sql);
            instance.execute(sql);
        }
    }
}
