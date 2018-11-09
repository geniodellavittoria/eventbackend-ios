import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IRegistrationCategory } from 'app/shared/model/registration-category.model';
import { RegistrationCategoryService } from './registration-category.service';

@Component({
    selector: 'jhi-registration-category-update',
    templateUrl: './registration-category-update.component.html'
})
export class RegistrationCategoryUpdateComponent implements OnInit {
    registrationCategory: IRegistrationCategory;
    isSaving: boolean;

    constructor(private registrationCategoryService: RegistrationCategoryService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ registrationCategory }) => {
            this.registrationCategory = registrationCategory;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.registrationCategory.id !== undefined) {
            this.subscribeToSaveResponse(this.registrationCategoryService.update(this.registrationCategory));
        } else {
            this.subscribeToSaveResponse(this.registrationCategoryService.create(this.registrationCategory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IRegistrationCategory>>) {
        result.subscribe(
            (res: HttpResponse<IRegistrationCategory>) => this.onSaveSuccess(),
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
}
