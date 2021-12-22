package com.alvaro.core.domain

sealed class UIComponent {
    data class Dialog(val title: String, val message: String) : UIComponent()
    data class None(val message: String) : UIComponent()
}
