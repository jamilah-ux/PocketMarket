package com.example.pocketmarket.domain.model

sealed class AppError : Exception() {
    data object NoInternet : AppError()
    data object Timeout : AppError()
    data class ServerError(val code: Int, override val message: String?) : AppError()
    data class ClientError(val code: Int, override val message: String?) : AppError()
    data class Unknown(override val message: String?) : AppError()

    fun toUserFriendlyMessage(): String = when (this) {
        is NoInternet -> "لا يوجد اتصال بالإنترنت. يرجى التحقق من الشبكة وإعادة المحاولة."
        is Timeout -> "استغرقت الاستجابة وقتاً أطول من المتوقع. أعد المحاولة."
        is ServerError -> "الخادم غير متاح حالياً ($code). جاري العمل على حل المشكلة."
        is ClientError -> "تعذر إتمام الطلب ($code). تحقق من صحة البيانات."
        is Unknown -> message ?: "حدث خطأ غير متوقع. يرجى المحاولة لاحقاً."
    }
}