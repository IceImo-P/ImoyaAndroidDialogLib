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
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.TwoButtonDialog.Builder

/**
 * ボタン2個のダイアログ
 *
 * タイトル, メッセージ, positive ボタン, negative ボタンを持つダイアログ [Fragment] です。
 *  * [Builder]を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogListener.onDialogResult] メソッドがコールされます。
 *  * positive ボタン押下に伴うダイアログ終了時、 [DialogListener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。このとき、引数 data の [Intent.getIntExtra] へ
 * [DialogBase.EXTRA_KEY_WHICH] を入力することで、クリックされたボタンの種類を取得できます。
 *  * positive ボタン押下以外の理由でダイアログが終了した場合、
 * [DialogListener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 */
open class TwoButtonDialog : DialogBase() {
    /**
     * ダイアログビルダ
     *
     * @param parent    親画面
     * @param requestCode リクエストコード
     */
    open class Builder(parent: DialogParent, requestCode: Int) :
        DialogBuilder(parent, requestCode) {
        /**
         * positive ボタン文言
         */
        @Suppress("MemberVisibilityCanBePrivate")
        protected var positiveButtonTitle: String? = null

        /**
         * negative ボタン文言
         */
        @Suppress("MemberVisibilityCanBePrivate")
        protected var negativeButtonTitle: String? = null

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
         * positive ボタン文言を設定します。デフォルト値は [android.R.string.ok] です。
         *
         * @param buttonTitle ボタン文言
         * @return [Builder]
         */
        open fun setPositiveButtonTitle(buttonTitle: String): Builder {
            positiveButtonTitle = buttonTitle
            return this
        }

        /**
         * negative ボタン文言を設定します。デフォルト値は [android.R.string.cancel] です。
         *
         * @param buttonTitle ボタン文言
         * @return [Builder]
         */
        open fun setNegativeButtonTitle(buttonTitle: String): Builder {
            negativeButtonTitle = buttonTitle
            return this
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return [DialogBase]
         */
        override fun createFragment(): DialogBase {
            return TwoButtonDialog()
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ [Bundle]
         */
        override fun makeArguments(): Bundle {
            val arguments = super.makeArguments()
            if (positiveButtonTitle != null) {
                arguments.putString(KEY_POSITIVE_BUTTON_TITLE, positiveButtonTitle)
            } else {
                arguments.putString(
                    KEY_POSITIVE_BUTTON_TITLE,
                    context.getString(android.R.string.ok)
                )
            }
            if (negativeButtonTitle != null) {
                arguments.putString(KEY_NEGATIVE_BUTTON_TITLE, negativeButtonTitle)
            } else {
                arguments.putString(
                    KEY_NEGATIVE_BUTTON_TITLE,
                    context.getString(android.R.string.cancel)
                )
            }
            return arguments
        }
    }

    /**
     * ダイアログ生成処理
     *
     * @param savedInstanceState 前回強制終了時の保存データ
     * @return 生成した [Dialog]
     */
    public override fun createDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = requireArguments()
        val buttonClickListener = DialogItemClickListener(this, listener)
        return AlertDialog.Builder(requireActivity(), this.theme)
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

    companion object {
        /**
         * ダイアログ引数キー:positive ボタン文言
         */
        /* protected */ const val KEY_POSITIVE_BUTTON_TITLE = "positiveButtonTitle"

        /**
         * ダイアログ引数キー:negative ボタン文言
         */
        /* protected */ const val KEY_NEGATIVE_BUTTON_TITLE = "negativeButtonTitle"

        // private static final String TAG = "TwoButtonDialog";
    }
}