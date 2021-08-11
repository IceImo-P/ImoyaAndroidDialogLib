package net.imoya.android.dialog

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager

/**
 * ダイアログ親画面となる [AppCompatActivity]
 *
 *  * 親画面となる [AppCompatActivity] は [DialogListener] を実装する必要があります。
 */
@Suppress("unused")
open class DialogParentActivity<T>(
    protected val activity: T
) : DialogParent where T : AppCompatActivity, T : DialogListener {
    override val context: Context
        get() = activity.applicationContext

    override val listener: DialogListener
        get() = activity

    override val fragmentManager: FragmentManager
        get() = activity.supportFragmentManager
}