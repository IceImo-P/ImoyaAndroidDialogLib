package net.imoya.android.dialog

import android.content.Intent

/**
 * ダイアログリスナ
 */
interface DialogListener {
    /**
     * ダイアログ終了時にコールされます。
     *
     * @param requestCode [DialogBuilder] に設定したリクエストコード
     * @param resultCode 結果コード
     * @param data 追加のデータを含む [Intent]
     */
    fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent?)
}