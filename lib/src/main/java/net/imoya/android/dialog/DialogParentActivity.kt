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