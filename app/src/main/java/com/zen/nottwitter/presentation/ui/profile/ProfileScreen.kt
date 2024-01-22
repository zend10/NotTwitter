package com.zen.nottwitter.presentation.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.R
import com.zen.nottwitter.presentation.ui.base.BaseScreen
import com.zen.nottwitter.presentation.ui.component.ActionTopBar
import com.zen.nottwitter.presentation.ui.component.EmptyScreen
import com.zen.nottwitter.presentation.ui.component.GeneralAlertDialog
import com.zen.nottwitter.presentation.ui.component.PostItem
import com.zen.nottwitter.presentation.ui.landing.LandingScreen
import com.zen.nottwitter.presentation.ui.viewer.ViewerScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
class ProfileScreen :
    BaseScreen<ProfileViewModel, ProfileUIState, ProfileUIEffect, ProfileInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: ProfileUIEffect, navigator: Navigator) {
        when (effect) {
            is ProfileUIEffect.ViewImage -> navigator.push(ViewerScreen(effect.imageUrl))
        }
    }

    @Composable
    override fun OnRender(state: ProfileUIState, listener: ProfileInteractionListener) {
        val lazyListState = rememberLazyListState()
        val pullRefreshState =
            rememberPullRefreshState(refreshing = state.isLoading, onRefresh = listener::onRefresh)
        val shouldStartPaginate = remember {
            derivedStateOf {
                (lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    ?: -9) >= lazyListState.layoutInfo.totalItemsCount - 2
            }
        }

        Scaffold(
            topBar = {
                ActionTopBar(
                    title = state.nickname,
                    actionItem = {
                        TextButton(onClick = listener::onLogoutClick) {
                            Text(text = stringResource(id = R.string.logout))
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .pullRefresh(pullRefreshState)
            ) {
                if (state.posts.isEmpty()) {
                    EmptyScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    )
                } else {
                    PostList(
                        lazyListState,
                        shouldStartPaginate,
                        state,
                        listener
                    )
                }
                PullRefreshIndicator(
                    refreshing = state.isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }

        AlertDialog(state, listener)
    }

    @Composable
    private fun PostList(
        lazyListState: LazyListState,
        shouldStartPaginate: State<Boolean>,
        state: ProfileUIState,
        listener: ProfileInteractionListener,
        modifier: Modifier = Modifier
    ) {
        LaunchedEffect(shouldStartPaginate.value) {
            if (shouldStartPaginate.value) listener.onLoadNextPage()
        }

        LazyColumn(state = lazyListState) {
            items(state.posts) {
                PostItem(
                    post = it,
                    onImageClick = listener::onPostImageClick
                )
            }
            item(state.isLoadingNextPage) {
                if (state.isLoadingNextPage) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    @Composable
    private fun AlertDialog(state: ProfileUIState, listener: ProfileInteractionListener) {
        if (state.showLogoutDialog) {
            GeneralAlertDialog(
                onDismissRequest = listener::onLogoutDialogDismiss,
                title = stringResource(R.string.logout_dialog_title),
                message = stringResource(R.string.logout_dialog_message),
                positiveCta = stringResource(id = R.string.logout),
                negativeCta = stringResource(id = R.string.cancel),
                onPositiveCtaClick = listener::onLogoutDialogPositiveCtaClick
            )
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun ProfileScreenPreview() {
        OnRender(state = ProfileUIState(), listener = object : ProfileInteractionListener {
            override fun onLogoutClick() {
                TODO("Not yet implemented")
            }

            override fun onPostImageClick(imageUrl: String) {
                TODO("Not yet implemented")
            }

            override fun onRefresh() {
                TODO("Not yet implemented")
            }

            override fun onLoadNextPage() {
                TODO("Not yet implemented")
            }

            override fun onLogoutDialogDismiss() {
                TODO("Not yet implemented")
            }

            override fun onLogoutDialogPositiveCtaClick() {
                TODO("Not yet implemented")
            }
        })
    }
}