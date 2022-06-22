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

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent

/**
 * ダイアログのボタンクリックを親画面へ通知するロジック
 */
open class DialogItemClickHandler (
    /**
     * ダイアログ
     */
    @JvmField
    protected val dialog: DialogBase
) : DialogInterface.OnClickListener {
    /**
     * 押された項目
     */
    @JvmField
    protected var which = 0

    /**
     * ボタン押下時に [DialogListener.onDialogResult] へ入力する [Intent] を生成して返します。
     *
     * @return [Intent]
     */
    protected open fun makeData(): Intent {
        val data = Intent()
        data.putExtra(DialogBase.EXTRA_KEY_WHICH, which)
        val tag = dialog.tag
        if (tag != null) {
            data.putExtra(DialogBase.EXTRA_KEY_TAG, tag)
        }
        return data
    }

    /**
     * ダイアログリスナへ、クリックを通知します。
     *
     * @param dialogInterface [DialogInterface]
     * @param which           クリックされた項目
     */
    protected open fun setResult(dialogInterface: DialogInterface, which: Int) {
        dialog.setDialogResult(Activity.RESULT_OK, makeData())
    }

    /**
     * 項目クリック時の処理
     *
     * @param dialogInterface [DialogInterface]
     * @param which           クリックされた項目
     */
    override fun onClick(dialogInterface: DialogInterface, which: Int) {
        this.which = which
        try {
            // 親画面へ通知する
            setResult(dialogInterface, which)
        } catch (e: Exception) {
            DialogLog.e(TAG, "Exception", e)
        }
    }

    companion object {
        /**
         * Tag for log
         */
        private const val TAG = "DialogItemClickHandler"
    }}