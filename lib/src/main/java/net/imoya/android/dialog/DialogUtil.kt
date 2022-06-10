package net.imoya.android.dialog

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

@Suppress("unused")
object DialogUtil {
    /**
     * ダイアログ親画面である [Fragment] が、ダイアログの結果を受け取るための初期設定を行います。
     *
     * 親画面の [Fragment.onViewCreated] で呼び出してください。
     *
     * @param fragment 親画面の [Fragment]
     * @param listener 結果を受け取る [DialogListener]
     * @param requestCode ダイアログへ設定するリクエストコード
     */
    @JvmStatic
    fun registerDialogListener(fragment: Fragment, listener: DialogListener, requestCode: Int) {
        fragment.childFragmentManager.setFragmentResultListener(
            "imoya-dialog-$requestCode", fragment.viewLifecycleOwner
        ) { _, bundle ->
            DialogLog.v(TAG) { "onFragmentResult: start. requestCode = $requestCode" }

            val intent = Intent()
            val action = bundle.getString(DialogBase.KEY_INTERNAL_RESULT_DATA_ACTION)
            if (action != null) {
                intent.action = action
            }
            if (bundle.containsKey(DialogBase.KEY_INTERNAL_RESULT_DATA_DATA)) {
                intent.data = bundle.getParcelable(DialogBase.KEY_INTERNAL_RESULT_DATA_DATA)
            }
            val source: Bundle? =
                bundle.getBundle(DialogBase.KEY_INTERNAL_RESULT_DATA_EXTRA)
            if (source != null) {
                intent.putExtras(source)
            }
            listener.onDialogResult(
                requestCode, bundle.getInt(DialogBase.KEY_INTERNAL_RESULT_CODE), intent
            )

            DialogLog.v(TAG) { "onFragmentResult: end. requestCode = $requestCode" }
        }
    }

    /**
     * ダイアログ親画面である [Fragment] が、ダイアログの結果を受け取るための初期設定を行います。
     *
     * 親画面の [Fragment.onViewCreated] で呼び出してください。
     *
     * @param fragment 親画面の [Fragment] 。 [DialogListener] を実装する必要があります。
     * @param requestCode ダイアログへ設定するリクエストコード
     */
    @JvmStatic
    fun <T> registerDialogListener(
        fragment: T,
        requestCode: Int
    ) where T : Fragment, T : DialogListener {
        registerDialogListener(fragment, fragment, requestCode)
    }

    /**
     * ダイアログ親画面である [Fragment] が、ダイアログの結果を受け取るための初期設定を行います。
     *
     * 親画面の [Fragment.onViewCreated] で呼び出してください。
     *
     * @param parent 親画面の [DialogParentFragment]
     * @param requestCode ダイアログへ設定するリクエストコード
     */
    @JvmStatic
    fun registerDialogListener(parent: DialogParentFragment<*>, requestCode: Int) {
        registerDialogListener(parent.fragment, parent.fragment as DialogListener, requestCode)
    }

    /**
     * Tag for log
     */
    const val TAG = "DialogUtil"
}