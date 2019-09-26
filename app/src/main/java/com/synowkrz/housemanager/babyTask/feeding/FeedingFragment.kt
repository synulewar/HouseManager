package com.synowkrz.housemanager.babyTask.feeding


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.synowkrz.housemanager.R
import com.synowkrz.housemanager.babyTask.BabyEventListAdapter
import com.synowkrz.housemanager.babyTask.model.FeedingType
import com.synowkrz.housemanager.databinding.FragmentFeedingBinding


class FeedingFragment : Fragment() {

    private val viewModel: FeedingViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProviders.of(this, FeedingViewModel.Factory(activity.application, name))
            .get(FeedingViewModel::class.java)
    }

    private lateinit var binding: FragmentFeedingBinding
    private lateinit var name : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        name = FeedingFragmentArgs.fromBundle(arguments!!).profileName
        binding = FragmentFeedingBinding.inflate(inflater)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel
        binding.recyclerView.adapter = BabyEventListAdapter(BabyEventListAdapter.OnBabyEventClickListener{
            Toast.makeText(activity, "Klikniety event", Toast.LENGTH_SHORT).show()
        }, activity!!.application)


        viewModel.feedingStateView.observe(this, Observer {
            if (it == null) {
                return@Observer
            }

            when(it) {
                FeedingState.STARTED -> prepareStartedScreen()
                FeedingState.STOPPED -> prepareStoppedScreen()
                FeedingState.PAUSED -> preparePausedScreen()
                FeedingState.ACTIVE -> prepareActiveScreen()
            }
        })


        viewModel.prepareFeedingScreen.observe(this, Observer {
            if (it == null) {
                return@Observer
            }

            when(it) {
                FeedingType.LEFT -> setFeddingTypesVisibility(left = View.VISIBLE)
                FeedingType.RIGHT -> setFeddingTypesVisibility(right = View.VISIBLE)
                FeedingType.BOTTLE -> setFeddingTypesVisibility(bottle = View.VISIBLE)
                FeedingType.SOLID -> setFeddingTypesVisibility(solid = View.VISIBLE)
            }
            viewModel.prepareFeedinScreenFinished()
        })

        viewModel.timerText.observe(this, Observer {
            binding.feedingTime.text = it
        })

        return binding.root
    }

    fun preparePausedScreen() {
        setScreenElementVisibility(View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE)
        binding.startPause.text = getString(R.string.start)
        binding.feedingTitle.text = String.format("%s %s", name, getString(R.string.wait))
        viewModel.onScreenPrepareFinished()
    }

    fun prepareStartedScreen() {
        setScreenElementVisibility(View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE)
        binding.startPause.text = getString(R.string.pause)
        binding.feedingTitle.text = String.format("%s %s", name, getString(R.string.eat))
        viewModel.onScreenPrepareFinished()
    }

    fun prepareActiveScreen() {
        setScreenElementVisibility(View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE)
        binding.startPause.text = getString(R.string.start)
        binding.feedingTitle.text = String.format("%s %s", name, getString(R.string.eat))
        viewModel.onScreenPrepareFinished()
    }

    fun prepareStoppedScreen() {
        setScreenElementVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE)
        setFeddingTypesVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE)
        viewModel.onScreenPrepareFinished()
    }

    fun setFeddingTypesVisibility(left: Int = View.INVISIBLE,
                                  right: Int = View.INVISIBLE,
                                  bottle: Int = View.INVISIBLE,
                                  solid: Int = View.INVISIBLE) {
        binding.leftFeeding.visibility = left
        binding.rightFeeding.visibility = right
        binding.bottleFeeding.visibility = bottle
        binding.solidFeeding.visibility = solid
    }

    fun setScreenElementVisibility(recycler: Int,
                                   feedingTime: Int,
                                   feedingTitle: Int,
                                   startPause: Int,
                                   stop: Int) {
        binding.recyclerView.visibility = recycler
        binding.feedingTime.visibility = feedingTime
        binding.feedingTitle.visibility = feedingTitle
        binding.startPause.visibility = startPause
        binding.stop.visibility = stop
    }

}
