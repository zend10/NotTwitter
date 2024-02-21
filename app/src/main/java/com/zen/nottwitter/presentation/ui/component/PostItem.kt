package com.zen.nottwitter.presentation.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zen.nottwitter.R
import com.zen.nottwitter.data.model.Post
import com.zen.nottwitter.data.model.User
import com.zen.nottwitter.domain.TimeUtils

@Composable
fun PostItem(
    user: User,
    post: Post,
    onImageClick: (imageUrl: String) -> Unit,
    modifier: Modifier = Modifier,
    onPostClick: ((post: Post) -> Unit)? = null,
    onDeletePostClick: ((post: Post) -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onPostClick != null, onClick = { onPostClick?.invoke(post) })
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = post.nickname, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.weight(1f))
            if (user.uid == post.userUid) {
                IconButton(onClick = { onDeletePostClick?.invoke(post) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_post)
                    )
                }
            }
            Text(
                text = if (post.createdOn == 0L)
                    stringResource(id = R.string.just_now)
                else
                    TimeUtils.getDiff(post.createdOn),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(text = post.message, modifier = Modifier.padding(horizontal = 16.dp))
        if (post.imageUrl.isNotBlank()) {
            AsyncImage(
                model = post.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .clickable { onImageClick(post.imageUrl) }
            )
        }
        Divider(modifier = Modifier.padding(top = 16.dp))
    }
}

@Preview
@Composable
private fun PostItemPreview() {
    PostItem(User(), Post(), {})
}