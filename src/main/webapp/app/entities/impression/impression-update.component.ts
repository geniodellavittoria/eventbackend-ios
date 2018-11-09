import { Component, OnInit, ElementRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { IImpression } from 'app/shared/model/impression.model';
import { ImpressionService } from './impression.service';

@Component({
    selector: 'jhi-impression-update',
    templateUrl: './impression-update.component.html'
})
export class ImpressionUpdateComponent implements OnInit {
    impression: IImpression;
    isSaving: boolean;

    constructor(
        private dataUtils: JhiDataUtils,
        private impressionService: ImpressionService,
        private elementRef: ElementRef,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ impression }) => {
            this.impression = impression;
        });
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
        this.dataUtils.clearInputImage(this.impression, this.elementRef, field, fieldContentType, idInput);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.impression.id !== undefined) {
            this.subscribeToSaveResponse(this.impressionService.update(this.impression));
        } else {
            this.subscribeToSaveResponse(this.impressionService.create(this.impression));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IImpression>>) {
        result.subscribe((res: HttpResponse<IImpression>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
