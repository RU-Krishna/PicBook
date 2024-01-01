package com.common.media.imageMedia

import android.net.Uri

data class Images(
    val id: Long,
    val name: String,
    val uri: Uri = Uri.EMPTY
)
