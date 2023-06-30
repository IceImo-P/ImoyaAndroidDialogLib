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
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

/**
 * ダイアログ [Fragment] の abstract
 *
 * 本ライブラリが提供する全ダイアログ [Fragment] の基底クラスです。
 *  * 親画面は [DialogListener] を実装した [Fragment] 又は [AppCompatActivity] を想定していますが、
 *  [DialogParent] の実装クラスを独自に作成することにより、他のクラスを親画面とすることも可能です。
 *  * [DialogBuilder] を継承したクラスを使用して表示内容を設定し、
 *  [DialogBuilder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogListener.onDialogResult] メソッドがコールされます。
 *  * ダイアログがキャンセル終了した場合、 [DialogListener.onDialogResult] メソッドの引数 resultCode の値が
 *  [Activity.RESULT_CANCELED] となります。
 */
abstract class DialogBase : DialogFragment(), DialogInterface.OnCancelListener {
    /**
     * リクエストコードを取得します。
     *
     * @return リクエストコード
     */
    val requestCode: Int
        get() = requireArguments().getInt(KEY_REQUEST_CODE)

    /**
     * ダイアログ生成処理
     *
     * @param savedInstanceState 前回強制終了時の保存データ
     * @return 生成した [Dialog]
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        DialogLog.v(TAG) { "onCreateDialog: $this: start" }

        val arguments = requireArguments()
        val dialog = createDialog(savedInstanceState)
        dialog.setCancelable(arguments.getBoolean(KEY_CANCELABLE, true))
        dialog.setCanceledOnTouchOutside(arguments.getBoolean(KEY_CANCELED_ON_TOUCH_OUTSIDE, true))

        DialogLog.v(TAG) { "onCreateDialog: $this: end" }

        return dialog
    }

    /**
     * ダイアログ生成処理
     *
     * @param savedInstanceState 前回強制終了時の保存データ
     * @return 生成した [Dialog]
     */
    protected abstract fun createDialog(savedInstanceState: Bundle?): Dialog

    /**
     * ダイアログキャンセル時処理
     *
     * @param dialog キャンセルされたダイアログ
     */
    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        try {
            // 親画面へ通知する
            callCancelListener()
        } catch (e: Exception) {
            DialogLog.e(TAG, "Exception", e)
        }
    }

    /**
     * リスナへキャンセルを通知します。
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun callCancelListener() {
        setDialogResult(Activity.RESULT_CANCELED, null)
    }

    /**
     * 親画面へ結果を通知します。
     *
     * @param resultCode 結果コード
     * @param data       追加データ
     */
    fun setDialogResult(resultCode: Int, data: Intent?) {
        DialogLog.v(TAG, "setDialogResult: start")

        DialogLog.v(TAG, "setDialogResult: calling setFragmentResult")
        val bundle = Bundle()
        bundle.putInt(KEY_INTERNAL_RESULT_CODE, resultCode)
        if (data != null) {
            bundle.putParcelable(KEY_INTERNAL_RESULT_DATA, data)
        }
        val requestKey = DialogUtil.getRequestKey(requestCode)
        parentFragmentManager.setFragmentResult(requestKey, bundle)
        DialogLog.v(TAG) {
            "setDialogResult: called setFragmentResult(\"$requestKey\", bundle)"
        }

        DialogLog.v(TAG, "setDialogResult: end")
    }

    companion object {
        /**
         * 結果キー定義:タグ
         */
        const val EXTRA_KEY_TAG = "DialogBase.tag"

        /**
         * 結果キー定義:クリックされた項目
         */
        const val EXTRA_KEY_WHICH = "DialogBase.which"

        /**
         * ダイアログ引数キー:リクエストコード
         */
        /* protected */ const val KEY_REQUEST_CODE = "requestCode"

        /**
         * ダイアログ引数キー:タイトル
         */
        /* protected */ const val KEY_TITLE = "title"

        /**
         * ダイアログ引数キー:メッセージ
         */
        /* protected */ const val KEY_MESSAGE = "message"

        /**
         * ダイアログ引数キー:カスタムビューのレイアウトリソースID
         */
        /* protected */ const val KEY_LAYOUT_RESOURCE_ID = "layoutResourceId"

        /**
         * ダイアログ引数キー:キャンセル可能フラグ
         */
        /* protected */ const val KEY_CANCELABLE = "cancelable"

        /**
         * ダイアログ引数キー:ダイアログ外クリック時キャンセル実行フラグ
         */
        /* protected */ const val KEY_CANCELED_ON_TOUCH_OUTSIDE = "canceledOnTouchOutside"

        /**
         * 内部キー定義: ダイアログ結果コード
         */
        const val KEY_INTERNAL_RESULT_CODE = "imoyaDialog-resultCode"

        /**
         * 内部キー定義: ダイアログ結果-追加データ
         */
        const val KEY_INTERNAL_RESULT_DATA = "imoyaDialog-resultData"

        /**
         * Tag for log
         */
        private const val TAG = "DialogBase"
    }
}