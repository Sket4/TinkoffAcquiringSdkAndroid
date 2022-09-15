public TinkoffPaymentParameters Parameters;

    void Start()
    {
        TinkoffPaymentParameters.StartPaymentProcess(Parameters);
    }
}