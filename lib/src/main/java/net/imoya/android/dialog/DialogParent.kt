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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

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

    /**
     * Returns [LifecycleOwner]
     */
    val lifecycleOwner: LifecycleOwner

    /**
     * 指定のリクエストコードで表示したダイアログの終了時に、
     * [FragmentManager] が [DialogListener.onDialogResult] をコールように設定します。
     *
     * 登録のタイミングはいつでも良いですが、なるべく画面の生成時に実行することを推奨します。
     *
     * @param requestCode リクエストコード
     */
    @Suppress("unused")
    fun registerListener(requestCode: Int) {
        DialogUtil.registerDialogListener(this, requestCode)
    }
}