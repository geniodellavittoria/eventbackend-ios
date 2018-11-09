import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IImpression } from 'app/shared/model/impression.model';
import { Principal } from 'app/core';
import { ImpressionService } from './impression.service';

@Component({
    selector: 'jhi-impression',
    templateUrl: './impression.component.html'
})
export class ImpressionComponent implements OnInit, OnDestroy {
    impressions: IImpression[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private impressionService: ImpressionService,
        private jhiAlertService: JhiAlertService,
        private dataUtils: JhiDataUtils,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.impressionService.query().subscribe(
            (res: HttpResponse<IImpression[]>) => {
                this.impressions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInImpressions();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IImpression) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    registerChangeInImpressions() {
        this.eventSubscriber = this.eventManager.subscribe('impressionListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
