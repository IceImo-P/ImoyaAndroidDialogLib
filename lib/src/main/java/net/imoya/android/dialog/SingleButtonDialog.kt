package net.imoya.android.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.SingleButtonDialog.Builder

/**
 * ボタン1個のダイアログ
 *
 * タイトル, メッセージ, 1個のボタンを持つダイアログ [Fragment] です。
 *  * 親画面は [DialogBase.Listener] を実装した [Fragment] 又は [AppCompatActivity] を想定しています。
 *  * [Builder] を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogBase.Listener.onDialogResult] メソッドがコールされます。
 *  * ボタン押下に伴うダイアログ終了時、 [DialogBase.Listener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。
 *  * ボタン押下以外の理由でダイアログが終了した場合、
 * [DialogBase.Listener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 */
open class SingleButtonDialog : DialogBase() {
    /**
     * ダイアログビルダ
     */
    open class Builder(parent: BuilderParent, requestCode: Int) :
        DialogBase.Builder(parent, requestCode) {
        /**
         * ボタン文言
         */
        protected open var buttonTitle: String? = null

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
        open fun setButtonTitle(buttonTitle: String): Builder {
            this.buttonTitle = buttonTitle
            return this
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return [DialogBase]
         */
        override fun createFragment(): DialogBase {
            return SingleButtonDialog()
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ [Bundle]
         */
        override fun makeArguments(): Bundle {
            val arguments = super.makeArguments()
            if (buttonTitle != null) {
                arguments.putString(KEY_BUTTON_TITLE, buttonTitle)
            } else {
                arguments.putString(
                    KEY_BUTTON_TITLE,
                    context.getString(android.R.string.ok)
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
        return AlertDialog.Builder(requireActivity(), this.theme)
            .setTitle(arguments.getString(KEY_TITLE))
            .setMessage(arguments.getString(KEY_MESSAGE))
            .setPositiveButton(
                arguments.getString(KEY_BUTTON_TITLE),
                DialogItemClickListener(this, listener)
            )
            .create()
    }

    companion object {
        /**
         * ダイアログ引数キー:ボタン文言
         */
        /* protected */ const val KEY_BUTTON_TITLE = "buttonTitle"

//        /**
//         * Tag for log
//         */
//        private const val TAG = "SingleButtonDialog"
    }
}