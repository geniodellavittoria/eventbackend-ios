import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IEvent } from 'app/shared/model/event.model';
import { Principal } from 'app/core';
import { EventService } from './event.service';

@Component({
    selector: 'jhi-event',
    templateUrl: './event.component.html'
})
export class EventComponent implements OnInit, OnDestroy {
    events: IEvent[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private eventService: EventService,
        private jhiAlertService: JhiAlertService,
        private dataUtils: JhiDataUtils,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.eventService.query().subscribe(
            (res: HttpResponse<IEvent[]>) => {
                this.events = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInEvents();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IEvent) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInEvents() {
        this.eventSubscriber = this.eventManager.subscribe('eventListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
