package net.imoya.android.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import net.imoya.android.util.Log;

import java.util.Arrays;

/**
 * 単一項目選択ダイアログの abstract
 * <p/>
 * タイトル、単一選択リスト、OKボタン、キャンセルボタンを持つ、
 * 汎用ダイアログフラグメントの abstract です。<ul>
 * <li>親画面は {@link Listener} を実装した {@link Fragment} 又は {@link AppCompatActivity}
 * でなければなりません。</li>
 * <li>{@link Builder}を使用して表示内容を設定し、 {@link Builder#show()}
 * メソッドを呼び出して表示してください。</li>
 * <li>ダイアログ終了時 {@link Listener#onDialogResult(int, int, Intent)}
 * メソッドが呼び出されます。</li>
 * <li>OKボタンがクリックされた場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_OK} となります。このとき、引数 data の
 * {@link Intent#getIntExtra(String, int)} へ {@link #EXTRA_KEY_WHICH}
 * を入力することで、選択された項目の位置(又は、未選択を表す-1)を取得できます。</li>
 * <li>キャンセルボタンがクリックされた場合、又はダイアログがキャンセル終了した場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_CANCELED} となります。</li>
 * </ul>
 */
@SuppressWarnings("unused")
public abstract class SingleChoiceDialogBase extends OkCancelDialog {
    /**
     * ダイアログビルダ
     */
    public abstract static class Builder extends OkCancelDialog.Builder {
        /**
         * 選択項目リスト
         */
        private String[] items = null;
        /**
         * 初期選択位置
         */
        private int selectedPosition = -1;

        /**
         * コンストラクタ
         *
         * @param activity    親画面となる {@link AppCompatActivity}
         * @param requestCode リクエストコード
         * @param <T>         親画面は {@link Listener} を実装した {@link AppCompatActivity} であること
         */
        public <T extends AppCompatActivity & Listener> Builder(
                @NonNull T activity, int requestCode) {
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
         * このメソッドは使用できません。
         *
         * @param message メッセージ文言
         * @return {@link Builder}
         * @throws IllegalArgumentException このメソッドが呼び出されました。
         */
        @Override
        @NonNull
        public Builder setMessage(@NonNull String message) {
            throw new IllegalArgumentException(
                    "Don't use setMessage for StringArrayDialog. Use setTitle() instead.");
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
         * OKボタン文言を設定します。デフォルト値は {@link android.R.string#ok } です。
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
         * キャンセルボタン文言を設定します。デフォルト値は {@link android.R.string#cancel } です。
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
         * 選択項目リストを設定します。
         *
         * @param items 選択項目リスト
         * @return {@link Builder}
         */
        @NonNull
        public Builder setItems(String[] items) {
            this.items = items;
            return this;
        }

        /**
         * 初期選択位置を設定します。
         *
         * @param selectedPosition 初期選択位置、又は未選択状態を指定する -1
         * @return {@link Builder}
         */
        @NonNull
        public Builder setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
            return this;
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

            arguments.putStringArray(KEY_ITEMS, this.items);
            arguments.putInt(EXTRA_KEY_WHICH, this.selectedPosition);

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
        protected DialogButtonClickListener(SingleChoiceDialogBase dialog, Listener listener) {
            super(dialog, listener);
        }

        /**
         * ボタン押下時に {@link Fragment#onActivityResult(int, int, Intent)} 又は
         * {@link Listener#onDialogResult(int, int, Intent)} へ通知する
         * {@link Intent} を生成して返します。
         *
         * @return {@link Intent}
         */
        @Override
        @NonNull
        protected Intent makeData() {
            final Intent data = super.makeData();
            data.putExtra(
                    EXTRA_KEY_WHICH, ((SingleChoiceDialogBase) this.dialog).selectedPosition);
            return data;
        }
    }

    /**
     * ダイアログ引数キー:選択項目リスト
     */
    protected static final String KEY_ITEMS = "items";

    private static final String TAG = "SingleChoiceDialogBase";

    /**
     * 選択項目リスト
     */
    protected String[] items = null;
    /**
     * 現在の選択位置
     */
    protected int selectedPosition = -1;

    /**
     * 現在の選択位置を取得します。
     *
     * @return 現在の選択位置、又は未選択状態を表す -1
     */
    public int getSelectedPosition() {
        return this.selectedPosition;
    }

    /**
     * {@link Fragment} 生成時の処理
     *
     * @param savedInstanceState 再生成前に保存された情報
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle arguments = this.requireArguments();

        // 選択項目リストを、ダイアログ引数より取得する
        final String[] tmpItems = arguments.getStringArray(KEY_ITEMS);
        if (tmpItems == null) {
            // 必ず指定しなければならない
            throw new NullPointerException("items == null");
        }
        // 参照する引数の変化を防ぐため、必ずcloneする
        this.items = tmpItems.clone();

        // 生成時の選択位置を、ダイアログ引数又は再生成前の保存情報より取得する
        this.selectedPosition = (savedInstanceState != null
                ? savedInstanceState.getInt(EXTRA_KEY_WHICH)
                : arguments.getInt(EXTRA_KEY_WHICH));
        if (this.selectedPosition < -1 || this.selectedPosition >= this.items.length) {
            // 異常値の場合は、未選択とする
            Log.w(TAG, "onCreate: Illegal position(" + this.selectedPosition + ")");
            this.selectedPosition = -1;
        }

        Log.d(TAG, "onCreate: items = " + Arrays.asList(items)
                + ", selectedPosition = " + this.selectedPosition);
    }

    /**
     * {@link Fragment} 再生成前の状態保存処理
     *
     * @param outState 保存先
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(EXTRA_KEY_WHICH, this.selectedPosition);
    }
}
