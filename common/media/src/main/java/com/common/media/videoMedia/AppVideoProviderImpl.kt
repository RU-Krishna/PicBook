package com.common.media.videoMedia

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore

class AppVideoProviderImpl(
    private val context: Context
) : AppVideoProvider {

    private val contentResolver = context.contentResolver

    private var videoList: MutableList<Videos> = mutableListOf()

    override fun getVideoData(): List<Videos> {
        return mediaGetter()
    }

    private fun mediaGetter(): List<Videos> {

        videoList.clear()

        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE
        )

        val selection = "${MediaStore.Video.Media.RELATIVE_PATH} == ?"
        val selectionArgs = arrayOf(
            "Pictures/PicBook/Videos/"
        )

        val sortOrder = "${MediaStore.Video.Media.DATE_TAKEN} DESC"

        contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->

            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)

                val uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                videoList += Videos(
                    uri = uri,
                    id = id,
                    name = name,
                    duration = duration,
                    size = size
                )


            }


        }

        return if (videoList.isNotEmpty())
            videoList.toList()
        else
            emptyList()
    }
}