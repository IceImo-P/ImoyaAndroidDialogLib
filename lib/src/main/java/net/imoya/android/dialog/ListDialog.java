package net.imoya.android.dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * リスト選択ダイアログの abstract
 */
public abstract class ListDialog extends DialogBase {
    /**
     * ダイアログビルダ
     *
     * @param <T> 選択項目の型。 {@link Bundle} に含められる型である必要があります。
     */
    public abstract static class Builder<T> extends DialogBase.Builder {
        /**
         * 選択項目リスト
         */
        protected T[] items;

        /**
         * コンストラクタ
         *
         * @param activity    親画面となる {@link AppCompatActivity}
         * @param requestCode リクエストコード
         * @param <P>        親画面は {@link Listener} を実装した {@link AppCompatActivity} であること
         */
        public <P extends AppCompatActivity & Listener> Builder(@NonNull P activity, int requestCode) {
            super(new BuilderParentActivity<>(activity), requestCode);
        }

        /**
         * コンストラクタ
         *
         * @param fragment    親画面となる{@link Fragment}
         * @param requestCode リクエストコード
         * @param <P>        親画面は {@link Listener} を実装した {@link Fragment} であること
         */
        public <P extends Fragment & Listener> Builder(@NonNull P fragment, int requestCode) {
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
        public Builder<T> setTitle(@NonNull String title) {
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
        public Builder<T> setMessage(@NonNull String message) {
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
        public Builder<T> setTag(@NonNull String tag) {
            super.setTag(tag);
            return this;
        }

        /**
         * 選択項目リストを設定します。
         *
         * @param items 選択項目リスト
         * @return {@link Builder}
         */
        @NonNull
        public Builder<T> setItems(@NonNull T[] items) {
            this.items = items;
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
            return new SingleButtonDialog();
        }
    }

    /**
     * ダイアログ引数キー:選択項目リスト
     */
    protected static final String KEY_ITEMS = "items";
}
