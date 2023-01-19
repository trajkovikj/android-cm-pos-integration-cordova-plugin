package io.thinker.cmposintegration

import android.content.Context
import org.apache.cordova.CordovaPlugin
import org.apache.cordova.CallbackContext
import org.json.JSONArray

import com.cm.androidposintegration.enums.PreAuthFinishType
import com.cm.androidposintegration.enums.TransactionType
import com.cm.androidposintegration.initializer.AndroidPosIntegration
import com.google.gson.Gson
import io.thinker.cmposintegration.models.*
import org.apache.cordova.CordovaInterface
import org.apache.cordova.CordovaWebView
import java.math.BigDecimal
import java.util.*

class CmPosIntegrationPluginImpl : CordovaPlugin() {

    override fun initialize(cordova: CordovaInterface?, webView: CordovaWebView?) {
        super.initialize(cordova, webView)

        val context: Context = this.cordova.activity.applicationContext
        AndroidPosIntegration.init(context);
    }

    override fun execute(
        action: String,
        args: JSONArray,
        callbackContext: CallbackContext
    ): Boolean {
        if (action == "doTransaction") {
            val message: String = args.getString(0)
            doTransaction(message, callbackContext)
            return true
        } else if (action == "transactionStatuses") {
            val message: String = args.getString(0)
            transactionStatuses(message, callbackContext)
            return true
        } else if (action == "getLastReceipt") {
            val message: String = args.getString(0)
            getLastReceipt(message, callbackContext)
            return true
        } else if (action == "getTerminalDayTotals") {
            val message: String = args.getString(0)
            getTerminalDayTotals(message, callbackContext)
            return true
        } else if (action == "getTerminalInfo") {
            getTerminalInfo(callbackContext)
            return true
        } else if (action == "finishPreAuth") {
            val message: String = args.getString(0)
            finishPreAuth(message, callbackContext)
            return true
        }

        return false
    }


    private fun doTransaction(
        requestString: String,
        callbackContext: CallbackContext
    ) {
        try {
            val request = Gson().fromJson<DoTransactionMessage>(
                requestString,
                DoTransactionMessage::class.java
            )

            var transactionType = TransactionType.PURCHASE
            if (request.transactionType.equals("PURCHASE", true)) transactionType =
                TransactionType.PURCHASE
            else if (request.transactionType.equals("REFUND", true)) transactionType =
                TransactionType.REFUND
            else if (request.transactionType.equals("PRE_AUTH", true)) transactionType =
                TransactionType.PRE_AUTH

            var amount = BigDecimal.valueOf(0)
            if (request.amount >= 0) amount = BigDecimal.valueOf(request.amount)

            var currency = Currency.getInstance("EUR")
            if (request.currency.length == 3) currency =
                Currency.getInstance(request.currency.uppercase())

            val adapter = AndroidPosIntegrationAdapter()
            adapter.doTransaction(
                transactionType,
                amount,
                currency,
                request.orderReference,
                request.refundStan,
                request.refundDate,
                callbackContext
            )
        } catch (e: java.lang.Exception) {
            callbackContext.error("INVALID_INPUT::$e");
        }

    }

    private fun transactionStatuses(
        requestString: String,
        callbackContext: CallbackContext
    ) {
        try {
            val request = Gson().fromJson<TransactionStatusesMessage>(
                requestString,
                TransactionStatusesMessage::class.java
            )

            val adapter = AndroidPosIntegrationAdapter()
            adapter.transactionStatuses(
                request.orderReference,
                request.page,
                request.size,
                request.sortField,
                request.sortValue,
                callbackContext
            )
        } catch (e: java.lang.Exception) {
            callbackContext.error("INVALID_INPUT::$e");
        }

    }

    private fun getLastReceipt(
        requestString: String,
        callbackContext: CallbackContext
    ) {
        try {
            val request = Gson().fromJson<GetLastReceiptMessage>(
                requestString,
                GetLastReceiptMessage::class.java
            )

            val adapter = AndroidPosIntegrationAdapter()
            adapter.getLastReceipt(
                request.isShowReceipt,
                callbackContext
            )
        } catch (e: java.lang.Exception) {
            callbackContext.error("INVALID_INPUT::$e");
        }
    }

    private fun getTerminalDayTotals(
        requestString: String,
        callbackContext: CallbackContext
    ) {
        try {
            val request = Gson().fromJson<GetTerminalDayTotalsMessage>(
                requestString,
                GetTerminalDayTotalsMessage::class.java
            )

            val adapter = AndroidPosIntegrationAdapter()
            adapter.getTerminalDayTotals(
                request.isShowReceipt,
                callbackContext
            )
        } catch (e: java.lang.Exception) {
            callbackContext.error("INVALID_INPUT::$e");
        }
    }

    private fun getTerminalInfo(
        callbackContext: CallbackContext
    ) {
        val adapter = AndroidPosIntegrationAdapter()
        adapter.getTerminalInfo(
            callbackContext
        )
    }

    private fun finishPreAuth(
        requestString: String,
        callbackContext: CallbackContext
    ) {
        try {
            val request = Gson().fromJson<FinishPreAuthMessage>(
                requestString,
                FinishPreAuthMessage::class.java
            )

            var type = PreAuthFinishType.CANCEL_PRE_AUTH
            if (request.type.equals("SALE_AFTER_PRE_AUTH", true)) type =
                PreAuthFinishType.SALE_AFTER_PRE_AUTH
            else if (request.type.equals("CANCEL_PRE_AUTH", true)) type =
                PreAuthFinishType.CANCEL_PRE_AUTH

            val adapter = AndroidPosIntegrationAdapter()
            adapter.finishPreAuth(
                type,
                request.originalStan,
                request.originalDate,
                request.orderRef,
                callbackContext
            )
        } catch (e: java.lang.Exception) {
            callbackContext.error("INVALID_INPUT::$e");
        }

    }

}
