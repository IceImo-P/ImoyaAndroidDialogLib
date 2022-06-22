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
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.SingleButtonAndCheckDialog.Builder

/**
 * ボタン1個とチェックボックス1個のダイアログ
 *
 * タイトル, メッセージ, 1個のチェックボックス, 1個のボタンを持つダイアログ [Fragment] です。
 *  * [Builder]を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogListener.onDialogResult] メソッドがコールされます。
 *  * ボタン押下に伴うダイアログ終了時、 [DialogListener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。このとき、引数 data の
 *  [Intent.getBooleanExtra] メソッドへ [SingleButtonAndCheckDialog.EXTRA_KEY_CHECKED], false
 * を入力することで、チェックボックスのチェック有無を取得できます。
 *  * ボタン押下以外の理由でダイアログが終了した場合、
 * [DialogListener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 */
@Suppress("unused")
open class SingleButtonAndCheckDialog : SingleButtonDialog() {
    /**
     * ダイアログビルダ
     *
     * @param parent    親画面
     * @param requestCode リクエストコード
     */
    open class Builder(parent: DialogParent, requestCode: Int) :
        SingleButtonDialog.Builder(parent, requestCode) {
        /**
         * チェックボックス文言
         */
        @Suppress("MemberVisibilityCanBePrivate")
        protected var checkBoxText: String? = null

        /**
         * チェックボックス初期状態
         */
        @Suppress("MemberVisibilityCanBePrivate")
        protected var checked = false

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
        override fun setButtonTitle(buttonTitle: String): Builder {
            super.setButtonTitle(buttonTitle)
            return this
        }

        /**
         * チェックボックス文言を設定します。
         *
         * @param checkBoxText チェックボックス文言
         * @return [Builder]
         */
        fun setCheckBoxText(checkBoxText: String?): Builder {
            this.checkBoxText = checkBoxText
            return this
        }

        /**
         * チェックボックス初期状態を設定します。
         *
         * @param checked チェックボックス初期状態
         * @return [Builder]
         */
        fun setChecked(checked: Boolean): Builder {
            this.checked = checked
            return this
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return [DialogBase]
         */
        override fun createFragment(): DialogBase {
            return SingleButtonAndCheckDialog()
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ [Bundle]
         */
        override fun makeArguments(): Bundle {
            val arguments = super.makeArguments()
            if (checkBoxText != null) {
                arguments.putString(KEY_CHECK_BOX_TEXT, checkBoxText)
            }
            arguments.putBoolean(EXTRA_KEY_CHECKED, checked)
            return arguments
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    private class DialogButtonClickListener(
        dialog: DialogBase
    ) : DialogItemClickHandler(dialog) {
        override fun makeData(): Intent {
            // チェックボックス状態を含める
            val data = Intent()
            data.putExtra(
                EXTRA_KEY_CHECKED,
                (dialog as SingleButtonAndCheckDialog).isChecked
            )
            return data
        }
    }

    /**
     * チェックボックス状態を取得します。
     *
     * @return チェック時true、未チェック時false
     */
    val isChecked: Boolean
        get() = (this.dialog?.findViewById<View>(R.id.check) as CompoundButton).isChecked

    override fun createDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = requireArguments()
        val builder = AlertDialog.Builder(requireContext(), theme)
            .setTitle(arguments.getString(KEY_TITLE))
            .setMessage(arguments.getString(KEY_MESSAGE))
            .setPositiveButton(
                arguments.getString(KEY_BUTTON_TITLE),
                DialogButtonClickListener(this)
            )
        val view = LayoutInflater.from(builder.context).inflate(
            arguments.getInt(
                KEY_LAYOUT_RESOURCE_ID, R.layout.dialog_single_button_and_check
            ), null
        )
        val checkBox = view.findViewById<CompoundButton>(R.id.check)
        checkBox.text = arguments.getString(KEY_CHECK_BOX_TEXT, "")
        checkBox.isChecked = arguments.getBoolean(EXTRA_KEY_CHECKED)
        return builder.setView(view).create()
    }

    companion object {
        /**
         * 結果キー定義:チェックボックス状態
         */
        const val EXTRA_KEY_CHECKED = "checked"

        /**
         * ダイアログ引数キー:入力部分のレイアウトリソースID
         */
        /* protected */ const val KEY_LAYOUT_RESOURCE_ID = "layoutResourceId"

        /**
         * ダイアログ引数キー:チェックボックス文言
         */
        /* protected */ const val KEY_CHECK_BOX_TEXT = "checkBoxText"

//        /**
//         * Tag for log
//         */
//        private const val TAG = "SingleButtonDialog"
    }
}