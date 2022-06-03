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
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.SeekBarAndButtonDialog.Builder

/**
 * シークバー付き、数値入力ダイアログ
 *
 * タイトル, メッセージ, シークバー, 入力欄, OKボタン, キャンセルボタンを持つダイアログ [Fragment] です。
 *  * [Builder]を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogListener.onDialogResult] メソッドがコールされます。
 *  * OKボタン押下に伴うダイアログ終了時、 [DialogListener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。このとき、引数 data の [Intent.getIntExtra] へ
 * [SeekBarInputDialog.EXTRA_KEY_INPUT_VALUE], 0 を入力することで、入力された値を取得できます。
 *  * OKボタン押下以外の理由でダイアログが終了した場合は、
 * [DialogListener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 */
@Suppress("unused")
open class SeekBarAndButtonDialog : SeekBarInputDialog(), View.OnClickListener {
    /**
     * ダイアログビルダ
     *
     * @param parent    親画面
     * @param requestCode リクエストコード
     */
    open class Builder(parent: DialogParent, requestCode: Int) :
        SeekBarInputDialog.Builder(parent, requestCode) {
        /**
         * 追加ボタン文言
         */
        @JvmField
        protected var additionalButtonText: String? = null

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
         *  * スライダ(シークバー)となる、 id="slider" である [SeekBar]
         *  * 入力欄となる、 id="value" である [EditText]
         *  * 追加ボタンとなる、 id="button" である [Button]
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
         * 最小値を設定します。
         *
         * @param min 最小値
         * @return [Builder]
         */
        override fun setMin(min: Int): Builder {
            super.setMin(min)
            return this
        }

        /**
         * 最大値を設定します。
         *
         * @param max 最大値
         * @return [Builder]
         */
        override fun setMax(max: Int): Builder {
            super.setMax(max)
            return this
        }

        /**
         * 初期値を設定します。
         *
         * @param value 初期値
         * @return [Builder]
         */
        override fun setValue(value: Int): Builder {
            super.setValue(value)
            return this
        }

        /**
         * 追加ボタン文言を設定します。
         *
         * @param additionalButtonText 追加ボタン文言
         * @return [Builder]
         */
        open fun setAdditionalButtonText(additionalButtonText: String?): Builder {
            this.additionalButtonText = additionalButtonText
            return this
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return [DialogBase]
         */
        override fun createFragment(): DialogBase {
            return SeekBarAndButtonDialog()
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ [Bundle]
         */
        override fun makeArguments(): Bundle {
            val arguments = super.makeArguments()
            arguments.putString(KEY_ADDITIONAL_BUTTON_TEXT, additionalButtonText)
            return arguments
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    private class DialogButtonClickListener(
        dialog: SeekBarAndButtonDialog,
        listener: DialogListener
    ) : OkCancelDialog.DialogButtonClickListener(dialog, listener) {
        override fun makeData(): Intent {
            val intent = super.makeData()
            intent.putExtra(
                EXTRA_KEY_INPUT_VALUE,
                (dialog as SeekBarAndButtonDialog).value
            )
            return intent
        }
    }

    override fun createDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = requireArguments()
        min = arguments.getInt(KEY_MIN, 0)
        max = arguments.getInt(KEY_MAX, 100)
        val builder = AlertDialog.Builder(
            requireActivity(), this.theme
        )
            .setTitle(arguments.getString(KEY_TITLE))
            .setMessage(arguments.getString(KEY_MESSAGE))
            .setPositiveButton(
                arguments.getString(KEY_POSITIVE_BUTTON_TITLE),
                DialogButtonClickListener(this, listener)
            )
            .setNegativeButton(
                arguments.getString(KEY_NEGATIVE_BUTTON_TITLE),
                DialogButtonClickListener(this, listener)
            )
        val view = LayoutInflater.from(builder.context).inflate(
            arguments.getInt(KEY_LAYOUT_RESOURCE_ID, R.layout.dialog_seekbar_and_button),
            null
        )
        editText = view.findViewById(R.id.value)
        editText.setText(arguments.getInt(EXTRA_KEY_INPUT_VALUE).toString())
        editText.addTextChangedListener(this)
        seekBar = view.findViewById(R.id.slider)
        seekBar.max = max - min
        seekBar.progress = arguments.getInt(EXTRA_KEY_INPUT_VALUE)
        seekBar.setOnSeekBarChangeListener(this)
        val additionalButton = view.findViewById<Button>(R.id.button)
        additionalButton.text = arguments.getString(KEY_ADDITIONAL_BUTTON_TEXT)
        additionalButton.setOnClickListener(this)
        return builder.setView(view).create()
    }

    override fun onClick(view: View) {
        DialogLog.i(TAG, "Additional Button is clicked")
    }

    companion object {
        /**
         * ダイアログ引数キー:追加ボタン文言
         */
        /* protected */ const val KEY_ADDITIONAL_BUTTON_TEXT = "additionalButtonText"

        /**
         * Tag for log
         */
        private const val TAG = "SeekBarInputDialog"
    }
}