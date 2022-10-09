using UnityEngine;

namespace TzarGames.Tinkoff
{
    [System.Serializable]
    public class TinkoffPaymentResult
    {
        public string PaymentId;
        public int ResultCode;
        public int ErrorCode;
        public string ErrorMessage;

        public bool IsSuccess()
        {
            return ResultCode == -1;
        }
        public bool IsCancelled()
        {
            return ResultCode == 0;
        }
        public bool IsFailed()
        {
            return !IsCancelled() && !IsSuccess();
        }
        public bool IsAlreadyPurchased()
        {
            return ErrorCode == 8;
        }
    }

    public class TinkoffPaymentCallbackReceiver : MonoBehaviour
    {
        public void OnPaymentResult(string serializedJsonResult)
        {
            Debug.Log($"Payment result {serializedJsonResult}");

            var result = JsonUtility.FromJson<TinkoffPaymentResult>(serializedJsonResult);

            Debug.Log($"Payment id: {result.PaymentId}");

            if(result.IsSuccess())
            {
                Debug.Log("Success");
            }
            else if(result.IsCancelled())
            {
                Debug.LogWarning("Cancelled");
            }
            else
            {
                Debug.LogError("Failed");

                if(result.IsAlreadyPurchased())
                {
                    Debug.LogError("Already purchased");
                }
            }

            Debug.Log($"Code: {result.ResultCode}, ErrorCode: {result.ErrorCode} Message: {result.ErrorMessage}");

            Api.NotifyPaymentResult(result);
        }
    }
}
