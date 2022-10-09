using System.Collections.Generic;

namespace TzarGames.Tinkoff
{
    [System.Serializable]
    public class AdditionalKeyValueData
    {
        public string Key;
        public string Value;
    }

    [System.Serializable]
    public class TinkoffApiRequest
    {
        public string TerminalKey;
        public string PublicKey;
    }

    [System.Serializable]
    public class TinkoffGetStateRequest : TinkoffApiRequest
    {
        public int PaymentId;
    }

    [System.Serializable]
    public class TinkoffStoreParameters 
    {
        public string TerminalKey;
        public string PublicKey;
        public string Password;
        public bool EnableFps;
        public bool IsDevMode;
        public bool IsDebugMode;
    }

    [System.Serializable]
    public class TinkoffPaymentParameters : TinkoffStoreParameters
    {
        public string OrderId;
        public string CustomerKey;
        public string ClientEmail;
        public int MoneyAmount;
        public string Title;
        public string Description;
        
        public List<AdditionalKeyValueData> AdditionalData = new List<AdditionalKeyValueData>();
    }
}


