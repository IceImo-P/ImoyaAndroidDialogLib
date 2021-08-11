package net.imoya.android.dialog

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * ダイアログ親画面となる [Fragment]
 *
 *  * 親画面となる [Fragment] は [DialogListener] を実装する必要があります。
 */
@Suppress("unused")
open class DialogParentFragment<T>(
    protected val fragment: T
) : DialogParent where T : Fragment, T : DialogListener {
    override val context: Context
        get() = fragment.requireContext().applicationContext

    override val listener: DialogListener
        get() = fragment

    override val fragmentManager: FragmentManager
        get() = fragment.parentFragmentManager
}