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
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.TextInputDialog.Builder

/**
 * 文字入力ダイアログ
 *
 * タイトル, メッセージ, 文字入力欄, OKボタン, キャンセルボタンを持つダイアログ [Fragment] です。
 *  * 親画面は [DialogListener] を実装した [Fragment] 又は [AppCompatActivity] を想定しています。
 *  * [Builder]を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogListener.onDialogResult] メソッドがコールされます。
 *  * OKボタン押下に伴うダイアログ終了時、 [DialogListener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。このとき、引数 data の [Intent.getStringExtra] へ
 * [InputDialog.EXTRA_KEY_INPUT_VALUE] を入力することで、入力された文字列を取得できます。
 *  * OKボタン押下以外の理由でダイアログが終了した場合は、
 * [DialogListener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 */
@Suppress("unused")
class TextInputDialog : InputDialog() {
    /**
     * ダイアログビルダ
     *
     * @param parent    親画面
     * @param requestCode リクエストコード
     */
    open class Builder(parent: DialogParent, requestCode: Int) :
        InputDialog.Builder(parent, requestCode) {
        /**
         * 入力初期値
         */
        @JvmField
        protected var text: String? = null

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
         * positiveボタン文言を設定します。デフォルト値は [android.R.string.ok] です。
         *
         * @param buttonTitle ボタン文言
         * @return [Builder]
         */
        override fun setPositiveButtonTitle(buttonTitle: String): Builder {
            super.setPositiveButtonTitle(buttonTitle)
            return this
        }

        /**
         * negativeボタン文言を設定します。デフォルト値は [android.R.string.cancel] です。
         *
         * @param buttonTitle ボタン文言
         * @return [Builder]
         */
        override fun setNegativeButtonTitle(buttonTitle: String): Builder {
            super.setNegativeButtonTitle(buttonTitle)
            return this
        }

        /**
         * 入力部分のレイアウトリソースIDを設定します。
         *
         *
         * リソースには、最低限次の要素が必要です:
         *  * 入力欄となる、 id="text" である [EditText]
         *
         *
         * @param layoutResourceId 入力部分のレイアウトリソースID
         * @return [Builder]
         */
        override fun setLayoutResourceId(layoutResourceId: Int): Builder {
            super.setLayoutResourceId(layoutResourceId)
            return this
        }

        /**
         * ヒント文言を設定します。
         *
         * @param hint ヒント文言
         * @return [Builder]
         */
        override fun setHint(hint: String?): Builder {
            super.setHint(hint)
            return this
        }

        /**
         * 入力種別を設定します。
         *
         * @param inputType 入力種別
         * @return [Builder]
         */
        override fun setInputType(inputType: Int): Builder {
            super.setInputType(inputType)
            return this
        }

        /**
         * 最大入力可能文字数を設定します。
         *
         * @param maxLength 最大入力可能文字数
         * @return [Builder]
         */
        override fun setMaxLength(maxLength: Int): Builder {
            super.setMaxLength(maxLength)
            return this
        }

        /**
         * 入力初期値を設定します。
         *
         * @param text 入力初期値
         * @return [Builder]
         */
        open fun setText(text: String): Builder {
            this.text = text
            return this
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return [DialogBase]
         */
        override fun createFragment(): DialogBase {
            return TextInputDialog()
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ [Bundle]
         */
        override fun makeArguments(): Bundle {
            val arguments = super.makeArguments()
            if (text != null) {
                arguments.putString(EXTRA_KEY_INPUT_VALUE, text)
            }
            return arguments
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    private class DialogButtonClickListener(dialog: TextInputDialog, listener: DialogListener) :
        InputDialog.DialogButtonClickListener(dialog, listener) {

        /**
         * ボタン押下時に [DialogListener.onDialogResult] へ入力する [Intent] を生成して返します。
         *
         * @return [Intent]
         */
        override fun makeData(): Intent {
            val intent = super.makeData()
            intent.putExtra(EXTRA_KEY_INPUT_VALUE, (dialog as TextInputDialog).inputText)
            return intent
        }
    }

    /**
     * 入力された文字列を取得します。
     *
     * @return 文字列
     */
    val inputText: String
        get() = (dialog?.findViewById<View>(R.id.text) as EditText).text.toString()

    /**
     * ボタンクリック時の処理を実装した [InputDialog.DialogButtonClickListener] を返します。
     * 返された [InputDialog.DialogButtonClickListener] は、ダイアログ全ボタンのリスナとなります。
     *
     * @return [InputDialog.DialogButtonClickListener]
     */
    override val dialogButtonClickListener: InputDialog.DialogButtonClickListener
        get() = DialogButtonClickListener(this, listener)

    /**
     * ダイアログへ組み込む入力部分の [View]
     * を指定する、レイアウトリソースIDのデフォルト値を返します。
     *
     * @return レイアウトリソースID
     */
    @get:LayoutRes
    override val defaultInputViewLayoutResourceId: Int
        get() = R.layout.dialog_text_input

    /**
     * 入力欄へ初期表示する内容を返します。
     *
     * @param context   [Context]
     * @param arguments ダイアログ引数を含む [Bundle]
     * @return 入力欄へ表示する文字列
     */
    override fun getInitialValue(context: Context, arguments: Bundle): CharSequence {
        return arguments.getString(EXTRA_KEY_INPUT_VALUE, "")
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "TextInputDialog"
//    }
}