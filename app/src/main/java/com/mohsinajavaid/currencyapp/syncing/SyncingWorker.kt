import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.mohsinajavaid.currencyapp.data.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.mohsinajavaid.currencyapp.utils.NetworkResult
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncingWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    val repository: Repository



) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        when (val response = repository.remote.getExchangeRates()) {
            is NetworkResult.Success -> {
                response.data?.let { }
            }
            else -> {}
        }


        return Result.success()
    }


    companion object {
        private const val WORK_TAG = "my_periodic_work"

        fun enqueuePeriodicWork() {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build()

            val periodicWorkRequest = PeriodicWorkRequest.Builder(
                SyncingWorker::class.java,
                24, TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance().enqueueUniquePeriodicWork(
                WORK_TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWorkRequest
            )
        }
    }
}
