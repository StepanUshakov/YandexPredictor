package com.example.yandexpredictor;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi {
    //Анотация какой запрос и на какой путь на сервере отправляем
    @GET("api/v1/predict.json/complete")
    //Метод с запросом и типом возвращаемого ответа
    Call<Model> getWord(@Query("key") String key, @Query("q") String q, @Query("lang") String lang);
}