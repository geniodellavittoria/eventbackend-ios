import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IUserEventRegistration } from 'app/shared/model/user-event-registration.model';
import { UserEventRegistrationService } from './user-event-registration.service';
import { IRegistrationCategory } from 'app/shared/model/registration-category.model';
import { RegistrationCategoryService } from 'app/entities/registration-category';
import { IUser, UserService } from 'app/core';
import { IEvent } from 'app/shared/model/event.model';
import { EventService } from 'app/entities/event';

@Component({
    selector: 'jhi-user-event-registration-update',
    templateUrl: './user-event-registration-update.component.html'
})
export class UserEventRegistrationUpdateComponent implements OnInit {
    userEventRegistration: IUserEventRegistration;
    isSaving: boolean;

    registrationcategories: IRegistrationCategory[];

    users: IUser[];

    events: IEvent[];
    timestamp: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private userEventRegistrationService: UserEventRegistrationService,
        private registrationCategoryService: RegistrationCategoryService,
        private userService: UserService,
        private eventService: EventService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ userEventRegistration }) => {
            this.userEventRegistration = userEventRegistration;
            this.timestamp =
                this.userEventRegistration.timestamp != null ? this.userEventRegistration.timestamp.format(DATE_TIME_FORMAT) : null;
        });
        this.registrationCategoryService.query().subscribe(
            (res: HttpResponse<IRegistrationCategory[]>) => {
                this.registrationcategories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.eventService.query().subscribe(
            (res: HttpResponse<IEvent[]>) => {
                this.events = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.userEventRegistration.timestamp = this.timestamp != null ? moment(this.timestamp, DATE_TIME_FORMAT) : null;
        if (this.userEventRegistration.id !== undefined) {
            this.subscribeToSaveResponse(this.userEventRegistrationService.update(this.userEventRegistration));
        } else {
            this.subscribeToSaveResponse(this.userEventRegistrationService.create(this.userEventRegistration));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IUserEventRegistration>>) {
        result.subscribe(
            (res: HttpResponse<IUserEventRegistration>) => this.onSaveSuccess(),
            (res: HttpErrorResponse) => this.onSaveError()
        );
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackRegistrationCategoryById(index: number, item: IRegistrationCategory) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackEventById(index: number, item: IEvent) {
        return item.id;
    }
}
