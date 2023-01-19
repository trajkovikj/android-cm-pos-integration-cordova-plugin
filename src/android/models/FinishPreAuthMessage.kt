package io.thinker.cmposintegration.models

import java.util.Date

class FinishPreAuthMessage(
    val type : String,
    val originalStan : String,
    val originalDate : Date,
    val orderRef: String) {

}