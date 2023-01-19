package io.thinker.cmposintegration.models

class TransactionStatusesMessage(
    val orderReference : String,
    val page : Int,
    val size : Int,
    val sortField : String?,
    val sortValue: String?) {

}