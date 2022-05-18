package net.imoya.android.dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.NumberInputDialog.Builder

/**
 * 整数値入力ダイアログ
 *
 * タイトル, メッセージ, 数値入力欄, 単位表示, OKボタン, キャンセルボタンを持つダイアログ [Fragment] です。
 *  * [Builder]を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogListener.onDialogResult] メソッドがコールされます。
 *  * OKボタン押下に伴うダイアログ終了時、 [DialogListener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。このとき、引数 data の [Intent.getIntExtra] へ
 * [InputDialog.EXTRA_KEY_INPUT_VALUE] を入力することで、入力された整数値を取得できます。
 * 但し空文字を入力されたか、数字以外の文字を入力された場合は、 [InputDialog.EXTRA_KEY_INPUT_VALUE]
 * の extra が存在しません([Intent.hasExtra] で判定してください)。
 *  * OKボタン押下以外の理由でダイアログが終了した場合は、
 * [DialogListener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 */
@Suppress("unused")
class NumberInputDialog : InputDialog() {
    /**
     * ダイアログビルダ
     *
     * @param parent    親画面
     * @param requestCode リクエストコード
     */
    open class Builder(parent: DialogParent, requestCode: Int) :
        InputDialog.Builder(parent, requestCode) {
        /**
         * 入力初期値
         */
        @JvmField
        protected var number = 0

        /**
         * 入力初期値設定済フラグ
         */
        @JvmField
        protected var numberIsSet = false

        /**
         * 単位
         */
        @JvmField
        protected var unit: String? = null

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
         * positiveボタン文言を設定します。デフォルト値は [android.R.string.ok] です。
         *
         * @param buttonTitle ボタン文言
         * @return [Builder]
         */
        override fun setPositiveButtonTitle(buttonTitle: String): Builder {
            super.setPositiveButtonTitle(buttonTitle)
            return this
        }

        /**
         * negativeボタン文言を設定します。デフォルト値は [android.R.string.cancel] です。
         *
         * @param buttonTitle ボタン文言
         * @return [Builder]
         */
        override fun setNegativeButtonTitle(buttonTitle: String): Builder {
            super.setNegativeButtonTitle(buttonTitle)
            return this
        }

        /**
         * 入力部分のレイアウトリソースIDを設定します。
         *
         *
         * リソースには、最低限次の要素が必要です:
         *  * 入力欄となる、 id="text" である [EditText]
         *  * 入力欄となる、 id="unit" である [TextView]
         *
         *
         * @param layoutResourceId 入力部分のレイアウトリソースID
         * @return [Builder]
         */
        override fun setLayoutResourceId(layoutResourceId: Int): Builder {
            super.setLayoutResourceId(layoutResourceId)
            return this
        }

        /**
         * ヒント文言を設定します。
         *
         * @param hint ヒント文言
         * @return [Builder]
         */
        override fun setHint(hint: String?): Builder {
            super.setHint(hint)
            return this
        }

        /**
         * 入力種別を設定します。
         *
         * @param inputType 入力種別
         * @return [Builder]
         */
        override fun setInputType(inputType: Int): Builder {
            super.setInputType(inputType)
            return this
        }

        /**
         * 入力初期値を設定します。
         *
         * @param number 入力初期値
         * @return [Builder]
         */
        open fun setNumber(number: Int): Builder {
            this.number = number
            numberIsSet = true
            return this
        }

        /**
         * 単位を設定します。
         *
         * @param unit 単位
         * @return [Builder]
         */
        open fun setUnit(unit: String?): Builder {
            this.unit = unit
            return this
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return [DialogBase]
         */
        override fun createFragment(): DialogBase {
            return NumberInputDialog()
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ [Bundle]
         */
        override fun makeArguments(): Bundle {
            val arguments = super.makeArguments()
            if (numberIsSet) {
                arguments.putInt(EXTRA_KEY_INPUT_VALUE, number)
            }
            if (unit != null) {
                arguments.putString(KEY_UNIT, unit)
            }
            return arguments
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    private class DialogButtonClickListener(
        dialog: NumberInputDialog, listener: DialogListener
    ) : InputDialog.DialogButtonClickListener(dialog, listener) {
        override fun makeData(): Intent {
            val intent = super.makeData()
            val dialog = dialog as NumberInputDialog
            if (dialog.isValidInputValue) {
                intent.putExtra(EXTRA_KEY_INPUT_VALUE, dialog.inputValue)
            }
            return intent
        }
    }

    /**
     * 入力された整数値を返します。
     * 入力欄が空であるか、数値以外の文字を入力された場合は、0を返します。
     *
     * @return 入力された値
     */
    val inputValue: Int
        get() {
            val text = inputText
            return if (isValidInputValue(text)) {
                text.toInt(10)
            } else {
                0
            }
        }

    /**
     * 入力欄が空でなく、且つ整数値が入力されているか否かを返します。
     *
     * @return 入力欄が空でなく、且つ整数値が入力されている場合はtrue、その他の場合はfalse
     */
    val isValidInputValue: Boolean
        get() = isValidInputValue(inputText)

    /**
     * 入力欄の文字列を取得します。
     *
     * @return 文字列
     */
    private val inputText: String
        get() = (this.dialog?.findViewById<View>(R.id.text) as EditText).text.toString()

    /**
     * ボタンクリック時の処理を実装した
     * [InputDialog.DialogButtonClickListener] を返します。
     * 返された
     * [InputDialog.DialogButtonClickListener] は、ダイアログ全ボタンのリスナとなります。
     *
     * @return [InputDialog.DialogButtonClickListener]
     */
    override val dialogButtonClickListener: InputDialog.DialogButtonClickListener
        get() = DialogButtonClickListener(this, listener)

    /**
     * ダイアログへ組み込む入力部分の [View]
     * を指定する、レイアウトリソースIDのデフォルト値を返します。
     *
     * @return レイアウトリソースID
     */
    @get:LayoutRes
    override val defaultInputViewLayoutResourceId: Int
        get() = R.layout.dialog_number_input

    /**
     * 入力欄へ初期表示する内容を返します。
     *
     * @param context   [Context]
     * @param arguments ダイアログ引数を含む [Bundle]
     * @return 入力欄へ表示する文字列
     */
    override fun getInitialValue(context: Context, arguments: Bundle): CharSequence {
        return if (arguments.containsKey(EXTRA_KEY_INPUT_VALUE)) {
            arguments.getInt(EXTRA_KEY_INPUT_VALUE).toString()
        } else {
            ""
        }
    }

    /**
     * ダイアログへ組み込む、入力部分の [View] を生成して返します。
     *
     * @param context   [Context]
     * @param arguments ダイアログ引数を含む [Bundle]
     * @return [View]
     */
    override fun inflateInputView(context: Context, arguments: Bundle): View {
        val view = super.inflateInputView(context, arguments)

        // 単位を表示する
        val unitView = view.findViewById<TextView>(R.id.unit)
        val unit = arguments.getString(KEY_UNIT)
        unitView.text = unit
        unitView.visibility = if (unit != null) View.VISIBLE else View.GONE
        return view
    }

    companion object {
        /**
         * ダイアログ引数キー:単位
         */
        private const val KEY_UNIT = "unit"

//        /**
//         * Tag for log
//         */
//        private const val TAG = "NumberInputDialog"

        /**
         * 文字列が数値へ変換可能あるか否かを返します。
         * 文字列が空でなく、全て数字の場合にtrueが返ります。
         *
         * @param text 文字列
         * @return 変換可能の場合はtrue、不可能の場合はfalse
         */
        private fun isValidInputValue(text: String): Boolean {
            if (text.isEmpty()) {
                return false
            }
            for (c in text) {
                if (c < '0' || c > '9') {
                    return false
                }
            }
            return true
        }
    }
}