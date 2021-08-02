package net.imoya.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import net.imoya.android.util.Log;

import java.util.Objects;

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
public class SeekBarInputDialog extends OkCancelDialog
        implements SeekBar.OnSeekBarChangeListener, TextWatcher {
    /**
     * 結果キー定義:入力値
     */
    public static final String EXTRA_KEY_INPUT_VALUE = "inputValue";

    /**
     * ダイアログビルダ
     */
    public static class Builder extends OkCancelDialog.Builder {
        /**
         * レイアウトリソースID
         */
        private int layoutResourceId = 0;
        /**
         * 最小値
         */
        private int min = 0;
        /**
         * 最大値
         */
        private int max = 100;
        /**
         * 入力初期値
         */
        private int value = 0;

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
         * OKボタン文言を設定します。デフォルト値は {@link android.R.string#ok } です。
         *
         * @param buttonTitle ボタン文言
         * @return {@link Builder}
         */
        public Builder setPositiveButtonTitle(@NonNull String buttonTitle) {
            super.setPositiveButtonTitle(buttonTitle);
            return this;
        }

        /**
         * キャンセルボタン文言を設定します。デフォルト値は {@link android.R.string#cancel } です。
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
         * </ul>
         *
         * @param layoutResourceId 入力部分のレイアウトリソースID
         * @return {@link Builder}
         */
        public Builder setLayoutResourceId(int layoutResourceId) {
            this.layoutResourceId = layoutResourceId;
            return this;
        }

        /**
         * 最小値を設定します。
         *
         * @param min 最小値
         * @return {@link Builder}
         */
        public Builder setMin(int min) {
            this.min = min;
            return this;
        }

        /**
         * 最大値を設定します。
         *
         * @param max 最大値
         * @return {@link Builder}
         */
        public Builder setMax(int max) {
            this.max = max;
            return this;
        }

        /**
         * 初期値を設定します。
         *
         * @param value 初期値
         * @return {@link Builder}
         */
        public Builder setValue(int value) {
            this.value = value;
            return this;
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return {@link DialogBase}
         */
        @Override
        protected DialogBase createFragment() {
            return new SeekBarInputDialog();
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ {@link Bundle}
         */
        @Override
        protected Bundle makeArguments() {
            final Bundle arguments = super.makeArguments();

            if (this.layoutResourceId != 0) {
                arguments.putInt(KEY_LAYOUT_RESOURCE_ID, this.layoutResourceId);
            }
            if (this.min > this.max) {
                throw new IllegalStateException("min > max");
            }
            arguments.putInt(KEY_MIN, this.min);
            arguments.putInt(KEY_MAX, this.max);
            arguments.putInt(EXTRA_KEY_INPUT_VALUE, this.value);

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
        private DialogButtonClickListener(SeekBarInputDialog dialog, Listener listener) {
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
            intent.putExtra(EXTRA_KEY_INPUT_VALUE, ((SeekBarInputDialog) this.dialog).getValue());
            return intent;
        }
    }

    private static final String TAG = "SeekBarInputDialog";

    /**
     * ダイアログ引数キー:最小値
     */
    protected static final String KEY_MIN = "min";
    /**
     * ダイアログ引数キー:最大値
     */
    protected static final String KEY_MAX = "max";

    /**
     * 最小値
     */
    protected int min = 0;
    /**
     * 最大値
     */
    protected int max = 100;
    /**
     * 数値入力欄
     */
    protected EditText editText = null;
    /**
     * シークバー({@link SeekBar})
     */
    protected SeekBar seekBar = null;

    /**
     * {@link #editText} 訂正中フラグ
     */
    protected boolean inCorrect = false;

    /**
     * 入力された値を取得します。
     *
     * @return 入力値
     */
    public int getValue() {
        return ((SeekBar) Objects.requireNonNull(this.getDialog()).findViewById(R.id.slider)).getProgress() + this.min;
    }

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
                arguments.getInt(KEY_LAYOUT_RESOURCE_ID, R.layout.dialog_seekbar_input),
                null);
        this.editText = view.findViewById(R.id.value);
        this.editText.setText(String.valueOf(arguments.getInt(EXTRA_KEY_INPUT_VALUE)));
        this.editText.addTextChangedListener(this);
        this.seekBar = view.findViewById(R.id.slider);
        this.seekBar.setMax(this.max - this.min);
        this.seekBar.setProgress(arguments.getInt(EXTRA_KEY_INPUT_VALUE) - this.min);
        this.seekBar.setOnSeekBarChangeListener(this);
        return builder.setView(view).create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        Log.d(TAG, "onDestroyView");

        this.editText = null;
        this.seekBar = null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG, "afterTextChanged: inCorrect = " + this.inCorrect);
        if (!this.inCorrect) {
            this.inCorrect = true;
            Log.d(TAG, "afterTextChanged: s = " + s.toString());
            if (s.length() == 0) {
                // 空文字の場合は最小値扱いとする
                this.seekBar.setProgress(0);
            } else if ("-".equals(s.toString()) && this.min < 0) {
                // 0未満を入力可能の場合、マイナス記号のみ入力はOKとする(入力途中且つ0扱い)
                this.seekBar.setProgress(-this.min);
            } else {
                int value;
                try {
                    value = Integer.parseInt(s.toString(), 10);
                } catch (Exception e) {
                    value = 0;
                }
                if (value < this.min) {
                    value = this.min;
                    s.clear();
                    s.append(String.valueOf(value));
                } else if (value > this.max) {
                    value = this.max;
                    s.clear();
                    s.append(String.valueOf(value));
                }
                this.seekBar.setProgress(value - this.min);
            }
            this.inCorrect = false;
            Log.d(TAG, "afterTextChanged: end");
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(TAG, "onProgressChanged: progress = " + progress + ", fromUser = " + fromUser);
        if (fromUser) {
            this.editText.setText(String.valueOf(progress + this.min));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
