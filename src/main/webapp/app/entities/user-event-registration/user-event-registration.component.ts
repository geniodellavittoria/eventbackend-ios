import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IUserEventRegistration } from 'app/shared/model/user-event-registration.model';
import { Principal } from 'app/core';
import { UserEventRegistrationService } from './user-event-registration.service';

@Component({
    selector: 'jhi-user-event-registration',
    templateUrl: './user-event-registration.component.html'
})
export class UserEventRegistrationComponent implements OnInit, OnDestroy {
    userEventRegistrations: IUserEventRegistration[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private userEventRegistrationService: UserEventRegistrationService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.userEventRegistrationService.query().subscribe(
            (res: HttpResponse<IUserEventRegistration[]>) => {
                this.userEventRegistrations = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInUserEventRegistrations();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IUserEventRegistration) {
        return item.id;
    }

    registerChangeInUserEventRegistrations() {
        this.eventSubscriber = this.eventManager.subscribe('userEventRegistrationListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
