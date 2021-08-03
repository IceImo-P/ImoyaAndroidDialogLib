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
import androidx.fragment.app.FragmentManager
import net.imoya.android.dialog.DialogBase.Builder
import net.imoya.android.dialog.DialogBase.Listener
import net.imoya.android.util.Log

/**
 * ダイアログ [Fragment] の abstract
 *
 * 本ライブラリが提供する全ダイアログ [Fragment] の基底クラスです。
 *  * 親画面は [Listener] を実装した [Fragment] 又は [AppCompatActivity] を想定しています。
 *  [BuilderParent] の実装クラスを独自に作成することにより、他のクラスを親画面とすることも可能です。
 *  * [Builder] を継承したクラスを使用して表示内容を設定し、[Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [Listener.onDialogResult] メソッドがコールされます。
 *  * ダイアログがキャンセル終了した場合、 [Listener.onDialogResult] メソッドの引数 resultCode の値が
 *  [Activity.RESULT_CANCELED] となります。
 */
@Suppress("unused")
abstract class DialogBase : AppCompatDialogFragment(), DialogInterface.OnCancelListener {
    /**
     * ダイアログリスナ
     */
    interface Listener {
        /**
         * ダイアログ終了時にコールされます。
         *
         * @param requestCode [Builder] に設定したリクエストコード
         * @param resultCode 結果コード
         * @param data 追加のデータを含む [Intent]
         */
        fun onDialogResult(requestCode: Int, resultCode: Int, data: Intent?)
    }

    /**
     * ダイアログの親画面
     */
    interface BuilderParent {
        /**
         * Returns application [Context]
         */
        val context: Context

        /**
         * Returns [Listener]
         */
        val listener: Listener

        /**
         * Returns [FragmentManager]
         */
        val fragmentManager: FragmentManager
    }

    /**
     * ダイアログの親画面である [AppCompatActivity]
     */
    open class BuilderParentActivity<T>(
        protected val activity: T
    ) : BuilderParent where T : AppCompatActivity, T : Listener {
        override val context: Context
            get() = activity.applicationContext

        override val listener: Listener
            get() = activity

        override val fragmentManager: FragmentManager
            get() = activity.supportFragmentManager
    }

    /**
     * ダイアログの親画面である [Fragment]
     */
    open class BuilderParentFragment<T>(
        protected val fragment: T
    ) : BuilderParent where T : Fragment, T : Listener {
        override val context: Context
            get() = fragment.requireContext().applicationContext

        override val listener: Listener
            get() = fragment

        override val fragmentManager: FragmentManager
            get() = fragment.parentFragmentManager
    }

    /**
     * ダイアログビルダ
     */
    abstract class Builder(
        /**
         * 親画面
         */
        @JvmField
        protected val parent: BuilderParent,

        /**
         * リクエストコード
         */
        @JvmField
        protected val requestCode: Int
    ) {
        /**
         * タイトル
         */
        @JvmField
        protected var title: String? = null

        /**
         * メッセージ
         */
        @JvmField
        protected var message: String? = null

        /**
         * タグ
         */
        @JvmField
        protected var tag: String? = null

        /**
         * キャンセル可能フラグ
         */
        @JvmField
        protected var cancelable = true

        /**
         * ダイアログ外クリック時キャンセル実行フラグ
         */
        @JvmField
        protected var canceledOnTouchOutside = true

        /**
         * タイトル文言を設定します。
         *
         * @param title タイトル文言
         * @return [Builder]
         */
        open fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        /**
         * メッセージ文言を設定します。
         *
         * @param message メッセージ文言
         * @return [Builder]
         */
        open fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        /**
         * インスタンス識別用タグを設定します。
         *
         * @param tag タグ
         * @return [Builder]
         */
        open fun setTag(tag: String): Builder {
            this.tag = tag
            return this
        }

        /**
         * キャンセル可能フラグを設定します。
         * デフォルト値は true です。
         *
         * @param cancelable キャンセル可能フラグ
         * @return [Builder]
         */
        open fun setCancelable(cancelable: Boolean): Builder {
            this.cancelable = cancelable
            return this
        }

        /**
         * ダイアログ外クリック時キャンセル実行フラグを設定します。
         * デフォルト値は true です。
         *
         * @param canceledOnTouchOutside ダイアログ外クリック時キャンセル実行フラグ
         * @return [Builder]
         */
        open fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean): Builder {
            this.canceledOnTouchOutside = canceledOnTouchOutside
            return this
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return [DialogBase]
         */
        protected abstract fun createFragment(): DialogBase

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ [Bundle]
         */
        protected open fun makeArguments(): Bundle {
            val arguments = Bundle()
            arguments.putInt(KEY_REQUEST_CODE, requestCode)
            arguments.putString(EXTRA_KEY_TAG, tag)
            if (title != null) {
                arguments.putString(KEY_TITLE, title)
            }
            if (message != null) {
                arguments.putString(KEY_MESSAGE, message)
            }
            arguments.putBoolean(KEY_CANCELABLE, cancelable)
            arguments.putBoolean(KEY_CANCELED_ON_TOUCH_OUTSIDE, canceledOnTouchOutside)
            return arguments
        }

        /**
         * ダイアログを表示します。
         */
        open fun show() {
            val fragment = createFragment()
            fragment.arguments = makeArguments()
            fragment.listener = parent.listener
            fragment.show(parent.fragmentManager, tag)
        }

        protected val context: Context get() = parent.context
    }

    /**
     * ダイアログクリックリスナの実装
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
        protected val listener: Listener
    ) : DialogInterface.OnClickListener {
        /**
         * 押された項目
         */
        @JvmField
        protected var which = 0

        /**
         * ボタン押下時に [Listener.onDialogResult] へ入力する [Intent] を生成して返します。
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
    }

    /**
     * リスナ
     */
    protected lateinit var listener: Listener

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
        if (context is Listener) {
            listener = context
        }
    }

    /**
     * Called when a fragment is first attached to its owner [Activity].
     * [Fragment.onCreate] will be called after this.
     *
     * @param activity [Activity]
     */
    @Suppress("deprecation")
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity is Listener) {
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