package com.kk.attendancemanagerapp.attendancemanagerapp.initialsetting

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.kk.attendancemanagerapp.attendancemanagerapp.manager.AppAlarmManager
import com.kk.attendancemanagerapp.attendancemanagerapp.R
import com.kk.attendancemanagerapp.attendancemanagerapp.attendance.AttendanceActivity
import com.kk.attendancemanagerapp.attendancemanagerapp.data.resource.DataRepository
import com.kk.attendancemanagerapp.attendancemanagerapp.utils.ActivityUtil
import com.kk.attendancemanagerapp.attendancemanagerapp.utils.ViewModelHolder

class InitialSettingActivity : AppCompatActivity(), InitialSettingNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial_setting)

        // ツールバーの設定
        setupToolbar()

        // fragmentを追加して取得する
        val fragment: InitialSettingFragment = findOrCreateViewFragment()

        // viewModelを生成
        val viewModel: InitialSettingViewModel = findOrCreateViewModel()

        // fragmentにviewModelをセット
        fragment.setViewModel(viewModel)

        // コールバック返却用にNavigatorをセットする
        viewModel.setNavigator(this)

        // 初期設定完了済みの場合は、勤怠入力画面に遷移
        if (viewModel.isCompleteInitialSetting() != null && viewModel.isCompleteInitialSetting()!!) {
            val intent = Intent(applicationContext, AttendanceActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivityForResult(intent, REQUEST_CODE_ATTENDANCE)
        }

        // アラームセット
        AppAlarmManager(applicationContext)
            .setDailyAlarm()
    }

    /**
     * ツールバーのセットアップ
     */
    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.initial_setting_toolbar)
        setSupportActionBar(toolbar)
    }

    /**
     * Fragmentを生成し、そのFragmentを返す
     * @return 追加したフラグメント
     */
    private fun findOrCreateViewFragment(): InitialSettingFragment {
        var initialSettingFragment
                = supportFragmentManager.findFragmentById(R.id.content_frame) as InitialSettingFragment?
        if (initialSettingFragment == null) {
            initialSettingFragment = InitialSettingFragment.newInstance()

            ActivityUtil.addFragmentToActivity(supportFragmentManager,
                initialSettingFragment, R.id.content_frame)
        }

        return initialSettingFragment
    }

    /**
     * ViewModelを生成して、そのViewModelを返す
     * @return 追加したViewModel
     */
    @SuppressWarnings("unchecked")
    private fun findOrCreateViewModel(): InitialSettingViewModel {
        val retainedViewModel: ViewModelHolder<InitialSettingViewModel>? =
            supportFragmentManager.findFragmentByTag(TAG_VIEWMODEL_INITIAL_SETTING)
                    as ViewModelHolder<InitialSettingViewModel>?

        return if (retainedViewModel != null || retainedViewModel?.getViewModel() != null) {
            retainedViewModel.getViewModel()!!
        } else {
            val viewModel = InitialSettingViewModel(
                DataRepository.getInstance(), applicationContext)
            // 画面に反映
            ActivityUtil.addFragmentToActivity(supportFragmentManager,
                ViewModelHolder.createContainer(viewModel), TAG_VIEWMODEL_INITIAL_SETTING)
            viewModel
        }
    }

    /**
     * 初期設定完了時の処理. ViewModelからのコールバック
     */
    override fun onComplete() {
        val intent = Intent(applicationContext, AttendanceActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivityForResult(intent, REQUEST_CODE_ATTENDANCE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ATTENDANCE) {
            finish()
        }
    }

    companion object {
        const val TAG_VIEWMODEL_INITIAL_SETTING = "TAG_VIEWMODEL_INITIAL_SETTING"

        // リクエストコード
        const val REQUEST_CODE_ATTENDANCE: Int = 0
    }

}
