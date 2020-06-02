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

package im.vector.matrix.android.api.session.integrationmanager

import im.vector.matrix.android.api.MatrixCallback
import im.vector.matrix.android.api.util.Cancelable

/**
 * This is the entry point to manage integration. You can grab an instance of this service through an active session.
 */
interface IntegrationManagerService {

    /**
     * This listener allow you to observe change related to integrations.
     */
    interface Listener {
        /**
         * Is called whenever integration is enabled or disabled, comes from user account data.
         */
        fun onIsEnabledChanged(enabled: Boolean) {
            // No-op
        }

        /**
         * Is called whenever configs from user account data or wellknown are updated.
         */
        fun onConfigurationChanged(configs: List<IntegrationManagerConfig>) {
            // No-op
        }

        /**
         * Is called whenever widget permissions from user account data are updated.
         */
        fun onWidgetPermissionsChanged(widgets: Map<String, Boolean>) {
            // No-op
        }
    }

    /**
     * Adds a listener to observe changes.
     */
    fun addListener(listener: Listener)

    /**
     * Removes a previously added listener.
     */
    fun removeListener(listener: Listener)

    /**
     * Return the list of current configurations, sorted by kind. First one is preferred.
     * See [IntegrationManagerConfig.Kind]
     */
    fun getOrderedConfigs(): List<IntegrationManagerConfig>

    /**
     * Return the preferred current configuration.
     * See [IntegrationManagerConfig.Kind]
     */
    fun getPreferredConfig(): IntegrationManagerConfig

    /**
     * Returns true if integration is enabled, false otherwise.
     */
    fun isIntegrationEnabled(): Boolean

    /**
     * Offers to enable or disable the integration.
     * @param enable the param to change
     * @param callback the matrix callback to listen for result.
     * @return Cancelable
     */
    fun setIntegrationEnabled(enable: Boolean, callback: MatrixCallback<Unit>): Cancelable

    /**
     * Offers to allow or disallow a widget.
     * @param stateEventId the eventId of the state event defining the widget.
     * @param allowed the param to change
     * @param callback the matrix callback to listen for result.
     * @return Cancelable
     */
    fun setWidgetAllowed(stateEventId: String, allowed: Boolean, callback: MatrixCallback<Unit>): Cancelable

    /**
     * Returns true if the widget is allowed, false otherwise.
     * @param stateEventId the eventId of the state event defining the widget.
     */
    fun isWidgetAllowed(stateEventId: String): Boolean

    /**
     * Offers to allow or disallow a native widget domain.
     * @param widgetType the widget type to check for
     * @param domain the domain to check for
     */
    fun setNativeWidgetDomainAllowed(widgetType: String, domain: String, allowed: Boolean, callback: MatrixCallback<Unit>): Cancelable

    /**
     * Returns true if the widget domain is allowed, false otherwise.
     * @param widgetType the widget type to check for
     * @param domain the domain to check for
     */
    fun isNativeWidgetDomainAllowed(widgetType: String, domain: String): Boolean
}
