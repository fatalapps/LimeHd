package com.apps.fatal.limehdtest.ui.fragments

import android.app.Activity
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.apps.fatal.app_domain.repositories.entities.ChannelEntity
import com.apps.fatal.common_domain.drawable
import com.apps.fatal.common_domain.string
import com.apps.fatal.common_ui.mvvm.BaseFragment
import com.apps.fatal.common_ui.mvvm.BaseViewModelFactory
import com.apps.fatal.common_ui.loadWithGlide
import com.apps.fatal.limehdtest.R
import com.apps.fatal.limehdtest.common.generateQualityList
import com.apps.fatal.limehdtest.common.getAppInjector
import com.apps.fatal.limehdtest.databinding.PlayerLayoutBinding
import com.apps.fatal.limehdtest.ui.viewmodels.PlayerViewModel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject


class PlayerFragment : BaseFragment<PlayerViewModel, PlayerLayoutBinding>(), Player.Listener {

    @Inject
    lateinit var viewModelFactory: BaseViewModelFactory

    private val playerView get() = binding.playerView
    private var player: ExoPlayer? = null

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var qualityPopUp: PopupMenu? = null
    private var qualityLabel: String? = null
    private var trackSelector: DefaultTrackSelector? = null
    private var qualityList = ArrayList<Pair<String, TrackSelectionParameters.Builder>>()


    private lateinit var icon: ImageView
    private lateinit var title: TextView
    private lateinit var subtitle: TextView
    private lateinit var backBtn: ImageView
    private lateinit var settingsBtn: ImageView

    companion object {

        private const val CH_ID_ARG = "mediaArg_"

        fun newInstance(id: Int): PlayerFragment {
            return PlayerFragment().apply {
                arguments = Bundle().apply { putInt(CH_ID_ARG, id) }
            }
        }
    }

    override fun provideViewModel(): PlayerViewModel {
        getAppInjector().inject(this)
        viewModelFactory.paramsInjector = {
            PlayerViewModel.Params(
                arguments?.getInt(CH_ID_ARG)
            )
        }
        return ViewModelProvider(this, viewModelFactory)[PlayerViewModel::class.java]
    }

    override fun onCreate(view: View): View {

        playerView.run {
            icon = findViewById(R.id.icon)
            title = findViewById(R.id.title)
            subtitle = findViewById(R.id.subtitle)
            backBtn = findViewById(R.id.back)
            settingsBtn = findViewById(R.id.settings)
        }

        backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        settingsBtn.setOnClickListener {
            qualityPopUp?.show()
            requireActivity().fullScreen()
        }

        return super.onCreate(view)
    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.filterNotNull().collectLatest {
                withContext(Dispatchers.Main) {
                    setupHeader(it)

                    if (Util.SDK_INT >= 24) {
                        initializePlayer()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if ((Util.SDK_INT < 24 || player == null)) {
            initializePlayer()
        }
    }

    override fun getInflater(inflater: LayoutInflater, vg: ViewGroup?, attach: Boolean) = PlayerLayoutBinding.inflate(inflater, vg, attach)


    private fun initializePlayer() {
        viewModel.stateFlow.value?.url?.let { url ->
            if (player == null) {
                trackSelector = DefaultTrackSelector(requireContext(), AdaptiveTrackSelection.Factory())

                player = ExoPlayer.Builder(requireContext()).apply {
                    setRenderersFactory(DefaultRenderersFactory(requireContext()))
                    setTrackSelector(trackSelector!!)
                    setLoadControl(DefaultLoadControl())
                }.build()

                playerView.player = player
                player?.playWhenReady = playWhenReady
                player?.addListener(this)
                player?.seekTo(currentWindow, playbackPosition)
            }
            val mediaSource = buildMediaSource(Uri.parse(url))
            mediaSource?.let { src ->
                player?.setMediaSource(src, true)
                player?.prepare()
            }
        }

        requireActivity().fullScreen()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    private fun buildMediaSource(uri: Uri): MediaSource? {

        val type = Util.inferContentType(uri)
        val dataSourceFactory: DataSource.Factory = DefaultHttpDataSource.Factory()

        return if (type == C.CONTENT_TYPE_HLS)
            HlsMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri)) else null
    }

    private fun releasePlayer() {
        if (player != null) {
            playbackPosition = player?.currentPosition ?: 0L
            currentWindow = player?.currentMediaItemIndex ?: 0
            playWhenReady = player?.playWhenReady ?: false
            player?.release()
            player = null
        }
    }

    private fun Activity.fullScreen() {
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        decorView.systemUiVisibility = uiOptions
    }

    private fun setUpQualityList() {
        qualityPopUp = PopupMenu(requireContext(), settingsBtn)
        qualityList.let {
            for ((i, videoQuality) in it.withIndex()) {
                val item = qualityPopUp?.menu?.add(0, i, 0, videoQuality.first)
                if (qualityLabel == videoQuality.first) item?.icon = (drawable(R.drawable.ic_baseline_check_24))
            }
            val autoItem = qualityPopUp?.menu?.add(0, it.size, 0, string(R.string.auto))
            if (qualityLabel == null) autoItem?.icon = (drawable(R.drawable.ic_baseline_check_24))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            qualityPopUp?.setForceShowIcon(true)
        }
        qualityPopUp?.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == qualityList.size) {
                qualityLabel = null
                player?.trackSelectionParameters = TrackSelectionParameters.Builder(requireContext()).build()
            } else {
                qualityList[menuItem.itemId].let {
                    trackSelector!!.parameters = trackSelector!!.parameters
                        .buildUpon()
                        .setTunnelingEnabled(true)
                        .build()

                    qualityLabel = it.first
                    player?.trackSelectionParameters = it.second.build()
                }
            }
            true
        }
    }

    private fun setupHeader(ch: ChannelEntity) {
        ch.image?.let { icon.loadWithGlide(it) }
        title.text = ch.current.title
        subtitle.text = ch.nameRu
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        if (playbackState == Player.STATE_READY) {
            trackSelector?.generateQualityList()?.let {
                qualityList = it
                setUpQualityList()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}