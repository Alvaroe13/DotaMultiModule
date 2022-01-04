package com.alvaro.core.domain

sealed class UIComponentState {
    object Show : UIComponentState()
    object Hide : UIComponentState()
}
