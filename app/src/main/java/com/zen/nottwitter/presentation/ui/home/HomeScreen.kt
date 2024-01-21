package com.zen.nottwitter.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import com.zen.nottwitter.R
import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.presentation.ui.base.BaseScreen
import com.zen.nottwitter.presentation.ui.component.ActionTopBar
import com.zen.nottwitter.presentation.ui.component.EmptyScreen
import com.zen.nottwitter.presentation.ui.component.PostItem
import com.zen.nottwitter.presentation.ui.editor.EditorScreen
import com.zen.nottwitter.presentation.ui.viewer.ViewerScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
class HomeScreen : BaseScreen<HomeViewModel, HomeUIState, HomeUIEffect, HomeInteractionListener>() {

    @Composable
    override fun Content() {
        Init(getViewModel())
    }

    override fun onEffect(effect: HomeUIEffect, navigator: Navigator) {
        when (effect) {
            HomeUIEffect.NavigateToPostEditor -> navigator.push(EditorScreen())
            is HomeUIEffect.ViewImage -> navigator.push(ViewerScreen(effect.imageUrl))
            is HomeUIEffect.ViewPost -> {}
        }
    }

    @Composable
    override fun OnRender(state: HomeUIState, listener: HomeInteractionListener) {
        Scaffold(
            topBar = {
                ActionTopBar(
                    title = "",
                    actionItem = {
                        IconButton(onClick = listener::onNewPostClick) {
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = stringResource(
                                    id = R.string.new_post
                                )
                            )
                        }
                    },
                    showAppIcon = true
                )
            }
        ) { paddingValues ->
            if (state.posts.isEmpty()) {
                EmptyScreen(modifier = Modifier.padding(paddingValues))
            } else {
                PostList(state, listener, modifier = Modifier.padding(paddingValues))
            }
        }
    }

    @Composable
    private fun PostList(
        state: HomeUIState,
        listener: HomeInteractionListener,
        modifier: Modifier = Modifier
    ) {
        val lazyListState = rememberLazyListState()
        val pullRefreshState =
            rememberPullRefreshState(refreshing = state.isLoading, onRefresh = listener::onRefresh)
        Box(modifier = modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
            PullRefreshIndicator(
                refreshing = state.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            LazyColumn(state = lazyListState) {
                items(state.posts) {
                    PostItem(
                        post = it,
                        onImageClick = listener::onPostImageClick,
                        onPostClick = listener::onPostClick
                    )
                }
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun HomeScreenPreview() {
        OnRender(state = HomeUIState(), listener = object : HomeInteractionListener {
            override fun onNewPostClick() {
                TODO("Not yet implemented")
            }

            override fun onPostClick(post: Post) {
                TODO("Not yet implemented")
            }

            override fun onPostImageClick(imageUrl: String) {
                TODO("Not yet implemented")
            }

            override fun onRefresh() {
                TODO("Not yet implemented")
            }
        })
    }
}