package com.adrian.library.task.scheduler;

import com.adrian.library.loan.BookAlreadyLoanedException;
import com.adrian.library.reservation.BookAlreadyReservedException;
import com.adrian.library.task.TaskServiceImpl;
import com.adrian.library.user.UserLacksActionPointsException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskSchedulerServiceImpl implements TaskSchedulerService {

    private final TaskServiceImpl taskService;

    // charge loans that have expired (return deadline has passed)
    // repeat every day at 00:00
    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void chargeLoansSchedule() {
        taskService.chargeLoans();
    }

    // cancel loans that haven't been picked up (pick up deadline has passed)
    // repeat every day at 00:00
    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void cancelLoansSchedule() throws UserLacksActionPointsException, BookAlreadyReservedException,
            BookAlreadyLoanedException {
        taskService.cancelLoans();
    }

    // cancel reservations whose waiting deadline has expired
    // repeat every day at 00:00
    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    public void cancelReservationsSchedule() {
        taskService.cancelReservations();
    }
}
