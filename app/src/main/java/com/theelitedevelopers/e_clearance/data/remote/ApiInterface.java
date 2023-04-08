package com.theelitedevelopers.e_clearance.data.remote;

import com.theelitedevelopers.e_clearance.data.models.dto.PayStackAuthorizationDto;
import com.theelitedevelopers.e_clearance.data.models.dto.PayStackRequest;

import java.util.Map;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    //Request for authorization URL from PayStack - Here, we also send the ticket details so after payment, the backend fetches them and verifies them.
    @POST("transaction/initialize")
    @Headers("Content-Type: application/json")
    Single<Response<PayStackAuthorizationDto>> fetchAuthorizationUrl(@HeaderMap Map<String, String> headers, @Body PayStackRequest payStackRequest);
}
