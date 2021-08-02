package net.imoya.android.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Objects;

/**
 * 文字入力ダイアログ
 * <p/>
 * タイトル、メッセージ、文字入力欄、OKボタン、キャンセルボタンを持つ、
 * 汎用ダイアログフラグメントです。<ul>
 * <li>親画面は {@link Listener} を実装した {@link Fragment} 又は {@link AppCompatActivity}
 * でなければなりません。</li>
 * <li>{@link Builder}を使用して表示内容を設定し、表示してください。</li>
 * <li>ダイアログ終了時 {@link Listener#onDialogResult(int, int, Intent)}
 * メソッドが呼び出されます。</li>
 * <li>OKボタンがクリックされた場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_OK} となります。このとき、引数 data の
 * {@link Intent#getStringExtra(String)} へ {@link TextInputDialog#EXTRA_KEY_INPUT_VALUE}
 * を入力することで、入力された文字列を取得できます。</li>
 * <li>キャンセルボタンがクリックされた場合、又はダイアログがキャンセル終了した場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_CANCELED} となります。</li>
 * </ul>
 */
@SuppressWarnings("unused")
public class TextInputDialog extends InputDialog {
    /**
     * ダイアログビルダ
     */
    public static class Builder extends InputDialog.Builder {
        /**
         * 入力初期値
         */
        private String text;

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
        @Override
        @NonNull
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
        @Override
        @NonNull
        public Builder setNegativeButtonTitle(@NonNull String buttonTitle) {
            super.setNegativeButtonTitle(buttonTitle);
            return this;
        }

        /**
         * 入力部分のレイアウトリソースIDを設定します。
         * <p>
         * リソースには、最低限次の要素が必要です:<ul>
         * <li>入力欄となる、 id="text" である {@link EditText}</li>
         * </ul>
         *
         * @param layoutResourceId 入力部分のレイアウトリソースID
         * @return {@link Builder}
         */
        @Override
        @NonNull
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
        @Override
        @NonNull
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
        @Override
        @NonNull
        public Builder setInputType(int inputType) {
            super.setInputType(inputType);
            return this;
        }

        /**
         * 最大入力可能文字数を設定します。
         *
         * @param maxLength 最大入力可能文字数
         * @return {@link Builder}
         */
        @Override
        @NonNull
        public Builder setMaxLength(int maxLength) {
            super.setMaxLength(maxLength);
            return this;
        }

        /**
         * 入力初期値を設定します。
         *
         * @param text 入力初期値
         * @return {@link Builder}
         */
        @NonNull
        public Builder setText(@NonNull String text) {
            this.text = text;
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
            return new TextInputDialog();
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

            if (this.text != null) {
                arguments.putString(EXTRA_KEY_INPUT_VALUE, this.text);
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
        private DialogButtonClickListener(TextInputDialog dialog, Listener listener) {
            super(dialog, listener);
        }

        /**
         * ボタン押下時に {@link Fragment#onActivityResult(int, int, Intent)} へ通知する
         * {@link Intent} を生成して返します。
         *
         * @return {@link Intent}
         */
        @Override
        @NonNull
        protected Intent makeData() {
            final Intent intent = super.makeData();
            intent.putExtra(EXTRA_KEY_INPUT_VALUE, ((TextInputDialog) this.dialog).getInputText());
            return intent;
        }
    }

    // private static final String TAG = "TextInputDialog";

    /**
     * 入力された文字列を取得します。
     *
     * @return 文字列
     */
    public String getInputText() {
        return ((EditText) Objects.requireNonNull(this.getDialog()).findViewById(R.id.text)).getText().toString();
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
        return R.layout.dialog_text_input;
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
        return arguments.getString(EXTRA_KEY_INPUT_VALUE, "");
    }
}
