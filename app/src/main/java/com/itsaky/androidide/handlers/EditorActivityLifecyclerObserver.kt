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

package com.itsaky.androidide.handlers

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.itsaky.androidide.EditorActivity
import com.itsaky.androidide.eventbus.events.Event
import com.itsaky.androidide.eventbus.events.EventReceiver
import com.itsaky.androidide.eventbus.events.editor.OnCreateEvent
import com.itsaky.androidide.eventbus.events.editor.OnDestroyEvent
import com.itsaky.androidide.eventbus.events.editor.OnPauseEvent
import com.itsaky.androidide.eventbus.events.editor.OnResumeEvent
import com.itsaky.androidide.eventbus.events.editor.OnStartEvent
import com.itsaky.androidide.eventbus.events.editor.OnStopEvent
import com.itsaky.androidide.projects.FileManager
import com.itsaky.androidide.projects.ProjectManager
import com.itsaky.androidide.utils.BootClasspathProvider
import java.util.concurrent.*

/**
 * Observes lifecycle events if [com.itsaky.androidide.EditorActivity].
 *
 * @author Akash Yadav
 */
class EditorActivityLifecyclerObserver : DefaultLifecycleObserver {

  private val fileActionsHandler = FileTreeActionHandler()
  
  override fun onCreate(owner: LifecycleOwner) {
    dispatchEvent(OnCreateEvent())
  }
  
  override fun onStart(owner: LifecycleOwner) {
    CompletableFuture.runAsync(BootClasspathProvider::init)
    register(fileActionsHandler, FileManager, ProjectManager)
    
    dispatchEvent(OnStartEvent())
  }
  
  override fun onResume(owner: LifecycleOwner) {
    dispatchEvent(OnResumeEvent())
  }
  
  override fun onPause(owner: LifecycleOwner) {
    dispatchEvent(OnPauseEvent())
  }

  override fun onStop(owner: LifecycleOwner) {
    unregister(fileActionsHandler, FileManager, ProjectManager)
  
    dispatchEvent(OnStopEvent())
  }
  
  override fun onDestroy(owner: LifecycleOwner) {
    dispatchEvent(OnDestroyEvent())
  }

  private fun register(vararg receivers: EventReceiver) {
    receivers.forEach { it.register() }
  }

  private fun unregister(vararg receivers: EventReceiver) {
    receivers.forEach { it.unregister() }
  }

  private fun dispatchEvent(event: Event) {}
}