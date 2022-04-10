/*
 *  This file is part of AndroidIDE.
 *
 *  AndroidIDE is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.itsaky.androidide.actions

import android.graphics.drawable.Drawable

/**
 * An action that can be registered using the [ActionsRegistry]
 * [com.itsaky.androidide.actions.ActionsRegistry]
 * @author Akash Yadav
 */
interface ActionItem {

    val id: String
    var label: String
    var visible: Boolean
    var enabled: Boolean
    var icon: Drawable?
    var requiresUIThread: Boolean
    var location: Location

    /**
     * Prepare the action. Subclasses can modify the visual properties of this action here.
     * @param data The data containing various information about the event.
     */
    fun prepare(data: ActionData)

    /**
     * Execute the action. The action executed in a background thread by default.
     *
     * @param data The data containing various information about the event.
     * @return `true` if this action was executed successfully, `false` otherwise.
     */
    fun execAction(data: ActionData): Any

    /**
     * Called just after the [execAction] method executes **successfully** (i.e. returns `true`).
     * Subclasses are free to do UI related work here as this method is called on UI thread.
     *
     * @param data The data containing various information about the event.
     */
    fun postExec(data: ActionData, result: Any) = Any()

    fun getShowAsActionFlags(data: ActionData): Int = -1

    /** Location where an action item will be shown. */
    enum class Location(val id: String) {

        /** Location marker for action items shown in editor activity's toolbar. */
        EDITOR_TOOLBAR("editor_toolbar"),

        /** Location marker for action items shown in editor's text action menu. */
        EDITOR_TEXT_ACTIONS("editor_text_actions"),

        /**
         * Location marker for action items shown in 'Code actions' submenu in editor's text action
         * menu.
         */
        EDITOR_CODE_ACTIONS("editor_code_actions");

        override fun toString(): String {
            return id
        }

        fun forId(id: String): Location {
            return values().first { it.id == id }
        }
    }
}
