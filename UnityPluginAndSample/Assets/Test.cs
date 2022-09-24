using TzarGames.Tinkoff;
using UnityEngine;

public class Test : MonoBehaviour
{
    public TinkoffPaymentParameters Parameters;

    void Start()
    {
        Api.StartPaymentProcess(Parameters);
    }

    public void Run()
    {
        Api.StartPaymentProcess(Parameters);
    }

    public void GetStatus()
    {
        Api.GetPaymentStatus(new TinkoffGetStateRequest
        {
            TerminalKey = Parameters.TerminalKey,
            PublicKey = Parameters.PublicKey,
            PaymentId = 1792296557,
        });
    }

    public void SetOrderId(string id)
    {
        Parameters.OrderId = id;
    }
}