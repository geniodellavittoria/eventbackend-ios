import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRegistrationCategory } from 'app/shared/model/registration-category.model';

@Component({
    selector: 'jhi-registration-category-detail',
    templateUrl: './registration-category-detail.component.html'
})
export class RegistrationCategoryDetailComponent implements OnInit {
    registrationCategory: IRegistrationCategory;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ registrationCategory }) => {
            this.registrationCategory = registrationCategory;
        });
    }

    previousState() {
        window.history.back();
    }
}
