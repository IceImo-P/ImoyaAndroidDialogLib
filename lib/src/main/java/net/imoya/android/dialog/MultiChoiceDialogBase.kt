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
import net.imoya.android.dialog.MultiChoiceDialogBase.Builder
import net.imoya.android.dialog.MultiChoiceDialogBase.Companion.EXTRA_KEY_CHECKED_LIST
import net.imoya.android.log.LogUtil

/**
 * 複数項目選択ダイアログ
 *
 * タイトル, 複数選択リスト, OKボタン, キャンセルボタンを持つダイアログ [Fragment] です。
 *  * [Builder]を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogListener.onDialogResult] メソッドがコールされます。
 *  * OKボタン押下に伴うダイアログ終了時、 [DialogListener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。このとき、引数 data の
 * [Intent.getBooleanArrayExtra] へ [EXTRA_KEY_CHECKED_LIST]
 * を入力することで、全項目の選択状態リストを取得できます。
 *  * OKボタン押下以外の理由でダイアログが終了した場合は、
 * [DialogListener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 */
@Suppress("unused")
abstract class MultiChoiceDialogBase : OkCancelDialog() {
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
        @Suppress("MemberVisibilityCanBePrivate")
        @JvmField
        protected var items: Array<String>? = null

        /**
         * 全項目の初期選択状態リスト
         */
        @Suppress("MemberVisibilityCanBePrivate")
        @JvmField
        protected var checkedList: BooleanArray? = null

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
         *  * [Builder.show] メソッドを呼び出す前に、必ず設定してください。
         *
         * @param items 選択項目リスト
         * @return [Builder]
         */
        open fun setItems(items: Array<String>): Builder {
            this.items = items
            return this
        }

        /**
         * 全選択項目の初期選択状態リストを設定します。
         *  * [.show] メソッドを呼び出す前にこのメソッドを呼び出さなかった場合や、
         * null を指定した場合は、全項目が未選択となります。
         *  * null 以外を設定する場合は、必ず [.setItems]
         * メソッドと同一の項目数を設定してください。
         *
         *
         * @param checkedList 全選択項目の選択状態リスト
         * @return [Builder]
         */
        open fun setCheckedList(checkedList: BooleanArray?): Builder {
            this.checkedList = checkedList
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
            arguments.putBooleanArray(EXTRA_KEY_CHECKED_LIST, checkedList)
            return arguments
        }

        override fun show() {
            if (items == null) {
                throw RuntimeException("items == null")
            }
            super.show()
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    protected class DialogButtonClickListener(
        dialog: MultiChoiceDialogBase
    ) : OkCancelDialog.DialogButtonClickListener(dialog) {
        override fun makeData(): Intent {
            val data = super.makeData()
            data.putExtra(
                EXTRA_KEY_CHECKED_LIST, (dialog as MultiChoiceDialogBase).checkedList
            )
            return data
        }
    }

    /**
     * 選択項目リスト
     */
    protected lateinit var items: Array<String>

    /**
     * 全項目の現在の選択状態を保持する配列
     */
    protected lateinit var checkedList: BooleanArray

    /**
     * [Fragment] 生成時の処理
     *
     * @param savedInstanceState 再生成前に保存された情報
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val arguments = requireArguments()

        // 選択項目リストをダイアログ引数より取得する
        val tmpItems = arguments.getStringArray(KEY_ITEMS)
            ?: throw RuntimeException("arguments[KEY_ITEMS] == null")
        // 参照する引数の変化を防ぐため、必ずcloneする
        items = tmpItems.clone()

        // 生成時のチェック状態を、ダイアログ引数又は再生成前の保存情報より取得する
        val tmpCheckedList = if (savedInstanceState != null) savedInstanceState.getBooleanArray(
            EXTRA_KEY_CHECKED_LIST
        ) else arguments.getBooleanArray(EXTRA_KEY_CHECKED_LIST)
        DialogLog.v(TAG) { "onCreate: tmpCheckedList = ${LogUtil.logString(tmpCheckedList)}" }
        if (tmpCheckedList != null && tmpCheckedList.size != items.size) {
            // チェック状態指定が選択項目数と異なる場合は、警告ログを出力する
            DialogLog.w(TAG) {
                "onCreate: Illegal checked list length(Item count is ${
                    items.size
                } but checked list count is ${
                    tmpCheckedList.size
                })"
            }
        }
        // 参照する引数の変化を防ぐため、必ずcloneする
        // 引数が存在しない場合や項目数が異なる場合は、リストを新規作成する
        checkedList =
            if (tmpCheckedList != null && tmpCheckedList.size == items.size) tmpCheckedList.clone()
            else BooleanArray(items.size)
        DialogLog.v(TAG) {
            "onCreate: items = ${
                LogUtil.logString(items)
            }, checkedList = ${
                LogUtil.logString(checkedList)
            }"
        }
    }

    /**
     * [Fragment] 再生成前の状態保存処理
     *
     * @param outState 保存先
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBooleanArray(EXTRA_KEY_CHECKED_LIST, checkedList)
    }

    companion object {
        /**
         * 結果キー定義:選択位置
         */
        const val EXTRA_KEY_CHECKED_LIST = "checkedList"

        /**
         * ダイアログ引数キー:選択項目リスト
         */
        /* protected */ const val KEY_ITEMS = "items"

        /**
         * Tag for log
         */
        private const val TAG = "MultiChoiceDialogBase"
    }
}