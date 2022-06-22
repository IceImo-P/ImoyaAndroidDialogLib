package net.imoya.android.dialog

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

@Suppress("unused")
object DialogUtil {
    /**
     * リクエストコードに対応するリクエストキーを返します。
     *
     * @param requestCode リクエストコード
     * @return リクエストキー
     */
    @JvmStatic
    fun getRequestKey(requestCode: Int): String {
        return "imoya-dialog-$requestCode"
    }

    /**
     * ダイアログの親画面が、ダイアログの結果を受け取るための初期設定を行います。
     *
     * @param fragmentManager ダイアログを開始する [FragmentManager]
     * @param lifecycleOwner 親画面の [LifecycleOwner]
     * @param listener 結果を受け取る [DialogListener]
     * @param requestCode ダイアログへ設定するリクエストコード
     */
    @JvmStatic
    fun registerDialogListener(
        fragmentManager: FragmentManager,
        lifecycleOwner: LifecycleOwner,
        listener: DialogListener,
        requestCode: Int
    ) {
        fragmentManager.setFragmentResultListener(
            getRequestKey(requestCode),
            lifecycleOwner,
            FragmentResultHandler(requestCode, listener)
        )
    }

    /**
     * ダイアログの親画面である [AppCompatActivity] が、ダイアログの結果を受け取るための初期設定を行います。
     *
     * @param activity 親画面の [AppCompatActivity]
     * @param listener 結果を受け取る [DialogListener]
     * @param requestCode ダイアログへ設定するリクエストコード
     */
    @JvmStatic
    fun registerDialogListener(
        activity: AppCompatActivity,
        listener: DialogListener,
        requestCode: Int
    ) {
        registerDialogListener(
            activity.supportFragmentManager,
            activity,
            listener,
            requestCode
        )
    }

    /**
     * ダイアログの親画面である [Fragment] が、ダイアログの結果を受け取るための初期設定を行います。
     *
     * @param fragment 親画面の [Fragment]
     * @param listener 結果を受け取る [DialogListener]
     * @param requestCode ダイアログへ設定するリクエストコード
     */
    @JvmStatic
    fun registerDialogListener(fragment: Fragment, listener: DialogListener, requestCode: Int) {
        registerDialogListener(
            fragment.childFragmentManager,
            fragment.viewLifecycleOwner,
            listener,
            requestCode
        )
    }

    /**
     * ダイアログの親画面が、ダイアログの結果を受け取るための初期設定を行います。
     *
     * @param parent 親画面の [DialogParent]
     * @param requestCode ダイアログへ設定するリクエストコード
     */
    @JvmStatic
    fun registerDialogListener(parent: DialogParent, requestCode: Int) {
        registerDialogListener(
            parent.fragmentManager,
            parent.lifecycleOwner,
            parent.listener,
            requestCode
        )
    }

    /**
     * Tag for log
     */
    const val TAG = "DialogUtil"
}