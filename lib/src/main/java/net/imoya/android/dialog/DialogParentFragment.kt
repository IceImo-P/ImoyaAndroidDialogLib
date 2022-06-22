/*
 * Copyright (C) 2022 IceImo-P
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.imoya.android.dialog

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

/**
 * ダイアログの親画面となる [Fragment]
 */
@Suppress("unused")
open class DialogParentFragment(
    /**
     * 親画面となる [Fragment]
     */
    @JvmField
    val fragment: Fragment,
    /**
     * ダイアログより結果を受け取る [DialogListener]
     */
    @JvmField
    protected var dialogListener: DialogListener
) : DialogParent {
    override val context: Context
        get() = fragment.requireContext().applicationContext

    override val listener: DialogListener
        get() = dialogListener

    override val fragmentManager: FragmentManager
        get() = fragment.childFragmentManager

    override val lifecycleOwner: LifecycleOwner
        get() = fragment.viewLifecycleOwner
}