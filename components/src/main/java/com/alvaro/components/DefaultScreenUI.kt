package com.alvaro.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alvaro.core.domain.ProgressBarState
import com.alvaro.core.domain.Queue
import com.alvaro.core.domain.UIComponent

@Composable
fun DefaultScreenUI(
    progressBarState: ProgressBarState = ProgressBarState.Idle,
    messageQueue: Queue<UIComponent> = Queue(mutableListOf()),
    onRemoveHeadFromQueue: () -> Unit,
    content: @Composable () -> Unit,
){
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ){

            content()

            // process the queue
            if(!messageQueue.isEmpty()){
                messageQueue.peek()?.let { uiComponent ->
                    if(uiComponent is UIComponent.Dialog){
                        GenericDialog(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                            ,
                            title = uiComponent.title,
                            description = uiComponent.message,
                            onRemoveHeadFromQueue = onRemoveHeadFromQueue
                        )
                    }
                }
            }

            if(progressBarState is ProgressBarState.Loading){
                CircularIndeterminateProgressBar()
            }
        }
    }
}