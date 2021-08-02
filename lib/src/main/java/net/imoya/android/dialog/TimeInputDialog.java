package net.imoya.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import net.imoya.android.util.Log;
import net.imoya.android.util.TimePickerUtil;

/**
 * 時刻入力ダイアログ
 * <p/>
 * タイトル、メッセージ、時刻入力欄({@link TimePicker})、OKボタン、キャンセルボタンを持つ、
 * 汎用ダイアログフラグメントです。<ul>
 * <li>親画面は {@link Listener} を実装した {@link Fragment} 又は {@link AppCompatActivity}
 * でなければなりません。</li>
 * <li>{@link Builder}を使用して表示内容を設定し、表示してください。</li>
 * <li>ダイアログ終了時 {@link Listener#onDialogResult(int, int, Intent)}
 * メソッドが呼び出されます。</li>
 * <li>OKボタンがクリックされた場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_OK} となります。このとき、引数 data の
 * {@link Intent#getIntExtra(String, int)} へ {@link TimeInputDialog#EXTRA_KEY_HOUR}
 * を入力することで時刻(時、0-23)を、{@link TimeInputDialog#EXTRA_KEY_MINUTE}
 * を入力することで時刻(分、0-60)を取得できます。</li>
 * <li>キャンセルボタンがクリックされた場合、又はダイアログがキャンセル終了した場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_CANCELED} となります。</li>
 * </ul>
 */
@SuppressWarnings("unused")
public class TimeInputDialog extends OkCancelDialog {
    /**
     * 結果キー定義:入力時刻-時(0～23)
     */
    public static final String EXTRA_KEY_HOUR = "hour";
    /**
     * 結果キー定義:入力時刻-分
     */
    public static final String EXTRA_KEY_MINUTE = "minute";

    /**
     * ダイアログビルダ
     */
    public static class Builder extends DialogBase.Builder {
        /**
         * positiveボタン文言
         */
        private String positiveButtonTitle;
        /**
         * negativeボタン文言
         */
        private String negativeButtonTitle;
        /**
         * 時初期値
         */
        private int hour = 0;
        /**
         * 分初期値
         */
        private int minute = 0;
        /**
         * 24時間表示フラグ
         */
        private boolean is24HourView = false;

        /**
         * コンストラクタ
         *
         * @param activity    親画面となる {@link AppCompatActivity}
         * @param requestCode リクエストコード
         * @param <T>         親画面は {@link Listener} を実装した {@link AppCompatActivity} であること
         */
        public <T extends AppCompatActivity & Listener> Builder(
                @NonNull T activity, int requestCode) {
            super(new BuilderParentActivity<>(activity), requestCode);
        }

        /**
         * コンストラクタ
         *
         * @param fragment    親画面となる{@link Fragment}
         * @param requestCode リクエストコード
         */
        public <T extends Fragment & Listener> Builder(@NonNull T fragment, int requestCode) {
            super(new BuilderParentFragment<>(fragment), requestCode);
        }

        /**
         * タイトル文言を設定します。
         *
         * @param title タイトル文言
         * @return {@link Builder}
         */
        @Override
        @NonNull
        public Builder setTitle(@NonNull String title) {
            super.setTitle(title);
            return this;
        }

        /**
         * メッセージ文言を設定します。
         *
         * @param message メッセージ文言
         * @return {@link Builder}
         */
        @Override
        @NonNull
        public Builder setMessage(@NonNull String message) {
            super.setMessage(message);
            return this;
        }

        /**
         * インスタンス識別用タグを設定します。
         *
         * @param tag タグ
         * @return {@link Builder}
         */
        @Override
        @NonNull
        public Builder setTag(@NonNull String tag) {
            super.setTag(tag);
            return this;
        }

        /**
         * positiveボタン文言を設定します。デフォルト値は {@link android.R.string#ok } です。
         *
         * @param buttonTitle ボタン文言
         * @return {@link Builder}
         */
        @NonNull
        public Builder setPositiveButtonTitle(@NonNull String buttonTitle) {
            this.positiveButtonTitle = buttonTitle;
            return this;
        }

        /**
         * negativeボタン文言を設定します。デフォルト値は {@link android.R.string#cancel } です。
         *
         * @param buttonTitle ボタン文言
         * @return {@link Builder}
         */
        @NonNull
        public Builder setNegativeButtonTitle(@NonNull String buttonTitle) {
            this.negativeButtonTitle = buttonTitle;
            return this;
        }

        /**
         * 時初期値を設定します。
         *
         * @param hour 時初期値
         * @return {@link Builder}
         */
        public Builder setHour(int hour) {
            this.hour = hour;
            return this;
        }

        /**
         * 分初期値を設定します。
         *
         * @param minute 分初期値
         * @return {@link Builder}
         */
        public Builder setMinute(int minute) {
            this.minute = minute;
            return this;
        }

        /**
         * 24時間表示フラグを設定します。
         *
         * @param is24HourView 24時間表示フラグ
         * @return {@link Builder}
         */
        public Builder setIs24HourView(boolean is24HourView) {
            this.is24HourView = is24HourView;
            return this;
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return {@link DialogBase}
         */
        @Override
        @NonNull
        protected DialogBase createFragment() {
            return new TimeInputDialog();
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ {@link Bundle}
         */
        @Override
        @NonNull
        protected Bundle makeArguments() {
            final Bundle arguments = super.makeArguments();

            if (this.positiveButtonTitle != null) {
                arguments.putString(KEY_POSITIVE_BUTTON_TITLE, this.positiveButtonTitle);
            } else {
                arguments.putString(KEY_POSITIVE_BUTTON_TITLE,
                        this.getContext().getString(android.R.string.ok));
            }
            if (this.negativeButtonTitle != null) {
                arguments.putString(KEY_NEGATIVE_BUTTON_TITLE, this.negativeButtonTitle);
            } else {
                arguments.putString(KEY_NEGATIVE_BUTTON_TITLE,
                        this.getContext().getString(android.R.string.cancel));
            }
            arguments.putInt(EXTRA_KEY_HOUR, this.hour);
            arguments.putInt(EXTRA_KEY_MINUTE, this.minute);
            arguments.putBoolean(KEY_IS_24_HOUR_VIEW, this.is24HourView);

            return arguments;
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    protected static class DialogButtonClickListener
            extends OkCancelDialog.DialogButtonClickListener {
        /**
         * コンストラクタ
         *
         * @param dialog   ダイアログ
         * @param listener ダイアログのリスナ
         */
        protected DialogButtonClickListener(TimeInputDialog dialog, Listener listener) {
            super(dialog, listener);
        }

        @Override
        @NonNull
        protected Intent makeData() {
            final Intent data = super.makeData();
            final TimeInputDialog dialog = (TimeInputDialog) this.dialog;

            data.putExtra(EXTRA_KEY_HOUR, TimePickerUtil.getHour(dialog.timePicker));
            data.putExtra(EXTRA_KEY_MINUTE, TimePickerUtil.getMinute(dialog.timePicker));
            Log.d(TAG, "makeData: hour = " + data.getIntExtra(EXTRA_KEY_HOUR, -1) + " minute = " + data.getIntExtra(EXTRA_KEY_MINUTE, -1));

            return data;
        }
    }

    /**
     * Tag for log
     */
    private static final String TAG = "TimeInputDialog";

    /**
     * ダイアログ引数キー:positiveボタン文言
     */
    protected static final String KEY_POSITIVE_BUTTON_TITLE = "positiveButtonTitle";
    /**
     * ダイアログ引数キー:negativeボタン文言
     */
    protected static final String KEY_NEGATIVE_BUTTON_TITLE = "negativeButtonTitle";
    /**
     * ダイアログ引数キー:24時間表示フラグ
     */
    private static final String KEY_IS_24_HOUR_VIEW = "is24HourView";

    private TimePicker timePicker;

    /**
     * ダイアログ生成処理
     *
     * @param savedInstanceState 前回強制終了時の保存データ
     * @return 生成した{@link Dialog}
     */
    @Override
    @NonNull
    public Dialog createDialog(Bundle savedInstanceState) {
        final DialogButtonClickListener buttonClickListener = new DialogButtonClickListener(
                this, this.listener);
        final Bundle arguments = this.requireArguments();
        final View view = LayoutInflater.from(this.requireContext())
                .inflate(R.layout.dialog_time_input, null);
        this.timePicker = view.findViewById(R.id.time);
        TimePickerUtil.setHourAndMinute(
                this.timePicker,
                arguments.getInt(EXTRA_KEY_HOUR),
                arguments.getInt(EXTRA_KEY_MINUTE));
        this.timePicker.setIs24HourView(arguments.getBoolean(KEY_IS_24_HOUR_VIEW));
        return new AlertDialog.Builder(this.requireContext(), this.getTheme())
                .setTitle(arguments.getString(KEY_TITLE, null))
                .setMessage(arguments.getString(KEY_MESSAGE, null))
                .setView(view)
                .setPositiveButton(
                        arguments.getString(KEY_POSITIVE_BUTTON_TITLE), buttonClickListener)
                .setNegativeButton(
                        arguments.getString(KEY_NEGATIVE_BUTTON_TITLE), buttonClickListener)
                .create();
    }
}
