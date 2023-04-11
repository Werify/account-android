package net.werify.id.interfaces

import net.werify.id.common.ConnectionQuality




interface ConnectionQualityChangeListener {
    fun onChange(currentConnectionQuality: ConnectionQuality, currentBandwidth: Int)
}