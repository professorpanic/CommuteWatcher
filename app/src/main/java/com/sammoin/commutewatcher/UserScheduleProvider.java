package com.sammoin.commutewatcher;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.tjeannin.provigen.ProviGenOpenHelper;
import com.tjeannin.provigen.ProviGenProvider;

/**
 * Created by DoctorMondo on 4/25/2016.
 */
public class UserScheduleProvider extends ProviGenProvider {

    private static Class[] contracts = new Class[]{UserScheduleContract.class};

    @Override
    public SQLiteOpenHelper openHelper(Context context) {
        return new ProviGenOpenHelper(getContext(), "userInfo", null, 1, contracts);
    }

    @Override
    public Class[] contractClasses() {
        return contracts;
    }

//    public static interface Schedule extends ProviGenBaseContract {
//
//        @Column(Column.Type.TEXT)
//        public static final String USER_START_ADDRESS = "string";
//
//        @Column(Column.Type.TEXT)
//        public static final String USER_END_ADDRESS = "string";
//
//        @Column(Column.Type.INTEGER)
//        public static final String USER_WORKDAY="int";
//
//        @Column(Column.Type.INTEGER)
//        public static final String USER_START_TIME="int";
//
//        @Column(Column.Type.INTEGER)
//        public static final String USER_ITEM_ACTIVE="int";
//
//        @ContentUri
//        public static final Uri CONTENT_URI = Uri.parse("content://com.sammoin.commuterwatcher/userschedule");
//    }


}