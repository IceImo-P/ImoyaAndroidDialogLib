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

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import java.util.*

/**
 * 入力ダイアログの共通部分
 */
abstract class InputDialog : OkCancelDialog() {
    /**
     * ダイアログビルダ
     *
     * @param parent    親画面
     * @param requestCode リクエストコード
     */
    abstract class Builder(parent: DialogParent, requestCode: Int) :
        OkCancelDialog.Builder(parent, requestCode) {
        /**
         * レイアウトリソースID
         */
        @Suppress("MemberVisibilityCanBePrivate")
        protected var layoutResourceId = 0

        /**
         * ヒント文言
         */
        @Suppress("MemberVisibilityCanBePrivate")
        protected var hint: String? = null

        /**
         * 入力種別
         */
        @Suppress("MemberVisibilityCanBePrivate")
        protected var inputType = 0

        /**
         * 入力種別設定済フラグ
         */
        @Suppress("MemberVisibilityCanBePrivate")
        protected var inputTypeIsSet = false

        /**
         * 最大入力可能文字数
         */
        @Suppress("MemberVisibilityCanBePrivate")
        protected var maxLength = Int.MAX_VALUE

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
        open fun setLayoutResourceId(layoutResourceId: Int): Builder {
            this.layoutResourceId = layoutResourceId
            return this
        }

        /**
         * ヒント文言を設定します。
         *
         * @param hint ヒント文言
         * @return [Builder]
         */
        open fun setHint(hint: String?): Builder {
            this.hint = hint
            return this
        }

        /**
         * 入力種別を設定します。
         *
         * @param inputType 入力種別
         * @return [Builder]
         */
        open fun setInputType(inputType: Int): Builder {
            this.inputType = inputType
            inputTypeIsSet = true
            return this
        }

        /**
         * 最大入力可能文字数を設定します。
         *
         * @param maxLength 最大入力可能文字数
         * @return [Builder]
         */
        open fun setMaxLength(maxLength: Int): Builder {
            this.maxLength = maxLength
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
            if (layoutResourceId != 0) {
                arguments.putInt(KEY_LAYOUT_RESOURCE_ID, layoutResourceId)
            }
            if (hint != null) {
                arguments.putString(KEY_HINT, hint)
            }
            if (inputTypeIsSet) {
                arguments.putInt(KEY_INPUT_TYPE, inputType)
            }
            arguments.putInt(KEY_MAX_LENGTH, maxLength)
            return arguments
        }
    }

    /**
     * ボタンクリックリスナ
     */
    protected abstract class DialogButtonClickListener(
        dialog: InputDialog
    ) : OkCancelDialog.DialogButtonClickListener(dialog)

    /**
     * ボタンクリック時の処理を実装した [DialogButtonClickListener] を返します。
     * 返された [DialogButtonClickListener] は、ダイアログ全ボタンのリスナとなります。
     *
     * @return [DialogButtonClickListener]
     */
    protected abstract val dialogButtonClickListener: DialogButtonClickListener

    /**
     * ダイアログへ組み込む入力部分の [View]
     * を指定する、レイアウトリソースIDのデフォルト値を返します。
     *
     * @return レイアウトリソースID
     */
    @get:LayoutRes
    protected abstract val defaultInputViewLayoutResourceId: Int

    /**
     * 入力欄へ初期表示する内容を返します。
     *
     * @param context   [Context]
     * @param arguments ダイアログ引数を含む [Bundle]
     * @return 入力欄へ表示する文字列
     */
    protected abstract fun getInitialValue(context: Context, arguments: Bundle): CharSequence

    /**
     * ダイアログへ組み込む、入力部分の [View] を生成して返します。
     *
     * @param context   [Context]
     * @param arguments ダイアログ引数を含む [Bundle]
     * @return [View]
     */
    protected open fun inflateInputView(context: Context, arguments: Bundle): View {
        val view = LayoutInflater.from(context).inflate(
            arguments.getInt(
                KEY_LAYOUT_RESOURCE_ID,
                defaultInputViewLayoutResourceId
            ),
            null
        )
        val editText = view.findViewById<EditText>(R.id.text)
        editText.hint = arguments.getString(KEY_HINT, "")
        if (arguments.containsKey(KEY_INPUT_TYPE)) {
            editText.inputType = arguments.getInt(KEY_INPUT_TYPE, InputType.TYPE_CLASS_TEXT)
        }
        val maxLength = arguments.getInt(KEY_MAX_LENGTH)
        if (maxLength != Int.MAX_VALUE) {
            val filters = ArrayList<InputFilter>()
            val sourceFilters = editText.filters
            for (filter in sourceFilters) {
                if (filter !is LengthFilter) {
                    filters.add(filter)
                }
            }
            filters.add(LengthFilter(maxLength))
            editText.filters = filters.toTypedArray()
        }
        editText.setText(getInitialValue(context, arguments))
        return view
    }

    /**
     * ダイアログ生成処理
     *
     * @param savedInstanceState 前回強制終了時の保存データ
     * @return 生成した [Dialog]
     */
    override fun createDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = requireArguments()
        val buttonClickListener = dialogButtonClickListener
        val builder = AlertDialog.Builder(
            requireContext(), this.theme
        )
            .setTitle(arguments.getString(KEY_TITLE))
            .setMessage(arguments.getString(KEY_MESSAGE))
            .setPositiveButton(
                arguments.getString(KEY_POSITIVE_BUTTON_TITLE), buttonClickListener
            )
            .setNegativeButton(
                arguments.getString(KEY_NEGATIVE_BUTTON_TITLE), buttonClickListener
            )
        val view = inflateInputView(builder.context, arguments)
        return builder.setView(view).create()
    }

    companion object {
        /**
         * 結果キー定義:入力値
         */
        const val EXTRA_KEY_INPUT_VALUE = "inputValue"

        /**
         * ダイアログ引数キー:ヒント文言
         */
        /* protected */ const val KEY_HINT = "hint"

        /**
         * ダイアログ引数キー:入力種別
         */
        /* protected */ const val KEY_INPUT_TYPE = "inputType"

        /**
         * ダイアログ引数キー:最大入力可能文字数
         */
        /* protected */ const val KEY_MAX_LENGTH = "maxLength"

        // private const val TAG = "InputDialog"
    }
}