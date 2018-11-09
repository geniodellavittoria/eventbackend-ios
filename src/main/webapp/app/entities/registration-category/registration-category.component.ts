import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IRegistrationCategory } from 'app/shared/model/registration-category.model';
import { Principal } from 'app/core';
import { RegistrationCategoryService } from './registration-category.service';

@Component({
    selector: 'jhi-registration-category',
    templateUrl: './registration-category.component.html'
})
export class RegistrationCategoryComponent implements OnInit, OnDestroy {
    registrationCategories: IRegistrationCategory[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private registrationCategoryService: RegistrationCategoryService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.registrationCategoryService.query().subscribe(
            (res: HttpResponse<IRegistrationCategory[]>) => {
                this.registrationCategories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInRegistrationCategories();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IRegistrationCategory) {
        return item.id;
    }

    registerChangeInRegistrationCategories() {
        this.eventSubscriber = this.eventManager.subscribe('registrationCategoryListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
