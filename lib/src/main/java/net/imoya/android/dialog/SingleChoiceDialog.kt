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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.SingleChoiceDialog.Builder

/**
 * 単一項目選択ダイアログ
 *
 * タイトル, 単一選択リスト, OKボタン, キャンセルボタンを持つダイアログ [Fragment] です。
 *  * [Builder]を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogListener.onDialogResult] メソッドがコールされます。
 *  * OKボタン押下に伴うダイアログ終了時、 [DialogListener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。このとき、引数 data の [Intent.getIntExtra] へ
 * [DialogBase.EXTRA_KEY_WHICH] を入力することで、選択された項目の位置(又は未選択を表す -1)を取得できます。
 *  * OKボタン押下以外の理由でダイアログが終了した場合は、
 * [DialogListener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 */
@Suppress("unused")
class SingleChoiceDialog : SingleChoiceDialogBase() {
    /**
     * ダイアログビルダ
     *
     * @param parent    親画面
     * @param requestCode リクエストコード
     */
    class Builder(parent: DialogParent, requestCode: Int) :
        SingleChoiceDialogBase.Builder(parent, requestCode) {
        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return [DialogBase]
         */
        override fun createFragment(): DialogBase {
            return SingleChoiceDialog()
        }
    }

    override fun createDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = requireArguments()
        return AlertDialog.Builder(requireContext(), this.theme)
            .setTitle(arguments.getString(KEY_TITLE))
            .setMessage(arguments.getString(KEY_MESSAGE))
            .setSingleChoiceItems(
                items, selectedPosition
            ) { _: DialogInterface?, which: Int -> selectedPosition = which }
            .setPositiveButton(
                arguments.getString(KEY_POSITIVE_BUTTON_TITLE),
                DialogButtonClickListener(this, listener)
            )
            .setNegativeButton(
                arguments.getString(KEY_NEGATIVE_BUTTON_TITLE),
                DialogButtonClickListener(this, listener)
            )
            .create()
    }

//    companion object {
//        /**
//         * Tag for log
//         */
//        private const val TAG = "SingleChoiceDialog"
//    }
}