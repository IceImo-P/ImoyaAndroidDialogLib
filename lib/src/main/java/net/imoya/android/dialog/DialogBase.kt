package net.imoya.android.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import net.imoya.android.util.Log

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
@Suppress("unused")
abstract class DialogBase : AppCompatDialogFragment(), DialogInterface.OnCancelListener {
    @Deprecated("Use DialogListener")
    interface Listener : DialogListener

    @Deprecated("Use DialogParent")
    interface BuilderParent : DialogParent

    @Deprecated("Use DialogParentActivity")
    class BuilderParentActivity<T>(activity: T) :
        DialogParentActivity<T>(activity) where T : AppCompatActivity, T : DialogListener

    @Deprecated("Use DialogParentFragment")
    class BuilderParentFragment<T>(fragment: T) :
        DialogParentFragment<T>(fragment) where T : Fragment, T : DialogListener

    @Deprecated("Use DialogBuilder")
    abstract class Builder(parent: DialogParent, requestCode: Int) :
        DialogBuilder(parent, requestCode)

    /**
     * ダイアログのボタンクリックを [DialogListener] へ通知するロジック
     */
    protected open class DialogItemClickListener(
        /**
         * ダイアログ
         */
        @JvmField
        protected val dialog: DialogBase,
        /**
         * ダイアログのリスナ
         */
        @JvmField
        protected val listener: DialogListener
    ) : DialogInterface.OnClickListener {
        /**
         * 押された項目
         */
        @JvmField
        protected var which = 0

        /**
         * ボタン押下時に [DialogListener.onDialogResult] へ入力する [Intent] を生成して返します。
         *
         * @return [Intent]
         */
        protected open fun makeData(): Intent {
            val data = Intent()
            data.putExtra(EXTRA_KEY_WHICH, which)
            val tag = dialog.tag
            if (tag != null) {
                data.putExtra(EXTRA_KEY_TAG, tag)
            }
            return data
        }

        /**
         * ダイアログリスナへ、クリックを通知します。
         *
         * @param dialogInterface [DialogInterface]
         * @param which           クリックされた項目
         */
        protected open fun callListener(dialogInterface: DialogInterface, which: Int) {
            listener.onDialogResult(
                dialog.requestCode, Activity.RESULT_OK, makeData()
            )
        }

        /**
         * 項目クリック時の処理
         *
         * @param dialogInterface [DialogInterface]
         * @param which           クリックされた項目
         */
        override fun onClick(dialogInterface: DialogInterface, which: Int) {
            this.which = which
            try {
                // 親画面へ通知する
                callListener(dialogInterface, which)
            } catch (e: Exception) {
                Log.e(TAG, "Exception", e)
            }
        }

        companion object {
            /**
             * Tag for log
             */
            private const val TAG = "DialogItemClickListener"
        }
    }

    /**
     * リスナ
     */
    lateinit var listener: DialogListener

    /**
     * リクエストコードを取得します。
     *
     * @return リクエストコード
     */
    val requestCode: Int
        get() = requireArguments().getInt(KEY_REQUEST_CODE)

    /**
     * Called when a fragment is first attached to its context.
     * [Fragment.onCreate] will be called after this.
     *
     * @param context [Context]
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DialogListener) {
            listener = context
        }
    }

    /**
     * Called when a fragment is first attached to its owner [Activity].
     * [Fragment.onCreate] will be called after this.
     *
     * @param activity [Activity]
     */
    @Deprecated("Deprecated in Java")
    @Suppress("deprecation")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is DialogListener) {
            listener = activity
        }
    }

    /**
     * ダイアログ生成処理
     *
     * @param savedInstanceState 前回強制終了時の保存データ
     * @return 生成した [Dialog]
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = requireArguments()
        val dialog = createDialog(savedInstanceState)
        dialog.setCancelable(arguments.getBoolean(KEY_CANCELABLE, true))
        dialog.setCanceledOnTouchOutside(arguments.getBoolean(KEY_CANCELED_ON_TOUCH_OUTSIDE, true))
        dialog.setOnCancelListener(this)
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
            Log.e(TAG, "Exception", e)
        }
    }

    /**
     * リスナへキャンセルを通知します。
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected fun callCancelListener() {
        if (::listener.isInitialized) {
            listener.onDialogResult(requestCode, Activity.RESULT_CANCELED, null)
        }
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
         * Tag for log
         */
        private const val TAG = "DialogBase"
    }
}