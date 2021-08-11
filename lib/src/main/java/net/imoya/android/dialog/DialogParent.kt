package net.imoya.android.dialog

import android.content.Context
import androidx.fragment.app.FragmentManager

/**
 * ダイアログの親画面が実装する interface
 */
interface DialogParent {
    /**
     * Returns application [Context]
     */
    val context: Context

    /**
     * Returns [DialogListener]
     */
    val listener: DialogListener

    /**
     * Returns [FragmentManager]
     */
    val fragmentManager: FragmentManager
}