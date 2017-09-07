
/**
Android提供了一个名为SQLiteDatabase的类，该类封装了一些操作数据库的API，
 使用该类可以完成对数据进行添加(Create)、查询(Retrieve)、更新(Update)和删除(Delete)操作（这些操作简称为CRUD）。
 对SQLiteDatabase的学习，我们应该重点掌握execSQL()和rawQuery()方法。
 execSQL()方法可以执行insert、delete、update和CREATE TABLE之类有更改行为的SQL语句；
 rawQuery()方法用于执行select语句。
execSQL()方法的使用例子：
SQLiteDatabase db = ....;
db.execSQL("insert into person(name, age) values('测试数据', 4)");
db.close();
执行上面SQL语句会往person表中添加进一条记录，在实际应用中，
 语句中的“测试数据”这些参数值会由用户输入界面提供，如果把用户输入的内容原样组拼到上面的insert语句，
 当用户输入的内容含有单引号时，组拼出来的SQL语句就会存在语法错误。要解决这个问题需要对单引号进行转义，
 也就是把单引号转换成两个单引号。有些时候用户往往还会输入像“ & ”这些特殊SQL符号，
 为保证组拼好的SQL语句语法正确，必须对SQL语句中的这些特殊SQL符号都进行转义，显然，
 对每条SQL语句都做这样的处理工作是比较烦琐的。
 SQLiteDatabase类提供了一个重载后的execSQL(String sql, Object[] bindArgs)方法，
 使用这个方法可以解决前面提到的问题，因为这个方法支持使用占位符参数(?)。使用例子如下：
SQLiteDatabase db = ....;
db.execSQL("insert into person(name, age) values(?,?)", new Object[]{"测试数据", 4});
db.close();
execSQL(String sql, Object[] bindArgs)方法的第一个参数为SQL语句，
 第二个参数为SQL语句中占位符参数的值，参数值在数组中的顺序要和占位符的位置对应。
*/

package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static data.petContract.petEntry;
/**
 * Created by YI on 2017/8/29.
 */

public class petSQLite extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
//创建表格
    private  String SQL_CREATE_TABLE = "CREATE TABLE " + petEntry.TABLE_NAME //列表名称
            + "("
            + petEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," //id
            + petEntry.PET_NAME + TEXT_TYPE + COMMA_SEP //姓名  COMMA_SEP : ,
            + petEntry.PET_BREED + TEXT_TYPE + COMMA_SEP//品种
            + petEntry.PET_GENDER + " INTEGER" + COMMA_SEP//性别
            + petEntry.PET_WEIGHT + " INTEGER" //体重
            + ")";
//删除表格
    private static final String SQL_DELET_TABLE = "DROP TABLE IF EXISTS " + petEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "petTable.db";
//构造函数
    public petSQLite(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
        Log.d("petSQLite","create table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL(SQL_DELET_TABLE);
//        onCreate(sqLiteDatabase);
//        Log.d("petSQLite","update");
    }
}
