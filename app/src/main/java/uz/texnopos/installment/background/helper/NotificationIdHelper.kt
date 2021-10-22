package uz.texnopos.installment.background.helper

import java.util.concurrent.atomic.AtomicInteger

object NotificationIdHelper {

    private val c = AtomicInteger(0)

    fun getId(): Int = c.incrementAndGet()

}