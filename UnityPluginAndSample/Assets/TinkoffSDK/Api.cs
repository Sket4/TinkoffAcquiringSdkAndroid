using UnityEngine;

namespace TzarGames.Tinkoff
{
    public static class Api
    {
        public static void StartPaymentProcess(TinkoffPaymentParameters parameters)
        {
            checkReceiver();

            AndroidJNIHelper.debug = parameters.IsDebugMode;
            using (AndroidJavaClass jc = new AndroidJavaClass("com.tzargames.tinkoff_unity.UnityApi"))
            {
                jc.CallStatic("StartPaymentProcess", JsonUtility.ToJson(parameters));
            }
        }

        public static void GetPaymentStatus(TinkoffGetStateRequest request)
        {
            checkReceiver();

            using (AndroidJavaClass jc = new AndroidJavaClass("com.tzargames.tinkoff_unity.UnityApi"))
            {
                jc.CallStatic("GetPaymentStatus", JsonUtility.ToJson(request));
            }
        }

        static void checkReceiver()
        {
            var receiver = GameObject.Find("TinkoffPaymentCallbackReceiver");
            if (receiver == null)
            {
                Debug.Log($"Creating {nameof(TinkoffPaymentCallbackReceiver)}");
                receiver = new GameObject("TinkoffPaymentCallbackReceiver");
                receiver.AddComponent<TinkoffPaymentCallbackReceiver>();
            }
        }
    }
}


