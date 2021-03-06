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
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.SingleButtonDialog.Builder

/**
 * ボタン1個のダイアログ
 *
 * タイトル, メッセージ, 1個のボタンを持つダイアログ [Fragment] です。
 *  * [Builder] を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogListener.onDialogResult] メソッドがコールされます。
 *  * ボタン押下に伴うダイアログ終了時、 [DialogListener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。
 *  * ボタン押下以外の理由でダイアログが終了した場合、
 * [DialogListener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 */
open class SingleButtonDialog : DialogBase() {
    /**
     * ダイアログビルダ
     *
     * @param parent      親画面
     * @param requestCode リクエストコード
     */
    open class Builder(parent: DialogParent, requestCode: Int) :
        DialogBuilder(parent, requestCode) {
        /**
         * ボタン文言
         */
        protected open var buttonTitle: String? = null

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
         * ボタン文言を設定します。
         *
         * @param buttonTitle ボタン文言
         * @return [Builder]
         */
        open fun setButtonTitle(buttonTitle: String): Builder {
            this.buttonTitle = buttonTitle
            return this
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return [DialogBase]
         */
        override fun createFragment(): DialogBase {
            return SingleButtonDialog()
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ [Bundle]
         */
        override fun makeArguments(): Bundle {
            val arguments = super.makeArguments()
            if (buttonTitle != null) {
                arguments.putString(KEY_BUTTON_TITLE, buttonTitle)
            } else {
                arguments.putString(
                    KEY_BUTTON_TITLE,
                    context.getString(android.R.string.ok)
                )
            }
            return arguments
        }
    }

    public override fun createDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = requireArguments()
        return AlertDialog.Builder(requireContext(), this.theme)
            .setTitle(arguments.getString(KEY_TITLE))
            .setMessage(arguments.getString(KEY_MESSAGE))
            .setPositiveButton(
                arguments.getString(KEY_BUTTON_TITLE),
                DialogItemClickHandler(this)
            )
            .create()
    }

    companion object {
        /**
         * ダイアログ引数キー:ボタン文言
         */
        /* protected */ const val KEY_BUTTON_TITLE = "buttonTitle"

//        /**
//         * Tag for log
//         */
//        private const val TAG = "SingleButtonDialog"
    }
}