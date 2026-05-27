package com.example.ui

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.CobaltRequest
import com.example.api.CobaltResponse
import com.example.api.RetrofitClient
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

sealed interface DownloadState {
    object Idle : DownloadState
    object Loading : DownloadState
    data class Success(val downloadUrl: String, val mediaTitle: String) : DownloadState
    data class Error(val message: String) : DownloadState
}

class XDownloaderViewModel(application: Application) : AndroidViewModel(application) {

    private val database: AppDatabase = AppDatabase.getDatabase(application)
    private val downloadRepository: DownloadRepository
    val allDownloads: StateFlow<List<DownloadItem>>

    private val techRepository: TechRepository
    val allTechItems: StateFlow<List<TechItem>>

    private val cobaltApiService = RetrofitClient.createService()

    // State bindings
    var urlInput by mutableStateOf("")
    var selectedQuality by mutableStateOf("720") // "1080", "720", "480", "360"
    var downloadMode by mutableStateOf("auto")   // "auto", "audio", "mute"
    
    var downloadState: DownloadState by mutableStateOf(DownloadState.Idle)
        private set

    init {
        downloadRepository = DownloadRepository(database.downloadDao())
        allDownloads = downloadRepository.allDownloads.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        techRepository = TechRepository(database.techDao())
        allTechItems = techRepository.allTechItems.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
        seedTechnologiesIfNeeded()
    }

    fun onUrlChange(newUrl: String) {
        urlInput = newUrl
    }

    // Trigger cobalt API to resolve the video/audio link and download it
    fun startDownload(context: Context) {
        val targetUrl = urlInput.trim()
        if (targetUrl.isBlank()) {
            Toast.makeText(context, "الرجاء إدخال رابط صالح أولاً!", Toast.LENGTH_SHORT).show()
            return
        }

        downloadState = DownloadState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val req = CobaltRequest(
                    url = targetUrl,
                    videoQuality = selectedQuality,
                    downloadMode = downloadMode
                )
                val response = cobaltApiService.downloadMedia(req)

                if (response.status == "error") {
                    val errMsg = response.text ?: "فشل فك تشفير الرابط عن طريق الخادم"
                    updateState(DownloadState.Error(errMsg))
                } else if (response.status == "success" || response.status == "stream" || response.status == "redirect") {
                    val finalUrl = response.url
                    if (finalUrl.isNullOrBlank()) {
                        updateState(DownloadState.Error("جاء الرد فارغاً من خادم الخدمة"))
                        return@launch
                    }

                    val titleText = "فيديو اكس_${System.currentTimeMillis()}"
                    val mediaType = if (downloadMode == "audio") "صوت" else "فيديو"

                    // Start actual download using Android DownloadManager
                    enqueueAndroidDownload(context, finalUrl, titleText, mediaType)

                    // Insert to Room DB
                    val newItem = DownloadItem(
                        url = targetUrl,
                        title = titleText,
                        filename = titleText,
                        mediaType = mediaType,
                        quality = selectedQuality,
                        duration = "غير محدد",
                        status = "مكتمل",
                        progress = 1.0f
                    )
                    downloadRepository.insert(newItem)

                    // Reset form and set status SUCCESS
                    urlInput = ""
                    updateState(DownloadState.Success(finalUrl, titleText))
                } else {
                    updateState(DownloadState.Error("خادم الدعم لا يدعم هذا الرابط أو الفئة حالياً"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                updateState(DownloadState.Error(e.localizedMessage ?: "حدث خطأ غير متوقع أثناء الاتصال بالشبكة!"))
            }
        }
    }

    fun resetState() {
        downloadState = DownloadState.Idle
    }

    private fun updateState(newState: DownloadState) {
        viewModelScope.launch(Dispatchers.Main) {
            downloadState = newState
        }
    }

    private fun enqueueAndroidDownload(context: Context, downloadUrl: String, title: String, type: String) {
        try {
            val extension = if (type == "صوت") "mp3" else "mp4"
            val uri = Uri.parse(downloadUrl)
            val request = DownloadManager.Request(uri)
                .setTitle(title)
                .setDescription("تنزيل ملف $type عبر تطبيق اكس عربي...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$title.$extension")
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(context, "بدأ تحميل الملف في الخلفية بنجاح!", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            viewModelScope.launch(Dispatchers.Main) {
                Toast.makeText(context, "فشل تمرير العملية لمدير التحميلات بالهاتف: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun deleteDownload(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            downloadRepository.delete(id)
        }
    }

    fun clearAllDownloads() {
        viewModelScope.launch(Dispatchers.IO) {
            downloadRepository.deleteAll()
        }
    }

    private fun seedTechnologiesIfNeeded() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (techRepository.getCount() == 0) {
                    val preseeded = listOf(
                        TechItem(
                            title = "نماذج Gemini 1.5 Pro",
                            category = "الذكاء الاصطناعي",
                            description = "نموذج توليدي فائق التقدم من جوجل مع سياق بيانات ضخم يتسع لأكثر من مليون رمز. ممتاز لتحليل الأكواد واللغات وصناعة عملاء الذكاء الاصطناعي بدقة متناهية.",
                            launchYear = 2024,
                            complexity = "متوسط",
                            importance = 5,
                            popularity = 98,
                            logoName = "auto_awesome"
                        ),
                        TechItem(
                            title = "إطار العمل Next.js 15",
                            category = "تطوير الويب",
                            description = "المنصة الأكثر استقراراً لبناء تطبيقات ريأكت السريعة. يدعم التقديم البرمجي على جهة الخادم React Server Components والتهيئة المتكاملة لمحركات البحث (SEO).",
                            launchYear = 2024,
                            complexity = "خبير",
                            importance = 5,
                            popularity = 92,
                            logoName = "web"
                        ),
                        TechItem(
                            title = "لغة الأنظمة Rust",
                            category = "تطوير الويب",
                            description = "لغة برمجة تجمع بين كفاءة السي والصدأ السريع وخلو كامل للذاكرة من الأخطاء بدون الحاجة إلى مجمع مهملات (Garbage Collector).",
                            launchYear = 2015,
                            complexity = "خبير",
                            importance = 4,
                            popularity = 89,
                            logoName = "code"
                        ),
                        TechItem(
                            title = "أمن الشبكات Zero Trust",
                            category = "الأمن السيبراني",
                            description = "مفهوم أمني معاصر مالي على مبدأ صارم (لا تثق بالجميع، تحقق دائماً). يحمي الوصول لجميع طبقات النظم السحابية والشبكات من الاختراقات المتقدمة.",
                            launchYear = 2020,
                            complexity = "متوسط",
                            importance = 5,
                            popularity = 95,
                            logoName = "security"
                        ),
                        TechItem(
                            title = "التقنية السحابية Docker",
                            category = "التقنيات السحابية",
                            description = "أداة بناء الخوادم السحابية المعزولة عن طريق الحاويات. تسمح بتغليق برمجياتك بكامل ملفاتها لتعمل بثبات تام على أي بنية تحتية رقمية.",
                            launchYear = 2013,
                            complexity = "مبتدئ",
                            importance = 5,
                            popularity = 96,
                            logoName = "cloud"
                        ),
                        TechItem(
                            title = "عملاء الذكاء الاصطناعي LangChain",
                            category = "الذكاء الاصطناعي",
                            description = "إطار عمل برمجي فريد من نوعه يسهل ربط نماذج اللغات الكبيرة LLMs بقواعد البيانات والأدوات الخارجية لبناء تطبيقات ذات استجابة تفاعلية.",
                            launchYear = 2022,
                            complexity = "متوسط",
                            importance = 5,
                            popularity = 91,
                            logoName = "auto_awesome"
                        ),
                        TechItem(
                            title = "إدارة السحابة Kubernetes",
                            category = "التقنيات السحابية",
                            description = "النظام مفتوح المصدر الأرقى لإدارة أحمال حزم التطبيقات السحابية وموازنة النواقل والأحمال بشكل آلي ذكي فائق السرعة والمثالية.",
                            launchYear = 2014,
                            complexity = "خبير",
                            importance = 4,
                            popularity = 90,
                            logoName = "dns"
                        ),
                        TechItem(
                            title = "حزمة أمن التوصيلات API Security",
                            category = "الأمن السيبراني",
                            description = "المعايير المعتمدة من منظمة OWASP لحماية وتحصين قنوات نقل البيانات (APIs) والخدمات المصغرة من هجمات الحقن وسرقة الرموز وغيرها.",
                            launchYear = 2023,
                            complexity = "متوسط",
                            importance = 5,
                            popularity = 88,
                            logoName = "security"
                        ),
                        TechItem(
                            title = "تقنية الويب عالية الأداء WebAssembly",
                            category = "تطوير الويب",
                            description = "تقنية تشغيل لغات المنصات عالية الأداء مثل Rust و C++ مباشرة في متصفحات الويب وبكفاءة وسرعة تكاد تضاهي الأنظمة المكتوبة مسبقاً للحواسيب.",
                            launchYear = 2017,
                            complexity = "خبير",
                            importance = 4,
                            popularity = 84,
                            logoName = "terminal"
                        ),
                        TechItem(
                            title = "برمجة الكم Qiskit",
                            category = "الذكاء الاصطناعي",
                            description = "أداة برمجية تفاعلية لتطوير واختبار دارات الحوسبة الكمومية والتطبيقات المستقبلية للخوارزميات المتطورة التي تعتمد على الذكاء الكمي.",
                            launchYear = 2017,
                            complexity = "خبير",
                            importance = 3,
                            popularity = 75,
                            logoName = "wifi"
                        )
                    )
                    techRepository.insertAll(preseeded)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleFavorite(id: Int, currentStatus: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            techRepository.updateFavorite(id, !currentStatus)
        }
    }

    fun addNewTech(
        title: String,
        category: String,
        description: String,
        launchYear: Int,
        complexity: String,
        importance: Int,
        popularity: Int,
        logoName: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = TechItem(
                title = title,
                category = category,
                description = description,
                launchYear = launchYear,
                complexity = complexity,
                importance = importance,
                popularity = popularity,
                logoName = logoName,
                isFavorite = false,
                timestamp = System.currentTimeMillis()
            )
            techRepository.insert(item)
        }
    }

    fun deleteTechItem(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            techRepository.delete(id)
        }
    }
}
