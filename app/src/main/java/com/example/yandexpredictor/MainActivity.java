package com.example.yandexpredictor;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {

    private static String PREDICTOR_URI_JSON = "https://predictor.yandex.net/";
    private static String PREDICTOR_KEY = "pdct.1.1.20200428T133609Z.44038f0b8dff0584.f4583bbb8627fee7f48876ac419ea5f0bb602fc5";
    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.editText1);
        textView = (TextView) findViewById(R.id.textView1);
        // слушатель изменения текста
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                getReport();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
    }

    void getReport() {
        /*
            создание объекта класса Retrofit
            .baseUrl - url для запроса
            .addConverterFactory - метод конвертации с параметром
            В итоге мы получили объект Retrofit, содержащий базовый URL и
            способность преобразовывать JSON-данные с помощью указанного конвертера Gson.
        */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PREDICTOR_URI_JSON)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        /*
            Реализация интерфейса RestApi
            create() позволяет создавать объект без вызова анонимного класса (особенность Retrofit)
        */
        RestApi restApi = retrofit.create(RestApi.class);

        /*
            Создаем объект, который реализует конкретный запрос из интерфейса
            Call - класс Retrofit для формирования запроса
            Call — это класс, который знает, что такое десериализация и может работать
            с HTTP-запросами. Один экземпляр этого класса — это одна пара «запрос — ответ».
        */
        Call<Model> call = restApi.getWord(PREDICTOR_KEY, editText.getText().toString(), "en");

        /*
           Выпронение асинхронного запроса на сервер
           enqueue() - автоматически в отдельном потоке
           Callback - класс Retrofit для получения ответа
        */
        call.enqueue(new Callback<Model>() {
            /*
               В случае ответа
            */
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                if(response.code() == 200) {
                    String resultText = response.body().getText().get(0);
                    textView.setText(resultText);
                }
                //Log.e("RESPONSE CODE", Integer.toString(response.code()));
            }

            /*
               В случае потери соединения
            */
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Connection error", Toast.LENGTH_LONG).show();
            }
        });
    }
}