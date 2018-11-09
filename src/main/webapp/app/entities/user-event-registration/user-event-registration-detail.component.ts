import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserEventRegistration } from 'app/shared/model/user-event-registration.model';

@Component({
    selector: 'jhi-user-event-registration-detail',
    templateUrl: './user-event-registration-detail.component.html'
})
export class UserEventRegistrationDetailComponent implements OnInit {
    userEventRegistration: IUserEventRegistration;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ userEventRegistration }) => {
            this.userEventRegistration = userEventRegistration;
        });
    }

    previousState() {
        window.history.back();
    }
}
