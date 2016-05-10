package org.banksy.domain.account.scheduled

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

@Component
class InterestPaymentScheduler {

    @Scheduled(cron="* * * * 1 ?")
    fun run() {
        System.out.println("Method executed at every 5 seconds. Current time is :: "+ Date())
    }

}
