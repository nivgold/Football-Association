package com.domain.logic.users;


import com.domain.logic.data_types.PersonalPage;

public interface IPersonalPageObserver {
    void pageUpdate(PersonalPage personalPage);
}
