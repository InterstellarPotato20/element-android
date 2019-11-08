/*
 * Copyright 2019 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package im.vector.riotx.features.roomdirectory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.airbnb.mvrx.viewModel
import im.vector.riotx.R
import im.vector.riotx.core.di.ScreenComponent
import im.vector.riotx.core.extensions.addFragment
import im.vector.riotx.core.extensions.addFragmentToBackstack
import im.vector.riotx.core.platform.VectorBaseActivity
import im.vector.riotx.features.roomdirectory.createroom.CreateRoomActions
import im.vector.riotx.features.roomdirectory.createroom.CreateRoomFragment
import im.vector.riotx.features.roomdirectory.createroom.CreateRoomViewModel
import im.vector.riotx.features.roomdirectory.picker.RoomDirectoryPickerFragment
import javax.inject.Inject

class RoomDirectoryActivity : VectorBaseActivity() {

    @Inject lateinit var createRoomViewModelFactory: CreateRoomViewModel.Factory
    @Inject lateinit var roomDirectoryViewModelFactory: RoomDirectoryViewModel.Factory
    private val roomDirectoryViewModel: RoomDirectoryViewModel by viewModel()
    private val createRoomViewModel: CreateRoomViewModel by viewModel()
    private lateinit var actionViewModel: RoomDirectorySharedActionViewModel

    override fun getLayoutRes() = R.layout.activity_simple

    override fun injectWith(injector: ScreenComponent) {
        injector.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionViewModel = ViewModelProviders.of(this, viewModelFactory).get(RoomDirectorySharedActionViewModel::class.java)

        if (isFirstCreation()) {
            roomDirectoryViewModel.handle(RoomDirectoryActions.FilterWith(intent?.getStringExtra(INITIAL_FILTER) ?: ""))
        }

        actionViewModel.observe()
                .subscribe { navigation ->
                    when (navigation) {
                        is RoomDirectorySharedAction.Back           -> onBackPressed()
                        is RoomDirectorySharedAction.CreateRoom     -> addFragmentToBackstack(R.id.simpleFragmentContainer, CreateRoomFragment::class.java)
                        is RoomDirectorySharedAction.ChangeProtocol -> addFragmentToBackstack(R.id.simpleFragmentContainer, RoomDirectoryPickerFragment::class.java)
                        is RoomDirectorySharedAction.Close          -> finish()
                    }
                }
                .disposeOnDestroy()

        roomDirectoryViewModel.selectSubscribe(this, PublicRoomsViewState::currentFilter) { currentFilter ->
            // Transmit the filter to the createRoomViewModel
            createRoomViewModel.handle(CreateRoomActions.SetName(currentFilter))
        }
    }

    override fun initUiAndData() {
        if (isFirstCreation()) {
            addFragment(R.id.simpleFragmentContainer, PublicRoomsFragment::class.java)
        }
    }

    companion object {
        private const val INITIAL_FILTER = "INITIAL_FILTER"

        fun getIntent(context: Context, initialFilter: String = ""): Intent {
            val intent = Intent(context, RoomDirectoryActivity::class.java)
            intent.putExtra(INITIAL_FILTER, initialFilter)
            return intent
        }
    }
}
