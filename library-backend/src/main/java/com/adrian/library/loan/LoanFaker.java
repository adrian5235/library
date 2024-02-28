package com.adrian.library.loan;

import com.adrian.library.copy.Copy;
import com.adrian.library.copy.CopyRepository;
import com.adrian.library.loan.status.LoanStatus;
import com.adrian.library.loan.status.LoanStatusRepository;
import com.adrian.library.user.User;
import com.adrian.library.user.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class LoanFaker {

    @Autowired
    CopyRepository copyRepository;
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LoanStatusRepository loanStatusRepository;
    private final Faker faker = new Faker();
    Random r = new Random();

    public void saveLoans(int n) {
        for (int i = 0; i < n; i ++) {
            Calendar calendar = Calendar.getInstance();
            Date date = faker.date().past(500, 31, TimeUnit.DAYS);
            calendar.setTime(date);
            LocalDate createdOn = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            LocalDate pickUpDeadline = createdOn.plusDays(7);
            LocalDate pickedUpOn = createdOn.plusDays(r.nextInt(7));
            LocalDate returnDeadline = pickedUpOn.plusDays(30);
            LocalDate returnedOn = pickedUpOn.plusDays(r.nextInt(30));
            Copy copy = randomCopy();
            User user = randomUser();
            LoanStatus status = loanStatusRepository.findByName("zakończone");

            loanRepository.save(new Loan(createdOn, pickUpDeadline, pickedUpOn, returnDeadline,
                    returnedOn, copy, user, status));

            copyRepository.save(copy);
        }
    }

    private Copy randomCopy() {
        List<Copy> copies = copyRepository.findByAvailable(true);
        return copies.get(r.nextInt(copies.size()));
    }

    private User randomUser() {
        List<User> users = userRepository.findAll();
        return users.get(r.nextInt(users.size()));
    }
}
