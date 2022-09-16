using TzarGames.Tinkoff;
using UnityEngine;

public class Test : MonoBehaviour
{
    public TinkoffPaymentParameters Parameters;

    void Start()
    {
        TinkoffPaymentParameters.StartPaymentProcess(Parameters);
    }
}