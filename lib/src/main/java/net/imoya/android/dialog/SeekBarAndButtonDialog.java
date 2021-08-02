package net.imoya.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import net.imoya.android.util.Log;

/**
 * シークバー付き、数値入力ダイアログ
 * <p/>
 * タイトル、メッセージ、シークバー、入力欄、OKボタン、キャンセルボタンを持つ、
 * 汎用ダイアログフラグメントです。<ul>
 * <li>親画面は {@link Listener} を実装した {@link Fragment} 又は {@link AppCompatActivity}
 * でなければなりません。</li>
 * <li>{@link Builder}を使用して表示内容を設定し、表示してください。</li>
 * <li>ダイアログ終了時 {@link Listener#onDialogResult(int, int, Intent)}
 * メソッドが呼び出されます。</li>
 * <li>OKボタンがクリックされた場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_OK} となります。このとき、引数 data の
 * {@link Intent#getIntExtra(String, int)} へ {@link SeekBarInputDialog#EXTRA_KEY_INPUT_VALUE}, 0
 * を入力することで、入力された値を取得できます。</li>
 * <li>キャンセルボタンがクリックされた場合、又はダイアログがキャンセル終了した場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_CANCELED} となります。</li>
 * </ul>
 */
@SuppressWarnings("unused")
public class SeekBarAndButtonDialog extends SeekBarInputDialog implements View.OnClickListener {
    /**
     * ダイアログビルダ
     */
    public static class Builder extends SeekBarInputDialog.Builder {
        /**
         * 追加ボタン文言
         */
        private String additionalButtonText;

        /**
         * コンストラクタ
         *
         * @param activity    親画面となる {@link AppCompatActivity}
         * @param requestCode リクエストコード
         * @param <T>         親画面は {@link Listener} を実装した {@link AppCompatActivity} であること
         */
        public <T extends AppCompatActivity & Listener> Builder(@NonNull T activity, int requestCode) {
            super(activity, requestCode);
        }

        /**
         * コンストラクタ
         *
         * @param fragment    親画面となる{@link Fragment}
         * @param requestCode リクエストコード
         * @param <T>         親画面は {@link Listener} を実装した {@link Fragment} であること
         */
        public <T extends Fragment & Listener> Builder(@NonNull T fragment, int requestCode) {
            super(fragment, requestCode);
        }

        /**
         * タイトル文言を設定します。
         *
         * @param title タイトル文言
         * @return {@link Builder}
         */
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
        public Builder setPositiveButtonTitle(@NonNull String buttonTitle) {
            super.setPositiveButtonTitle(buttonTitle);
            return this;
        }

        /**
         * negativeボタン文言を設定します。デフォルト値は {@link android.R.string#cancel } です。
         *
         * @param buttonTitle ボタン文言
         * @return {@link Builder}
         */
        public Builder setNegativeButtonTitle(@NonNull String buttonTitle) {
            super.setNegativeButtonTitle(buttonTitle);
            return this;
        }

        /**
         * 入力部分のレイアウトリソースIDを設定します。
         * <p>
         * リソースには、最低限次の要素が必要です:<ul>
         * <li>スライダ(シークバー)となる、 id="slider" である {@link SeekBar}</li>
         * <li>入力欄となる、 id="value" である {@link EditText}</li>
         * <li>追加ボタンとなる、 id="button" である {@link Button}</li>
         * </ul>
         *
         * @param layoutResourceId 入力部分のレイアウトリソースID
         * @return {@link Builder}
         */
        public Builder setLayoutResourceId(int layoutResourceId) {
            super.setLayoutResourceId(layoutResourceId);
            return this;
        }

        /**
         * 最小値を設定します。
         *
         * @param min 最小値
         * @return {@link Builder}
         */
        public Builder setMin(int min) {
            super.setMin(min);
            return this;
        }

        /**
         * 最大値を設定します。
         *
         * @param max 最大値
         * @return {@link Builder}
         */
        public Builder setMax(int max) {
            super.setMax(max);
            return this;
        }

        /**
         * 初期値を設定します。
         *
         * @param value 初期値
         * @return {@link Builder}
         */
        public Builder setValue(int value) {
            super.setValue(value);
            return this;
        }

        /**
         * 追加ボタン文言を設定します。
         *
         * @param additionalButtonText 追加ボタン文言
         * @return {@link Builder}
         */
        public Builder setAdditionalButtonText(String additionalButtonText) {
            this.additionalButtonText = additionalButtonText;
            return this;
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return {@link DialogBase}
         */
        @Override
        protected DialogBase createFragment() {
            return new SeekBarAndButtonDialog();
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ {@link Bundle}
         */
        @Override
        protected Bundle makeArguments() {
            final Bundle arguments = super.makeArguments();

            arguments.putString(KEY_ADDITIONAL_BUTTON_TEXT, this.additionalButtonText);

            return arguments;
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    private static class DialogButtonClickListener extends OkCancelDialog.DialogButtonClickListener {
        /**
         * コンストラクタ
         *
         * @param dialog   ダイアログ
         * @param listener ダイアログのリスナ
         */
        private DialogButtonClickListener(SeekBarAndButtonDialog dialog, Listener listener) {
            super(dialog, listener);
        }

        /**
         * ボタン押下時に {@link Fragment#onActivityResult(int, int, Intent)} へ通知する
         * {@link Intent} を生成して返します。
         *
         * @return {@link Intent}
         */
        @Override
        protected Intent makeData() {
            final Intent intent = super.makeData();
            intent.putExtra(EXTRA_KEY_INPUT_VALUE,
                    ((SeekBarAndButtonDialog) this.dialog).getValue());
            return intent;
        }
    }

    private static final String TAG = "SeekBarInputDialog";

    /**
     * ダイアログ引数キー:追加ボタン文言
     */
    private static final String KEY_ADDITIONAL_BUTTON_TEXT = "additionalButtonText";

    /**
     * ダイアログ生成処理
     *
     * @param savedInstanceState 前回強制終了時の保存データ
     * @return 生成した{@link Dialog}
     */
    @Override
    @NonNull
    public Dialog createDialog(Bundle savedInstanceState) {
        final Bundle arguments = this.requireArguments();
        this.min = arguments.getInt(KEY_MIN, 0);
        this.max = arguments.getInt(KEY_MAX, 100);
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                this.requireActivity(), this.getTheme())
                .setTitle(arguments.getString(KEY_TITLE))
                .setMessage(arguments.getString(KEY_MESSAGE))
                .setPositiveButton(
                        arguments.getString(KEY_POSITIVE_BUTTON_TITLE),
                        new DialogButtonClickListener(this, this.listener))
                .setNegativeButton(
                        arguments.getString(KEY_NEGATIVE_BUTTON_TITLE),
                        new DialogButtonClickListener(this, this.listener));
        final View view = LayoutInflater.from(builder.getContext()).inflate(
                arguments.getInt(KEY_LAYOUT_RESOURCE_ID, R.layout.dialog_seekbar_and_button),
                null);
        this.editText = view.findViewById(R.id.value);
        this.editText.setText(String.valueOf(arguments.getInt(EXTRA_KEY_INPUT_VALUE)));
        this.editText.addTextChangedListener(this);
        this.seekBar = view.findViewById(R.id.slider);
        this.seekBar.setMax(this.max - this.min);
        this.seekBar.setProgress(arguments.getInt(EXTRA_KEY_INPUT_VALUE));
        this.seekBar.setOnSeekBarChangeListener(this);
        final Button additionalButton = view.findViewById(R.id.button);
        additionalButton.setText(arguments.getString(KEY_ADDITIONAL_BUTTON_TEXT));
        additionalButton.setOnClickListener(this);

        return builder.setView(view).create();
    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "Additional Button is clicked");
    }
}
