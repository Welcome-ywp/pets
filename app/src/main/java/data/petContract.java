package data;

import android.provider.BaseColumns;

/**
 * Created by YI on 2017/8/29.
 */

//final 不能被扩展 //不需要扩展或为此外部类实现任何内容
public final class petContract {

    public static final class petEntry implements BaseColumns{
        public static final String TABLE_NAME = "pets";

        public static final String _ID = BaseColumns._ID;
        public static final String PET_NAME = "name";
        public static final String PET_BREED = "breed";
        public static final String PET_GENDER = "gender";
        public static final String PET_WEIGHT = "weight";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_FEMALE = 1;
        public static final int GENDER_MALE = 2;
    }
}
