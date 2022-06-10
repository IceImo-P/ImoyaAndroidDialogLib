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
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.OkCancelDialog.Builder

/**
 * OKボタン、キャンセルボタンを持つダイアログ
 *
 * タイトル, メッセージ, OKボタン, キャンセルボタンを持つダイアログ [Fragment] です。
 *  * [Builder]を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogListener.onDialogResult] メソッドがコールされます。
 *  * OKボタン押下に伴うダイアログ終了時、 [DialogListener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。
 *  * OKボタン押下以外の理由でダイアログが終了した場合は、
 * [DialogListener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 */
open class OkCancelDialog : TwoButtonDialog() {
    /**
     * ダイアログビルダ
     *
     * @param parent    親画面
     * @param requestCode リクエストコード
     */
    open class Builder(parent: DialogParent, requestCode: Int) :
        TwoButtonDialog.Builder(parent, requestCode) {
        /**
         * タイトル文言を設定します。
         *
         * @param title タイトル文言
         * @return [Builder]
         */
        override fun setTitle(title: String): Builder {
            super.setTitle(title)
            return this
        }

        /**
         * メッセージ文言を設定します。
         *
         * @param message メッセージ文言
         * @return [Builder]
         */
        override fun setMessage(message: String): Builder {
            super.setMessage(message)
            return this
        }

        /**
         * インスタンス識別用タグを設定します。
         *
         * @param tag タグ
         * @return [Builder]
         */
        override fun setTag(tag: String): Builder {
            super.setTag(tag)
            return this
        }

        /**
         * OKボタン文言を設定します。デフォルト値は [android.R.string.ok] です。
         *
         * @param buttonTitle ボタン文言
         * @return [Builder]
         */
        override fun setPositiveButtonTitle(buttonTitle: String): Builder {
            super.setPositiveButtonTitle(buttonTitle)
            return this
        }

        /**
         * キャンセルボタン文言を設定します。デフォルト値は [android.R.string.cancel] です。
         *
         * @param buttonTitle ボタン文言
         * @return [Builder]
         */
        override fun setNegativeButtonTitle(buttonTitle: String): Builder {
            super.setNegativeButtonTitle(buttonTitle)
            return this
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return [DialogBase]
         */
        override fun createFragment(): DialogBase {
            return OkCancelDialog()
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    protected open class DialogButtonClickListener(
        dialog: OkCancelDialog
    ) : DialogItemClickListener(dialog) {
        /**
         * 親画面へ通知する結果コードを返します。
         *
         * @param which クリックされたボタンの位置
         * @return 結果コード
         */
        private fun getResultCode(which: Int): Int {
            return if (which == AlertDialog.BUTTON_POSITIVE) Activity.RESULT_OK
            else Activity.RESULT_CANCELED
        }

        /**
         * ダイアログリスナへ、ボタンクリックを通知します。
         *
         * @param dialogInterface [DialogInterface]
         * @param which           クリックされたボタンの位置
         */
        override fun callListener(dialogInterface: DialogInterface, which: Int) {
            dialog.setDialogResult(getResultCode(which), makeData())
        }
    }

    override fun createDialog(savedInstanceState: Bundle?): Dialog {
        val buttonClickListener = DialogButtonClickListener(this)
        val arguments = requireArguments()
        return AlertDialog.Builder(requireContext(), this.theme)
            .setTitle(arguments.getString(KEY_TITLE))
            .setMessage(arguments.getString(KEY_MESSAGE))
            .setPositiveButton(
                arguments.getString(KEY_POSITIVE_BUTTON_TITLE), buttonClickListener
            )
            .setNegativeButton(
                arguments.getString(KEY_NEGATIVE_BUTTON_TITLE), buttonClickListener
            )
            .create()
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "OkCancelDialog"
//    }
}