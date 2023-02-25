package io.thinker.cmposintegration

import android.util.Log
import com.cm.androidposintegration.beans.*
import com.cm.androidposintegration.enums.PreAuthFinishType
import com.cm.androidposintegration.enums.TransactionType
import com.cm.androidposintegration.initializer.AndroidPosIntegration
import com.cm.androidposintegration.service.callback.ReceiptCallback
import com.cm.androidposintegration.service.callback.StatusesCallback
import com.cm.androidposintegration.service.callback.TerminalInfoCallback
import com.cm.androidposintegration.service.callback.TransactionCallback
import com.cm.androidposintegration.service.callback.beans.*
import com.google.gson.Gson
import org.apache.cordova.CallbackContext
import java.math.BigDecimal
import java.text.SimpleDateFormat;  
import java.util.*

public class AndroidPosIntegrationAdapter {

    fun doTransaction(
        transactionType: TransactionType,
        amount: BigDecimal,
        currency: Currency,
        orderReference: String,
        refundStan: String?,
        refundDate: String?,
        callbackContext: CallbackContext
    ) {

        val transactionCallback: TransactionCallback = object : TransactionCallback {
            override fun onCrash() {
                Log.d("CM__DO_TRANSACTION__ON_CRASH", "app crashed")
                callbackContext.error("APP_CRASHED");

            }

            override fun onError(error: ErrorCode) {
                Log.d("CM__DO_TRANSACTION__ON_ERROR", error.toString())
                callbackContext.error(error.toString());

            }

            override fun onResult(data: TransactionResultData) {
                Log.d("CM__DO_TRANSACTION__ON_RESULT", data.toString())
                // data.merchantReceipt.receiptLines
                // data.customerReceipt.receiptLines
                callbackContext.success(Gson().toJson(data));

            }
        }

        val data = TransactionData(
            transactionType,
            amount,
            currency,
            orderReference
        )

        if(refundStan != null && refundStan.length > 0) data.refundStan = refundStan // Can be found on the receipt

        if(refundDate != null && refundDate.length > 0) {
            val dateFormat = SimpleDateFormat("dd/MM/yy")
            data.refundDate = dateFormat.parse(refundDate) // Can be found on the receipt
        }

        val integrationService = AndroidPosIntegration.getInstance()
        integrationService?.doTransaction(data, transactionCallback)
    }

    fun transactionStatuses(
        orderReference: String,
        page: Int,
        size: Int,
        sortField: String?,
        sortValue: String?,
        callbackContext: CallbackContext
    ) {
        val callback : StatusesCallback = object : StatusesCallback {
            override fun onCrash() {
                Log.d("CM__TRANSACTION_STATUSES__ON_CRASH", "app crashed")
                callbackContext.error("APP_CRASHED");

            }

            override fun onError(error: ErrorCode) {
                Log.d("CM__TRANSACTION_STATUSES__ON_ERROR", error.toString())
                callbackContext.error(error.toString());
            }

            override fun onResult(data: TransactionStatusesData) {
                Log.d("CM__TRANSACTION_STATUSES__ON_RESULT", data.toString())
                callbackContext.success(Gson().toJson(data))
            }

        }

        val data = RequestStatusData(
            orderReference,
            page,
            size,
            sortField,
            sortValue
        )

        val integrationService = AndroidPosIntegration.getInstance()
        integrationService?.transactionStatuses(data, callback)
    }

    fun getLastReceipt(
        isShowReceipt: Boolean,
        callbackContext: CallbackContext
    ) {
        val callback : ReceiptCallback = object : ReceiptCallback {
            override fun onCrash() {
                Log.d("CM__GET_LAST_RECEIPT__ON_CRASH", "app crashed")
                callbackContext.error("APP_CRASHED")

            }

            override fun onError(error: ErrorCode) {
                Log.d("CM__GET_LAST_RECEIPT__ON_ERROR", error.toString())
                callbackContext.error(error.toString())
            }

            override fun onResult(data: LastReceiptResultData) {
                Log.d("CM__GET_LAST_RECEIPT__ON_RESULT", data.toString())
                callbackContext.success(Gson().toJson(data));
            }
        }

        val options = LastReceiptOptions(isShowReceipt)
        val integrationService = AndroidPosIntegration.getInstance()
        integrationService?.getLastReceipt(options, callback)
    }

    fun getTerminalDayTotals(
        isShowReceipt: Boolean,
        callbackContext: CallbackContext
    ) {
        val callback : ReceiptCallback = object : ReceiptCallback {
            override fun onCrash() {
                Log.d("CM__GET_TERMINAL_DAY_TOTALS__ON_CRASH", "app crashed")
                callbackContext.error("APP_CRASHED")

            }

            override fun onError(error: ErrorCode) {
                Log.d("CM__GET_TERMINAL_DAY_TOTALS__ON_ERROR", error.toString())
                callbackContext.error(error.toString())
            }

            override fun onResult(data: LastReceiptResultData) {
                Log.d("CM__GET_TERMINAL_DAY_TOTALS__ON_RESULT", data.toString())
                callbackContext.success(Gson().toJson(data));
            }


        }

        val options = DayTotalsOptions(isShowReceipt)
        val integrationService = AndroidPosIntegration.getInstance()
        integrationService?.getTerminalDayTotals(options, callback)
    }

    fun getTerminalInfo(
        callbackContext: CallbackContext
    ) {
        val callback : TerminalInfoCallback = object : TerminalInfoCallback {
            override fun onCrash() {
                Log.d("CM__GET_TERMINAL_INFO__ON_CRASH", "app crashed")
                callbackContext.error("APP_CRASHED")

            }

            override fun onError(error: ErrorCode) {
                Log.d("CM__GET_TERMINAL_INFO__ON_ERROR", error.toString())
                callbackContext.error(error.toString())
            }

            override fun onResult(data: TerminalInfoData) {
                Log.d("CM__GET_TERMINAL_INFO__ON_RESULT", data.toString())
                callbackContext.success(Gson().toJson(data));
            }


        }

        val integrationService = AndroidPosIntegration.getInstance()
        integrationService?.getTerminalInfo(callback)
    }

    fun finishPreAuth(
        type: PreAuthFinishType,
        originalStan: String,
        originalDate: Date,
        orderRef: String,
        callbackContext: CallbackContext
    ) {
        val callback : TransactionCallback = object : TransactionCallback {
            override fun onCrash() {
                Log.d("CM__FINISH_PRE_AUTH__ON_CRASH", "app crashed")
                callbackContext.error("APP_CRASHED")

            }

            override fun onError(error: ErrorCode) {
                Log.d("CM__FINISH_PRE_AUTH__ON_ERROR", error.toString())
                callbackContext.error(error.toString())
            }

            override fun onResult(data: TransactionResultData) {
                Log.d("CM__FINISH_PRE_AUTH__ON_RESULT", data.toString())
                callbackContext.success(Gson().toJson(data));
            }


        }

        val data = PreAuthFinishData(
            type,
            originalStan,
            originalDate,
            orderRef
        )

        val integrationService = AndroidPosIntegration.getInstance()
        integrationService?.finishPreAuth(data, callback)
    }

}