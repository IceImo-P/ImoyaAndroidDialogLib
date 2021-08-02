package net.imoya.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * 入力ダイアログの共通部分
 */
@SuppressWarnings("unused")
public abstract class InputDialog extends OkCancelDialog {
    /**
     * 結果キー定義:入力値
     */
    public static final String EXTRA_KEY_INPUT_VALUE = "inputValue";

    /**
     * ダイアログビルダ
     */
    public static abstract class Builder extends OkCancelDialog.Builder {
        /**
         * レイアウトリソースID
         */
        private int layoutResourceId = 0;
        /**
         * ヒント文言
         */
        private String hint;
        /**
         * 入力種別
         */
        private int inputType;
        /**
         * 入力種別設定済フラグ
         */
        private boolean inputTypeIsSet = false;
        /**
         * 最大入力可能文字数
         */
        private int maxLength = Integer.MAX_VALUE;

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
        @NonNull
        public Builder setLayoutResourceId(int layoutResourceId) {
            this.layoutResourceId = layoutResourceId;
            return this;
        }

        /**
         * ヒント文言を設定します。
         *
         * @param hint ヒント文言
         * @return {@link Builder}
         */
        @NonNull
        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        /**
         * 入力種別を設定します。
         *
         * @param inputType 入力種別
         * @return {@link Builder}
         */
        @NonNull
        public Builder setInputType(int inputType) {
            this.inputType = inputType;
            this.inputTypeIsSet = true;
            return this;
        }

        /**
         * 最大入力可能文字数を設定します。
         *
         * @param maxLength 最大入力可能文字数
         * @return {@link Builder}
         */
        @NonNull
        public Builder setMaxLength(int maxLength) {
            this.maxLength = maxLength;
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

            if (this.layoutResourceId != 0) {
                arguments.putInt(KEY_LAYOUT_RESOURCE_ID, this.layoutResourceId);
            }
            if (this.hint != null) {
                arguments.putString(KEY_HINT, this.hint);
            }
            if (this.inputTypeIsSet) {
                arguments.putInt(KEY_INPUT_TYPE, this.inputType);
            }
            arguments.putInt(KEY_MAX_LENGTH, this.maxLength);

            return arguments;
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    protected static abstract class DialogButtonClickListener extends OkCancelDialog.DialogButtonClickListener {
        /**
         * コンストラクタ
         *
         * @param dialog   ダイアログ
         * @param listener ダイアログのリスナ
         */
        protected DialogButtonClickListener(InputDialog dialog, Listener listener) {
            super(dialog, listener);
        }
    }

    /**
     * ダイアログ引数キー:ヒント文言
     */
    private static final String KEY_HINT = "hint";
    /**
     * ダイアログ引数キー:入力種別
     */
    private static final String KEY_INPUT_TYPE = "inputType";
    /**
     * ダイアログ引数キー:最大入力可能文字数
     */
    private static final String KEY_MAX_LENGTH = "maxLength";

//    private static final String TAG = "InputDialog";

    /**
     * ボタンクリック時の処理を実装した {@link DialogButtonClickListener} を返します。
     * 返された {@link DialogButtonClickListener} は、ダイアログ全ボタンのリスナとなります。
     *
     * @return {@link DialogButtonClickListener}
     */
    protected abstract DialogButtonClickListener getDialogButtonClickListener();

    /**
     * ダイアログへ組み込む入力部分の {@link View}
     * を指定する、レイアウトリソースIDのデフォルト値を返します。
     *
     * @return レイアウトリソースID
     */
    @LayoutRes
    protected abstract int getDefaultInputViewLayoutResourceId();

    /**
     * 入力欄へ初期表示する内容を返します。
     *
     * @param context   {@link Context}
     * @param arguments ダイアログ引数を含む {@link Bundle}
     * @return 入力欄へ表示する文字列
     */
    protected abstract CharSequence getInitialValue(Context context, Bundle arguments);

    /**
     * ダイアログへ組み込む、入力部分の {@link View} を生成して返します。
     *
     * @param context   {@link Context}
     * @param arguments ダイアログ引数を含む {@link Bundle}
     * @return {@link View}
     */
    protected View inflateInputView(Context context, Bundle arguments) {
        final View view = LayoutInflater.from(context).inflate(
                arguments.getInt(KEY_LAYOUT_RESOURCE_ID,
                        this.getDefaultInputViewLayoutResourceId()),
                null);
        final EditText editText = view.findViewById(R.id.text);
        editText.setHint(arguments.getString(KEY_HINT, ""));
        if (arguments.containsKey(KEY_INPUT_TYPE)) {
            editText.setInputType(arguments.getInt(KEY_INPUT_TYPE, InputType.TYPE_CLASS_TEXT));
        }
        final int maxLength = arguments.getInt(KEY_MAX_LENGTH);
        if (maxLength != Integer.MAX_VALUE) {
            final ArrayList<InputFilter> filters = new ArrayList<>();
            final InputFilter[] sourceFilters = editText.getFilters();
            for (InputFilter filter : sourceFilters) {
                if (!(filter instanceof InputFilter.LengthFilter)) {
                    filters.add(filter);
                }
            }
            filters.add(new InputFilter.LengthFilter(maxLength));
            editText.setFilters(filters.toArray(new InputFilter[0]));
        }
        editText.setText(this.getInitialValue(context, arguments));

        return view;
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
        final DialogButtonClickListener buttonClickListener = this.getDialogButtonClickListener();
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                this.requireActivity(), this.getTheme())
                .setTitle(arguments.getString(KEY_TITLE))
                .setMessage(arguments.getString(KEY_MESSAGE))
                .setPositiveButton(
                        arguments.getString(KEY_POSITIVE_BUTTON_TITLE), buttonClickListener)
                .setNegativeButton(
                        arguments.getString(KEY_NEGATIVE_BUTTON_TITLE), buttonClickListener);
        final View view = this.inflateInputView(builder.getContext(), arguments);
        return builder.setView(view).create();
    }
}
