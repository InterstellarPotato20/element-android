/*
 * Copyright (c) 2020 New Vector Ltd
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

package im.vector.matrix.android.api.session.widgets

import androidx.lifecycle.LiveData
import im.vector.matrix.android.api.MatrixCallback
import im.vector.matrix.android.api.query.QueryStringValue
import im.vector.matrix.android.api.session.events.model.Content
import im.vector.matrix.android.api.util.Cancelable
import im.vector.matrix.android.api.session.widgets.model.Widget


/**
 * This is the entry point to manage widgets. You can grab an instance of this service through an active session.
 */
interface WidgetService {

    /**
     * Returns an instance of [WidgetURLFormatter].
     */
    fun getWidgetURLFormatter(): WidgetURLFormatter

    /**
     * Returns an instance of [WidgetPostAPIMediator].
     * This is to be used for "admin" widgets so you can interact through JS.
     */
    fun getWidgetPostAPIMediator(): WidgetPostAPIMediator

    /**
     * Returns the current room widgets defined through state events.
     * Some widgets can be deactivated, so be sure to check for isActive if needed.
     *
     * @param roomId the room where you want to fetch widgets
     * @param widgetId if you want to fetch for some particular widget
     * @param widgetTypes if you want to filter some widget type.
     * @param excludedTypes if you want to exclude some widget type.
     */
    fun getRoomWidgets(
            roomId: String,
            widgetId: QueryStringValue = QueryStringValue.NoCondition,
            widgetTypes: Set<String>? = null,
            excludedTypes: Set<String>? = null
    ): List<Widget>

    /**
     * Returns the live room widgets so you can listen to them.
     * Some widgets can be deactivated, so be sure to check for isActive.
     *
     * @param roomId the room where you want to fetch widgets
     * @param widgetId if you want to fetch for some particular widget
     * @param widgetTypes if you want to filter some widget type.
     * @param excludedTypes if you want to exclude some widget type.
     */
    fun getRoomWidgetsLive(
            roomId: String,
            widgetId: QueryStringValue = QueryStringValue.NoCondition,
            widgetTypes: Set<String>? = null,
            excludedTypes: Set<String>? = null
    ): LiveData<List<Widget>>

    /**
     * Returns the current user widgets.
     * Some widgets can be deactivated, so be sure to check for isActive.
     *
     * @param widgetTypes if you want to filter some widget type.
     * @param excludedTypes if you want to exclude some widget type.
     */
    fun getUserWidgets(
            widgetTypes: Set<String>? = null,
            excludedTypes: Set<String>? = null
    ): List<Widget>

    /**
     * Returns the live user widgets so you can listen to them.
     * Some widgets can be deactivated, so be sure to check for isActive.
     *
     * @param widgetTypes if you want to filter some widget type.
     * @param excludedTypes if you want to exclude some widget type.
     */
    fun getUserWidgetsLive(
            widgetTypes: Set<String>? = null,
            excludedTypes: Set<String>? = null
    ): LiveData<List<Widget>>

    /**
     * Creates a new widget in a room. It makes sure you have the rights to handle this.
     *
     * @param roomId: the room where you want to deactivate the widget.
     * @param widgetId: the widget to deactivate.
     * @param callback the matrix callback to listen for result.
     * @return Cancelable
     */
    fun createRoomWidget(roomId: String, widgetId: String, content: Content, callback: MatrixCallback<Widget>): Cancelable

    /**
     * Deactivate a widget in a room. It makes sure you have the rights to handle this.
     *
     * @param roomId: the room where you want to deactivate the widget.
     * @param widgetId: the widget to deactivate.
     * @param callback the matrix callback to listen for result.
     * @return Cancelable
     */
    fun destroyRoomWidget(roomId: String, widgetId: String, callback: MatrixCallback<Unit>): Cancelable

    /**
     * Returns true if you can add/remove widgets. It goes through
     * @param roomId the room where you want to administrate widgets.
     */
    fun hasPermissionsToHandleWidgets(roomId: String): Boolean
}
