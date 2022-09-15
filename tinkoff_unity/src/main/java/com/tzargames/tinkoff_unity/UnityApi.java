package com.tzargames.tinkoff_unity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Field;

import ru.tinkoff.acquiring.sdk.AcquiringSdk;
import ru.tinkoff.acquiring.sdk.TinkoffAcquiring;
import ru.tinkoff.acquiring.sdk.models.enums.CheckType;
import ru.tinkoff.acquiring.sdk.models.options.CustomerOptions;
import ru.tinkoff.acquiring.sdk.models.options.FeaturesOptions;
import ru.tinkoff.acquiring.sdk.models.options.OrderOptions;
import ru.tinkoff.acquiring.sdk.models.options.screen.PaymentOptions;
import ru.tinkoff.acquiring.sdk.utils.Money;
import ru.tinkoff.acquiring.sdk.utils.SampleAcquiringTokenGenerator;
import ru.tinkoff.cardio.CameraCardIOScanner;

class TinkoffPaymentParameters
{
    public String TerminalKey;
    public String PublicKey;
    public String Password;
    public String OrderId;
    public String CustomerKey = "CUSTOMER_KEY";
    public String ClientEmail;
    public int MoneyAmount;
    public String Title;
    public String Description;
    public boolean IsDevMode;
    public boolean IsDebugMode;
    public boolean EnableFps;
}

public class UnityApi
{
    public static final String TAG = "Tinkoff_Unity";
    public static final String PaymentParametersKeyName = "PaymentParameters";
    public static final int PAYMENT_REQUEST_CODE = 1;

    public static final void StartPaymentProcess(String paymentParamsJson)
    {
        try
        {
            Class<?> unityPlayerClass = Class.forName("com.unity3d.player.UnityPlayer");
            Field activityField = unityPlayerClass.getField("currentActivity");
            Activity mainActivity = (Activity) activityField.get(null);

            Intent intent = new Intent(mainActivity, TinkoffPaymentActivity.class);

            intent.putExtra(PaymentParametersKeyName, paymentParamsJson);
            mainActivity.startActivity(intent);
        }
        catch (Exception exception)
        {
            Log.e(TAG, exception.getLocalizedMessage());
        }
    }

    public static final void OpenPaymentScreen(Activity activity, String paymentParamsJson)
    {
        try
        {
            Log.i(TAG, "Trying to parse payment parameters");
            Gson gson = new Gson();
            TinkoffPaymentParameters params = gson.fromJson(paymentParamsJson, TinkoffPaymentParameters.class);
            Log.i(TAG, params.Description);

            Log.i(TAG, "Creating payment options");
            PaymentOptions options = new PaymentOptions();

            OrderOptions orderOptions = new OrderOptions();
            orderOptions.orderId = params.OrderId;                // ID заказа в вашей системе
            orderOptions.amount = Money.ofRubles(params.MoneyAmount);       // сумма для оплаты
            orderOptions.setTitle(params.Title);          // название платежа, видимое пользователю
            orderOptions.setDescription(params.Description);    // описание платежа, видимое пользователю
            orderOptions.setRecurrentPayment(false);            // флаг определяющий является ли платеж рекуррентным [1]
            options.order = orderOptions;

            CustomerOptions customerOptions = new CustomerOptions();
            customerOptions.setCheckType(CheckType.NO.toString()); // тип привязки карты
            String customerKey = params.CustomerKey == null || params.CustomerKey.isEmpty() ? null : params.CustomerKey;
            customerOptions.setCustomerKey(customerKey);        // уникальный ID пользователя для сохранения данных его карты
            customerOptions.setEmail(params.ClientEmail);          // E-mail клиента для отправки уведомления об оплате
            options.setCustomer(customerOptions);

            FeaturesOptions featuresOptions = new FeaturesOptions();
            featuresOptions.setUseSecureKeyboard(true);            // флаг использования безопасной клавиатуры [2]
            featuresOptions.setCameraCardScanner(new CameraCardIOScanner());
            featuresOptions.setFpsEnabled(params.EnableFps);
            //featuresOptions.setTheme(themeId);


            Log.i(TAG, "Setting dev mode parameters, dev: " + params.IsDevMode + ", debug: " + params.IsDebugMode);
            AcquiringSdk.isDeveloperMode = params.IsDevMode;
            AcquiringSdk.isDebug = params.IsDebugMode;

            if(params.Password != null && params.Password.isEmpty() == false)
            {
                Log.i(TAG, "Setting token generator");
                AcquiringSdk.tokenGenerator = new SampleAcquiringTokenGenerator(params.Password);
            }

            TinkoffAcquiring tinkoffAcquiring = new TinkoffAcquiring(activity.getApplicationContext(), params.TerminalKey, params.PublicKey);


            Log.i(TAG, "Trying top open payment screen");
            tinkoffAcquiring.openPaymentScreen(activity, options, PAYMENT_REQUEST_CODE);
        }
        catch (Exception ex)
        {
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }
}
