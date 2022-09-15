using UnityEngine;

namespace TzarGames.Tinkoff
{
    [System.Serializable]
    public class TinkoffPaymentParameters
    {
        public string TerminalKey;
        public string PublicKey;
        public string Password;
        public string OrderId;
        public string CustomerKey;
        public string ClientEmail;
        public int MoneyAmount;
        public string Title;
        public string Description;
        public bool IsDevMode;
        public bool IsDebugMode;
        public bool EnableFps;

        public static void StartPaymentProcess(TinkoffPaymentParameters parameters)
        {
            AndroidJNIHelper.debug = parameters.IsDebugMode;
            using (AndroidJavaClass jc = new AndroidJavaClass("com.tzargames.tinkoff_unity.UnityApi"))
            {
                jc.CallStatic("StartPaymentProcess", JsonUtility.ToJson(parameters));
            } 
        }
    }    
}
