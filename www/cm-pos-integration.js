/*global cordova, module*/

module.exports = {
  doTransaction: function (data, successCallback, errorCallback) {
    cordova.exec(
      successCallback,
      errorCallback,
      "CmPosIntegrationPluginImpl",
      "doTransaction",
      [data]
    );
  },

  transactionStatuses: function (data, successCallback, errorCallback) {
    cordova.exec(
      successCallback,
      errorCallback,
      "CmPosIntegrationPluginImpl",
      "transactionStatuses",
      [data]
    );
  },

  getLastReceipt: function (data, successCallback, errorCallback) {
    cordova.exec(
      successCallback,
      errorCallback,
      "CmPosIntegrationPluginImpl",
      "getLastReceipt",
      [data]
    );
  },

  getTerminalDayTotals: function (data, successCallback, errorCallback) {
    cordova.exec(
      successCallback,
      errorCallback,
      "CmPosIntegrationPluginImpl",
      "getTerminalDayTotals",
      [data]
    );
  },

  getTerminalInfo: function (data, successCallback, errorCallback) {
    cordova.exec(
      successCallback,
      errorCallback,
      "CmPosIntegrationPluginImpl",
      "getTerminalInfo",
      [data]
    );
  },

  finishPreAuth: function (data, successCallback, errorCallback) {
    cordova.exec(
      successCallback,
      errorCallback,
      "CmPosIntegrationPluginImpl",
      "finishPreAuth",
      [data]
    );
  },
};
