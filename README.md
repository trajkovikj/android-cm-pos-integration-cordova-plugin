# Android CM POS Integration Cordova Plugin

This is cordova plugin for android SunMi devices with integrated card reader. It uses cm.com SDK for App 2 App communication with their terminal application.

## 1. Installation

Go to your cordova project and execute the following command:

`cordova plugin add https://github.com/trajkovikj/android-cm-pos-integration-cordova-plugin.git`

To install specific version for specific tag you can use the following command:

`cordova plugin add https://github.com/trajkovikj/android-cm-pos-integration-cordova-plugin.git#v1.0.0`

## 2. Usage

To interact with cordova and its plugins you will first need to wait on `deviceready` event to fire. After `deviceready` event fires, you can use the `cmPosIntegration` plugin from the window object. There is a simple example code below that presents the most simple usage of the plugin.

```
document.addEventListener("deviceready", onDeviceReady, false);

function successCallback(response) {
    // response is a JSON object that has all the data from a successfull transaction
  console.log(JSON.parse(response));
}

function errorCallback(response) {
    // response is a string that is returned in case App Crashes or the transaction is cancelled in some way
    console.log(response);
}

function onDeviceReady() {
        var myData = {
            transactionType: "PURCHASE",
            amount: 11.11,
            currency: "EUR",
            orderReference: "123456",
        };

        var dataString = JSON.stringify(myData);

        window.cmPosIntegration.doTransaction(
            dataString,
            successCallback,
            errorCallback
        );
}
```

Below are example of functions provided by the plugin and the CM SDK. To read more details about the functions and understand the processes, read the following article on the CM Developer support page [developers.cm.com](https://developers.cm.com/payments-platform/v1.0.2/docs/app-2-app-integration)

### 2.1 Do transaction

This function is used to perform a transaction on CM POS Payments app.

`window.cmPosIntegration.doTransaction(data, successCallback, errorCallback);`

**successCallback(response: String)** - callback function that receives one parameter of type string

**errorCallback(response: String)** - callback function that receives one parameter of type string

**data** - JSON string

```
var dataObject = {
    transactionType: "REFUND",
    amount: 11.11,
    currency: "EUR",
    orderReference: "1234-5678",
    refundStan: "065987", // Can be found on the receipt
    refundDate: "21/01/22", // Can be found on the receipt
}

var data = JSON.stringify(dataObject);
```

| Property        | Description                                                                                                                                                                          |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| transactionType | PURCHASE / REFUND / PRE_AUTH                                                                                                                                                         |
| amount          | decimal number                                                                                                                                                                       |
| currency        | 3 letter ISO currency e.g. EUR / USD                                                                                                                                                 |
| orderReference  | string, generated from yourside, so you can reference the order                                                                                                                      |
| refundStan      | (optional) Only for refund transactions. This data must be taken from the previous transaction (transaction that you are refunding)                                                  |
| refundDate      | (optional) Only for refund transactions. Date of the transaction that is beeing refunded. This data must be taken from the previous transaction (transaction that you are refunding) |

**Error responses**

| Error code                            | Description                                                                                   |
| ------------------------------------- | --------------------------------------------------------------------------------------------- |
| NO_ERROR                              | (0, "No error")                                                                               |
| UNKNOWN_ERROR                         | (-1, "Unknown error")                                                                         |
| AMOUNT_INVALID                        | (-2, "Amount used was invalid")                                                               |
| NO_INTERNET                           | (-12, "Device is not connected to the network")                                               |
| PRINTER_INIT_FAIL                     | (-17, "Printer initialization failed")                                                        |
| POS_NOT_CONFIGURED                    | (-18, "Device is not configured in gateway")                                                  |
| AUTO_TIMEZONE_NOT_ENABLED             | (-23, "Autotimezone is not enabled on device")                                                |
| BAD_TIMEZONE                          | (-24, "Timezone on the device is not correct")                                                |
| HOST_NOT_CONNECTED                    | (-25, "Cannot connect with the gateway")                                                      |
| MERCHANT_ORDER_REF_NOT_PRESENT        | (-29, "Order reference not present in request data")                                          |
| TIMEOUT                               | (-30, "Timeout reaching the gateway")                                                         |
| REPEATED_OPERATION                    | (-31, "Transaction already in progress")                                                      |
| MERCHANT_ORDER_REF_TOO_LONG           | (-32, "Order ref exceeds allowed length")                                                     |
| AMOUNT_LIMIT_EXCEEDED                 | (-33, "Transaction exceeds allowed limit")                                                    |
| INTERNAL_PROCESSING_ERROR             | (-34, "Internal processing error during transaction flow. Transaction couldn't be processed") |
| INVALID_ORIGINAL_DATA                 | (-35, "Data related with previous transaction is invalid")                                    |
| LOW_BATTERY_LEVEL                     | (-36, "Battery level is too low to start a transaction")                                      |
| WRONG_EMV_AUTHORIZATION_DATE_AND_TIME | (-37, "Wrong date and time in authorization request. Please reboot your device.")             |
| TRANSACTION_STATUS_ERROR              | (-50, "Status information not received")                                                      |
| INFO_REQUEST_FAILED                   | (-51, "Info Request towards the gateway has failed")                                          |

**1. Example of sending a new payment transaction request.**

```
/* Sending a payment transaction */

functino purchase() {
        var myData = {
            transactionType: "PURCHASE",
            amount: 11.11,
            currency: "EUR",
            orderReference: "1234-5678",
        };

        var dataString = JSON.stringify(myData);

        window.cmPosIntegration.doTransaction(
            dataString,
            successCallback,
            errorCallback
        );
}

function successCallback(response) {
    // response is a JSON object that has all the data from a successfull transaction
  console.log(JSON.parse(response));
}

function errorCallback(response) {
    // response is a string that is returned in case App Crashes or the transaction is cancelled in some way
    console.log(response);
}

```

**2. Example of sending a refund transaction request.**

```
/* Start refund transaction */

functino refund() {
        var myData = {
            transactionType: "REFUND",
            amount: 11.11,
            currency: "EUR",
            orderReference: "1234-5678",
            refundStan: "065987", // Can be found on the receipt
            refundDate: "21/01/22", // Can be found on the receipt
        };

        var dataString = JSON.stringify(myData);

        window.cmPosIntegration.doTransaction(
            dataString,
            successCallback,
            errorCallback
        );
}
```

### 2.2 Transaction statuses

This function is used to request status of a previous transaction performed by CM POS Payments Terminal app.

```
/* Sending a payment transaction */

functino previousTransactionStatus() {
        var myData = {
            orderReference: "1234-5678",
            page: 1,
            size: 0, // 0 is ignored
            sortField: String?, // optional
            sortValue: "asc", // optional

            transactionType: "REFUND",
            amount: 11.11,
            currency: "EUR",
            orderReference: "1234-5678",
            refundStan: "065987", // Can be found on the receipt
            refundDate: "21/01/22", // Can be found on the receipt
        };

        var dataString = JSON.stringify(myData);

        window.cmPosIntegration.doTransaction(
            dataString,
            successCallback,
            errorCallback
        );
}
```

### 2.3 Get last receipt

This function is used to request the receipt of the last transaction performed by Terminal application.

### 2.4 Get terminal day totals

This function is used to request the totals information from the gateway.

### 2.5 Get terminal info

This function is used to request information about the merchant shop in which the terminal is connected.

### 2.6 Finish pre auth

This function is used to finish a previously pre authorized transaction.

## 3. References

[cm.com official website](https://cm.com)

[CM App 2 App integration developers documentation](https://developers.cm.com/payments-platform/v1.0.2/docs/app-2-app-integration)
