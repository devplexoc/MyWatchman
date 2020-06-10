package com.plexoc.mywatchman.Utils;

public class Constants {


    public static final String DefaultErrorMessage = "Something Went Wrong. Please Contact Administrator";
    public static final Integer DefaultErrorCode = 1000;

    public static final int SuccessCode = 200;
    public static final int BadRequest = 400;
    public static final int INVALID = 401;
    public static final Integer NotFound = 404;
    public static final int InternalServerError = 500;
    public static final int To_Do_Id = 1;
    public static final int Need_Attention_Id = 2;
    public static final String LOCATION_DATA_UPLOAD_WORK_NAME = "location_data_work";
    public static final int TEXTNOTE = 1;
    public static final int IMAGEORVIDEONOTE = 2;

    public static final int Active = 1;
    public static final int Inactive = 0;
    public static final int Pending = 2;
    public static final int Complete = 3;

    public static boolean isRequestedStaff = false;

    public static String DeviceToken;
    public static boolean isFromNotification = false;


}
