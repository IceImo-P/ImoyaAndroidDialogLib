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

import android.content.Intent

/**
 * ダイアログリスナ
 */
interface DialogListener {
    /**
     * ダイアログ終了時にコールされ、ユーザの選択結果を通知します。
     *
     * @param requestCode [DialogBuilder] に設定したリクエストコード
     * @param resultCode 結果コード
     * @param data 追加のデータを含む [Intent]
     */
    fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent?)
}