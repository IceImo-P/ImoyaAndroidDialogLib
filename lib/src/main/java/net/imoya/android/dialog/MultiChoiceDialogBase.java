package net.imoya.android.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import net.imoya.android.util.Log;
import net.imoya.android.util.LogUtil;

import java.util.Arrays;

/**
 * 複数項目選択ダイアログ
 * <p/>
 * タイトル、複数選択リスト、OKボタン、キャンセルボタンを持つ、
 * 汎用ダイアログフラグメントです。<ul>
 * <li>親画面は {@link Listener} を実装した
 * {@link Fragment} 又は {@link AppCompatActivity} でなければなりません。</li>
 * <li>{@link Builder}を使用して表示内容を設定し、 {@link Builder#show()}
 * メソッドを呼び出して表示してください。</li>
 * <li>ダイアログ終了時 {@link Listener#onDialogResult(int, int, Intent)}
 * メソッドが呼び出されます。</li>
 * <li>OKボタンがクリックされた場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_OK} となります。このとき、引数 data の
 * {@link Intent#getBooleanArrayExtra(String)} へ {@link #EXTRA_KEY_CHECKED_LIST}
 * を入力することで、全項目の選択状態リストを取得できます。</li>
 * <li>キャンセルボタンがクリックされた場合、又はダイアログがキャンセル終了した場合は、
 * {@link Listener#onDialogResult(int, int, Intent)} メソッドの引数 resultCode の値が
 * {@link Activity#RESULT_CANCELED} となります。</li>
 * </ul>
 */
public abstract class MultiChoiceDialogBase extends OkCancelDialog {
    /**
     * 結果キー定義:選択位置
     */
    public static final String EXTRA_KEY_CHECKED_LIST = "checkedList";

    /**
     * ダイアログビルダ
     */
    public abstract static class Builder extends OkCancelDialog.Builder {
        /**
         * 選択項目リスト
         */
        private String[] items = null;
        /**
         * 全項目の初期選択状態リスト
         */
        private boolean[] checkedList = null;

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
        public Builder setNegativeButtonTitle(@NonNull String buttonTitle) {
            super.setNegativeButtonTitle(buttonTitle);
            return this;
        }

        /**
         * 選択項目リストを設定します。<ul>
         * <li>{@link #show()} メソッドを呼び出す前に、必ず設定してください。</li>
         * </ul>
         *
         * @param items 選択項目リスト
         * @return {@link Builder}
         */
        public Builder setItems(@NonNull String[] items) {
            this.items = items;
            return this;
        }

        /**
         * 全選択項目の初期選択状態リストを設定します。<ul>
         * <li>{@link #show()} メソッドを呼び出す前にこのメソッドを呼び出さなかった場合や、
         * null を指定した場合は、全項目が未選択となります。</li>
         * <li>null 以外を設定する場合は、必ず {@link #setItems(String[])}
         * メソッドと同一の項目数を設定してください。</li>
         * </ul>
         *
         * @param checkedList 全選択項目の選択状態リスト
         * @return {@link Builder}
         */
        public Builder setCheckedList(boolean[] checkedList) {
            this.checkedList = checkedList;
            return this;
        }

        /**
         * ダイアログへ渡す引数を生成して返します。
         *
         * @return 引数を含んだ {@link Bundle}
         */
        @Override
        protected Bundle makeArguments() {
            final Bundle arguments = super.makeArguments();

            arguments.putStringArray(KEY_ITEMS, this.items);
            arguments.putBooleanArray(EXTRA_KEY_CHECKED_LIST, this.checkedList);

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
        protected DialogButtonClickListener(MultiChoiceDialogBase dialog, Listener listener) {
            super(dialog, listener);
        }

        @Override
        protected Intent makeData() {
            final Intent data = super.makeData();
            data.putExtra(
                    EXTRA_KEY_CHECKED_LIST, ((MultiChoiceDialogBase) this.dialog).checkedList);
            return data;
        }
    }

    /**
     * ダイアログ引数キー:選択項目リスト
     */
    protected static final String KEY_ITEMS = "items";

    private static final String TAG = "MultiChoiceDialogBase";

    /**
     * 選択項目リスト
     */
    protected String[] items = null;
    /**
     * 現在の選択状態
     */
    protected boolean[] checkedList = null;

    /**
     * 現在の選択状態を取得します。
     *
     * @return 全項目の選択状態リスト
     */
    public boolean[] getCheckedList() {
        return this.checkedList;
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

        // 生成時のチェック状態を、ダイアログ引数又は再生成前の保存情報より取得する
        final boolean[] tmpCheckedList = (savedInstanceState != null
                ? savedInstanceState.getBooleanArray(EXTRA_KEY_CHECKED_LIST)
                : arguments.getBooleanArray(EXTRA_KEY_CHECKED_LIST));
        Log.d(TAG, "onCreate: tmpCheckedList = " +  LogUtil.logString(tmpCheckedList));
        if (tmpCheckedList != null && tmpCheckedList.length != this.items.length) {
            // チェック状態指定が選択項目数と異なる場合は、警告ログを出力する
            Log.w(TAG, "onCreate: Illegal checked list length(Item count is "
                    + this.items.length + " but checked list count is "
                    + tmpCheckedList.length + ")");
        }
        // 参照する引数の変化を防ぐため、必ずcloneする
        // 引数が存在しない場合や項目数が異なる場合は、リストを新規作成する
        this.checkedList = (tmpCheckedList != null && tmpCheckedList.length == this.items.length
                ? tmpCheckedList.clone() : new boolean[this.items.length]);

        Log.d(TAG, "onCreate: items = " + Arrays.asList(items)
                + ", checkedList = " +  LogUtil.logString(this.checkedList));
    }

    /**
     * {@link Fragment} 再生成前の状態保存処理
     *
     * @param outState 保存先
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBooleanArray(EXTRA_KEY_CHECKED_LIST, this.checkedList);
    }
}
