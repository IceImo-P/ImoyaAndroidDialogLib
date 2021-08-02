package net.imoya.android.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.Objects;

/**
 * ボタン1個とチェックボックス1個のダイアログ
 * <p/>
 * タイトル、メッセージ、1個のチェックボックス、
 * 1個のボタンを持つ、汎用ダイアログフラグメントです。<ul>
 * <li>親画面は {@link Listener} を実装した {@link Fragment} 又は {@link AppCompatActivity}
 * でなければなりません。</li>
 * <li>{@link Builder}を使用して表示内容を設定し、 {@link Builder#show()}
 * メソッドを呼び出して表示してください。</li>
 * <li>ダイアログ終了時 {@link Listener#onDialogResult(int, int, Intent)}
 * メソッドが呼び出されます。</li>
 * <li>ボタンがクリックされた場合、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_OK} となります。このとき、引数 data の
 * {@link Intent#getBooleanExtra(String, boolean)} メソッドへ
 * {@link SingleButtonAndCheckDialog#EXTRA_KEY_CHECKED}, false
 * を入力することで、チェックボックスのチェック有無を取得できます。</li>
 * <li>ダイアログがキャンセル終了した場合、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_CANCELED} となります。</li>
 * </ul>
 */
@SuppressWarnings("unused")
public class SingleButtonAndCheckDialog extends SingleButtonDialog {
    /**
     * 結果キー定義:チェックボックス状態
     */
    public static final String EXTRA_KEY_CHECKED = "checked";

    /**
     * ダイアログビルダ
     */
    public static class Builder extends SingleButtonDialog.Builder {
        /**
         * チェックボックス文言
         */
        private String checkBoxText = null;
        /**
         * チェックボックス初期状態
         */
        private boolean checked = false;

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
         * ボタン文言を設定します。
         *
         * @param buttonTitle ボタン文言
         * @return {@link Builder}
         */
        @Override
        @NonNull
        public Builder setButtonTitle(@NonNull String buttonTitle) {
            super.setButtonTitle(buttonTitle);
            return this;
        }

        /**
         * チェックボックス文言を設定します。
         *
         * @param checkBoxText チェックボックス文言
         * @return {@link Builder}
         */
        @NonNull
        public Builder setCheckBoxText(String checkBoxText) {
            this.checkBoxText = checkBoxText;
            return this;
        }

        /**
         * チェックボックス初期状態を設定します。
         *
         * @param checked チェックボックス初期状態
         * @return {@link Builder}
         */
        @NonNull
        public Builder setChecked(boolean checked) {
            this.checked = checked;
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
            return new SingleButtonAndCheckDialog();
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

            if (this.checkBoxText != null) {
                arguments.putString(KEY_CHECK_BOX_TEXT, this.checkBoxText);
            }
            arguments.putBoolean(EXTRA_KEY_CHECKED, this.checked);

            return arguments;
        }
    }

    /**
     * ボタンクリックリスナの実装
     */
    private static class DialogButtonClickListener extends DialogItemClickListener {
        /**
         * コンストラクタ
         *
         * @param dialog   ダイアログ
         * @param listener ダイアログのリスナ
         */
        private DialogButtonClickListener(DialogBase dialog, Listener listener) {
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
            // チェックボックス状態を含める
            final Intent data = new Intent();
            data.putExtra(SingleButtonAndCheckDialog.EXTRA_KEY_CHECKED,
                    ((SingleButtonAndCheckDialog) this.dialog).isChecked());
            return data;
        }
    }

    // private static final String TAG = "SingleButtonDialog";

    /**
     * ダイアログ引数キー:入力部分のレイアウトリソースID
     */
    private static final String KEY_LAYOUT_RESOURCE_ID = "layoutResourceId";
    /**
     * ダイアログ引数キー:チェックボックス文言
     */
    private static final String KEY_CHECK_BOX_TEXT = "checkBoxText";

    /**
     * チェックボックス状態を取得します。
     *
     * @return チェック時true、未チェック時false
     */
    public boolean isChecked() {
        return ((CompoundButton) Objects.requireNonNull(this.getDialog()).findViewById(R.id.check)).isChecked();
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                this.requireActivity(), this.getTheme())
                .setTitle(arguments.getString(KEY_TITLE))
                .setMessage(arguments.getString(KEY_MESSAGE))
                .setPositiveButton(
                        arguments.getString(KEY_BUTTON_TITLE),
                        new DialogButtonClickListener(this, this.listener));
        final View view = LayoutInflater.from(builder.getContext()).inflate(
                arguments.getInt(
                        KEY_LAYOUT_RESOURCE_ID, R.layout.dialog_single_button_and_check), null);
        final CompoundButton checkBox = view.findViewById(R.id.check);
        checkBox.setText(arguments.getString(KEY_CHECK_BOX_TEXT, ""));
        checkBox.setChecked(arguments.getBoolean(EXTRA_KEY_CHECKED));
        return builder.setView(view).create();
    }
}
