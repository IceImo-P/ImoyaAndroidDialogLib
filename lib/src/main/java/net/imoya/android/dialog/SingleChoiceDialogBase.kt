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
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.SingleChoiceDialogBase.Builder
import net.imoya.android.log.LogUtil

/**
 * 単一項目選択ダイアログの abstract
 *
 * タイトル, 単一選択リスト, OKボタン, キャンセルボタンを持つダイアログ [Fragment] の abstract です。
 *  * [Builder]を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogListener.onDialogResult] メソッドがコールされます。
 *  * OKボタン押下に伴うダイアログ終了時、 [DialogListener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。このとき、引数 data の [Intent.getIntExtra] へ
 *  [DialogBase.EXTRA_KEY_WHICH] を入力することで、選択された項目の位置(又は未選択を表す -1)を取得できます。
 *  * OKボタン押下以外の理由でダイアログが終了した場合は、
 * [DialogListener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 */
abstract class SingleChoiceDialogBase : OkCancelDialog() {
    /**
     * ダイアログビルダ
     *
     * @param parent    親画面
     * @param requestCode リクエストコード
     */
    abstract class Builder(parent: DialogParent, requestCode: Int) :
        OkCancelDialog.Builder(parent, requestCode) {
        /**
         * 選択項目リスト
         */
        private var items: Array<String>? = null

        /**
         * 初期選択位置
         */
        private var selectedPosition = -1

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
         * このメソッドは使用できません。
         *
         * @param message メッセージ文言
         * @return [Builder]
         * @throws IllegalArgumentException このメソッドが呼び出されました。
         */
        override fun setMessage(message: String): Builder {
            throw IllegalArgumentException(
                "Don't use setMessage for StringArrayDialog. Use setTitle() instead."
            )
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
         * 選択項目リストを設定します。
         *
         * @param items 選択項目リスト
         * @return [Builder]
         */
        open fun setItems(items: Array<String>?): Builder {
            this.items = items
            return this
        }

        /**
         * 初期選択位置を設定します。
         *
         * @param selectedPosition 初期選択位置、又は未選択状態を指定する -1
         * @return [Builder]
         */
        open fun setSelectedPosition(selectedPosition: Int): Builder {
            this.selectedPosition = selectedPosition
            return this
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ [Bundle]
         */
        override fun makeArguments(): Bundle {
            val arguments = super.makeArguments()
            arguments.putStringArray(KEY_ITEMS, items)
            arguments.putInt(EXTRA_KEY_WHICH, selectedPosition)
            return arguments
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    protected class DialogButtonClickListener(
        dialog: SingleChoiceDialogBase
    ) : OkCancelDialog.DialogButtonClickListener(dialog) {
        /**
         * ボタン押下時に [DialogListener.onDialogResult] へ入力する [Intent] を生成して返します。
         *
         * @return [Intent]
         */
        override fun makeData(): Intent {
            val data = super.makeData()
            data.putExtra(
                EXTRA_KEY_WHICH, (dialog as SingleChoiceDialogBase).selectedPosition
            )
            return data
        }
    }

    /**
     * 選択項目リスト
     */
    protected lateinit var items: Array<String>

    /**
     * 現在の選択位置
     */
    @JvmField
    protected var selectedPosition = -1

    /**
     * [Fragment] 生成時の処理
     *
     * @param savedInstanceState 再生成前に保存された情報
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = requireArguments()

        // 選択項目リストを、ダイアログ引数より取得する
        val tmpItems = arguments.getStringArray(KEY_ITEMS)
            ?: // 必ず指定しなければならない
            throw RuntimeException("arguments[KEY_ITEMS] == null")
        // 参照する引数の変化を防ぐため、必ずcloneする
        items = tmpItems.clone()

        // 生成時の選択位置を、ダイアログ引数又は再生成前の保存情報より取得する
        selectedPosition = savedInstanceState?.getInt(EXTRA_KEY_WHICH)
            ?: arguments.getInt(EXTRA_KEY_WHICH, -1)
        if (selectedPosition < -1 || selectedPosition >= items.size) {
            // 異常値の場合は、未選択とする
            DialogLog.w(TAG) { "onCreate: Illegal position($selectedPosition)" }
            selectedPosition = -1
        }
        DialogLog.v(TAG) {
            "onCreate: items = ${
                LogUtil.logString(items)
            }, selectedPosition = $selectedPosition"
        }
    }

    /**
     * [Fragment] 再生成前の状態保存処理
     *
     * @param outState 保存先
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(EXTRA_KEY_WHICH, selectedPosition)
    }

    companion object {
        /**
         * ダイアログ引数キー:選択項目リスト
         */
        /* protected */ const val KEY_ITEMS = "items"

        /**
         * Tag for log
         */
        private const val TAG = "SingleChoiceDialogBase"
    }
}