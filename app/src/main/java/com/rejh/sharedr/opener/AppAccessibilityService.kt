package com.rejh.sharedr.opener

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.rejh.sharedr.opener.Constants.Companion.DEBUG_TAG
import com.rejh.sharedr.opener.Constants.Companion.PKG_ANDROID
import com.rejh.sharedr.opener.Constants.Companion.SHAREDR_LABEL

class AppAccessibilityService : AccessibilityService() {

    /**
     * List of package names for different share sheets.
     * For stock android, package name of the default share sheet is "android".
     * It may be different for MIUI, Samsung ONE UI etc. Add them to the list.
     */
    private val sharePackageNames by lazy {
        listOf(
            PKG_ANDROID,
        )
    }

    /**
     * Flag to show a Toast only once before opening Sharedr
     */
    private var showToast = true

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val openPackageName: String = rootInActiveWindow?.packageName?.toString()?: "null"

        // If the window of the current package is in the list of correct packages, start searching.
        if (openPackageName in sharePackageNames) {
            findLinearLayouts(rootInActiveWindow)
            if (showToast) {
                showToast = false       // prevent showing Toast again
                Toast.makeText(this, R.string.please_wait_opening_sharedr, Toast.LENGTH_SHORT).show()
            }
        }
        else {
            showToast = true
        }
    }

    override fun onInterrupt() {

    }

    private fun findLinearLayouts(node: AccessibilityNodeInfo?) {

        if (node == null) return

        val count = node.childCount

        // All share options are in the form of LinearLayout.
        // Hence try searching for them.

        if (node.className == "android.widget.LinearLayout" && isShareDrTile(node)) {
            Log.d(DEBUG_TAG, "Valid Sharedr tile. Performing Click.")
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }

        // If not valid, check other nodes recursively
        else for (i in 0 until count) {
            node.getChild(i)?.let {
                findLinearLayouts(it)
            }
        }
    }

    /**
     * Method to check if the current share sheet item being checked is of Sharedr
     */
    private fun isShareDrTile(node: AccessibilityNodeInfo): Boolean {

        if (!node.isFocusable && !node.isEnabled && !node.isClickable) return false

        for (i in 0 until node.childCount) {

            // Check all children. If any text with "Sharedr" label, then it is the valid option
            node.getChild(i)?.let {
                if (it.text == SHAREDR_LABEL) return true
            }
        }
        return false
    }
}