using TzarGames.Tinkoff;
using UnityEngine;

public class Test : MonoBehaviour
{
    public TinkoffPaymentParameters Parameters;
    public bool RandomOrderId = false;

    void Start()
    {
        Api.StartPaymentProcess(Parameters);
    }

    public void Run()
    {
        if(RandomOrderId)
        {
            Parameters.OrderId = System.Guid.NewGuid().ToString();
        }
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