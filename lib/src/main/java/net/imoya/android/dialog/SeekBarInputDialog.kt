package net.imoya.android.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.SeekBarInputDialog.Builder
import net.imoya.android.util.Log

/**
 * シークバー付き、数値入力ダイアログ
 *
 *
 * タイトル, メッセージ, シークバー, 入力欄, OKボタン, キャンセルボタンを持つダイアログ [Fragment] です。
 *  * 親画面は [DialogBase.Listener] を実装した [Fragment] 又は [AppCompatActivity] を想定しています。
 *  * [Builder]を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogBase.Listener.onDialogResult] メソッドがコールされます。
 *  * OKボタン押下に伴うダイアログ終了時、 [DialogBase.Listener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。このとき、引数 data の [Intent.getIntExtra] へ
 * [SeekBarInputDialog.EXTRA_KEY_INPUT_VALUE], 0 を入力することで、入力された値を取得できます。
 *  * OKボタン押下以外の理由でダイアログが終了した場合は、
 * [DialogBase.Listener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 *
 */
open class SeekBarInputDialog : OkCancelDialog(), OnSeekBarChangeListener, TextWatcher {
    /**
     * ダイアログビルダ
     */
    open class Builder(parent: BuilderParent, requestCode: Int) :
        OkCancelDialog.Builder(parent, requestCode) {
        /**
         * レイアウトリソースID
         */
        @JvmField
        protected var layoutResourceId = 0

        /**
         * 最小値
         */
        @JvmField
        protected var min = 0

        /**
         * 最大値
         */
        @JvmField
        protected var max = 100

        /**
         * 入力初期値
         */
        @JvmField
        protected var value = 0

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
         * 入力部分のレイアウトリソースIDを設定します。
         *
         *
         * リソースには、最低限次の要素が必要です:
         *  * スライダ(シークバー)となる、 id="slider" である [SeekBar]
         *  * 入力欄となる、 id="value" である [EditText]
         *
         *
         * @param layoutResourceId 入力部分のレイアウトリソースID
         * @return [Builder]
         */
        open fun setLayoutResourceId(layoutResourceId: Int): Builder {
            this.layoutResourceId = layoutResourceId
            return this
        }

        /**
         * 最小値を設定します。
         *
         * @param min 最小値
         * @return [Builder]
         */
        open fun setMin(min: Int): Builder {
            this.min = min
            return this
        }

        /**
         * 最大値を設定します。
         *
         * @param max 最大値
         * @return [Builder]
         */
        open fun setMax(max: Int): Builder {
            this.max = max
            return this
        }

        /**
         * 初期値を設定します。
         *
         * @param value 初期値
         * @return [Builder]
         */
        open fun setValue(value: Int): Builder {
            this.value = value
            return this
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return [DialogBase]
         */
        override fun createFragment(): DialogBase {
            return SeekBarInputDialog()
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ [Bundle]
         */
        override fun makeArguments(): Bundle {
            val arguments = super.makeArguments()
            if (layoutResourceId != 0) {
                arguments.putInt(KEY_LAYOUT_RESOURCE_ID, layoutResourceId)
            }
            check(min <= max) { "min > max" }
            arguments.putInt(KEY_MIN, min)
            arguments.putInt(KEY_MAX, max)
            arguments.putInt(EXTRA_KEY_INPUT_VALUE, value)
            return arguments
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    private class DialogButtonClickListener(dialog: SeekBarInputDialog, listener: Listener) :
        OkCancelDialog.DialogButtonClickListener(dialog, listener) {
        /**
         * ボタン押下時に [DialogBase.Listener.onDialogResult] へ入力する [Intent] を生成して返します。
         *
         * @return [Intent]
         */
        override fun makeData(): Intent {
            val intent = super.makeData()
            intent.putExtra(EXTRA_KEY_INPUT_VALUE, (dialog as SeekBarInputDialog).value)
            return intent
        }
    }

    /**
     * 最小値
     */
    @JvmField
    protected var min = 0

    /**
     * 最大値
     */
    @JvmField
    protected var max = 100

    /**
     * 数値入力欄
     */
    protected lateinit var editText: EditText

    /**
     * シークバー([SeekBar])
     */
    protected lateinit var seekBar: SeekBar

    /**
     * [.editText] 訂正中フラグ
     */
    @JvmField
    protected var inCorrect = false

    /**
     * 入力された値を取得します。
     *
     * @return 入力値
     */
    val value: Int
        get() = (this.dialog?.findViewById<View>(R.id.slider) as SeekBar).progress + min

    /**
     * ダイアログ生成処理
     *
     * @param savedInstanceState 前回強制終了時の保存データ
     * @return 生成した [Dialog]
     */
    override fun createDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = requireArguments()
        min = arguments.getInt(KEY_MIN, 0)
        max = arguments.getInt(KEY_MAX, 100)
        val builder = AlertDialog.Builder(
            requireActivity(), this.theme
        )
            .setTitle(arguments.getString(KEY_TITLE))
            .setMessage(arguments.getString(KEY_MESSAGE))
            .setPositiveButton(
                arguments.getString(KEY_POSITIVE_BUTTON_TITLE),
                DialogButtonClickListener(this, listener)
            )
            .setNegativeButton(
                arguments.getString(KEY_NEGATIVE_BUTTON_TITLE),
                DialogButtonClickListener(this, listener)
            )
        val view = LayoutInflater.from(builder.context).inflate(
            arguments.getInt(KEY_LAYOUT_RESOURCE_ID, R.layout.dialog_seekbar_input),
            null
        )
        editText = view.findViewById(R.id.value)
        editText.setText(arguments.getInt(EXTRA_KEY_INPUT_VALUE).toString())
        editText.addTextChangedListener(this)
        seekBar = view.findViewById(R.id.slider)
        seekBar.max = max - min
        seekBar.progress = arguments.getInt(EXTRA_KEY_INPUT_VALUE) - min
        seekBar.setOnSeekBarChangeListener(this)
        return builder.setView(view).create()
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//
//        Log.d(TAG, "onDestroyView");
//        editText = null
//        seekBar = null
//    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        Log.d(TAG, "afterTextChanged: inCorrect = $inCorrect")
        if (!inCorrect) {
            inCorrect = true
            Log.d(TAG, "afterTextChanged: s = $s")
            if (s.isEmpty()) {
                // 空文字の場合は最小値扱いとする
                seekBar.progress = 0
            } else if ("-" == s.toString() && min < 0) {
                // 0未満を入力可能の場合、マイナス記号のみ入力はOKとする(入力途中且つ0扱い)
                seekBar.progress = -min
            } else {
                var value: Int
                value = try {
                    s.toString().toInt(10)
                } catch (e: Exception) {
                    0
                }
                if (value < min) {
                    value = min
                    s.clear()
                    s.append(value.toString())
                } else if (value > max) {
                    value = max
                    s.clear()
                    s.append(value.toString())
                }
                seekBar.progress = value - min
            }
            inCorrect = false
            Log.d(TAG, "afterTextChanged: end")
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        Log.d(TAG, "onProgressChanged: progress = $progress, fromUser = $fromUser")
        if (fromUser) {
            editText.setText((progress + min).toString())
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}

    companion object {
        /**
         * 結果キー定義:入力値
         */
        const val EXTRA_KEY_INPUT_VALUE = "inputValue"

        /**
         * ダイアログ引数キー:最小値
         */
        /* protected */ const val KEY_MIN = "min"

        /**
         * ダイアログ引数キー:最大値
         */
        /* protected */ const val KEY_MAX = "max"

        /**
         * Tag for log
         */
        private const val TAG = "SeekBarInputDialog"
    }
}