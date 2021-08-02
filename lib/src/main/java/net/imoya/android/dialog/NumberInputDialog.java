package net.imoya.android.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Objects;

/**
 * 整数値入力ダイアログ
 * <p/>
 * タイトル、メッセージ、数値入力欄、単位表示、OKボタン、キャンセルボタンを持つ、
 * 汎用ダイアログフラグメントです。<ul>
 * <li>親画面は {@link Listener} を実装した {@link Fragment} 又は {@link AppCompatActivity}
 * でなければなりません。</li>
 * <li>{@link Builder}を使用して表示内容を設定し、表示してください。</li>
 * <li>ダイアログ終了時 {@link Listener#onDialogResult(int, int, Intent)}
 * メソッドが呼び出されます。</li>
 * <li>OKボタンがクリックされた場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_OK} となります。このとき、引数 data の
 * {@link Intent#getIntExtra(String, int)} へ {@link #EXTRA_KEY_INPUT_VALUE}
 * を入力することで、入力された整数値を取得できます。
 * 但し空文字を入力されたか、数字以外の文字を入力された場合は、 {@link #EXTRA_KEY_INPUT_VALUE}
 * の extra は存在しません({@link Intent#hasExtra(String)} で判定してください)。</li>
 * <li>キャンセルボタンがクリックされた場合、又はダイアログがキャンセル終了した場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_CANCELED} となります。</li>
 * </ul>
 */
public class NumberInputDialog extends InputDialog {
    /**
     * ダイアログビルダ
     */
    public static class Builder extends InputDialog.Builder {
        /**
         * 入力初期値
         */
        private int number;
        /**
         * 入力初期値設定済フラグ
         */
        private boolean numberIsSet = false;
        /**
         * 単位
         */
        private String unit = null;

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
         * <li>入力欄となる、 id="text" である {@link EditText}</li>
         * <li>入力欄となる、 id="unit" である {@link TextView}</li>
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
         * ヒント文言を設定します。
         *
         * @param hint ヒント文言
         * @return {@link Builder}
         */
        public Builder setHint(String hint) {
            super.setHint(hint);
            return this;
        }

        /**
         * 入力種別を設定します。
         *
         * @param inputType 入力種別
         * @return {@link Builder}
         */
        public Builder setInputType(int inputType) {
            super.setInputType(inputType);
            return this;
        }

        /**
         * 入力初期値を設定します。
         *
         * @param number 入力初期値
         * @return {@link Builder}
         */
        public Builder setNumber(int number) {
            this.number = number;
            this.numberIsSet = true;
            return this;
        }

        /**
         * 単位を設定します。
         *
         * @param unit 単位
         * @return {@link Builder}
         */
        public Builder setUnit(@Nullable String unit) {
            this.unit = unit;
            return this;
        }

        /**
         * 実装クラスのインスタンスを生成して返します。
         *
         * @return {@link DialogBase}
         */
        @Override
        protected DialogBase createFragment() {
            return new NumberInputDialog();
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ {@link Bundle}
         */
        @Override
        protected Bundle makeArguments() {
            final Bundle arguments = super.makeArguments();

            if (this.numberIsSet) {
                arguments.putInt(EXTRA_KEY_INPUT_VALUE, this.number);
            }
            if (this.unit != null) {
                arguments.putString(KEY_UNIT, this.unit);
            }

            return arguments;
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    private static class DialogButtonClickListener extends InputDialog.DialogButtonClickListener {
        /**
         * コンストラクタ
         *
         * @param dialog   ダイアログ
         * @param listener ダイアログのリスナ
         */
        private DialogButtonClickListener(NumberInputDialog dialog, Listener listener) {
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
            final NumberInputDialog dialog = (NumberInputDialog) this.dialog;
            if (dialog.isValidInputValue()) {
                intent.putExtra(EXTRA_KEY_INPUT_VALUE, dialog.getInputValue());
            }
            return intent;
        }
    }

    /**
     * ダイアログ引数キー:単位
     */
    private static final String KEY_UNIT = "unit";

//    private static final String TAG = "NumberInputDialog";

    /**
     * 入力された整数値を返します。
     * 入力欄が空であるか、数値以外の文字を入力された場合は、0を返します。
     *
     * @return 入力された値
     */
    public int getInputValue() {
        final String text = this.getInputText();
        if (isValidInputValue(text)) {
            return Integer.parseInt(text, 10);
        } else {
            return 0;
        }
    }

    /**
     * 入力欄が空でなく、且つ整数値が入力されているか否かを返します。
     *
     * @return 入力欄が空でなく、且つ整数値が入力されている場合はtrue、その他の場合はfalse
     */
    public boolean isValidInputValue() {
        return isValidInputValue(this.getInputText());
    }

    /**
     * 入力欄の文字列を取得します。
     *
     * @return 文字列
     */
    private String getInputText() {
        return ((EditText) Objects.requireNonNull(this.getDialog()).findViewById(R.id.text))
                .getText().toString();
    }

    /**
     * 文字列が数値へ変換可能あるか否かを返します。
     * 文字列が空でなく、全て数字の場合にtrueが返ります。
     *
     * @param text 文字列
     * @return 変換可能の場合はtrue、不可能の場合はfalse
     */
    private static boolean isValidInputValue(String text) {
        if (text.length() == 0) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            final char c = text.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * ボタンクリック時の処理を実装した
     * {@link InputDialog.DialogButtonClickListener} を返します。
     * 返された
     * {@link InputDialog.DialogButtonClickListener} は、ダイアログ全ボタンのリスナとなります。
     *
     * @return {@link InputDialog.DialogButtonClickListener}
     */
    @Override
    protected InputDialog.DialogButtonClickListener getDialogButtonClickListener() {
        return new DialogButtonClickListener(this, this.listener);
    }

    /**
     * ダイアログへ組み込む入力部分の {@link View}
     * を指定する、レイアウトリソースIDのデフォルト値を返します。
     *
     * @return レイアウトリソースID
     */
    @Override
    @LayoutRes
    protected int getDefaultInputViewLayoutResourceId() {
        return R.layout.dialog_number_input;
    }

    /**
     * 入力欄へ初期表示する内容を返します。
     *
     * @param context   {@link Context}
     * @param arguments ダイアログ引数を含む {@link Bundle}
     * @return 入力欄へ表示する文字列
     */
    @Override
    protected CharSequence getInitialValue(Context context, Bundle arguments) {
        if (arguments.containsKey(EXTRA_KEY_INPUT_VALUE)) {
            return String.valueOf(arguments.getInt(EXTRA_KEY_INPUT_VALUE));
        } else {
            return "";
        }
    }

    /**
     * ダイアログへ組み込む、入力部分の {@link View} を生成して返します。
     *
     * @param context   {@link Context}
     * @param arguments ダイアログ引数を含む {@link Bundle}
     * @return {@link View}
     */
    @Override
    protected View inflateInputView(Context context, Bundle arguments) {
        final View view = super.inflateInputView(context, arguments);

        // 単位を表示する
        final TextView unitView = view.findViewById(R.id.unit);
        final String unit = arguments.getString(KEY_UNIT);
        unitView.setText(unit);
        unitView.setVisibility(unit != null ? View.VISIBLE : View.GONE);

        return view;
    }
}
