using TzarGames.Tinkoff;
using UnityEngine;

public class Test : MonoBehaviour
{
    public string PublicKey;

    public string TerminalKey;
    public string Password;
    public bool IsDevMode = false;
    public bool IsDebugMode = false;
    public bool EnableFPS = false;

    void Start()
    {
        var paymentParams = new TinkoffPaymentParameters
        {
            Title = "Все задания + созвездия",
            Description = "Разблокировка всех заданий и созвездий. Новые задания и созвездия будут бесплатными",
            ClientEmail = "",
            MoneyAmount = 10,
            OrderId = "COSMOSEA_UNLOCK_ALL",
            IsDebugMode = IsDebugMode,
            IsDevMode = IsDevMode,
            TerminalKey = TerminalKey,
            PublicKey = PublicKey,
            CustomerKey = "",
            Password = Password,
            EnableFps = EnableFPS
        };
        
        TinkoffPaymentParameters.StartPaymentProcess(paymentParams);
    }
}
