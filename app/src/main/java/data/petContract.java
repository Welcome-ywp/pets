package data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

import java.net.URI;
import java.security.PublicKey;

/**
 * Created by YI on 2017/8/29.
 */

//final 不能被扩展 //不需要扩展或为此外部类实现任何内容
public final class petContract {

    /**
     * content provider
     */
    //由与 PetsProvider 关联的每一个 URI 共用
    public static final String CONTENT_AUTHORITY = "com.ywp.provider.pets";
    //URI
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //此常数存储位置将附加到基本内容 URI 的每个表的路径
    public static final String PATH_PETS = "pets";

    public static final class petEntry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);

        public static final String TABLE_NAME = "pets";

        public static final String _ID = BaseColumns._ID;
        public static final String PET_NAME = "name";
        public static final String PET_BREED = "breed";
        public static final String PET_GENDER = "gender";
        public static final String PET_WEIGHT = "weight";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

    }
}
