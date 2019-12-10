package net.nguyenthanhnam.dataextraction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetDataService {

    @FormUrlEncoded
    @POST("/parse")
    Call<List<Item>> parse(@Field("content") String content);
}
