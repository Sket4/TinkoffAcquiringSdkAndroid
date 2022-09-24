package com.tzargames.tinkoff_unity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import ru.tinkoff.acquiring.sdk.TinkoffAcquiring;
import ru.tinkoff.acquiring.sdk.exceptions.AcquiringApiException;
import ru.tinkoff.acquiring.sdk.responses.AcquiringResponse;

public class TinkoffPaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinkoff_payment);

        String paymentParametersJson = getIntent().getStringExtra(UnityApi.PaymentParametersKeyName);
        UnityApi.OpenPaymentScreen(this, paymentParametersJson);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        Log.d("", ">>> onActivityResult: " + requestCode + " " + resultCode + " " + intent);

        super.onActivityResult(requestCode, resultCode, intent);
        TinkoffPaymentResult result = new TinkoffPaymentResult();
        result.ResultCode = resultCode;

        switch (resultCode)
        {
            case Activity.RESULT_OK:
                Log.i(UnityApi.TAG, "Payment successful, payment id: " + intent.getLongExtra(TinkoffAcquiring.EXTRA_PAYMENT_ID, 0));
                break;

            case Activity.RESULT_CANCELED:
                Log.w(UnityApi.TAG, "Payment cancelled");
                break;

            case TinkoffAcquiring.RESULT_ERROR:
                Log.e(UnityApi.TAG, "Payment error: " + resultCode);
                Throwable extraError = (Throwable) intent.getSerializableExtra(TinkoffAcquiring.EXTRA_ERROR);

                String errorMessage = "Unknown error";

                if(extraError instanceof AcquiringApiException)
                {
                    AcquiringApiException apiException = (AcquiringApiException) extraError;
                    AcquiringResponse response = apiException.getResponse();
                    if(response != null)
                    {
                        result.ErrorCode = Integer.parseInt(response.getErrorCode());
                        result.ErrorMessage = response.getDetails();
                        errorMessage = response.getErrorCode() + ": " + response.getMessage() + ", Details: " + response.getDetails();
                    }
                }
                else if(extraError instanceof SecurityException)
                {
                    SecurityException securityException = (SecurityException) extraError;
                    errorMessage = securityException.getLocalizedMessage();
                    result.ErrorMessage = errorMessage;
                }

                Log.e(UnityApi.TAG, errorMessage);
                break;
        }

        try
        {
            Class<?> unityPlayerClass;
            unityPlayerClass = Class.forName("com.unity3d.player.UnityPlayer");
            Method[] methods = unityPlayerClass.getMethods();
            for (Method meth: methods)
            {
                //Log.i(UnityApi.TAG, "Method " + meth.getName());

                if(meth.getName() == "UnitySendMessage")
                {
//                    Class<?>[] parameterTypes = meth.getParameterTypes();
//
//                    for (Class<?> param: parameterTypes)
//                    {
//                        Log.i(UnityApi.TAG, param.getName());
//                    }

                    Gson gson = new Gson();
                    String serialized = gson.toJson(result);
                    meth.invoke(null, "TinkoffPaymentCallbackReceiver", "OnPaymentResult", serialized);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e(UnityApi.TAG, "Failed to call unity method");
        }

        finish();
    }
}