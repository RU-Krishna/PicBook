package com.common.media.imageMedia

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import android.util.Log


class AppImageProviderImpl(
    private val context: Context
) : AppImageProvider {


    private var imagesList: MutableList<Images> = mutableListOf()

    override fun getImageData(): List<Images> {
        return mediaGetter()
    }

    private fun mediaGetter(): List<Images> {

        imagesList.clear()

        val contentResolver = context.contentResolver

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )

        val selection = "${MediaStore.Images.Media.RELATIVE_PATH} == ?"

        val selectionArgs = arrayOf(
            "Pictures/Pixabay/Images/"
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"


        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)

                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                imagesList.add(
                    Images(
                        id = id,
                        name = name,
                        uri = uri
                    )
                )

            }

        }

        Log.d("Images", "${imagesList.size}")

        return if (imagesList.isNotEmpty())
            imagesList.toList().reversed()
        else
            emptyList()

    }
}