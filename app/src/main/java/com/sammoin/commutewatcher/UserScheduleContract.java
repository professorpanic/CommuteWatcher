package com.sammoin.commutewatcher;

import android.net.Uri;

import com.tjeannin.provigen.ProviGenBaseContract;
import com.tjeannin.provigen.annotation.Column;
import com.tjeannin.provigen.annotation.ContentUri;

/**
 * Created by DoctorMondo on 4/25/2016.
 */

public interface UserScheduleContract extends ProviGenBaseContract {


public static final String CONTENT_AUTHORITY = "com.sammoin.commutewatcher.userscheduleprovider";


    @Column(Column.Type.TEXT)
    public static final String USER_START_ADDRESS = "start_addy";

    @Column(Column.Type.TEXT)
    public static final String USER_END_ADDRESS = "end_addy";

    @Column(Column.Type.INTEGER)
    public static final String USER_WORKDAY="workday_num";

    @Column(Column.Type.REAL)
    public static final String USER_START_TIME="start_time_int";

    @Column(Column.Type.INTEGER)
    public static final String USER_ITEM_ACTIVE="active_one_or_zero";

    @ContentUri
    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY + "/userschedule" );




}