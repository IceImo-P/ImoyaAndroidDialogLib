package net.imoya.android.dialog

import android.content.Context
import android.os.Bundle

/**
 * ダイアログビルダの基底クラス
 *
 * @see [DialogBase]
 *
 * @param parent    親画面
 * @param requestCode リクエストコード
 */
abstract class DialogBuilder(
    /**
     * 親画面
     */
    @JvmField
    protected val parent: DialogParent,

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
     * @return [DialogBuilder]
     */
    open fun setTitle(title: String): DialogBuilder {
        this.title = title
        return this
    }

    /**
     * メッセージ文言を設定します。
     *
     * @param message メッセージ文言
     * @return [DialogBuilder]
     */
    open fun setMessage(message: String): DialogBuilder {
        this.message = message
        return this
    }

    /**
     * インスタンス識別用タグを設定します。
     *
     * @param tag タグ
     * @return [DialogBuilder
     */
    open fun setTag(tag: String): DialogBuilder {
        this.tag = tag
        return this
    }

    /**
     * キャンセル可能フラグを設定します。
     * デフォルト値は true です。
     *
     * @param cancelable キャンセル可能フラグ
     * @return [DialogBuilder]
     */
    open fun setCancelable(cancelable: Boolean): DialogBuilder {
        this.cancelable = cancelable
        return this
    }

    /**
     * ダイアログ外クリック時キャンセル実行フラグを設定します。
     * デフォルト値は true です。
     *
     * @param canceledOnTouchOutside ダイアログ外クリック時キャンセル実行フラグ
     * @return [DialogBuilder]
     */
    open fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean): DialogBuilder {
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
        arguments.putInt(DialogBase.KEY_REQUEST_CODE, requestCode)
        arguments.putString(DialogBase.EXTRA_KEY_TAG, tag)
        if (title != null) {
            arguments.putString(DialogBase.KEY_TITLE, title)
        }
        if (message != null) {
            arguments.putString(DialogBase.KEY_MESSAGE, message)
        }
        arguments.putBoolean(DialogBase.KEY_CANCELABLE, cancelable)
        arguments.putBoolean(DialogBase.KEY_CANCELED_ON_TOUCH_OUTSIDE, canceledOnTouchOutside)
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