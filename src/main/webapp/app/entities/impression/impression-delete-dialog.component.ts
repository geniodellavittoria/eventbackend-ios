import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IImpression } from 'app/shared/model/impression.model';
import { ImpressionService } from './impression.service';

@Component({
    selector: 'jhi-impression-delete-dialog',
    templateUrl: './impression-delete-dialog.component.html'
})
export class ImpressionDeleteDialogComponent {
    impression: IImpression;

    constructor(private impressionService: ImpressionService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.impressionService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'impressionListModification',
                content: 'Deleted an impression'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-impression-delete-popup',
    template: ''
})
export class ImpressionDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ impression }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ImpressionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.impression = impression;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
