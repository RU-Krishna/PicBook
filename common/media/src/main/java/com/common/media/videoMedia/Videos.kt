package com.common.media.videoMedia

import android.net.Uri

data class Videos(
    val uri: Uri = Uri.EMPTY,
    val id: Long = 0L,
    val name: String = "",
    val duration: Int = 0,
    val size: Int = 0

)
