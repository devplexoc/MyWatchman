package com.plexoc.myapplication.Service;

import com.plexoc.myapplication.Model.Address;
import com.plexoc.myapplication.Model.ChangePassword;
import com.plexoc.myapplication.Model.EmergencyContact;
import com.plexoc.myapplication.Model.ListResponse;
import com.plexoc.myapplication.Model.Plan;
import com.plexoc.myapplication.Model.PlanDurationDiscount;
import com.plexoc.myapplication.Model.QuestionAnswer;
import com.plexoc.myapplication.Model.QuestionAnswerBody;
import com.plexoc.myapplication.Model.RaisedSOSUser;
import com.plexoc.myapplication.Model.Rating;
import com.plexoc.myapplication.Model.Response;
import com.plexoc.myapplication.Model.SosType;
import com.plexoc.myapplication.Model.TransactionHistory;
import com.plexoc.myapplication.Model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiClient {

    @POST("customer/CustomerUpsert")
    Call<Response<User>> SignUp(@Body User user);

    @GET("customer/LogIn")
    Call<Response<User>> Login(@Query("UserName") String UserName, @Query("password") String password);

    @POST("customer/AddCustomerAddress")
    Call<Response<Address>> AddCustomerAddress(@Body Address address);

    @GET("customer/CustomerAddressesByCustomerId")
    Call<ListResponse<Address>> getAddress(@Query("Id") int Id, @Query("Offset") int Offset, @Query("Limit") int Limit);

    @POST("customer/ForgotPassword")
    Call<Response<User>> Forgotpassword(@Query("Email") String Email);

    @POST("customer/EmergencyContactUpsert")
    Call<Response<EmergencyContact>> EmergencycontactAdd(@Body EmergencyContact emergencyContact);

    @GET("customer/EmergencyContactAll")
    Call<ListResponse<EmergencyContact>> getAllEmergencyContact(@Query("CustomerId") int CustomerId, @Query("Offset") int Offset, @Query("Limit") int Limit);

    @GET("customer/EmergencyContactAllActive")
    Call<ListResponse<EmergencyContact>> getAllActiveEmergencyContact(@Query("CustomerId") int CustomerId, @Query("Offset") int Offset, @Query("Limit") int Limit);

    @GET("plan/PlanSelectAll")
    Call<ListResponse<Plan>> getPlan(@Query("Offset") int Offset, @Query("Limit") int Limit);

    @GET("plan/CustomersVSTransactionSelectAllByCustomerId")
    Call<ListResponse<TransactionHistory>> getTransactionHistory(@Query("CustomerId") int UserId, @Query("Offset") int Offset, @Query("Limit") int Limit);

   /* @GET("plan/SOSSelectAllByUserId")
    Call<ListResponse<RaisedSOSUser>> getAllRaisedSOSByUserId(@Query("userid") int userid, @Query("Offset") int Offset, @Query("Limit") int Limit);*/

    @GET("plan/SOSByCustomerId")
    Call<Response<RaisedSOSUser>> getActiveSOSByUserId(@Query("CustomerId") int userid);

    @POST("user/UsersQuestionariesInsert")
    Call<Response<QuestionAnswer>> AddQuestionAnswer(@Query("CustomerId") int UserId, @Query("SOSId") int SOSId, @Query("Note") String Note, @Body List<QuestionAnswer> questionAnswers);

    @POST("plan/SOSUpsert")
    Call<Response<RaisedSOSUser>> RaisedSOSinsert(@Body RaisedSOSUser raisedSOSUser);

    @POST("plan/TransactionVSPlan")
    Call<Response<TransactionHistory>> PlanInsert(@Query("CustomerId") int CustomerId, @Query("PlanId") int PlanId, @Query("SettingId") int SettingId);

    @GET("customer/GetCustomerById/{customerId}")
    Call<Response<User>> getCustomerByid(@Path("customerId") int customerId);

    @POST("customer/UpdatePlanByCustomerId")
    Call<Response<User>> PlanCancel(@Query("CustomerId") int CustomerId);

    @GET("plan/SOSSelectAllByAddresIdAndCustomerId")
    Call<ListResponse<RaisedSOSUser>> getAddressWiseRaisedSOS(@Query("CustomerId") int UserId, @Query("AddressId") int AddressId, @Query("Offset") int Offset, @Query("Limit") int Limit);

    @POST("plan/SOSIsactiveORInactive")
    Call<Response<RaisedSOSUser>> CompleteSOS(@Query("SosId") int SosId, @Query("Status") int Status);

    @GET("customer/EmergencyContactSelectByEmail")
    Call<ListResponse<User>> getCommunityRequest(@Query("Offset") int Offset, @Query("Limit") int Limit, @Query("ContactPhone") String ContactPhone, @Query("ContactEmail") String ContactEmail);

    @GET("customer/CustomerSigupByMobileExistsGenerateOTP")
    Call<Response<User>> Checkuser(@Query("Mobile") String Mobile, @Query("Username") String Username, @Query("Email") String Email);

    @POST("customer/EmergencyContactUpdateStatus")
    Call<Response<EmergencyContact>> CommunityRequestAproveReject(@Query("id") int id, @Query("ApprovedStatus") boolean ApprovedStatus);

    @GET("plan/SettingSelectAll")
    Call<ListResponse<PlanDurationDiscount>> getPlandurationDiscount(@Query("Offset") int Offset, @Query("Limit") int Limit);

    @FormUrlEncoded
    @POST("customer/SubCustomerUpsert")
    Call<Response<User>> SubUserSignup(@Field("Id") int Id, @Field("ParentCustomer") int ParentCustomer, @Field("FirstName") String FirstName, @Field("LastName") String LastName, @Field("Mobile") String Mobile, @Field("Password") String Password);

    @GET("customer/SubCustomerAll")
    Call<ListResponse<User>> getAllSubUsers(@Query("CustomerId") int CustomerId, @Query("Offset") int Offset, @Query("Limit") int Limit, @Query("search") String search);

    @POST("customer/CustomerDelete")
    Call<Void> delete(@Query("CustomerId") int id);

    @GET("sOSType/SOSTypeAll")
    Call<ListResponse<SosType>> getSOSType(@Query("Offset") int Offset, @Query("Limit") int Limit);

    @GET("plan/GetSOSById/{SOSId}")
    Call<Response<RaisedSOSUser>> getSOS(@Path("SOSId") int SOSId);

    @GET("customer/UserChangePassword")
    Call<Response<ChangePassword>> getChangePassword(@Query("UserId") int UserId, @Query("Password") String Password, @Query("Type") int Type);

    @GET("customer/ResendOtp")
    Call<Response<User>> ResendOTP(@Query("Mobile") String Mobile, @Query("Otp") String Otp);

    @POST("emergencyContact/EmergencyContactDeleteByCustomerId")
    Call<Object> DeleteContact(@Query("CustomerId") int CustomerId);

    @POST("rating/RatingUpsert")
    Call<Response<Rating>> insertRating(@Body Rating rating);

}
