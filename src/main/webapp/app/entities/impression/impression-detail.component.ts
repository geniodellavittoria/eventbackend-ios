import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IImpression } from 'app/shared/model/impression.model';

@Component({
    selector: 'jhi-impression-detail',
    templateUrl: './impression-detail.component.html'
})
export class ImpressionDetailComponent implements OnInit {
    impression: IImpression;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
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
    previousState() {
        window.history.back();
    }
}
