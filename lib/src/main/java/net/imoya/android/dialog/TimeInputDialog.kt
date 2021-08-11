package net.imoya.android.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import net.imoya.android.dialog.TimeInputDialog.Builder
import net.imoya.android.util.Log
import net.imoya.android.util.TimePickerHelper

/**
 * 時刻入力ダイアログ
 *
 * タイトル, メッセージ, 時刻入力欄([TimePicker]), OKボタン, キャンセルボタンを持つダイアログ [Fragment] です。
 *  * [Builder]を使用して表示内容を設定し、 [Builder.show] メソッドをコールして表示してください。
 *  * ダイアログ終了時 [DialogListener.onDialogResult] メソッドがコールされます。
 *  * OKボタン押下に伴うダイアログ終了時、 [DialogListener.onDialogResult] メソッドの引数
 *  resultCode の値が [Activity.RESULT_OK] となります。このとき、引数 data の [Intent.getIntExtra] へ
 * [TimeInputDialog.EXTRA_KEY_HOUR] を入力することで時刻(時、0-23)を、
 * [TimeInputDialog.EXTRA_KEY_MINUTE] を入力することで時刻(分、0-60)を取得できます。
 *  * OKボタン押下以外の理由でダイアログが終了した場合は、
 * [DialogListener.onDialogResult] メソッドの引数 resultCode の値が
 * [Activity.RESULT_CANCELED] となります。
 */
@Suppress("unused")
open class TimeInputDialog : OkCancelDialog() {
    /**
     * ダイアログビルダ
     */
    open class Builder(parent: DialogParent, requestCode: Int) :
        DialogBuilder(parent, requestCode) {
        /**
         * positive ボタン文言
         */
        @JvmField
        protected var positiveButtonTitle: String? = null

        /**
         * negative ボタン文言
         */
        @JvmField
        protected var negativeButtonTitle: String? = null

        /**
         * 時初期値
         */
        @JvmField
        protected var hour = 0

        /**
         * 分初期値
         */
        @JvmField
        protected var minute = 0

        /**
         * 24時間表示フラグ
         */
        @JvmField
        protected var is24HourView = false

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
         * positive ボタン文言を設定します。デフォルト値は [android.R.string.ok] です。
         *
         * @param buttonTitle ボタン文言
         * @return [Builder]
         */
        fun setPositiveButtonTitle(buttonTitle: String): Builder {
            positiveButtonTitle = buttonTitle
            return this
        }

        /**
         * negative ボタン文言を設定します。デフォルト値は [android.R.string.cancel] です。
         *
         * @param buttonTitle ボタン文言
         * @return [Builder]
         */
        fun setNegativeButtonTitle(buttonTitle: String): Builder {
            negativeButtonTitle = buttonTitle
            return this
        }

        /**
         * 時初期値を設定します。
         *
         * @param hour 時初期値
         * @return [Builder]
         */
        fun setHour(hour: Int): Builder {
            this.hour = hour
            return this
        }

        /**
         * 分初期値を設定します。
         *
         * @param minute 分初期値
         * @return [Builder]
         */
        fun setMinute(minute: Int): Builder {
            this.minute = minute
            return this
        }

        /**
         * 24時間表示フラグを設定します。
         *
         * @param is24HourView 24時間表示フラグ
         * @return [Builder]
         */
        fun setIs24HourView(is24HourView: Boolean): Builder {
            this.is24HourView = is24HourView
            return this
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return [DialogBase]
         */
        override fun createFragment(): DialogBase {
            return TimeInputDialog()
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ [Bundle]
         */
        override fun makeArguments(): Bundle {
            val arguments = super.makeArguments()
            if (positiveButtonTitle != null) {
                arguments.putString(KEY_POSITIVE_BUTTON_TITLE, positiveButtonTitle)
            } else {
                arguments.putString(
                    KEY_POSITIVE_BUTTON_TITLE,
                    context.getString(android.R.string.ok)
                )
            }
            if (negativeButtonTitle != null) {
                arguments.putString(KEY_NEGATIVE_BUTTON_TITLE, negativeButtonTitle)
            } else {
                arguments.putString(
                    KEY_NEGATIVE_BUTTON_TITLE,
                    context.getString(android.R.string.cancel)
                )
            }
            arguments.putInt(EXTRA_KEY_HOUR, hour)
            arguments.putInt(EXTRA_KEY_MINUTE, minute)
            arguments.putBoolean(KEY_IS_24_HOUR_VIEW, is24HourView)
            return arguments
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    protected open class DialogButtonClickListener(
        dialog: TimeInputDialog,
        listener: DialogListener
    ) :
        OkCancelDialog.DialogButtonClickListener(dialog, listener) {
        /**
         * ボタン押下時に [DialogListener.onDialogResult] へ入力する [Intent] を生成して返します。
         *
         * @return [Intent]
         */
        override fun makeData(): Intent {
            val data = super.makeData()
            val dialog = dialog as TimeInputDialog
            val pickerHelper = TimePickerHelper(dialog.timePicker)
            data.putExtra(EXTRA_KEY_HOUR, pickerHelper.getHour())
            data.putExtra(EXTRA_KEY_MINUTE, pickerHelper.getMinute())
            Log.d(
                TAG,
                "makeData: hour = ${
                    data.getIntExtra(EXTRA_KEY_HOUR, -1)
                } minute = ${
                    data.getIntExtra(EXTRA_KEY_MINUTE, -1)
                }"
            )
            return data
        }
    }

    /**
     * [TimePicker]
     */
    private lateinit var timePicker: TimePicker

    override fun createDialog(savedInstanceState: Bundle?): Dialog {
        val buttonClickListener = DialogButtonClickListener(
            this, listener
        )
        val arguments = requireArguments()
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_time_input, null)
        timePicker = view.findViewById(R.id.time)
        TimePickerHelper(timePicker).setHourAndMinute(
            arguments.getInt(EXTRA_KEY_HOUR),
            arguments.getInt(EXTRA_KEY_MINUTE)
        )
        timePicker.setIs24HourView(arguments.getBoolean(KEY_IS_24_HOUR_VIEW))
        return AlertDialog.Builder(requireContext(), this.theme)
            .setTitle(arguments.getString(KEY_TITLE, null))
            .setMessage(arguments.getString(KEY_MESSAGE, null))
            .setView(view)
            .setPositiveButton(
                arguments.getString(KEY_POSITIVE_BUTTON_TITLE), buttonClickListener
            )
            .setNegativeButton(
                arguments.getString(KEY_NEGATIVE_BUTTON_TITLE), buttonClickListener
            )
            .create()
    }

    companion object {
        /**
         * 結果キー定義:入力時刻-時(0～23)
         */
        const val EXTRA_KEY_HOUR = "hour"

        /**
         * 結果キー定義:入力時刻-分
         */
        const val EXTRA_KEY_MINUTE = "minute"

        /**
         * ダイアログ引数キー:positiveボタン文言
         */
        /* protected */ const val KEY_POSITIVE_BUTTON_TITLE = "positiveButtonTitle"

        /**
         * ダイアログ引数キー:negativeボタン文言
         */
        /* protected */ const val KEY_NEGATIVE_BUTTON_TITLE = "negativeButtonTitle"

        /**
         * ダイアログ引数キー:24時間表示フラグ
         */
        /* protected */ const val KEY_IS_24_HOUR_VIEW = "is24HourView"

        /**
         * Tag for log
         */
        private const val TAG = "TimeInputDialog"
    }
}