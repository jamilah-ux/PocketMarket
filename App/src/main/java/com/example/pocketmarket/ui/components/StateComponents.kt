package com.example.pocketmarket.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pocketmarket.R
import com.example.pocketmarket.ui.theme.PocketMarketTheme

// 1. Loading Component
@Composable
fun PocketLoadingState(modifier: Modifier = Modifier) {
    val loadingDescription = stringResource(R.string.common_loading)
    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = loadingDescription },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// 2. Empty State Component
@Composable
fun PocketEmptyState(
    title: String = stringResource(R.string.common_empty_title),
    description: String = stringResource(R.string.common_empty_description),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    }
}

// 3. Error State Component
@Composable
fun PocketErrorState(
    errorMessage: String? = null,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.common_error_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = errorMessage ?: stringResource(R.string.common_error_default),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        PocketButton(
            text = stringResource(R.string.common_retry),
            onClick = onRetry
        )
    }
}

// --- Multi-State Previews (Light & Dark) ---

@Preview(name = "Loading Light", showBackground = true)
@Preview(name = "Loading Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewLoading() {
    PocketMarketTheme {
        Surface { PocketLoadingState() }
    }
}

@Preview(name = "Empty Light", showBackground = true)
@Preview(name = "Empty Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewEmpty() {
    PocketMarketTheme {
        Surface { PocketEmptyState() }
    }
}

@Preview(name = "Error Light", showBackground = true)
@Preview(name = "Error Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun PreviewError() {
    PocketMarketTheme {
        Surface { PocketErrorState(onRetry = {}) }
    }
}