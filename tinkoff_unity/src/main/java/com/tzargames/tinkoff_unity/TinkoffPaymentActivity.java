package com.tzargames.tinkoff_unity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        switch (resultCode)
        {
            case Activity.RESULT_OK:
                Log.i(UnityApi.TAG, "SUCCESSA, payment id: " + intent.getStringExtra(TinkoffAcquiring.EXTRA_PAYMENT_ID));
                break;

            case Activity.RESULT_CANCELED:
                Log.w(UnityApi.TAG, "CANCELLA!");
                break;

            case TinkoffAcquiring.RESULT_ERROR:
                Log.e(UnityApi.TAG, "ERRIHA! " + resultCode);
                Throwable extraError = (Throwable) intent.getSerializableExtra(TinkoffAcquiring.EXTRA_ERROR);

                String errorMessage = "Unknown error";

                if(extraError instanceof AcquiringApiException)
                {
                    AcquiringApiException apiException = (AcquiringApiException) extraError;
                    AcquiringResponse response = apiException.getResponse();
                    if(response != null)
                    {
                        errorMessage = response.getErrorCode() + ": " + response.getMessage() + ", Details: " + response.getDetails();
                    }
                }
                else if(extraError instanceof SecurityException)
                {
                    SecurityException securityException = (SecurityException) extraError;
                    errorMessage = securityException.getLocalizedMessage();
                }

                Log.e(UnityApi.TAG, errorMessage);
                break;
        }

        finish();
    }
}