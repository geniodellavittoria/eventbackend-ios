import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { IEvent } from 'app/shared/model/event.model';
import { EventService } from './event.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category';
import { IUser, UserService } from 'app/core';
import { IImpression } from 'app/shared/model/impression.model';
import { ImpressionService } from 'app/entities/impression';

@Component({
    selector: 'jhi-event-update',
    templateUrl: './event-update.component.html'
})
export class EventUpdateComponent implements OnInit {
    event: IEvent;
    isSaving: boolean;

    categories: ICategory[];

    users: IUser[];

    impressions: IImpression[];
    timestamp: string;
    eventStart: string;
    eventEnd: string;

    constructor(
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private eventService: EventService,
        private categoryService: CategoryService,
        private userService: UserService,
        private impressionService: ImpressionService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ event }) => {
            this.event = event;
            this.timestamp = this.event.timestamp != null ? this.event.timestamp.format(DATE_TIME_FORMAT) : null;
            this.eventStart = this.event.eventStart != null ? this.event.eventStart.format(DATE_TIME_FORMAT) : null;
            this.eventEnd = this.event.eventEnd != null ? this.event.eventEnd.format(DATE_TIME_FORMAT) : null;
        });
        this.categoryService.query().subscribe(
            (res: HttpResponse<ICategory[]>) => {
                this.categories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.userService.query().subscribe(
            (res: HttpResponse<IUser[]>) => {
                this.users = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.impressionService.query().subscribe(
            (res: HttpResponse<IImpression[]>) => {
                this.impressions = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clearInputImage(field: string, fieldContentType: string, idInput: string) {
        this.dataUtils.clearInputImage(this.event, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.event.timestamp = this.timestamp != null ? moment(this.timestamp, DATE_TIME_FORMAT) : null;
        this.event.eventStart = this.eventStart != null ? moment(this.eventStart, DATE_TIME_FORMAT) : null;
        this.event.eventEnd = this.eventEnd != null ? moment(this.eventEnd, DATE_TIME_FORMAT) : null;
        if (this.event.id !== undefined) {
            this.subscribeToSaveResponse(this.eventService.update(this.event));
        } else {
            this.subscribeToSaveResponse(this.eventService.create(this.event));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEvent>>) {
        result.subscribe((res: HttpResponse<IEvent>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCategoryById(index: number, item: ICategory) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    trackImpressionById(index: number, item: IImpression) {
        return item.id;
    }
}
